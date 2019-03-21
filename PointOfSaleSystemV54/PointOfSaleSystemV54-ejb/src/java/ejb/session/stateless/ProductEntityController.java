package ejb.session.stateless;

import entity.CategoryEntity;
import entity.ProductEntity;
import entity.SaleTransactionLineItemEntity;
import entity.TagEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewProductException;
import util.exception.DeleteProductException;
import util.exception.InputDataValidationException;
import util.exception.ProductInsufficientQuantityOnHandException;
import util.exception.ProductNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UpdateProductException;



@Stateless
@Local(ProductEntityControllerLocal.class)
@Remote(ProductEntityControllerRemote.class)

public class ProductEntityController implements ProductEntityControllerLocal, ProductEntityControllerRemote
{
    @PersistenceContext(unitName = "PointOfSaleSystemV54-ejbPU")
    private javax.persistence.EntityManager entityManager;
    
    // Added in v5.0
    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;
    // Added in v5.1
    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;
    @EJB
    private SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    
    public ProductEntityController()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    @Override
    // Updated in v5.0 to include association with new category entity
    public ProductEntity createNewProduct(ProductEntity newProductEntity, Long categoryId, List<Long> tagIds) throws InputDataValidationException, CreateNewProductException
    {
        // Updated in v4.1 to check for duplicated SKU code
        
        // Updated in v4.2 with bean validation
        
        // Updated in v5.1 with category entity and tag entity processing
        
        Set<ConstraintViolation<ProductEntity>>constraintViolations = validator.validate(newProductEntity);
        
        if(constraintViolations.isEmpty())
        {        
            try
            {
                if(categoryId == null)
                {
                    throw new CreateNewProductException("The new product must be associated a leaf category");
                }
                
                CategoryEntity categoryEntity = categoryEntityControllerLocal.retrieveCategoryByCategoryId(categoryId);
                
                if(!categoryEntity.getSubCategoryEntities().isEmpty())
                {
                    throw new CreateNewProductException("Selected category for the new product is not a leaf category");
                }
                
                entityManager.persist(newProductEntity);
                newProductEntity.setCategoryEntity(categoryEntity);
                
                if(tagIds != null && (!tagIds.isEmpty()))
                {
                    for(Long tagId:tagIds)
                    {
                        TagEntity tagEntity = tagEntityControllerLocal.retrieveTagByTagId(tagId);
                        newProductEntity.addTag(tagEntity);
                    }
                }
                
                entityManager.flush();

                return newProductEntity;
            }
            catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewProductException("Product with same SKU code already exist");
                }
                else
                {
                    throw new CreateNewProductException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
            catch(Exception ex)
            {
                throw new CreateNewProductException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    // Updated in v5.1
    
    @Override
    public List<ProductEntity> retrieveAllProducts()
    {
        Query query = entityManager.createQuery("SELECT p FROM ProductEntity p ORDER BY p.skuCode ASC");
        List<ProductEntity> productEntities = query.getResultList();
        
        for(ProductEntity productEntity:productEntities)
        {
            productEntity.getCategoryEntity();
            productEntity.getTagEntities().size();
        }
        
        return productEntities;
    }
    
    
    
    // Newly addded in v5.1
    
    @Override
    public List<ProductEntity> searchProductsByName(String searchString)
    {
        Query query = entityManager.createQuery("SELECT p FROM ProductEntity p WHERE p.name LIKE :inSearchString ORDER BY p.skuCode ASC");
        query.setParameter("inSearchString", "%" + searchString + "%");
        List<ProductEntity> productEntities = query.getResultList();
        
        for(ProductEntity productEntity:productEntities)
        {
            productEntity.getCategoryEntity();
            productEntity.getTagEntities().size();
        }
        
        return productEntities;
    }
    
    
    
    // Newly addded in v5.1
    
    @Override
    public List<ProductEntity> filterProductsByCategory(Long categoryId) throws CategoryNotFoundException
    {
        List<ProductEntity> productEntities = new ArrayList<>();
        CategoryEntity categoryEntity = categoryEntityControllerLocal.retrieveCategoryByCategoryId(categoryId);
        
        if(categoryEntity.getSubCategoryEntities().isEmpty())
        {
            productEntities = categoryEntity.getProductEntities();            
        }
        else
        {
            for(CategoryEntity subCategoryEntity:categoryEntity.getSubCategoryEntities())
            {
                productEntities.addAll(addSubCategoryProducts(subCategoryEntity));
            }
        }
        
        Collections.sort(productEntities, new Comparator<ProductEntity>()
            {
                public int compare(ProductEntity pe1, ProductEntity pe2) {
                    return pe1.getSkuCode().compareTo(pe2.getSkuCode());
                }
            });

        return productEntities;
    }
    
    
    
    @Override
    public List<ProductEntity> filterProductsByTags(List<Long> tagIds, String condition)
    {
        List<ProductEntity> productEntities = new ArrayList<>();
        
        if(tagIds == null || tagIds.isEmpty() || (!condition.equals("AND") && !condition.equals("OR")))
        {
            return productEntities;
        }
        else
        {
            if(condition.equals("OR"))
            {
                Query query = entityManager.createQuery("SELECT DISTINCT pe FROM ProductEntity pe, IN (pe.tagEntities) te WHERE te.tagId IN :inTagIds ORDER BY pe.skuCode ASC");
                query.setParameter("inTagIds", tagIds);
                productEntities = query.getResultList();
                
                for(ProductEntity productEntity:productEntities)
                {
                    productEntity.getCategoryEntity();
                    productEntity.getTagEntities().size();
                }                                
            }
            else // AND
            {
                String selectClause = "SELECT pe FROM ProductEntity pe";
                String whereClause = "";
                Boolean firstTag = true;
                Integer tagCount = 1;

                for(Long tagId:tagIds)
                {
                    selectClause += ", IN (pe.tagEntities) te" + tagCount;

                    if(firstTag)
                    {
                        whereClause = "WHERE te1.tagId = " + tagId;
                        firstTag = false;
                    }
                    else
                    {
                        whereClause += " AND te" + tagCount + ".tagId = " + tagId; 
                    }
                    
                    tagCount++;
                }
                
                String jpql = selectClause + " " + whereClause + " ORDER BY pe.skuCode ASC";
                Query query = entityManager.createQuery(jpql);
                productEntities = query.getResultList();
                
                for(ProductEntity productEntity:productEntities)
                {
                    productEntity.getCategoryEntity();
                    productEntity.getTagEntities().size();
                }
            }
            
            Collections.sort(productEntities, new Comparator<ProductEntity>()
            {
                public int compare(ProductEntity pe1, ProductEntity pe2) {
                    return pe1.getSkuCode().compareTo(pe2.getSkuCode());
                }
            });
            
            return productEntities;
        }
    }
    
    
    
    // Updated in v5.1
    
    @Override
    public ProductEntity retrieveProductByProductId(Long productId) throws ProductNotFoundException
    {
        if(productId == null)
        {
            throw new ProductNotFoundException("Product ID not provided");
        }
        
        ProductEntity productEntity = entityManager.find(ProductEntity.class, productId);
        
        if(productEntity != null)
        {
            productEntity.getCategoryEntity();
            productEntity.getTagEntities().size();
            
            return productEntity;
        }
        else
        {
            throw new ProductNotFoundException("Product ID " + productId + " does not exist!");
        }               
    }
    
    
    
    // Added in v4.1
    
    @Override
    public ProductEntity retrieveProductByProductSkuCode(String skuCode) throws ProductNotFoundException
    {
        if(skuCode == null)
        {
            throw new ProductNotFoundException("SKU Code not provided");
        }
        
        Query query = entityManager.createQuery("SELECT p FROM ProductEntity p WHERE p.skuCode = :inSkuCode");
        query.setParameter("inSkuCode", skuCode);
        
        try
        {
            ProductEntity productEntity = (ProductEntity)query.getSingleResult();
            productEntity.getCategoryEntity();
            productEntity.getTagEntities().size();
                        
            return productEntity;
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new ProductNotFoundException("SKU Code " + skuCode + " does not exist!");
        }
    }
    
    
    
    // Updated in v4.1
    
    // Updated in v4.2 to include reorderQuantity and bean validation
    
    // Updated in v5.1 with category entity and tag entity processing
    
    @Override
    public void updateProduct(ProductEntity productEntity, Long categoryId, List<Long> tagIds) throws InputDataValidationException, ProductNotFoundException, CategoryNotFoundException, TagNotFoundException, UpdateProductException
    {
        Set<ConstraintViolation<ProductEntity>>constraintViolations = validator.validate(productEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(productEntity.getProductId()!= null)
            {
                ProductEntity productEntityToUpdate = retrieveProductByProductId(productEntity.getProductId());
                
                if(productEntityToUpdate.getSkuCode().equals(productEntity.getSkuCode()))
                {
                    if(categoryId != null && (!productEntityToUpdate.getCategoryEntity().getCategoryId().equals(categoryId)))
                    {
                        CategoryEntity categoryEntityToUpdate = categoryEntityControllerLocal.retrieveCategoryByCategoryId(categoryId);
                        
                        if(!categoryEntityToUpdate.getSubCategoryEntities().isEmpty())
                        {
                            throw new UpdateProductException("Selected category for the new product is not a leaf category");
                        }
                        
                        productEntityToUpdate.setCategoryEntity(categoryEntityToUpdate);
                    }
                    
                    if(tagIds != null)
                    {
                        for(TagEntity tagEntity:productEntityToUpdate.getTagEntities())
                        {
                            tagEntity.getProductEntities().remove(productEntityToUpdate);
                        }
                        
                        productEntityToUpdate.getTagEntities().clear();
                        
                        for(Long tagId:tagIds)
                        {
                            TagEntity tagEntity = tagEntityControllerLocal.retrieveTagByTagId(tagId);
                            productEntityToUpdate.addTag(tagEntity);
                        }
                    }
                    
                    productEntityToUpdate.setName(productEntity.getName());
                    productEntityToUpdate.setDescription(productEntity.getDescription());
                    productEntityToUpdate.setQuantityOnHand(productEntity.getQuantityOnHand());
                    productEntityToUpdate.setReorderQuantity(productEntity.getReorderQuantity());
                    productEntityToUpdate.setUnitPrice(productEntity.getUnitPrice());
                    productEntityToUpdate.setProductRating((productEntity.getProductRating()));
                }
                else
                {
                    throw new UpdateProductException("SKU Code of product record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new ProductNotFoundException("Product ID not provided for product to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    // Updated in v4.1
    
    @Override
    public void deleteProduct(Long productId) throws ProductNotFoundException, DeleteProductException
    {
        ProductEntity productEntityToRemove = retrieveProductByProductId(productId);
        
        List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = saleTransactionEntityControllerLocal.retrieveSaleTransactionLineItemsByProductId(productId);
        
        if(saleTransactionLineItemEntities.isEmpty())
        {
            productEntityToRemove.getCategoryEntity().getProductEntities().remove(productEntityToRemove);
            
            for(TagEntity tagEntity:productEntityToRemove.getTagEntities())
            {
                tagEntity.getProductEntities().remove(productEntityToRemove);
            }
            
            productEntityToRemove.getTagEntities().clear();
            
            entityManager.remove(productEntityToRemove);
        }
        else
        {
            throw new DeleteProductException("Product ID " + productId + " is associated with existing sale transaction line item(s) and cannot be deleted!");
        }
    }
    
    
    
    // Added in v4.1
    
    @Override
    public void debitQuantityOnHand(Long productId, Integer quantityToDebit) throws ProductNotFoundException, ProductInsufficientQuantityOnHandException
    {
        ProductEntity productEntity = retrieveProductByProductId(productId);
        
        if(productEntity.getQuantityOnHand() >= quantityToDebit)
        {
            productEntity.setQuantityOnHand(productEntity.getQuantityOnHand() - quantityToDebit);
        }
        else
        {
            throw new ProductInsufficientQuantityOnHandException("Product " + productEntity.getSkuCode() + " quantity on hand is " + productEntity.getQuantityOnHand() + " versus quantity to debit of " + quantityToDebit);
        }
    }
    
    
    
    // Added in v4.1
    
    @Override
    public void creditQuantityOnHand(Long productId, Integer quantityToCredit) throws ProductNotFoundException
    {
        ProductEntity productEntity = retrieveProductByProductId(productId);
        productEntity.setQuantityOnHand(productEntity.getQuantityOnHand() + quantityToCredit);
    }
    
    
    
    // Newly added in v5.1
    
    private List<ProductEntity> addSubCategoryProducts(CategoryEntity categoryEntity)
    {
        List<ProductEntity> productEntities = new ArrayList<>();
                
        if(categoryEntity.getSubCategoryEntities().isEmpty())
        {
            return categoryEntity.getProductEntities();
        }
        else
        {
            for(CategoryEntity subCategoryEntity:categoryEntity.getSubCategoryEntities())
            {
                productEntities.addAll(addSubCategoryProducts(subCategoryEntity));
            }
            
            return productEntities;
        }
    }
    
    
    
    // Added in v4.2
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ProductEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}