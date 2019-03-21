package ejb.session.singleton;

import ejb.session.stateless.CategoryEntityControllerLocal;
import ejb.session.stateless.ProductEntityControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import ejb.session.stateless.TagEntityControllerLocal;
import entity.CategoryEntity;
import entity.ProductEntity;
import entity.StaffEntity;
import entity.TagEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.AccessRightEnum;
import util.exception.InputDataValidationException;
import util.exception.StaffNotFoundException;



@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean
{    
    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;
    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;
    
    
    
    public DataInitializationSessionBean()
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        try
        {
            staffEntityControllerLocal.retrieveStaffByUsername("manager");
        }
        catch(StaffNotFoundException ex)
        {
            initializeData();
        }
    }
    
    
    
    private void initializeData()
    {
        try
        {
            staffEntityControllerLocal.createNewStaff(new StaffEntity("Default", "Manager", AccessRightEnum.MANAGER, "manager", "password"));
            staffEntityControllerLocal.createNewStaff(new StaffEntity("Default", "Cashier One", AccessRightEnum.CASHIER, "cashier1", "password"));
            staffEntityControllerLocal.createNewStaff(new StaffEntity("Default", "Cashier Two", AccessRightEnum.CASHIER, "cashier2", "password"));
            
            // Updated in v5.1
            CategoryEntity categoryEntityElectronics = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Electronics", "Electronics"), null);
            CategoryEntity categoryEntityFashions = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Fashions", "Fashions"), null);
            CategoryEntity categoryEntityA = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Category A", "Category A"), categoryEntityElectronics.getCategoryId());
            CategoryEntity categoryEntityB = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Category B", "Category B"), categoryEntityElectronics.getCategoryId());
            CategoryEntity categoryEntityC = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Category C", "Category C"), categoryEntityElectronics.getCategoryId());
            CategoryEntity categoryEntityX = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Category X", "Category X"), categoryEntityFashions.getCategoryId());
            CategoryEntity categoryEntityY = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Category Y", "Category Y"), categoryEntityFashions.getCategoryId());
            CategoryEntity categoryEntityZ = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Category Z", "Category Z"), categoryEntityFashions.getCategoryId());
            
            // Newly added in v5.1
            TagEntity tagEntityPopular = tagEntityControllerLocal.createNewTagEntity(new TagEntity("popular"));
            TagEntity tagEntityDiscount = tagEntityControllerLocal.createNewTagEntity(new TagEntity("discount"));
            TagEntity tagEntityNew = tagEntityControllerLocal.createNewTagEntity(new TagEntity("new"));
            
            // Updated in v4.2 to initialise reorderQuantity for all product entity instances to 10
            // Updated in v5.0
            List<Long> tagIdsPopular = new ArrayList<>();
            tagIdsPopular.add(tagEntityPopular.getTagId());
            
            List<Long> tagIdsDiscount = new ArrayList<>();
            tagIdsDiscount.add(tagEntityDiscount.getTagId());
            
            List<Long> tagIdsPopularDiscount = new ArrayList<>();
            tagIdsPopularDiscount.add(tagEntityPopular.getTagId());
            tagIdsPopularDiscount.add(tagEntityDiscount.getTagId());
            
            List<Long> tagIdsPopularNew = new ArrayList<>();
            tagIdsPopularNew.add(tagEntityPopular.getTagId());
            tagIdsPopularNew.add(tagEntityNew.getTagId());
            
            List<Long> tagIdsPopularDiscountNew = new ArrayList<>();
            tagIdsPopularDiscountNew.add(tagEntityPopular.getTagId());
            tagIdsPopularDiscountNew.add(tagEntityDiscount.getTagId());
            tagIdsPopularDiscountNew.add(tagEntityNew.getTagId());
            
            List<Long> tagIdsEmpty = new ArrayList<>();
            
            // Updated in v5.1
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD001", "Product A1", "Product A1", 100, 10, new BigDecimal("10.00"), 1), categoryEntityA.getCategoryId(), tagIdsPopular);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD002", "Product A2", "Product A2", 100, 10, new BigDecimal("25.50"), 2), categoryEntityA.getCategoryId(), tagIdsDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD003", "Product A3", "Product A3", 100, 10, new BigDecimal("15.00"), 3), categoryEntityA.getCategoryId(), tagIdsPopularDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD004", "Product B1", "Product B1", 100, 10, new BigDecimal("20.00"), 1), categoryEntityB.getCategoryId(), tagIdsPopularNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD005", "Product B2", "Product B2", 100, 10, new BigDecimal("10.00"), 2), categoryEntityB.getCategoryId(), tagIdsPopularDiscountNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD006", "Product B3", "Product B3", 100, 10, new BigDecimal("100.00"), 3), categoryEntityB.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD007", "Product C1", "Product C1", 100, 10, new BigDecimal("35.00"), 1), categoryEntityC.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD008", "Product C2", "Product C2", 100, 10, new BigDecimal("20.05"), 2), categoryEntityC.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD009", "Product C3", "Product C3", 100, 10, new BigDecimal("5.50"), 3), categoryEntityC.getCategoryId(), tagIdsEmpty);
            // Added in v5.0
            // Updated in v5.1
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD010", "Product X1", "Product X1", 100, 10, new BigDecimal("20.00"), 3), categoryEntityX.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD011", "Product X2", "Product X2", 100, 10, new BigDecimal("30.50"), 4), categoryEntityX.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD012", "Product X3", "Product X3", 100, 10, new BigDecimal("18.50"), 5), categoryEntityX.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD013", "Product Y1", "Product Y1", 100, 10, new BigDecimal("50.00"), 3), categoryEntityY.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD014", "Product Y2", "Product Y2", 100, 10, new BigDecimal("100.00"), 4), categoryEntityY.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD015", "Product Y3", "Product Y3", 100, 10, new BigDecimal("200.00"), 5), categoryEntityY.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD016", "Product Z1", "Product Z1", 100, 10, new BigDecimal("95.00"), 3), categoryEntityZ.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD017", "Product Z2", "Product Z2", 100, 10, new BigDecimal("19.05"), 4), categoryEntityZ.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD018", "Product Z3", "Product Z3", 100, 10, new BigDecimal("10.50"), 5), categoryEntityZ.getCategoryId(), tagIdsEmpty);
        }
        catch(InputDataValidationException ex)
        {
            System.err.println("********** DataInitializationSessionBean.initializeData(): " + ex.getMessage());
        }
        catch(Exception ex)
        {
            System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
    }
}