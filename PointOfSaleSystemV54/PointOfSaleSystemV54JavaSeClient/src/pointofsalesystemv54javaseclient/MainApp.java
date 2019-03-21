package pointofsalesystemv54javaseclient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import ws.client.productentity.CreateNewProductException_Exception;
import ws.client.productentity.InputDataValidationException_Exception;
import ws.client.productentity.InvalidLoginCredentialException_Exception;
import ws.client.productentity.ProductEntity;
import ws.client.productentity.ProductNotFoundException_Exception;
import ws.client.saletransactionentity.CreateNewSaleTransactionException_Exception;
import ws.client.saletransactionentity.RemoteCheckoutControllerNotFoundException_Exception;
import ws.client.saletransactionentity.RemoteCheckoutLineItem;
import ws.client.saletransactionentity.SaleTransactionEntity;
import ws.client.saletransactionentity.StaffNotFoundException_Exception;
import ws.client.saletransactionentity.UnableToCreateRemoteCheckoutControllerException_Exception;



public class MainApp 
{
    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response;
        
        while(true)
        {
            System.out.println("*** Welcome to Point-of-Sale (POS) System (v5.4) Java SE Client ***\n");
            System.out.println("1: Retrieve All Products");
            System.out.println("2: Retrieve Product by Product Id");
            System.out.println("3: Create New Product");
            System.out.println("4: Remote Checkout (Remote Client Maintains Own State)");
            System.out.println("5: Remote Checkout (Remote Client Relies on Server to Maintain State)");            
            System.out.println("6: Exit\n");
            response = 0;
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    demo1();
                }
                else if (response == 2)
                {
                    demo2();
                }
                else if (response == 3)
                {
                    demo3();
                }
                else if (response == 4)
                {
                    demo4();
                }
                else if (response == 5)
                {
                    demo5();
                }
                else if (response == 6)
                {
                    break;
                }
                else
                {
                    System.out.print("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 6)
            {
                break;
            }
        }
    }
    
    
    
