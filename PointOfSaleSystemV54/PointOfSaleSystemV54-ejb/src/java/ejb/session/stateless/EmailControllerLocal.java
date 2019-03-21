package ejb.session.stateless;

import entity.SaleTransactionEntity;
import java.util.concurrent.Future;



public interface EmailControllerLocal
{
    public Boolean emailCheckoutNotificationSync(SaleTransactionEntity saleTransactionEntity, String fromEmailAddress, String toEmailAddress);
    
    public Future<Boolean> emailCheckoutNotificationAsync(SaleTransactionEntity saleTransactionEntity, String fromEmailAddress, String toEmailAddress) throws InterruptedException;
}
