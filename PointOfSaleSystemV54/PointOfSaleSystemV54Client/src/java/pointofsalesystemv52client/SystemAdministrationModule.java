package pointofsalesystemv54client;

import ejb.session.stateless.CategoryEntityControllerRemote;
import ejb.session.stateless.CustomerEntityControllerRemote;
import ejb.session.stateless.MessageOfTheDayControllerRemote;
import ejb.session.stateless.ProductEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import ejb.session.stateless.TagEntityControllerRemote;
import entity.CategoryEntity;
import entity.MessageOfTheDayEntity;
import entity.ProductEntity;
import entity.StaffEntity;
import entity.TagEntity;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.AccessRightEnum;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.CreateNewProductException;
import util.exception.CreateNewTagException;
import util.exception.DeleteCategoryException;
import util.exception.DeleteProductException;
import util.exception.DeleteStaffException;
import util.exception.DeleteTagException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.ProductNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UpdateCategoryException;
import util.exception.UpdateProductException;
import util.exception.UpdateStaffException;
import util.exception.UpdateTagException;



public class SystemAdministrationModule
{
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
        
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private ProductEntityControllerRemote productEntityControllerRemote;
    private CategoryEntityControllerRemote categoryEntityControllerRemote;
    private TagEntityControllerRemote tagEntityControllerRemote;
    private CustomerEntityControllerRemote customerEntityControllerRemote;
    private MessageOfTheDayControllerRemote messageOfTheDayControllerRemote;
    
    private StaffEntity currentStaffEntity;

    
    
    public SystemAdministrationModule()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    
    
    public SystemAdministrationModule(StaffEntityControllerRemote staffEntityControllerRemote, ProductEntityControllerRemote productEntityControllerRemote, CategoryEntityControllerRemote categoryEntityControllerRemote, TagEntityControllerRemote tagEntityControllerRemote, CustomerEntityControllerRemote customerEntityControllerRemote, MessageOfTheDayControllerRemote messageOfTheDayControllerRemote, StaffEntity currentStaffEntity)
    {
        this();
        
        this.staffEntityControllerRemote = staffEntityControllerRemote;
        this.productEntityControllerRemote = productEntityControllerRemote;
        this.categoryEntityControllerRemote = categoryEntityControllerRemote;
        this.tagEntityControllerRemote = tagEntityControllerRemote;
        this.customerEntityControllerRemote = customerEntityControllerRemote;
        this.messageOfTheDayControllerRemote = messageOfTheDayControllerRemote;
        this.currentStaffEntity = currentStaffEntity;
    }
    
    
    