    private void demo1()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Point-of-Sale (POS) System (v5.4) Java SE Client :: 1 - Retrieve All Products ***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        
        try
        {
            List<ProductEntity> productEntities = retrieveAllProducts(username, password);

            for(ProductEntity productEntity:productEntities)
            {
                System.out.println("Product: " + productEntity.getSkuCode());
            }
        }
        catch(InvalidLoginCredentialException_Exception ex)
        {
            System.out.println("An error has occurred while retrieving products: " + ex.getMessage());
        }
    }
    
    
    
    private void demo2()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Point-of-Sale (POS) System (v5.4) Java SE Client :: 2 - Retrieve Product by Product Id ***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter product id> ");        
        Long productId = scanner.nextLong();
        
        try
        {
            ProductEntity productEntity = retrieveProductByProductId(username, password, productId);

            System.out.println("Product: " + productEntity.getProductId()+ "; " + productEntity.getSkuCode());
        }
        catch(InvalidLoginCredentialException_Exception | ProductNotFoundException_Exception ex)
        {
            System.out.println("An error has occurred while retrieving product: " + ex.getMessage());
        }
    }
    
    
    
    private void demo3()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Point-of-Sale (POS) System (v5.4) Java SE Client :: 3 - Create New Product ***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        
        ProductEntity newProductEntity = new ProductEntity();
        System.out.print("Enter SKU Code> ");
        newProductEntity.setSkuCode(scanner.nextLine().trim());
        System.out.print("Enter Name> ");
        newProductEntity.setName(scanner.nextLine().trim());
        System.out.print("Enter Description> ");
        newProductEntity.setDescription(scanner.nextLine().trim());
        System.out.print("Enter Quantity On Hand> ");
        newProductEntity.setQuantityOnHand(scanner.nextInt());
        System.out.print("Enter Reorder Quantity> ");
        newProductEntity.setReorderQuantity(scanner.nextInt());
        System.out.print("Enter Unit Price> $");
        newProductEntity.setUnitPrice(scanner.nextBigDecimal());
        scanner.nextLine();
        
        // Removed in v5.0
        // System.out.print("Enter Category> ");
        // newProductEntity.setCategory(scanner.nextLine().trim());
        
        // Removed in v5.0
        // try
        // {
        //     ProductEntity productEntity = createNewProduct(username, password, newProductEntity);
        // 
        //     System.out.println("Product: " + productEntity.getProductId()+ "; " + productEntity.getSkuCode());
        // }
        // catch(InvalidLoginCredentialException_Exception | InputDataValidationException_Exception | CreateNewProductException_Exception ex)
        // {
        //     System.out.println("An error has occurred while creating the new product: " + ex.getMessage());
        // }
    }
    
    
    
    private void demo4()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Point-of-Sale (POS) System (v5.4) Java SE Client :: 4 - Remote Checkout (Remote Client Maintains Own State) ***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        
        String moreItem = "";
        List<RemoteCheckoutLineItem> remoteCheckoutLineItems = new ArrayList<>();
        
        do
        {
            RemoteCheckoutLineItem remoteCheckoutLineItem = new RemoteCheckoutLineItem();
            System.out.print("Enter SKU Code> ");
            remoteCheckoutLineItem.setSkuCode(scanner.nextLine().trim());
            System.out.print("Enter required Quantity for " + remoteCheckoutLineItem.getSkuCode() + "> ");
            remoteCheckoutLineItem.setQuantity(scanner.nextInt());
            
            remoteCheckoutLineItems.add(remoteCheckoutLineItem);

            scanner.nextLine().trim();
            System.out.print("More item? (Enter 'N' to complete checkout)> ");
            moreItem = scanner.nextLine().trim();
        }
        while(!moreItem.equals("N"));
        
        try
        {
            SaleTransactionEntity saleTransactionEntity = clientStateRemoteCheckout(username, password, remoteCheckoutLineItems);
            
            System.out.println("Remote checkout completed successfully!: Sale Transaction ID: " + saleTransactionEntity.getSaleTransactionId());
        }
        catch(ws.client.saletransactionentity.InvalidLoginCredentialException_Exception | CreateNewSaleTransactionException_Exception ex)
        {
            System.out.println("An error has occurred while performing the client state remote checkout: " + ex.getMessage());
        }
    }
    
    
    
    private void demo5()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Point-of-Sale (POS) System (v5.4) Java SE Client :: 5 - Remote Checkout (Remote Client Relies on Server to Maintain State) ***\n");
        
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();

        try
        {
            String sessionKey = serverStateRequestRemoteCheckoutController(username, password);
            
            System.out.println("Your new remote checkout session is " + sessionKey);
            
            String moreItem = "";            

            do
            {
                RemoteCheckoutLineItem remoteCheckoutLineItem = new RemoteCheckoutLineItem();
                System.out.print("Enter SKU Code> ");
                remoteCheckoutLineItem.setSkuCode(scanner.nextLine().trim());
                System.out.print("Enter required Quantity for " + remoteCheckoutLineItem.getSkuCode() + "> ");
                remoteCheckoutLineItem.setQuantity(scanner.nextInt());
                
                serverStateRemoteAddItem(username, password, sessionKey, remoteCheckoutLineItem);                

                scanner.nextLine().trim();
                System.out.print("More item? (Enter 'N' to complete checkout)> ");
                moreItem = scanner.nextLine().trim();
            }
            while(!moreItem.equals("N"));
            
            SaleTransactionEntity saleTransactionEntity = serverStateRemoteCheckout(username, password, sessionKey);

            System.out.println("Remote checkout completed successfully!: Sale Transaction ID: " + saleTransactionEntity.getSaleTransactionId());
        }
        catch(ws.client.saletransactionentity.InvalidLoginCredentialException_Exception | UnableToCreateRemoteCheckoutControllerException_Exception | RemoteCheckoutControllerNotFoundException_Exception | StaffNotFoundException_Exception | ws.client.saletransactionentity.ProductNotFoundException_Exception | CreateNewSaleTransactionException_Exception ex)
        {
            System.out.println("An error has occurred while performing the server state remote checkout: " + ex.getMessage());
        }
    }

    
    
    private static java.util.List<ws.client.productentity.ProductEntity> retrieveAllProducts(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialException_Exception {
        ws.client.productentity.ProductEntityWebService_Service service = new ws.client.productentity.ProductEntityWebService_Service();
        ws.client.productentity.ProductEntityWebService port = service.getProductEntityWebServicePort();
        return port.retrieveAllProducts(username, password);
    }

    
    
    private static ProductEntity retrieveProductByProductId(java.lang.String username, java.lang.String password, java.lang.Long productId) throws InvalidLoginCredentialException_Exception, ProductNotFoundException_Exception {
        ws.client.productentity.ProductEntityWebService_Service service = new ws.client.productentity.ProductEntityWebService_Service();
        ws.client.productentity.ProductEntityWebService port = service.getProductEntityWebServicePort();
        return port.retrieveProductByProductId(username, password, productId);
    }

    
    
    private static ProductEntity createNewProduct(java.lang.String username, java.lang.String password, ws.client.productentity.ProductEntity newProductEntity, Long categoryId) throws InvalidLoginCredentialException_Exception, InputDataValidationException_Exception, CreateNewProductException_Exception {
        ws.client.productentity.ProductEntityWebService_Service service = new ws.client.productentity.ProductEntityWebService_Service();
        ws.client.productentity.ProductEntityWebService port = service.getProductEntityWebServicePort();
        return port.createNewProduct(username, password, newProductEntity, categoryId);
    }

    
    
    private static SaleTransactionEntity clientStateRemoteCheckout(java.lang.String username, java.lang.String password, java.util.List<ws.client.saletransactionentity.RemoteCheckoutLineItem> remoteCheckoutLineItems) throws CreateNewSaleTransactionException_Exception, ws.client.saletransactionentity.InvalidLoginCredentialException_Exception {
        ws.client.saletransactionentity.SaleTransactionEntityWebService_Service service = new ws.client.saletransactionentity.SaleTransactionEntityWebService_Service();
        ws.client.saletransactionentity.SaleTransactionEntityWebService port = service.getSaleTransactionEntityWebServicePort();
        return port.clientStateRemoteCheckout(username, password, remoteCheckoutLineItems);
    }

    
    
    private static String serverStateRequestRemoteCheckoutController(java.lang.String username, java.lang.String password) throws ws.client.saletransactionentity.InvalidLoginCredentialException_Exception, UnableToCreateRemoteCheckoutControllerException_Exception {
        ws.client.saletransactionentity.SaleTransactionEntityWebService_Service service = new ws.client.saletransactionentity.SaleTransactionEntityWebService_Service();
        ws.client.saletransactionentity.SaleTransactionEntityWebService port = service.getSaleTransactionEntityWebServicePort();
        return port.serverStateRequestRemoteCheckoutController(username, password);
    }

    
    
    private static BigDecimal serverStateRemoteAddItem(java.lang.String username, java.lang.String password, java.lang.String sessionKey, ws.client.saletransactionentity.RemoteCheckoutLineItem remoteCheckoutLineItem) throws ws.client.saletransactionentity.ProductNotFoundException_Exception, ws.client.saletransactionentity.InvalidLoginCredentialException_Exception, RemoteCheckoutControllerNotFoundException_Exception {
        ws.client.saletransactionentity.SaleTransactionEntityWebService_Service service = new ws.client.saletransactionentity.SaleTransactionEntityWebService_Service();
        ws.client.saletransactionentity.SaleTransactionEntityWebService port = service.getSaleTransactionEntityWebServicePort();
        return port.serverStateRemoteAddItem(username, password, sessionKey, remoteCheckoutLineItem);
    }

    
    
    private static SaleTransactionEntity serverStateRemoteCheckout(java.lang.String username, java.lang.String password, java.lang.String sessionKey) throws ws.client.saletransactionentity.InvalidLoginCredentialException_Exception, RemoteCheckoutControllerNotFoundException_Exception, StaffNotFoundException_Exception, CreateNewSaleTransactionException_Exception {
        ws.client.saletransactionentity.SaleTransactionEntityWebService_Service service = new ws.client.saletransactionentity.SaleTransactionEntityWebService_Service();
        ws.client.saletransactionentity.SaleTransactionEntityWebService port = service.getSaleTransactionEntityWebServicePort();
        return port.serverStateRemoteCheckout(username, password, sessionKey);
    }
}