package jsf.managedbean;

import ejb.session.stateless.CategoryEntityControllerLocal;
import ejb.session.stateless.ProductEntityControllerLocal;
import ejb.session.stateless.TagEntityControllerLocal;
import entity.CategoryEntity;
import entity.ProductEntity;
import entity.TagEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import util.exception.CreateNewProductException;
import util.exception.InputDataValidationException;



@Named(value = "createNewProductManagedBean")
@RequestScoped
public class CreateNewProductManagedBean 
{
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;
    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;
    
    private ProductEntity newProductEntity;
    private Long categoryId;
    private List<String> tagIdsString;
    private List<CategoryEntity> categoryEntities;
    private List<TagEntity> tagEntities;
    
    
    
    public CreateNewProductManagedBean() 
    {
        newProductEntity = new ProductEntity();
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        categoryEntities = categoryEntityControllerLocal.retrieveAllLeafCategories();
        tagEntities = tagEntityControllerLocal.retrieveAllTags();
    }
    
    
    
    public void createNewProduct(ActionEvent event)
    {
        List<Long> tagIds = null;
        
        if(categoryId == 0)
        {
            categoryId = null;
        }
        
        if(tagIdsString != null && (!tagIdsString.isEmpty()))
        {
            tagIds = new ArrayList<>();
            
            for(String tagIdString:tagIdsString)
            {
                tagIds.add(Long.valueOf(tagIdString));
            }
        }
        
        try
        {
            ProductEntity pe = productEntityControllerLocal.createNewProduct(newProductEntity, categoryId, tagIds);
            newProductEntity = new ProductEntity();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New product created successfully (Product ID: " + pe.getProductId() + ")", null));
        }
        catch(InputDataValidationException | CreateNewProductException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new product: " + ex.getMessage(), null));
        }
    }

    
    
    public ProductEntity getNewProductEntity() {
        return newProductEntity;
    }

    public void setNewProductEntity(ProductEntity newProductEntity) {
        this.newProductEntity = newProductEntity;
    }    

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getTagIdsString() {
        return tagIdsString;
    }

    public void setTagIdsString(List<String> tagIdsString) {
        this.tagIdsString = tagIdsString;
    }

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }
}
