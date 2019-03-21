package pointofsalesystemv54client;

import ejb.session.stateful.CheckoutControllerRemote;
import ejb.session.stateless.CategoryEntityControllerRemote;
import ejb.session.stateless.CustomerEntityControllerRemote;
import ejb.session.stateless.EmailControllerRemote;
import ejb.session.stateless.MessageOfTheDayControllerRemote;
import ejb.session.stateless.ProductEntityControllerRemote;
import ejb.session.stateless.SaleTransactionEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import ejb.session.stateless.TagEntityControllerRemote;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;



public class Main
{
    @EJB
    private static StaffEntityControllerRemote staffEntityControllerRemote;
    @EJB
    private static ProductEntityControllerRemote productEntityControllerRemote;
    @EJB
    private static CategoryEntityControllerRemote categoryEntityControllerRemote;
    @EJB
    private static TagEntityControllerRemote tagEntityControllerRemote;
    @EJB
    private static CustomerEntityControllerRemote customerEntityControllerRemote;
    @EJB
    private static SaleTransactionEntityControllerRemote saleTransactionEntityControllerRemote;
    @EJB
    private static CheckoutControllerRemote checkoutControllerRemote;
    @EJB
    private static EmailControllerRemote emailControllerRemote;
    @EJB
    private static MessageOfTheDayControllerRemote messageOfTheDayControllerRemote;
    
    @Resource(mappedName = "jms/queueCheckoutNotification")
    private static Queue queueCheckoutNotification;
    @Resource(mappedName = "jms/queueCheckoutNotificationFactory")
    private static ConnectionFactory queueCheckoutNotificationFactory;
    
    
    
    public static void main(String[] args)
    {
        MainApp mainApp = new MainApp(staffEntityControllerRemote, productEntityControllerRemote, categoryEntityControllerRemote, tagEntityControllerRemote, customerEntityControllerRemote, saleTransactionEntityControllerRemote, checkoutControllerRemote, emailControllerRemote, messageOfTheDayControllerRemote, queueCheckoutNotification, queueCheckoutNotificationFactory);
        mainApp.runApp();
    }
}