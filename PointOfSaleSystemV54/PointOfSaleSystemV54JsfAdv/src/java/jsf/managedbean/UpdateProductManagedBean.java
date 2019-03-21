package jsf.managedbean;

import ejb.session.stateless.CategoryEntityControllerLocal;
import ejb.session.stateless.ProductEntityControllerLocal;
import ejb.session.stateless.TagEntityControllerLocal;
import entity.CategoryEntity;
import entity.ProductEntity;
import entity.TagEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.ProductNotFoundException;



@Named(value = "updateProductManagedBean")
@ViewScoped

public class UpdateProductManagedBean implements Serializable
{
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;
    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;
    
    private Long productIdToUpdate;
    private ProductEntity productEntityToUpdate;
    private Long categoryId;
    private List<String> tagIdsString;
    private List<CategoryEntity> categoryEntities;
    private List<TagEntity> tagEntities;
    
    
    
    public UpdateProductManagedBean() 
    {        
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        productIdToUpdate = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("productIdToUpdate");
        
        try
        {            
            productEntityToUpdate = productEntityControllerLocal.retrieveProductByProductId(productIdToUpdate);
            categoryId = productEntityToUpdate.getCategoryEntity().getCategoryId();
            tagIdsString = new ArrayList<>();
            
            for(TagEntity tagEntity:productEntityToUpdate.getTagEntities())
            {
                tagIdsString.add(tagEntity.getTagId().toString());
            }
            
            categoryEntities = categoryEntityControllerLocal.retrieveAllLeafCategories();
            tagEntities = tagEntityControllerLocal.retrieveAllTags();
        }
        catch(ProductNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the product details: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    
    
    public void back(ActionEvent event) throws IOException
    {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productIdToView", productIdToUpdate);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }
    
    
    
    public void foo()
    {        
    }
    
    
    
    public void updateProduct()
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
            productEntityControllerLocal.updateProduct(productEntityToUpdate, categoryId, tagIds);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product updated successfully", null));
        }
        catch(ProductNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating product: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    
    
    public ProductEntity getProductEntityToUpdate() {
        return productEntityToUpdate;
    }

    public void setProductEntityToUpdate(ProductEntity productEntityToUpdate) {
        this.productEntityToUpdate = productEntityToUpdate;
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