    public void menuSystemAdministration() throws InvalidAccessRightException
    {
        if(currentStaffEntity.getAccessRightEnum() != AccessRightEnum.MANAGER)
        {
            throw new InvalidAccessRightException("You don't have MANAGER rights to access the system administration module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** POS System :: System Administration ***\n");
            System.out.println("1 : Create New Staff");
            System.out.println("2 : View Staff Details");
            System.out.println("3 : View All Staffs");
            System.out.println("---------------------------------");
            System.out.println("4 : Create New Product");
            System.out.println("5 : View Product Details");
            System.out.println("6 : View All Products");
            System.out.println("7 : Search Products by Name");
            System.out.println("8 : Filter Products by Category");
            System.out.println("9 : Filter Products by Tags");
            System.out.println("---------------------------------");
            System.out.println("10: Create New Category");
            System.out.println("11: View All Categories");
            System.out.println("12: Update Category");
            System.out.println("13: Delete Category");
            System.out.println("---------------------------------");
            System.out.println("14: Create New Tag");
            System.out.println("15: View All Tags");
            System.out.println("16: Update Tag");
            System.out.println("17: Delete Tag");
            System.out.println("---------------------------------");
            System.out.println("18: Register New Customer");
            System.out.println("19: View Customer Details");
            System.out.println("20: View All Customers");
            System.out.println("---------------------------------");
            System.out.println("21: Create New Message Of The Day");
            System.out.println("---------------------------------");
            System.out.println("22: Back\n");
            response = 0;
            
            while(response < 1 || response > 22)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateNewStaff();
                }
                else if(response == 2)
                {
                    doViewStaffDetails();
                }
                else if(response == 3)
                {
                    doViewAllStaffs();
                }
                else if(response == 4)
                {
                    doCreateNewProduct();
                }
                else if(response == 5)
                {
                    doViewProductDetails();
                }
                else if(response == 6)
                {
                    doViewAllProducts();
                }
                else if(response == 7)
                {
                    doSearchProductsByName();
                }
                else if(response == 8)
                {
                    doFilterProductsByCategory();
                }
                else if(response == 9)
                {
                    doFilterProductsByTags();
                }
                else if(response == 10)
                {
                    doCreateNewCategory();
                }
                else if(response == 11)
                {
                    doViewAllCategories();
                }
                else if(response == 12)
                {
                    doUpdateCategory();
                }
                else if(response == 13)
                {
                    doDeleteCategory();
                }
                else if(response == 14)
                {
                    doCreateNewTag();
                }
                else if(response == 15)
                {
                    doViewAllTags();
                }
                else if(response == 16)
                {
                    doUpdateTag();
                }
                else if(response == 17)
                {
                    doDeleteTag();
                }
                else if(response == 18)
                {
                    doRegisterNewCustomer();
                }
                else if(response == 19)
                {
                    doViewCustomerDetails();
                }
                else if(response == 20)
                {
                    doViewAllCustomers();
                }
                else if(response == 21)
                {
                    doCreateNewMessageOfTheDay();
                }
                else if (response == 22)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 22)
            {
                break;
            }
        }
    }
    
    
    
    // Updated in v4.2 with bean validation
    
    private void doCreateNewStaff()
    {
        Scanner scanner = new Scanner(System.in);
        StaffEntity newStaffEntity = new StaffEntity();
        
        System.out.println("*** POS System :: System Administration :: Create New Staff ***\n");
        System.out.print("Enter First Name> ");
        newStaffEntity.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newStaffEntity.setLastName(scanner.nextLine().trim());
        
        while(true)
        {
            System.out.print("Select Access Right (1: Cashier, 2: Manager)> ");
            Integer accessRightInt = scanner.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 2)
            {
                newStaffEntity.setAccessRightEnum(AccessRightEnum.values()[accessRightInt-1]);
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        scanner.nextLine();
        System.out.print("Enter Username> ");
        newStaffEntity.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newStaffEntity.setPassword(scanner.nextLine().trim());
        
        Set<ConstraintViolation<StaffEntity>>constraintViolations = validator.validate(newStaffEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                newStaffEntity = staffEntityControllerRemote.createNewStaff(newStaffEntity);
                System.out.println("New staff created successfully!: " + newStaffEntity.getStaffId() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForStaffEntity(constraintViolations);
        }
    }
    
    
    
    private void doViewStaffDetails()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** POS System :: System Administration :: View Staff Details ***\n");
        System.out.print("Enter Staff ID> ");
        Long staffId = scanner.nextLong();
        
        try
        {
            StaffEntity staffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(staffId);
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Staff ID", "First Name", "Last Name", "Access Right", "Username", "Password");
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", staffEntity.getStaffId().toString(), staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getAccessRightEnum().toString(), staffEntity.getUsername(), staffEntity.getPassword());         
            System.out.println("------------------------");
            System.out.println("1: Update Staff");
            System.out.println("2: Delete Staff");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateStaff(staffEntity);
            }
            else if(response == 2)
            {
                doDeleteStaff(staffEntity);
            }
        }
        catch(StaffNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving staff: " + ex.getMessage() + "\n");
        }
    }
    
    
    
    // Updated in v4.2 with bean validation
    
    private void doUpdateStaff(StaffEntity staffEntity)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** POS System :: System Administration :: View Staff Details :: Update Staff ***\n");
        System.out.print("Enter First Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            staffEntity.setFirstName(input);
        }
                
        System.out.print("Enter Last Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            staffEntity.setLastName(input);
        }
        
        while(true)
        {
            System.out.print("Select Access Right (0: No Change, 1: Cashier, 2: Manager)> ");
            Integer accessRightInt = scanner.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 2)
            {
                staffEntity.setAccessRightEnum(AccessRightEnum.values()[accessRightInt-1]);
                break;
            }
            else if (accessRightInt == 0)
            {
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }                
        
        Set<ConstraintViolation<StaffEntity>>constraintViolations = validator.validate(staffEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                staffEntityControllerRemote.updateStaff(staffEntity);
                System.out.println("Staff updated successfully!\n");
            }             
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
            catch (StaffNotFoundException | UpdateStaffException ex) 
            {
                System.out.println("An error has occurred while updating staff: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForStaffEntity(constraintViolations);
        }
    }
    
    
    
    private void doDeleteStaff(StaffEntity staffEntity)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** POS System :: System Administration :: View Staff Details :: Delete Staff ***\n");
        System.out.printf("Confirm Delete Staff %s %s (Staff ID: %d) (Enter 'Y' to Delete)> ", staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getStaffId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try 
            {
                staffEntityControllerRemote.deleteStaff(staffEntity.getStaffId());
                System.out.println("Staff deleted successfully!\n");
            }
            catch (StaffNotFoundException | DeleteStaffException ex) 
            {
                System.out.println("An error has occurred while deleting staff: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("Staff NOT deleted!\n");
        }
    }
    
    
    
    private void doViewAllStaffs()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: View All Staffs ***\n");
        
        List<StaffEntity> staffEntities = staffEntityControllerRemote.retrieveAllStaffs();
        System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Staff ID", "First Name", "Last Name", "Access Right", "Username", "Password");

        for(StaffEntity staffEntity:staffEntities)
        {
            System.out.printf("%8s%20s%20s%15s%20s%20s\n", staffEntity.getStaffId().toString(), staffEntity.getFirstName(), staffEntity.getLastName(), staffEntity.getAccessRightEnum().toString(), staffEntity.getUsername(), staffEntity.getPassword());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    
    // Updated in v4.2 to include reorderQuantity
    // Updated in v5.1 to handle category entity and tag entity processing
    
    private void doCreateNewProduct()
    {
        Scanner scanner = new Scanner(System.in);
        ProductEntity newProductEntity = new ProductEntity();
        Long categoryId = null;
        List<Long> tagIds = null;
        List<TagEntity> tagEntities = null;
        String addTag;
        
        System.out.println("*** POS System :: System Administration :: Create New Product ***\n");
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
        System.out.print("Enter Product Rating> ");
        newProductEntity.setProductRating(scanner.nextInt());
        
        System.out.println("Please Select a Category: ");            
        List<CategoryEntity> categoryEntities = categoryEntityControllerRemote.retrieveAllLeafCategories();
        System.out.printf("%3s %-33s %-33s\n", "ID", "Name", "Parent Category");

        for(CategoryEntity categoryEntity:categoryEntities)
        {
            System.out.printf("%3s %-33s %-33s\n", categoryEntity.getCategoryId(), categoryEntity.getName(), (categoryEntity.getParentCategoryEntity() != null)?(categoryEntity.getParentCategoryEntity().getName()):("-"));
        }

        System.out.print("Enter ID of Selected Category> ");
        categoryId = scanner.nextLong();
        scanner.nextLine();
        
        do
        {
            System.out.print("Add a Tag? (Enter 'Y' to add a Tag)> ");
            addTag = scanner.nextLine().trim();
            
            if(!addTag.equals("Y"))
            {
                break;
            }
            
            if(tagIds == null)
            {
                tagIds = new ArrayList<>();
            }
            
            if(tagEntities == null)
            {
                tagEntities = tagEntityControllerRemote.retrieveAllTags();
            }
            
            System.out.printf("%3s %-33s\n", "ID", "Tag");

            for(TagEntity tagEntity:tagEntities)
            {
                System.out.printf("%3s %-33s\n", tagEntity.getTagId(), tagEntity.getName());
            }

            System.out.print("Enter ID of Selected Tag> ");
            tagIds.add(scanner.nextLong());
            scanner.nextLine();
        }
        while(true);
        
        Set<ConstraintViolation<ProductEntity>>constraintViolations = validator.validate(newProductEntity);
        
        if(constraintViolations.isEmpty())
        {
            // Updated in v5.1
            
            try
            {
                newProductEntity = productEntityControllerRemote.createNewProduct(newProductEntity, categoryId, tagIds);

                System.out.println("New product created successfully!: " + newProductEntity.getProductId() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
            catch(CreateNewProductException ex)
            {
                System.out.println("An error has occurred while creating the new product: " + ex.getMessage() + "\n");
            }           
        }
        else
        {
            showInputDataValidationErrorsForProductEntity(constraintViolations);
        }
    }
    
    
    
    // Updated in v4.2 to include reorderQuantity
    
    private void doViewProductDetails()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** POS System :: System Administration :: View Product Details ***\n");
        System.out.print("Enter SKU Code> ");
        String skuCode = scanner.nextLine().trim();
        
        try
        {
            ProductEntity productEntity = productEntityControllerRemote.retrieveProductByProductSkuCode(skuCode);
            System.out.printf("%10s%20s%20s%20s%20s%13s%20s\n", "SKU Code", "Name", "Description", "Quantity On Hand", "Reorder Quantity", "Unit Price", "Category");
            System.out.printf("%10s%20s%20s%20s%20d%13s%20s\n", productEntity.getSkuCode(), productEntity.getName(), productEntity.getDescription(), productEntity.getQuantityOnHand(), productEntity.getReorderQuantity(), NumberFormat.getCurrencyInstance().format(productEntity.getUnitPrice()), productEntity.getCategoryEntity().getName());
            System.out.println("\nTags:");
            
            for(TagEntity tagEntity:productEntity.getTagEntities())
            {
                System.out.println("\t" + tagEntity.getName());
            }
            
            System.out.println("------------------------");
            System.out.println("1: Update Product");
            System.out.println("2: Delete Product");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if(response == 1)
            {
                doUpdateProduct(productEntity);
            }
            else if(response == 2)
            {
                doDeleteProduct(productEntity);
            }
        }
        catch(ProductNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving product: " + ex.getMessage() + "\n");
        }
    }
    
    
    
    // Updated in v4.2 to include reorderQuantity and bean validation
    // Updated in v5.1 to handle category entity and tag entity processing
    
    private void doUpdateProduct(ProductEntity productEntity)
    {
        Scanner scanner = new Scanner(System.in);        
        String input;
        Integer integerInput;
        BigDecimal bigDecimalInput;
        Long categoryId = null;
        List<Long> tagIds = null;
        List<TagEntity> tagEntities = null;
        String addTag;
        
        System.out.println("*** POS System :: System Administration :: View Product Details :: Update Product ***\n");        
        System.out.print("Enter Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            productEntity.setName(input);
        }
        
        System.out.print("Enter Description (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            productEntity.setDescription(input);
        }
        
        System.out.print("Enter Quantity On Hand (negative number if no change)> ");
        integerInput = scanner.nextInt();
        if(integerInput >= 0)
        {
            productEntity.setQuantityOnHand(integerInput);
        }
        
        System.out.print("Enter Reorder Quantity (negative number if no change)> ");
        integerInput = scanner.nextInt();
        if(integerInput >= 0)
        {
            productEntity.setReorderQuantity(integerInput);
        }
        
        System.out.print("Enter Unit Price (zero or negative amount if no change)> $");
        bigDecimalInput = scanner.nextBigDecimal();
        if(bigDecimalInput.compareTo(BigDecimal.ZERO) > 0)
        {
            productEntity.setUnitPrice(bigDecimalInput);
        }
        
        System.out.print("Enter Product Rating (zero or negative number if no change)> ");
        integerInput = scanner.nextInt();
        if(integerInput > 0)
        {
            productEntity.setProductRating(integerInput);
        }
        
        scanner.nextLine();        
        System.out.println("Do You Want to Change Category? (Enter 'Y' to change)> ");
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            System.out.println("Please Select a Category: ");
            List<CategoryEntity> categoryEntities = categoryEntityControllerRemote.retrieveAllLeafCategories();
            System.out.printf("%3s %-33s %-33s\n", "ID", "Name", "Parent Category");

            for(CategoryEntity categoryEntity:categoryEntities)
            {
                System.out.printf("%3s %-33s %-33s\n", categoryEntity.getCategoryId(), categoryEntity.getName(), (categoryEntity.getParentCategoryEntity() != null)?(categoryEntity.getParentCategoryEntity().getName()):("-"));
            }

            System.out.print("Enter ID of Selected Category> ");
            categoryId = scanner.nextLong();
            scanner.nextLine();
        }
        
        System.out.println("Do You Want to Assign a New Set of Tag(s)? (Enter 'Y' to assign)> ");
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            tagIds = new ArrayList<>();
            
            do
            {
                System.out.print("Add a Tag? (Enter 'Y' to add a Tag)> ");
                addTag = scanner.nextLine().trim();

                if(!addTag.equals("Y"))
                {
                    break;
                }

                if(tagEntities == null)
                {
                    tagEntities = tagEntityControllerRemote.retrieveAllTags();
                }

                System.out.printf("%3s %-33s\n", "ID", "Tag");

                for(TagEntity tagEntity:tagEntities)
                {
                    System.out.printf("%3s %-33s\n", tagEntity.getTagId(), tagEntity.getName());
                }

                System.out.print("Enter ID of Selected Tag> ");
                tagIds.add(scanner.nextLong());
                scanner.nextLine();
            }
            while(true);
        }               
        
        Set<ConstraintViolation<ProductEntity>>constraintViolations = validator.validate(productEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                productEntityControllerRemote.updateProduct(productEntity, categoryId, tagIds);
                System.out.println("Product updated successfully!\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
            catch(ProductNotFoundException | CategoryNotFoundException | TagNotFoundException | UpdateProductException ex) 
            {
                System.out.println("An error has occurred while updating product: " + ex.getMessage() + "\n");
            }            
        }
        else
        {
            showInputDataValidationErrorsForProductEntity(constraintViolations);
        }
    }
    
    
    
    private void doDeleteProduct(ProductEntity productEntity)
    {
        Scanner scanner = new Scanner(System.in);     
        String input;
        
        System.out.println("*** POS System :: System Administration :: View Product Details :: Delete Product ***\n");
        System.out.printf("Confirm Delete Product %s (SKU Code: %s) (Enter 'Y' to Delete)> ", productEntity.getName(), productEntity.getSkuCode());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try 
            {
                productEntityControllerRemote.deleteProduct(productEntity.getProductId());
                System.out.println("Product deleted successfully!\n");
            } 
            catch (ProductNotFoundException | DeleteProductException ex) 
            {
                System.out.println("An error has occurred while deleting product: " + ex.getMessage() + "\n");
            }
        }
        else
        {
            System.out.println("Product NOT deleted!\n");
        }
    }
    
    
    
    private void doViewAllProducts()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: View All Products ***\n");
        
        List<ProductEntity> productEntities = productEntityControllerRemote.retrieveAllProducts();
        System.out.printf("%10s%20s%20s%20s%13s%20s\n", "SKU Code", "Name", "Description", "Quantity On Hand", "Unit Price", "Category");

        for(ProductEntity productEntity:productEntities)
        {
            System.out.printf("%10s%20s%20s%20d%13s%20s\n", productEntity.getSkuCode(), productEntity.getName(), productEntity.getDescription(), productEntity.getQuantityOnHand(), NumberFormat.getCurrencyInstance().format(productEntity.getUnitPrice()), productEntity.getCategoryEntity().getName());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
    
    
    
    // Newly added in v5.1
    
    private void doSearchProductsByName()
    {
        Scanner scanner = new Scanner(System.in);
        String searchString;
        
        System.out.println("*** POS System :: System Administration :: Search Products By Name ***\n");
        System.out.print("Enter Search String> ");
        searchString = scanner.nextLine().trim();
        
        if(searchString.length() > 0)
        {       
            List<ProductEntity> productEntities = productEntityControllerRemote.searchProductsByName(searchString);
            System.out.printf("%10s%20s%20s%20s%13s%20s\n", "SKU Code", "Name", "Description", "Quantity On Hand", "Unit Price", "Category");

            for(ProductEntity productEntity:productEntities)
            {
                System.out.printf("%10s%20s%20s%20d%13s%20s\n", productEntity.getSkuCode(), productEntity.getName(), productEntity.getDescription(), productEntity.getQuantityOnHand(), NumberFormat.getCurrencyInstance().format(productEntity.getUnitPrice()), productEntity.getCategoryEntity().getName());
            }

            System.out.print("Press any key to continue...> ");
            scanner.nextLine();
        }
        else
        {
            System.out.println("Search string is required!\n");
        }
    }
    
    
    
    // Newly added in v5.1
    
    private void doFilterProductsByCategory()
    {
        Scanner scanner = new Scanner(System.in);
        Long categoryId;
        
        System.out.println("*** POS System :: System Administration :: Filter Products By Category ***\n");
        System.out.println("Please Select a Category: ");            
        List<CategoryEntity> categoryEntities = categoryEntityControllerRemote.retrieveAllCategories();
        System.out.printf("%3s %-33s %-33s\n", "ID", "Name", "Parent Category");

        for(CategoryEntity categoryEntity:categoryEntities)
        {
            System.out.printf("%3s %-33s %-33s\n", categoryEntity.getCategoryId(), categoryEntity.getName(), (categoryEntity.getParentCategoryEntity() != null)?(categoryEntity.getParentCategoryEntity().getName()):("-"));
        }

        System.out.print("Enter ID of Selected Category> ");
        categoryId = scanner.nextLong();
        scanner.nextLine();                
        
        try
        {
            List<ProductEntity> productEntities = productEntityControllerRemote.filterProductsByCategory(categoryId);
            System.out.printf("%10s%20s%20s%20s%13s%20s\n", "SKU Code", "Name", "Description", "Quantity On Hand", "Unit Price", "Category");

            for(ProductEntity productEntity:productEntities)
            {
                System.out.printf("%10s%20s%20s%20d%13s%20s\n", productEntity.getSkuCode(), productEntity.getName(), productEntity.getDescription(), productEntity.getQuantityOnHand(), NumberFormat.getCurrencyInstance().format(productEntity.getUnitPrice()), productEntity.getCategoryEntity().getName());
            }

            System.out.print("Press any key to continue...> ");
            scanner.nextLine();
        }
        catch(CategoryNotFoundException ex)
        {
            System.out.println("An error has occurred while filtering products: " + ex.getMessage() + "\n");
        }
    }
    
    
    
    // Newly added in v5.1
    
    private void doFilterProductsByTags()
    {
        Scanner scanner = new Scanner(System.in);
        List<Long> tagIds = null;
        List<TagEntity> tagEntities = null;
        String addTag;
        String condition = "OR";
        
        System.out.println("*** POS System :: System Administration :: Filter Products By Tags ***\n");
        
        do
        {
            System.out.print("Add a Tag? (Enter 'Y' to add a Tag)> ");
            addTag = scanner.nextLine().trim();
            
            if(!addTag.equals("Y"))
            {
                break;
            }
            
            if(tagIds == null)
            {
                tagIds = new ArrayList<>();
            }
            
            if(tagEntities == null)
            {
                tagEntities = tagEntityControllerRemote.retrieveAllTags();
            }
            
            System.out.printf("%3s %-33s\n", "ID", "Tag");

            for(TagEntity tagEntity:tagEntities)
            {
                System.out.printf("%3s %-33s\n", tagEntity.getTagId(), tagEntity.getName());
            }

            System.out.print("Enter ID of Selected Tag> ");
            tagIds.add(scanner.nextLong());
            scanner.nextLine();
        }
        while(true);
        
        if(tagIds == null || tagIds.isEmpty())
        {
            System.out.println("Unable to filter without any tag!\n");
        }
        else
        {
            if(tagIds.size() > 1)
            {
                System.out.print("You have Selected Multiple Tags. Enter Filter Condition (AND or OR)> ");
                condition = scanner.nextLine().trim();
            }
            
            if(condition.equals("OR") || condition.equals("AND"))
            {
                List<ProductEntity> productEntities = productEntityControllerRemote.filterProductsByTags(tagIds, condition);
                System.out.printf("%10s%20s%20s%20s%13s%20s\n", "SKU Code", "Name", "Description", "Quantity On Hand", "Unit Price", "Category");

                for(ProductEntity productEntity:productEntities)
                {
                    System.out.printf("%10s%20s%20s%20d%13s%20s\n", productEntity.getSkuCode(), productEntity.getName(), productEntity.getDescription(), productEntity.getQuantityOnHand(), NumberFormat.getCurrencyInstance().format(productEntity.getUnitPrice()), productEntity.getCategoryEntity().getName());
                }

                System.out.print("Press any key to continue...> ");
                scanner.nextLine();
            }
            else
            {
                System.out.println("Invalid filter condition!\n");
            }
        }        
    }
    
    
    
    // Newly added in v5.1
    
    private void doCreateNewCategory()
    {
        Scanner scanner = new Scanner(System.in);
        CategoryEntity newCategoryEntity = new CategoryEntity();
        String rootCategory;
        Long parentCategoryId = null;
        
        System.out.println("*** POS System :: System Administration :: Create New Category ***\n");
        System.out.print("Enter Name> ");
        newCategoryEntity.setName(scanner.nextLine().trim());
        System.out.print("Enter Description> ");
        newCategoryEntity.setDescription(scanner.nextLine().trim());
        System.out.print("Is this a Root Category, i.e., No Parent Category? (Enter 'Y' for Root Category)> ");
        rootCategory = scanner.nextLine().trim();
        
        if(!rootCategory.equals("Y"))
        {
            System.out.println("Please Select a Category below as the Parent Category: ");
            
            List<CategoryEntity> categoryEntities = categoryEntityControllerRemote.retrieveAllCategoriesWithoutProduct();
            System.out.printf("%3s %-33s %-33s\n", "ID", "Name", "Parent Category");

            for(CategoryEntity categoryEntity:categoryEntities)
            {
                System.out.printf("%3s %-33s %-33s\n", categoryEntity.getCategoryId(), categoryEntity.getName(), (categoryEntity.getParentCategoryEntity() != null)?(categoryEntity.getParentCategoryEntity().getName()):("-"));
            }

            System.out.print("Enter ID of Selected Parent Category> ");
            parentCategoryId = scanner.nextLong();
        }
        
        Set<ConstraintViolation<CategoryEntity>>constraintViolations = validator.validate(newCategoryEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                newCategoryEntity = categoryEntityControllerRemote.createNewCategoryEntity(newCategoryEntity, parentCategoryId);

                System.out.println("New category created successfully!: " + newCategoryEntity.getCategoryId() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
            catch(CreateNewCategoryException ex)
            {
                System.out.println("An error has occurred while creating the new category: " + ex.getMessage() + "\n");
            }                        
        }
        else
        {
            showInputDataValidationErrorsForCategoryEntity(constraintViolations);
        }
    }
    
    
    
    // Newly added in v5.1
    
    private void doViewAllCategories()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: View All Categories ***\n");
        
        List<CategoryEntity> categoryEntities = categoryEntityControllerRemote.retrieveAllCategories();
        System.out.printf("%3s %-33s %-65s %-33s%19s %13s\n", "ID", "Name", "Description", "Parent Category", "Sub-Category Count", "Product Count");

        for(CategoryEntity categoryEntity:categoryEntities)
        {
            System.out.printf("%3s %-33s %-65s %-33s%19s %13s\n", categoryEntity.getCategoryId(), categoryEntity.getName(), categoryEntity.getDescription(), (categoryEntity.getParentCategoryEntity() != null)?(categoryEntity.getParentCategoryEntity().getName()):("-"), categoryEntity.getSubCategoryEntities().size(), categoryEntity.getProductEntities().size());
        }
        
        System.out.println("\n==============================\n");
        
        List<CategoryEntity> rootCategoryEntities = categoryEntityControllerRemote.retrieveAllRootCategories();
        
        for(CategoryEntity rootCategoryEntity:rootCategoryEntities)
        {
            printCategoryTreeEntry(rootCategoryEntity, 0);
        }
        
        System.out.print("\nPress any key to continue...> ");
        scanner.nextLine();
    }
    
    
    
    private void doUpdateCategory()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            Long categoryToUpdateId;
            CategoryEntity categoryEntityToUpdate;
            String input;
            Long parentCategoryId;

            System.out.println("*** POS System :: System Administration :: Update Category ***\n");
            System.out.print("Enter ID of Category to Update> ");
            categoryToUpdateId = scanner.nextLong();
            scanner.nextLine();
            categoryEntityToUpdate = categoryEntityControllerRemote.retrieveCategoryByCategoryId(categoryToUpdateId);            
            
            System.out.println("Name: " + categoryEntityToUpdate.getName());
            System.out.print("Enter Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if(input.length() > 0)
            {
                categoryEntityToUpdate.setName(input);
            }

            System.out.println("Description: " + categoryEntityToUpdate.getDescription());
            System.out.print("Enter Description (blank if no change)> ");
            input = scanner.nextLine().trim();
            if(input.length() > 0)
            {
                categoryEntityToUpdate.setDescription(input);
            }

            String parentCategory = (categoryEntityToUpdate.getParentCategoryEntity() == null)?("-"):(categoryEntityToUpdate.getParentCategoryEntity().getName() + " (ID: " + categoryEntityToUpdate.getParentCategoryEntity().getCategoryId() + ")");
            System.out.println("Parent Category: " + parentCategory);
            System.out.print("Would you like to change the Parent Category (Enter 'Y' to change)>");
            input = scanner.nextLine().trim();

            if(input.equals("Y"))
            {
                System.out.print("Is this a Root Category, i.e., No Parent Category? (Enter 'Y' for Root Category)> ");
                input = scanner.nextLine().trim();

                if(!input.equals("Y"))
                {
                    System.out.println("Please Select a Category below as the Parent Category: ");

                    List<CategoryEntity> categoryEntities = categoryEntityControllerRemote.retrieveAllCategoriesWithoutProduct();
                    System.out.printf("%3s %-33s %-33s\n", "ID", "Name", "Parent Category");

                    for(CategoryEntity categoryEntity:categoryEntities)
                    {
                        if(!categoryEntity.getCategoryId().equals(categoryEntityToUpdate.getCategoryId()))
                        {
                            System.out.printf("%3s %-33s %-33s\n", categoryEntity.getCategoryId(), categoryEntity.getName(), (categoryEntity.getParentCategoryEntity() != null)?(categoryEntity.getParentCategoryEntity().getName()):("-"));
                        }
                    }

                    System.out.print("Enter ID of Selected Parent Category> ");
                    parentCategoryId = scanner.nextLong();
                }
                else
                {
                    parentCategoryId = null;
                }
            }
            else
            {
                parentCategoryId = (categoryEntityToUpdate.getParentCategoryEntity() == null)?(null):(categoryEntityToUpdate.getParentCategoryEntity().getCategoryId());
            }

            Set<ConstraintViolation<CategoryEntity>>constraintViolations = validator.validate(categoryEntityToUpdate);

            if(constraintViolations.isEmpty())
            {            
                try
                {
                    categoryEntityControllerRemote.updateCategory(categoryEntityToUpdate, parentCategoryId);
                    System.out.println("Category updated successfully!\n");
                }
                catch(InputDataValidationException ex)
                {
                    System.out.println(ex.getMessage() + "\n");
                }
                catch(CategoryNotFoundException | UpdateCategoryException ex) 
                {
                    System.out.println("An error has occurred while updating category: " + ex.getMessage() + "\n");
                }
            }
            else
            {
                showInputDataValidationErrorsForCategoryEntity(constraintViolations);
            }
        }
        catch(CategoryNotFoundException ex)
        {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    
    
    // Newly added in v5.1
    
    private void doDeleteCategory()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);     
            Long categoryToDeleteId;
            CategoryEntity categoryEntityToDelete;
            String input;

            System.out.println("*** POS System :: System Administration :: Delete Category ***\n");        
            System.out.print("Enter ID of Category to Delete> ");
            categoryToDeleteId = scanner.nextLong();
            scanner.nextLine();
            categoryEntityToDelete = categoryEntityControllerRemote.retrieveCategoryByCategoryId(categoryToDeleteId);                                   

            System.out.printf("Confirm Delete Category %s (ID: %d) (Enter 'Y' to Delete)> ", categoryEntityToDelete.getName(), categoryEntityToDelete.getCategoryId());
            input = scanner.nextLine().trim();

            if(input.equals("Y"))
            {
                try 
                {
                    categoryEntityControllerRemote.deleteCategory(categoryEntityToDelete.getCategoryId());
                    System.out.println("Category deleted successfully!\n");
                } 
                catch (CategoryNotFoundException | DeleteCategoryException ex) 
                {
                    System.out.println("An error has occurred while deleting category: " + ex.getMessage() + "\n");
                }
            }
            else
            {
                System.out.println("Category NOT deleted!\n");
            }
        }
        catch(CategoryNotFoundException ex)
        {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    
    
    // Newly added in v5.1
    
    private void doCreateNewTag()
    {
        Scanner scanner = new Scanner(System.in);
        TagEntity newTagEntity = new TagEntity();
        
        System.out.println("*** POS System :: System Administration :: Create New Tag ***\n");
        System.out.print("Enter Name> ");
        newTagEntity.setName(scanner.nextLine().trim());        
        
        Set<ConstraintViolation<TagEntity>>constraintViolations = validator.validate(newTagEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                newTagEntity = tagEntityControllerRemote.createNewTagEntity(newTagEntity);

                System.out.println("New tag created successfully!: " + newTagEntity.getTagId() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
            catch(CreateNewTagException ex)
            {
                System.out.println("An error has occurred while creating the new category: " + ex.getMessage() + "\n");
            }                        
        }
        else
        {
            showInputDataValidationErrorsForTagEntity(constraintViolations);
        }
    }
    
    
    
    // Newly added in v5.1
    
    private void doViewAllTags()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: View All Tags ***\n");
        
        List<TagEntity> tagEntities = tagEntityControllerRemote.retrieveAllTags();
        System.out.printf("%3s %-33s %13s\n", "ID", "Name", "Product Count");

        for(TagEntity tagEntity:tagEntities)
        {
            System.out.printf("%3s %-33s %13s\n", tagEntity.getTagId(), tagEntity.getName(), tagEntity.getProductEntities().size());
        }
        
        System.out.print("\nPress any key to continue...> ");
        scanner.nextLine();
    }
    
    
    
    // Newly added in v5.1
    
    private void doUpdateTag()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            Long tagToUpdateId;
            TagEntity tagEntityToUpdate;
            String input;

            System.out.println("*** POS System :: System Administration :: Update Tag ***\n");
            System.out.print("Enter ID of Tag to Update> ");
            tagToUpdateId = scanner.nextLong();
            scanner.nextLine();
            tagEntityToUpdate = tagEntityControllerRemote.retrieveTagByTagId(tagToUpdateId);            
            
            System.out.println("Name: " + tagEntityToUpdate.getName());
            System.out.print("Enter Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if(input.length() > 0)
            {
                tagEntityToUpdate.setName(input);
            }            

            Set<ConstraintViolation<TagEntity>>constraintViolations = validator.validate(tagEntityToUpdate);

            if(constraintViolations.isEmpty())
            {            
                try
                {
                    tagEntityControllerRemote.updateTag(tagEntityToUpdate);
                    System.out.println("Tag updated successfully!\n");
                }
                catch(InputDataValidationException ex)
                {
                    System.out.println(ex.getMessage() + "\n");
                }
                catch(TagNotFoundException | UpdateTagException ex) 
                {
                    System.out.println("An error has occurred while updating tag: " + ex.getMessage() + "\n");
                }
            }
            else
            {
                showInputDataValidationErrorsForTagEntity(constraintViolations);
            }
        }
        catch(TagNotFoundException ex)
        {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    
    
    // Newly added in v5.1
    
    private void doDeleteTag()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);     
            Long tagToDeleteId;
            TagEntity tagEntityToDelete;
            String input;

            System.out.println("*** POS System :: System Administration :: Delete Tag ***\n");        
            System.out.print("Enter ID of Tag to Delete> ");
            tagToDeleteId = scanner.nextLong();
            scanner.nextLine();
            tagEntityToDelete = tagEntityControllerRemote.retrieveTagByTagId(tagToDeleteId);                                   

            System.out.printf("Confirm Delete Tag %s (ID: %d) (Enter 'Y' to Delete)> ", tagEntityToDelete.getName(), tagEntityToDelete.getTagId());
            input = scanner.nextLine().trim();

            if(input.equals("Y"))
            {
                try 
                {
                    tagEntityControllerRemote.deleteTag(tagEntityToDelete.getTagId());
                    System.out.println("Tag deleted successfully!\n");
                } 
                catch (TagNotFoundException | DeleteTagException ex) 
                {
                    System.out.println("An error has occurred while deleting tag: " + ex.getMessage() + "\n");
                }
            }
            else
            {
                System.out.println("Tag NOT deleted!\n");
            }
        }
        catch(TagNotFoundException ex)
        {
            System.out.println(ex.getMessage() + "\n");
        }
    }
    
    
    
    // Newly added in v5.1
    
    private void doRegisterNewCustomer()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: Register New Customer ***\n");
        
        throw new UnsupportedOperationException();
    }
    
    
    
    // Newly added in v5.1
    
    private void doViewCustomerDetails()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: View Customer Details ***\n");
        
        throw new UnsupportedOperationException();
    }
    
    
    
    // Newly added in v5.1
    
    private void doViewAllCustomers()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** POS System :: System Administration :: View All Customers ***\n");
        
        throw new UnsupportedOperationException();
    }
    
        
    
    // Newly added in v5.1
    
    private void doCreateNewMessageOfTheDay()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
            MessageOfTheDayEntity newMessageOfTheDayEntity = new MessageOfTheDayEntity();

            System.out.println("*** POS System :: System Administration :: Create New Message Of The Day ***\n");
            System.out.print("Enter Title> ");
            newMessageOfTheDayEntity.setTitle(scanner.nextLine().trim());
            System.out.print("Enter Message> ");
            newMessageOfTheDayEntity.setMessage(scanner.nextLine().trim());
            System.out.print("Enter Message Date (dd/mm/yyyy)> ");
            newMessageOfTheDayEntity.setMessageDate(inputDateFormat.parse(scanner.nextLine().trim()));                

            Set<ConstraintViolation<MessageOfTheDayEntity>>constraintViolations = validator.validate(newMessageOfTheDayEntity);

            if(constraintViolations.isEmpty())
            {
                try
                {
                    newMessageOfTheDayEntity = messageOfTheDayControllerRemote.createNewMessageOfTheDay(newMessageOfTheDayEntity);
                    System.out.println("New message of the day created successfully!: " + newMessageOfTheDayEntity.getMotdId() + "\n");
                }
                catch(InputDataValidationException ex)
                {
                    System.out.println(ex.getMessage() + "\n");
                }
            }
            else
            {
                showInputDataValidationErrorsForMessageOfTheDayEntity(constraintViolations);
            }
        }
        catch(ParseException ex)
        {
            System.out.println("Invalid message date input!\n");
        }
    }
    
    
    
    // Newly added in v5.1
    
    private void printCategoryTreeEntry(CategoryEntity categoryEntity, Integer tabCount)
    {
        for(int i=0; i < tabCount; i++)
        {
            System.out.print("\t");
        }
        
        System.out.println(categoryEntity.getName() + " (ID: " + categoryEntity.getCategoryId() + ")");
        
        for(CategoryEntity ce:categoryEntity.getSubCategoryEntities())
        {
            printCategoryTreeEntry(ce, tabCount + 1);
        }
    }
    
    
    
    // Newly added in v4.2
    
    private void showInputDataValidationErrorsForStaffEntity(Set<ConstraintViolation<StaffEntity>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    
    
    // Newly added in v4.2
    
    private void showInputDataValidationErrorsForProductEntity(Set<ConstraintViolation<ProductEntity>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    
    
    // Newly added in v5.1
    
    private void showInputDataValidationErrorsForCategoryEntity(Set<ConstraintViolation<CategoryEntity>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    
    
    // Newly added in v5.1
    
    private void showInputDataValidationErrorsForTagEntity(Set<ConstraintViolation<TagEntity>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    
    
    // Newly added in v5.1
    
    private void showInputDataValidationErrorsForMessageOfTheDayEntity(Set<ConstraintViolation<MessageOfTheDayEntity>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}