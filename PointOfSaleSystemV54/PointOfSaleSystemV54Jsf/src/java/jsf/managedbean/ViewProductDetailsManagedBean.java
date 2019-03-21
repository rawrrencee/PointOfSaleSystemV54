package jsf.managedbean;

import ejb.session.stateless.ProductEntityControllerLocal;
import entity.ProductEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.ProductNotFoundException;



@Named(value = "viewProductDetailsManagedBean")
@ViewScoped

public class ViewProductDetailsManagedBean implements Serializable
{
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    
    private Long productIdToView;
    private ProductEntity productEntityToView;
    
    
    
    public ViewProductDetailsManagedBean() 
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        productIdToView = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("productIdToView");
        
        try
        {            
            productEntityToView = productEntityControllerLocal.retrieveProductByProductId(productIdToView);
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
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewAllProducts.xhtml");
    }
    
    
    
    public void foo()
    {        
    }
    
    
    
    public void updateProduct(ActionEvent event) throws IOException
    {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productIdToUpdate", productIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("updateProduct.xhtml");
    }
    
    
    
    public void deleteProduct(ActionEvent event) throws IOException
    {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productIdToDelete", productIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("deleteProduct.xhtml");
    }
    
    
    
    public ProductEntity getProductEntityToView() {
        return productEntityToView;
    }

    public void setProductEntityToView(ProductEntity productEntityToView) {
        this.productEntityToView = productEntityToView;
    }
}
