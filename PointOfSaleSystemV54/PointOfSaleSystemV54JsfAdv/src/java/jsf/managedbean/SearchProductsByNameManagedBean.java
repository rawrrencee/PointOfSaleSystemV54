package jsf.managedbean;

import ejb.session.stateless.ProductEntityControllerLocal;
import entity.ProductEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;



@Named(value = "searchProductsByNameManagedBean")
@ViewScoped

public class SearchProductsByNameManagedBean implements Serializable
{
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    
    private String searchString;
    private List<ProductEntity> productEntities;
    
    
    
    public SearchProductsByNameManagedBean() 
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        searchString = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productSearchString");
        
        if(searchString == null || searchString.trim().length() == 0)
        {
            productEntities = productEntityControllerLocal.retrieveAllProducts();
        }
        else
        {
            productEntities = productEntityControllerLocal.searchProductsByName(searchString);
        }
    }
    
    
    
    public void searchProduct()
    {
        if(searchString == null || searchString.trim().length() == 0)
        {
            productEntities = productEntityControllerLocal.retrieveAllProducts();
        }
        else
        {
            productEntities = productEntityControllerLocal.searchProductsByName(searchString);
        }
    }
    
    
    
    public void viewProductDetails(ActionEvent event) throws IOException
    {
        Long productIdToView = (Long)event.getComponent().getAttributes().get("productId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productIdToView", productIdToView);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "searchProductsByName");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }

    
    
    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productSearchString", searchString);
    }
    
    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }    
}
