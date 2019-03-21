package ejb.session.ws;

import datamodel.ws.RemoteCheckoutLineItem;
import ejb.session.singleton.RemoteCheckoutManager;
import ejb.session.stateful.CheckoutControllerLocal;
import ejb.session.stateless.ProductEntityControllerLocal;
import ejb.session.stateless.SaleTransactionEntityControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import entity.ProductEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import entity.StaffEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CreateNewSaleTransactionException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ProductNotFoundException;
import util.exception.RemoteCheckoutControllerNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.UnableToCreateRemoteCheckoutControllerException;



@WebService(serviceName = "SaleTransactionEntityWebService")
@Stateless

public class SaleTransactionEntityWebService 
{
    @PersistenceContext(unitName = "PointOfSaleSystemV54-ejbPU")
    private EntityManager em;
    
    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal;
    
    @EJB
    private RemoteCheckoutManager remoteCheckoutManager;
    
    
        
    @WebMethod(operationName = "clientStateRemoteCheckout")
    public SaleTransactionEntity clientStateRemoteCheckout(@WebParam(name = "username") String username,
                                                            @WebParam(name = "password") String password,
                                                            @WebParam(name = "remoteCheckoutLineItems") List<RemoteCheckoutLineItem> remoteCheckoutLineItems)
            throws InvalidLoginCredentialException, CreateNewSaleTransactionException
    {
        StaffEntity staffEntity = staffEntityControllerLocal.staffLogin(username, password);
        System.out.println("********** SaleTransactionEntityWebService.clientStateRemoteCheckout(): Staff " + staffEntity.getUsername() + " login remotely via web service");
        
        if(remoteCheckoutLineItems != null || remoteCheckoutLineItems.isEmpty())
        {
            try
            {
                List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = new ArrayList<>();
                Integer totalLineItem = 0;    
                Integer totalQuantity = 0;    
                BigDecimal totalAmount = new BigDecimal("0.00");

                for(RemoteCheckoutLineItem remoteCheckoutLineItem:remoteCheckoutLineItems)
                {
                    ProductEntity productEntity = productEntityControllerLocal.retrieveProductByProductSkuCode(remoteCheckoutLineItem.getSkuCode());

                    BigDecimal subTotal = productEntity.getUnitPrice().multiply(new BigDecimal(remoteCheckoutLineItem.getQuantity()));
                    ++totalLineItem;
                    saleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(totalLineItem, productEntity, remoteCheckoutLineItem.getQuantity(), productEntity.getUnitPrice(), subTotal));
                    totalQuantity += remoteCheckoutLineItem.getQuantity();
                    totalAmount = totalAmount.add(subTotal);
                }
                
                SaleTransactionEntity saleTransactionEntity = saleTransactionEntityControllerLocal.createNewSaleTransaction(staffEntity.getStaffId(), new SaleTransactionEntity(totalLineItem, totalQuantity, totalAmount, new Date(), saleTransactionLineItemEntities, false));
                
                em.detach(saleTransactionEntity);
                em.detach(saleTransactionEntity.getStaffEntity());
                saleTransactionEntity.setStaffEntity(null);
                
                return saleTransactionEntity;
            }
            catch(ProductNotFoundException ex)
            {
                throw new CreateNewSaleTransactionException("Unable to create new sale transaction remotely as product entity does not exist: " + ex.getMessage());
            }
            catch(StaffNotFoundException ex)
            {
                throw new CreateNewSaleTransactionException("Unable to create new sale transaction remotely as staff entity does not exist: " + ex.getMessage());
            }
        }
        else
        {
            throw new CreateNewSaleTransactionException("Nothing to checkout remotely!");
        }
    }
    
    
    
    @WebMethod(operationName = "serverStateRequestRemoteCheckoutController")
    public String serverStateRequestRemoteCheckoutController(@WebParam(name = "username") String username, 
                                                                @WebParam(name = "password") String password) 
            throws InvalidLoginCredentialException, UnableToCreateRemoteCheckoutControllerException
    {
        StaffEntity staffEntity = staffEntityControllerLocal.staffLogin(username, password);
        System.out.println("********** SaleTransactionEntityWebService.serverStateRequestRemoteCheckoutController(): Staff " + staffEntity.getUsername() + " login remotely via web service");
        
        String sessionKey = remoteCheckoutManager.createNewRemoteCheckoutController();
        
        if(sessionKey != null)
        {
            return sessionKey;
        }
        else
        {
            throw new UnableToCreateRemoteCheckoutControllerException("The server is unable to create a new checkout controller to support your remote checout request at this juncture. Please try again later.");
        }
    }
    
    
    
    @WebMethod(operationName = "serverStateRemoteAddItem")
    public BigDecimal serverStateRemoteAddItem(@WebParam(name = "username") String username, 
                                                @WebParam(name = "password") String password,
                                                @WebParam(name = "sessionKey") String sessionKey,
                                                @WebParam(name = "remoteCheckoutLineItem") RemoteCheckoutLineItem remoteCheckoutLineItem)
            throws InvalidLoginCredentialException, RemoteCheckoutControllerNotFoundException, ProductNotFoundException
    {
        StaffEntity staffEntity = staffEntityControllerLocal.staffLogin(username, password);
        System.out.println("********** SaleTransactionEntityWebService.serverStateRemoteAddItem(): Staff " + staffEntity.getUsername() + " login remotely via web service");
        
        CheckoutControllerLocal checkoutControllerLocal = remoteCheckoutManager.retrieveRemoteCheckoutController(sessionKey);
        ProductEntity productEntity = productEntityControllerLocal.retrieveProductByProductSkuCode(remoteCheckoutLineItem.getSkuCode());
        return checkoutControllerLocal.addItem(productEntity, remoteCheckoutLineItem.getQuantity());
    }
    
    
    
    @WebMethod(operationName = "serverStateRemoteCheckout")
    public SaleTransactionEntity serverStateRemoteCheckout(@WebParam(name = "username") String username, 
                                                @WebParam(name = "password") String password,
                                                @WebParam(name = "sessionKey") String sessionKey)
            throws InvalidLoginCredentialException, RemoteCheckoutControllerNotFoundException, StaffNotFoundException, CreateNewSaleTransactionException
    {
        StaffEntity staffEntity = staffEntityControllerLocal.staffLogin(username, password);
        System.out.println("********** SaleTransactionEntityWebService.serverStateRemoteAddItem(): Staff " + staffEntity.getUsername() + " login remotely via web service");
        
        CheckoutControllerLocal checkoutControllerLocal = remoteCheckoutManager.retrieveRemoteCheckoutController(sessionKey);
        SaleTransactionEntity saleTransactionEntity = checkoutControllerLocal.doCheckout(staffEntity.getStaffId());
        remoteCheckoutManager.removeRemoteCheckoutController(sessionKey);
        
        em.detach(saleTransactionEntity);
        em.detach(saleTransactionEntity.getStaffEntity());
        saleTransactionEntity.setStaffEntity(null);
        
        return saleTransactionEntity;
    }
}