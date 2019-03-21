package pointofsalesystemv54client;

import ejb.session.stateful.CheckoutControllerRemote;
import ejb.session.stateless.EmailControllerRemote;
import ejb.session.stateless.ProductEntityControllerRemote;
import ejb.session.stateless.SaleTransactionEntityControllerRemote;
import entity.ProductEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import entity.StaffEntity;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import util.exception.CreateNewSaleTransactionException;
import util.exception.ProductNotFoundException;
import util.exception.SaleTransactionAlreadyVoidedRefundedException;
import util.exception.SaleTransactionNotFoundException;
import util.exception.StaffNotFoundException;



public class CashierOperationModule
{
    private ProductEntityControllerRemote productEntityControllerRemote;
    private SaleTransactionEntityControllerRemote saleTransactionEntityControllerRemote;
    private CheckoutControllerRemote checkoutControllerRemote;
    private EmailControllerRemote emailControllerRemote;
    
    private Queue queueCheckoutNotification;
    private ConnectionFactory queueCheckoutNotificationFactory;
    
    private StaffEntity currentStaffEntity;   

    
    
    public CashierOperationModule()
    {
    }

    
    
    public CashierOperationModule(ProductEntityControllerRemote productEntityControllerRemote, SaleTransactionEntityControllerRemote saleTransactionEntityControllerRemote, CheckoutControllerRemote checkoutControllerRemote, EmailControllerRemote emailControllerRemote, Queue queueCheckoutNotification, ConnectionFactory queueCheckoutNotificationFactory, StaffEntity currentStaffEntity) 
    {
        this();
        
        this.productEntityControllerRemote = productEntityControllerRemote;
        this.saleTransactionEntityControllerRemote = saleTransactionEntityControllerRemote;
        this.checkoutControllerRemote = checkoutControllerRemote;
        this.emailControllerRemote = emailControllerRemote;
        
        this.queueCheckoutNotification = queueCheckoutNotification;
        this.queueCheckoutNotificationFactory = queueCheckoutNotificationFactory;
        
        this.currentStaffEntity = currentStaffEntity;
    }

    
    
    public void menuCashierOperation()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** POS System :: Cashier Operation ***\n");
            System.out.println("1: Checkout");
            System.out.println("2: Void/Refund");
            System.out.println("3: View My Sale Transactions");
            System.out.println("4: Back\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCheckout();
                }
                else if(response == 2)
                {
                    doVoidRefund();
                }
                else if(response == 3)
                {
                    doViewMySaleTransactions();
                }
                else if(response == 4)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    
    
    
    private void doCheckout()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        String skuCode = "";
        Integer quantity = 0;
        String moreItem = "";
        ProductEntity productEntity;
        String confirmCheckout = "";
        
        System.out.println("*** POS System :: Cashier Operation :: Checkout ***\n");
        
        do
        {
            System.out.print("Enter SKU Code> ");
            skuCode = scanner.nextLine().trim();
            
            try
            {
                productEntity = productEntityControllerRemote.retrieveProductByProductSkuCode(skuCode);
                System.out.print("Enter required Quantity for " + productEntity.getName() + "> ");
                quantity = scanner.nextInt();
                
                if(quantity > 0)
                {
                    BigDecimal subTotal = checkoutControllerRemote.addItem(productEntity, quantity);
                    System.out.println(productEntity.getName() + " added successfully!: " + quantity + " unit @ " + NumberFormat.getCurrencyInstance().format(subTotal) + "\n");
                }
                else
                {
                    System.out.println("Invalid quantity!\n");
                }
                
                scanner.nextLine().trim();
            }
            catch(ProductNotFoundException ex)
            {
                System.out.println("An error has occurred while retrieving product: " + ex.getMessage() + "\n");
            }
            
            System.out.print("More item? (Enter 'N' to complete checkout)> ");
            moreItem = scanner.nextLine().trim();
        }
        while(!moreItem.equals("N"));
        
