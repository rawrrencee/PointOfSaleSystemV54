package jsf.managedbean;

import ejb.session.stateless.ProductEntityControllerLocal;
import ejb.session.stateless.TagEntityControllerLocal;
import entity.ProductEntity;
import entity.TagEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;



@Named(value = "filterProductsByTagsManagedBean")
@ViewScoped

public class FilterProductsByTagsManagedBean implements Serializable
{
    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    
    private String condition;
    private List<String> selectedTagIds;
    private List<SelectItem> selectItems;
    private List<ProductEntity> productEntities;
    
    
    
    public FilterProductsByTagsManagedBean()
    {
        condition = "OR";
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        List<TagEntity> tagEntities = tagEntityControllerLocal.retrieveAllTags();
        selectItems = new ArrayList<>();
        
        for(TagEntity tagEntity:tagEntities)
        {
            selectItems.add(new SelectItem(tagEntity.getTagId(), tagEntity.getName(), tagEntity.getName()));
        }
        
        
        // Optional demonstration of the use of custom converter
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TagEntityConverter_tagEntities", tagEntities);
        
        condition = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productFilterCondition");
        selectedTagIds = (List<String>)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productFilterTags");
        
        filterProduct();
    }
    
    
    
    @PreDestroy
    public void preDestroy()
    {
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TagEntityConverter_tagEntities", null);
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("TagEntityConverter_tagEntities", null);
    }
    
    
    
    public void filterProduct()
    {
        List<Long> tagIds = new ArrayList<>();
        
        if(selectedTagIds != null && selectedTagIds.size() > 0)
        {
            for(String tagId:selectedTagIds)
            {
                tagIds.add(Long.valueOf(tagId));
            }
            
            productEntities = productEntityControllerLocal.filterProductsByTags(tagIds, condition);
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
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "filterProductsByTags");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }

    
    
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) 
    {
        this.condition = condition;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productFilterCondition", condition);
    }
    
    public List<String> getSelectedTagIds() {
        return selectedTagIds;
    }

    public void setSelectedTagIds(List<String> selectedTagIds) 
    {
        this.selectedTagIds = selectedTagIds;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productFilterTags", selectedTagIds);
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
