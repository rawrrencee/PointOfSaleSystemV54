package jsf.managedbean;

import ejb.session.stateless.ProductEntityControllerLocal;
import entity.ProductEntity;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;



@Named(value = "viewAllProductsManagedBean")
@RequestScoped

public class ViewAllProductsManagedBean 
{
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    
    private List<ProductEntity> productEntities;
    
    
    
    public ViewAllProductsManagedBean() 
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        productEntities = productEntityControllerLocal.retrieveAllProducts();
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
}
