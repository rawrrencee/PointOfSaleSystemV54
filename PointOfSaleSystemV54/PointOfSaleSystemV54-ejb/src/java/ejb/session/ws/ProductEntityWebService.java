package ejb.session.ws;

import ejb.session.stateless.ProductEntityControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import entity.ProductEntity;
import entity.StaffEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CreateNewProductException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ProductNotFoundException;



@WebService(serviceName = "ProductEntityWebService")
@Stateless

// Newly added in v4.3
public class ProductEntityWebService
{
    @PersistenceContext(unitName = "PointOfSaleSystemV54-ejbPU")
    private EntityManager em;
    
    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;    
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    
    
    
    // Updated in v5.1 to nullify bidirectional relationships in category and tags.
    
    @WebMethod(operationName = "retrieveAllProducts")
    public List<ProductEntity> retrieveAllProducts(@WebParam(name = "username") String username,
                                                    @WebParam(name = "password") String password) 
            throws InvalidLoginCredentialException
    {
        StaffEntity staffEntity = staffEntityControllerLocal.staffLogin(username, password);
        System.out.println("********** ProductEntityWebService.retrieveAllProducts(): Staff " + staffEntity.getUsername() + " login remotely via web service");
        
        List<ProductEntity> productEntities = productEntityControllerLocal.retrieveAllProducts();
        
        for(ProductEntity productEntity:productEntities)
        {
            em.detach(productEntity.getCategoryEntity());
            productEntity.getCategoryEntity().setParentCategoryEntity(null);
            productEntity.getCategoryEntity().getSubCategoryEntities().clear();
            productEntity.getCategoryEntity().getProductEntities().clear();
            productEntity.getTagEntities().size();
        }
        
        return productEntities;
    }
    
    
    
    // Updated in v5.1 to nullify bidirectional relationships in category and tags.
    
    @WebMethod(operationName = "retrieveProductByProductId")
    public ProductEntity retrieveProductByProductId(@WebParam(name = "username") String username,
                                                    @WebParam(name = "password") String password,
                                                    @WebParam(name = "productId") Long productId) 
            throws InvalidLoginCredentialException, ProductNotFoundException
    {
        StaffEntity staffEntity = staffEntityControllerLocal.staffLogin(username, password);
        System.out.println("********** ProductEntityWebService.retrieveProductByProductId(): Staff " + staffEntity.getUsername() + " login remotely via web service");
        
        ProductEntity productEntity = productEntityControllerLocal.retrieveProductByProductId(productId);
        
        em.detach(productEntity.getCategoryEntity());
        productEntity.getCategoryEntity().setParentCategoryEntity(null);
        productEntity.getCategoryEntity().getSubCategoryEntities().clear();
        productEntity.getCategoryEntity().getProductEntities().clear();
        productEntity.getTagEntities().size();
        
        return productEntity;
    }
    
    
    
    @WebMethod(operationName = "createNewProduct")
    public ProductEntity createNewProduct(@WebParam(name = "username") String username,
                                            @WebParam(name = "password") String password,
                                            @WebParam(name = "newProductEntity") ProductEntity newProductEntity,
                                            @WebParam(name = "categoryId") Long categoryId,
                                            @WebParam(name = "tagIds") List<Long> tagIds)
            throws InvalidLoginCredentialException, InputDataValidationException, CreateNewProductException
    {
        StaffEntity staffEntity = staffEntityControllerLocal.staffLogin(username, password);
        System.out.println("********** ProductEntityWebService.createNewProduct(): Staff " + staffEntity.getUsername() + " login remotely via web service");
        
        return productEntityControllerLocal.createNewProduct(newProductEntity, categoryId, tagIds);
    }
}
