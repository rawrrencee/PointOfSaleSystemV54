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
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.CreateNewProductException;
import util.exception.DeleteProductException;
import util.exception.InputDataValidationException;
import util.exception.ProductNotFoundException;



@Named
@ViewScoped

public class ProductManagementManagedBean implements Serializable
{
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;
    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;
    
    private List<ProductEntity> productEntities;
    private List<ProductEntity> filteredProductEntities;
    
    private ProductEntity newProductEntity;
    private Long categoryIdNew;
    private List<String> tagIdsStringNew;
    private List<CategoryEntity> categoryEntities;
    private List<TagEntity> tagEntities;
    
    private ProductEntity selectedProductEntityToView;    
    
    private ProductEntity selectedProductEntityToUpdate;
    private Long categoryIdUpdate;
    private List<String> tagIdsStringUpdate;
    
    
    
    public ProductManagementManagedBean()
    {
        newProductEntity = new ProductEntity();
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        productEntities = productEntityControllerLocal.retrieveAllProducts();
        categoryEntities = categoryEntityControllerLocal.retrieveAllLeafCategories();
        tagEntities = tagEntityControllerLocal.retrieveAllTags();
    }
    
    
    
    public void viewProductDetails(ActionEvent event) throws IOException
    {
        Long productIdToView = (Long)event.getComponent().getAttributes().get("productId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productIdToView", productIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }
    
    
    
    public void createNewProduct(ActionEvent event)
    {
        List<Long> tagIdsNew = null;
        
        if(categoryIdNew == 0)
        {
            categoryIdNew = null;
        }
        
        if(tagIdsStringNew != null && (!tagIdsStringNew.isEmpty()))
        {
            tagIdsNew = new ArrayList<>();
            
            for(String tagIdString:tagIdsStringNew)
            {
                tagIdsNew.add(Long.valueOf(tagIdString));
            }
        }
        
        try
        {
            ProductEntity pe = productEntityControllerLocal.createNewProduct(newProductEntity, categoryIdNew, tagIdsNew);
            productEntities.add(pe);
            
            newProductEntity = new ProductEntity();
            categoryIdNew = null;
            tagIdsStringNew = null;
            

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New product created successfully (Product ID: " + pe.getProductId() + ")", null));
        }
        catch(InputDataValidationException | CreateNewProductException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new product: " + ex.getMessage(), null));
        }
    }
    
    
    
    public void doUpdateProduct(ActionEvent event)
    {
        selectedProductEntityToUpdate = (ProductEntity)event.getComponent().getAttributes().get("productEntityToUpdate");
        
        categoryIdUpdate = selectedProductEntityToUpdate.getCategoryEntity().getCategoryId();
        tagIdsStringUpdate = new ArrayList<>();

        for(TagEntity tagEntity:selectedProductEntityToUpdate.getTagEntities())
        {
            tagIdsStringUpdate.add(tagEntity.getTagId().toString());
        }
    }
    
    
    
    public void updateProduct(ActionEvent event)
    {
        List<Long> tagIdsUpdate = null;
        
        if(categoryIdUpdate  == 0)
        {
            categoryIdUpdate = null;
        }
        
        if(tagIdsStringUpdate != null && (!tagIdsStringUpdate.isEmpty()))
        {
            tagIdsUpdate = new ArrayList<>();
            
            for(String tagIdString:tagIdsStringUpdate)
            {
                tagIdsUpdate.add(Long.valueOf(tagIdString));
            }
        }
        
        try
        {
            productEntityControllerLocal.updateProduct(selectedProductEntityToUpdate, categoryIdUpdate, tagIdsUpdate);
                        
            for(CategoryEntity ce:categoryEntities)
            {
                if(ce.getCategoryId().equals(categoryIdUpdate))
                {
                    selectedProductEntityToUpdate.setCategoryEntity(ce);
                    break;
                }                
            }
            
            selectedProductEntityToUpdate.getTagEntities().clear();
            
            for(TagEntity te:tagEntities)
            {
                if(tagIdsUpdate.contains(te.getTagId()))
                {
                    selectedProductEntityToUpdate.getTagEntities().add(te);
                }                
            }

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
    
    
    
    public void deleteProduct(ActionEvent event)
    {
        try
        {
            ProductEntity productEntityToDelete = (ProductEntity)event.getComponent().getAttributes().get("productEntityToDelete");
            productEntityControllerLocal.deleteProduct(productEntityToDelete.getProductId());
            
            productEntities.remove(productEntityToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product deleted successfully", null));
        }
        catch(ProductNotFoundException | DeleteProductException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting product: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    
    
    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }

    public List<ProductEntity> getFilteredProductEntities() {
        return filteredProductEntities;
    }

    public void setFilteredProductEntities(List<ProductEntity> filteredProductEntities) {
        this.filteredProductEntities = filteredProductEntities;
    }

    public ProductEntity getNewProductEntity() {
        return newProductEntity;
    }

    public void setNewProductEntity(ProductEntity newProductEntity) {
        this.newProductEntity = newProductEntity;
    }
    
    public Long getCategoryIdNew() {
        return categoryIdNew;
    }

    public void setCategoryIdNew(Long categoryIdNew) {
        this.categoryIdNew = categoryIdNew;
    }

    public List<String> getTagIdsStringNew() {
        return tagIdsStringNew;
    }

    public void setTagIdsStringNew(List<String> tagIdsStringNew) {
        this.tagIdsStringNew = tagIdsStringNew;
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

    public ProductEntity getSelectedProductEntityToView() {
        return selectedProductEntityToView;
    }

    public void setSelectedProductEntityToView(ProductEntity selectedProductEntityToView) {
        this.selectedProductEntityToView = selectedProductEntityToView;
    }

    public ProductEntity getSelectedProductEntityToUpdate() {
        return selectedProductEntityToUpdate;
    }

    public void setSelectedProductEntityToUpdate(ProductEntity selectedProductEntityToUpdate) {
        this.selectedProductEntityToUpdate = selectedProductEntityToUpdate;
    }

    public Long getCategoryIdUpdate() {
        return categoryIdUpdate;
    }

    public void setCategoryIdUpdate(Long categoryIdUpdate) {
        this.categoryIdUpdate = categoryIdUpdate;
    }

    public List<String> getTagIdsStringUpdate() {
        return tagIdsStringUpdate;
    }

    public void setTagIdsStringUpdate(List<String> tagIdsStringUpdate) {
        this.tagIdsStringUpdate = tagIdsStringUpdate;
    }    
}