package jsf.managedbean;

import ejb.session.stateless.CategoryEntityControllerLocal;
import ejb.session.stateless.ProductEntityControllerLocal;
import entity.CategoryEntity;
import entity.ProductEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CategoryNotFoundException;



@Named(value = "filterProductsByCategoryManagedBean")
@ViewScoped

public class FilterProductsByCategoryManagedBean implements Serializable
{
    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    
    private Long selectedCategoryId;
    private List<SelectItem> selectItems;
    private List<ProductEntity> productEntities;
    
    
    
    public FilterProductsByCategoryManagedBean() 
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        List<CategoryEntity> categoryEntities = categoryEntityControllerLocal.retrieveAllRootCategories();
        selectItems = new ArrayList<>();
        
        for(CategoryEntity categoryEntity:categoryEntities)
        {
            createSelectItem(categoryEntity, null);
        }
        
        selectedCategoryId = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productFilterCategory");
        
        filterProduct();
    }
    
    
    
    public void filterProduct()
    {
        System.out.println("********** " + selectedCategoryId);
        if(selectedCategoryId != null)
        {   
            if(selectedCategoryId.equals(0l))
            {
                productEntities = productEntityControllerLocal.retrieveAllProducts();
            }
            else
            {
                try
                {
                    productEntities = productEntityControllerLocal.filterProductsByCategory(selectedCategoryId);
                }
                catch(CategoryNotFoundException ex)
                {
                    productEntities = productEntityControllerLocal.retrieveAllProducts();
                }
            }
        }
        else
        {
            productEntities = productEntityControllerLocal.retrieveAllProducts();
        }
    }
    
    
    
    public void viewProductDetails(ActionEvent event) throws IOException
    {
        Long productIdToView = (Long)event.getComponent().getAttributes().get("productId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productIdToView", productIdToView);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "filterProductsByCategory");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }
    
    
    
    private void createSelectItem(CategoryEntity categoryEntity, String parent)
    {
        String categoryFullName = (parent != null && parent.trim().length() > 0)?(parent + " > " + categoryEntity.getName()):(categoryEntity.getName());
        selectItems.add(new SelectItem(categoryEntity.getCategoryId(), categoryFullName, categoryFullName));
        
        for(CategoryEntity ce:categoryEntity.getSubCategoryEntities())
        {
            createSelectItem(ce, categoryFullName);
        }
    }

    
    
    public Long getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(Long selectedCategoryId) 
    {
        this.selectedCategoryId = selectedCategoryId;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productFilterCategory", selectedCategoryId);
    }
    
    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }    
}
