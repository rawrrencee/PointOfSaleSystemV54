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
import entity.MessageOfTheDayEntity;
import entity.StaffEntity;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;



public class MainApp
{
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private ProductEntityControllerRemote productEntityControllerRemote;
    private CategoryEntityControllerRemote categoryEntityControllerRemote;
    private TagEntityControllerRemote tagEntityControllerRemote;
    private CustomerEntityControllerRemote customerEntityControllerRemote;
    private SaleTransactionEntityControllerRemote saleTransactionEntityControllerRemote;
    private CheckoutControllerRemote checkoutControllerRemote;
    private EmailControllerRemote emailControllerRemote;
    private MessageOfTheDayControllerRemote messageOfTheDayControllerRemote;
    
    private Queue queueCheckoutNotification;
    private ConnectionFactory queueCheckoutNotificationFactory;
    
    private CashierOperationModule cashierOperationModule;
    private SystemAdministrationModule systemAdministrationModule;
    
    private StaffEntity currentStaffEntity;
    
    
    
    public MainApp() 
    {        
    }

    
    
    public MainApp(StaffEntityControllerRemote staffEntityControllerRemote, ProductEntityControllerRemote productEntityControllerRemote, CategoryEntityControllerRemote categoryEntityControllerRemote, TagEntityControllerRemote tagEntityControllerRemote, CustomerEntityControllerRemote customerEntityControllerRemote, SaleTransactionEntityControllerRemote saleTransactionEntityControllerRemote, CheckoutControllerRemote checkoutControllerRemote, EmailControllerRemote emailControllerRemote, MessageOfTheDayControllerRemote messageOfTheDayControllerRemote, Queue queueCheckoutNotification, ConnectionFactory queueCheckoutNotificationFactory) 
    {
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.productEntityControllerRemote = productEntityControllerRemote;
        this.categoryEntityControllerRemote = categoryEntityControllerRemote;
        this.tagEntityControllerRemote = tagEntityControllerRemote;
        this.customerEntityControllerRemote = customerEntityControllerRemote;
        this.saleTransactionEntityControllerRemote = saleTransactionEntityControllerRemote;
        this.checkoutControllerRemote = checkoutControllerRemote;
        this.emailControllerRemote = emailControllerRemote;
        this.messageOfTheDayControllerRemote = messageOfTheDayControllerRemote;
        
        this.queueCheckoutNotification = queueCheckoutNotification;
        this.queueCheckoutNotificationFactory = queueCheckoutNotificationFactory;
    }
    
    
    
    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Integer response = 0;
        
        while(true)
        {
            System.out.println("\n\n\n*** Welcome to Point-of-Sale (POS) System (v5.4) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        System.out.println("==================================================\n");
                        
                        System.out.println("*** Message Of The Day ***\n");
                        List<MessageOfTheDayEntity> messageOfTheDayEntities = messageOfTheDayControllerRemote.retrieveAllMessagesOfTheDay();
                        
                        for(MessageOfTheDayEntity messageOfTheDayEntity:messageOfTheDayEntities)
                        {
                            System.out.println(outputDateFormat.format(messageOfTheDayEntity.getMessageDate()) + " - " + messageOfTheDayEntity.getTitle());
                            System.out.println(messageOfTheDayEntity.getMessage() + "\n");
                        }
                        
                        System.out.println("==================================================\n");
                        
                        cashierOperationModule = new CashierOperationModule(productEntityControllerRemote, saleTransactionEntityControllerRemote, checkoutControllerRemote, emailControllerRemote, queueCheckoutNotification, queueCheckoutNotificationFactory, currentStaffEntity);
                        systemAdministrationModule = new SystemAdministrationModule(staffEntityControllerRemote, productEntityControllerRemote, categoryEntityControllerRemote, tagEntityControllerRemote, customerEntityControllerRemote, messageOfTheDayControllerRemote, currentStaffEntity);
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
    }
    
    
    
    private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** POS System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentStaffEntity = staffEntityControllerRemote.staffLogin(username, password);      
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    
    
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Point-of-Sale (POS) System (v5.4) ***\n");
            System.out.println("You are login as " + currentStaffEntity.getFirstName() + " " + currentStaffEntity.getLastName() + " with " + currentStaffEntity.getAccessRightEnum().toString() + " rights\n");
            System.out.println("1: Cashier Operation");
            System.out.println("2: System Administration");
            System.out.println("3: Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    cashierOperationModule.menuCashierOperation();
                }
                else if(response == 2)
                {
                    try
                    {
                        systemAdministrationModule.menuSystemAdministration();
                    }
                    catch (InvalidAccessRightException ex)
                    {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 3)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }
}