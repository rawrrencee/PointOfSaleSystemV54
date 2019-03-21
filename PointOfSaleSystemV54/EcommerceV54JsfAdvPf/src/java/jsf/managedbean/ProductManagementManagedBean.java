package jsf.managedbean;

import ejb.session.stateless.CategoryEntityControllerLocal;
import ejb.session.stateless.ProductEntityControllerLocal;
import ejb.session.stateless.TagEntityControllerLocal;
import entity.CategoryEntity;
import entity.ProductEntity;
import entity.TagEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;



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