        if(checkoutControllerRemote.getTotalLineItem() > 0)
        {
            System.out.println("Checking out the following items:\n");
            System.out.printf("\n%3s%10s%20s%11s%13s%12s", "S/N", "SKU Code", "Product Name", "Quantity", "Unit Price", "Sub-Total");
            
            for(SaleTransactionLineItemEntity saleTransactionLineItemEntity:checkoutControllerRemote.getSaleTransactionLineItemEntities())
            {
                System.out.printf("\n%3s%10s%20s%11s%13s%12s", saleTransactionLineItemEntity.getSerialNumber(), saleTransactionLineItemEntity.getProductEntity().getSkuCode(), saleTransactionLineItemEntity.getProductEntity().getName(), saleTransactionLineItemEntity.getQuantity(), NumberFormat.getCurrencyInstance().format(saleTransactionLineItemEntity.getUnitPrice()), NumberFormat.getCurrencyInstance().format(saleTransactionLineItemEntity.getSubTotal()));
            }            
            
            System.out.printf("\nTotal Line Item: %d, Total Quantity: %d, Total Amount: %s\n", checkoutControllerRemote.getTotalLineItem(), checkoutControllerRemote.getTotalQuantity(), NumberFormat.getCurrencyInstance().format(checkoutControllerRemote.getTotalAmount()));
            System.out.println("------------------------");
            System.out.print("Confirm checkout? (Enter 'Y' to complete checkout)> ");
            confirmCheckout = scanner.nextLine().trim();
            
            if(confirmCheckout.equals("Y"))
            {
                try
                {
                    SaleTransactionEntity newSaleTransactionEntity = checkoutControllerRemote.doCheckout(currentStaffEntity.getStaffId());
                    currentStaffEntity.getSaleTransactionEntities().add(newSaleTransactionEntity);
                    System.out.println("Checkout completed successfully!: " + newSaleTransactionEntity.getSaleTransactionId() + "\n");

                    // New addition to send email                    
                    System.out.print("Enter customer email to send checkout notification (blank if notification is not required)> ");
                    String toEmailAddress = scanner.nextLine().trim();
                    if(toEmailAddress.length() > 0)
                    {
                        try
                        {
                            // 01 - Synchronous Session Bean Invocation
                            // emailControllerRemote.emailCheckoutNotificationSync(newSaleTransactionEntity, "XXX<XXX@comp.nus.edu.sg>", toEmailAddress);

                            // 02 - Asynchronous Session Bean Invocation
                            // Future<Boolean> asyncResult = emailControllerRemote.emailCheckoutNotificationAsync(newSaleTransactionEntity, "XXX<XXX@comp.nus.edu.sg>", toEmailAddress);
                            // RunnableNotification runnableNotification = new RunnableNotification(asyncResult);
                            // runnableNotification.start();

                            // 03 - JMS Messaging with Message Driven Bean
                            // sendJMSMessageToQueueCheckoutNotification(newSaleTransactionEntity.getSaleTransactionId(), "XXX<XXX@comp.nus.edu.sg>", toEmailAddress);

                            System.out.println("Checkout notification email sent successfully!\n");                                                        
                        }
                        catch(Exception ex)
                        {
                            System.out.println("An error has occurred while sending the checkout notification email: " + ex.getMessage() + "\n");
                        }
                    }
                }
                catch(StaffNotFoundException ex)
                {
                    System.out.println("An error has occurred while completing the checkout: " + ex.getMessage() + "\n");
                }
                catch(CreateNewSaleTransactionException ex)
                {
                    System.out.println("An error has occurred while performing the checkout: " + ex.getMessage() + "!\n");
                }
            }
            else
            {
                System.out.println("Checkout cancelled!\n");
            }
            
            checkoutControllerRemote.clearShoppingCart();
        }
        else
        {
            System.out.println("Nothing to checkout!\n");
        }
    }
    
    
    
    private void doVoidRefund()
    {
        Scanner scanner = new Scanner(System.in);        
        String confirmVoidRefund = "";
        
        System.out.println("*** POS System :: Cashier Operation :: Void/Refund ***\n");
        System.out.print("Enter Sale Transaction ID> ");
        Long saleTransactionId = scanner.nextLong();
        
        try
        {
            SaleTransactionEntity saleTransactionEntity = saleTransactionEntityControllerRemote.retrieveSaleTransactionBySaleTransactionId(saleTransactionId);
            
            System.out.printf("\n%3s%10s%20s%11s%13s%12s", "S/N", "SKU Code", "Product Name", "Quantity", "Unit Price", "Sub-Total");
            
            for(SaleTransactionLineItemEntity saleTransactionLineItemEntity:saleTransactionEntity.getSaleTransactionLineItemEntities())
            {
                System.out.printf("\n%3s%10s%20s%11s%13s%12s", saleTransactionLineItemEntity.getSerialNumber(), saleTransactionLineItemEntity.getProductEntity().getSkuCode(), saleTransactionLineItemEntity.getProductEntity().getName(), saleTransactionLineItemEntity.getQuantity(), NumberFormat.getCurrencyInstance().format(saleTransactionLineItemEntity.getUnitPrice()), NumberFormat.getCurrencyInstance().format(saleTransactionLineItemEntity.getSubTotal()));
            }
            
            System.out.printf("\nTotal Line Item: %d, Total Quantity: %d, Total Amount: %s\n", saleTransactionEntity.getTotalLineItem(), saleTransactionEntity.getTotalQuantity(), NumberFormat.getCurrencyInstance().format(saleTransactionEntity.getTotalAmount()));
            System.out.println("\n------------------------");
            scanner.nextLine();
            System.out.print("Confirm void/refund this sale transaction? (Enter 'Y' to complete void/refund)> ");
            confirmVoidRefund = scanner.nextLine().trim();
            
            if(confirmVoidRefund.equals("Y"))
            {
                try
                {
                    saleTransactionEntityControllerRemote.voidRefundSaleTransaction(saleTransactionId);
                    System.out.println("Sale transaction void/refund completed successfully!"); 
                }
                catch(SaleTransactionNotFoundException | SaleTransactionAlreadyVoidedRefundedException ex)
                {
                    System.out.println("An error has occurred while voiding/refunding the sale transaction: " + ex.getMessage() + "\n");
                }
            }
            else
            {
                System.out.println("Void/refund cancelled!\n");
            }
        }
        catch(SaleTransactionNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving sale transaction: " + ex.getMessage() + "\n");
        }
    }
    
    
    
    private void doViewMySaleTransactions()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: Cashier Operation :: View My Sale Transaction ***\n");
        
        List<SaleTransactionEntity> saleTransactionEntities = currentStaffEntity.getSaleTransactionEntities();
        System.out.printf("%19s%18s%17s%15s%31s\n", "Sale Transaction ID", "Total Line Item", "Total Quantity", "Total Amount", "Transaction Date/Time");

        for(SaleTransactionEntity saleTransactionEntity:saleTransactionEntities)
        {
            System.out.printf("%19s%18s%17s%15s%31s\n", saleTransactionEntity.getSaleTransactionId(), saleTransactionEntity.getTotalLineItem(), saleTransactionEntity.getTotalQuantity(), NumberFormat.getCurrencyInstance().format(saleTransactionEntity.getTotalAmount()), saleTransactionEntity.getTransactionDateTime());
        }        
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    
    private void sendJMSMessageToQueueCheckoutNotification(Long saleTransactionEntityId, String fromEmailAddress, String toEmailAddress) throws JMSException 
    {
        Connection connection = null;
        Session session = null;
        try 
        {
            connection = queueCheckoutNotificationFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("fromEmailAddress", fromEmailAddress);
            mapMessage.setString("toEmailAddress", toEmailAddress);            
            mapMessage.setLong("saleTransactionEntityId", saleTransactionEntityId);
            MessageProducer messageProducer = session.createProducer(queueCheckoutNotification);
            messageProducer.send(mapMessage);
        }
        finally
        {
            if (session != null) 
            {
                try 
                {
                    session.close();
                } 
                catch (JMSException ex) 
                {
                    ex.printStackTrace();
                }
            }
            
            if (connection != null) 
            {
                connection.close();
            }
        }
    }
}