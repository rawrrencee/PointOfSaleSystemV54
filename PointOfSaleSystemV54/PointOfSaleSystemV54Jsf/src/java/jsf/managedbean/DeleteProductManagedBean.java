package jsf.managedbean;

import ejb.session.stateless.ProductEntityControllerLocal;
import entity.ProductEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.DeleteProductException;
import util.exception.ProductNotFoundException;



@Named(value = "deleteProductManagedBean")
@ViewScoped

public class DeleteProductManagedBean implements Serializable
{
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    
    private Long productIdToDelete;
    private ProductEntity productEntityToDelete;
    
    
    
    public DeleteProductManagedBean() 
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        productIdToDelete = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("productIdToDelete");
        
        try
        {            
            productEntityToDelete = productEntityControllerLocal.retrieveProductByProductId(productIdToDelete);                       
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
    
    
    
    public void deleteProduct(ActionEvent event)
    {
        try
        {
            productEntityControllerLocal.deleteProduct(productIdToDelete);

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
    
    
    
    public void back(ActionEvent event) throws IOException
    {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productIdToView", productIdToDelete);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }
    
    
    
    public void foo()
    {        
    }
    
    
    
    public Long getProductIdToDelete() {
        return productIdToDelete;
    }

    public void setProductIdToDelete(Long productIdToDelete) {
        this.productIdToDelete = productIdToDelete;
    }

    public ProductEntity getProductEntityToDelete() {
        return productEntityToDelete;
    }

    public void setProductEntityToDelete(ProductEntity productEntityToDelete) {
        this.productEntityToDelete = productEntityToDelete;
    }
}
