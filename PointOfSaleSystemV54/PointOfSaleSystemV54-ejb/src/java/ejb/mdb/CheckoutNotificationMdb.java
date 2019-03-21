package ejb.mdb;

import ejb.session.stateless.EmailControllerLocal;
import ejb.session.stateless.SaleTransactionEntityControllerLocal;
import entity.SaleTransactionEntity;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import util.exception.SaleTransactionNotFoundException;



@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/queueCheckoutNotification"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})

public class CheckoutNotificationMdb implements MessageListener
{
    @EJB
    private SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal;
    @EJB
    private EmailControllerLocal emailControllerLocal;
    
    
    
    public CheckoutNotificationMdb()
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
    }
    
    
    
    @PreDestroy
    public void preDestroy()
    {
    }
    
    
    
    @Override
    public void onMessage(Message message)
    {
        try
        {
            if (message instanceof MapMessage)
            {
                MapMessage mapMessage = (MapMessage)message;                
                String toEmailAddress = mapMessage.getString("toEmailAddress");
                String fromEmailAddress = mapMessage.getString("fromEmailAddress");
                Long saleTransactionEntityId = (Long)mapMessage.getLong("saleTransactionEntityId");
                SaleTransactionEntity saleTransactionEntity = saleTransactionEntityControllerLocal.retrieveSaleTransactionBySaleTransactionId(saleTransactionEntityId);
                
                emailControllerLocal.emailCheckoutNotificationSync(saleTransactionEntity, fromEmailAddress, toEmailAddress);
                
                System.err.println("********** CheckoutNotificationMdb.onMessage: " + saleTransactionEntity.getSaleTransactionId() + "; " + toEmailAddress + "; " + fromEmailAddress);
            }
        }
        catch(SaleTransactionNotFoundException | JMSException ex)
        {
            System.err.println("CheckoutNotificationMdb.onMessage(): " + ex.getMessage());
        }
    }   
}