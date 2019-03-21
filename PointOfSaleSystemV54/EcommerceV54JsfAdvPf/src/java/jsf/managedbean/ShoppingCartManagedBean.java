/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerEntityControllerLocal;
import ejb.session.stateless.ShoppingCartControllerLocal;
import entity.CustomerEntity;
import entity.ProductEntity;
import entity.ShoppingCartEntity;
import entity.ShoppingCartLineEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CustomerNotFoundException;
import util.exception.ShoppingCartNotFoundException;

/**
 *
 * @author lawrence
 */
@Named(value = "shoppingCartManagedBean")
@SessionScoped
public class ShoppingCartManagedBean implements Serializable {

    @EJB(name = "ShoppingCartControllerLocal")
    private ShoppingCartControllerLocal shoppingCartControllerLocal;

    @EJB(name = "CustomerEntityControllerLocal")
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    private ShoppingCartEntity shoppingCartEntity;
    private List<ShoppingCartLineEntity> shoppingCartLineEntities;

    private ProductEntity productEntityToAdd;
    private ProductEntity productEntityToRemove;

    public ShoppingCartManagedBean() {
        shoppingCartEntity = new ShoppingCartEntity();
        shoppingCartLineEntities = new ArrayList<>();
        shoppingCartEntity.setShoppingCartLineEntities(shoppingCartLineEntities);
    }

    @PostConstruct
    public void postConstruct() {
        //       try {
        //           Long customerId = ((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity")).getCustomerId();
        //           System.err.println("CustomerId retrieved: " + customerId);
        //           ShoppingCartEntity shoppingCart = shoppingCartControllerLocal.retrieveShoppingCartByCustomerId(customerId);
        //           System.err.println("ShoppingCartId retrieved: " + shoppingCart.getShoppingCartId());
        //           setShoppingCartEntity(shoppingCart);
        //           setShoppingCartLineEntities(new ArrayList<>(getShoppingCartEntity().getShoppingCartLineEntities()));
        //       } catch (ShoppingCartNotFoundException ex) {
        //           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Shopping cart not found: " + ex.getMessage(), null));
        //           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New shopping cart created", null));
        //       } catch (Exception ex) {
        //           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "(Line 68) An unknown error has occurred: " + ex.getMessage(), null));
        //       }
    }

    public void addProductToShoppingCart(ActionEvent event) {
        setProductEntityToAdd((ProductEntity) event.getComponent().getAttributes().get("productEntityToAdd"));
        Boolean containsProduct = false;

        for (ShoppingCartLineEntity shoppingCartLineEntity : getShoppingCartLineEntities()) {
            if (shoppingCartLineEntity.getProductEntity().equals(getProductEntityToAdd())) {
                Integer currentQty = shoppingCartLineEntity.getQuantity();
                shoppingCartLineEntity.setQuantity(currentQty + 1);
                containsProduct = true;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product already added to cart, quantity increased", null));
                return;
            }
        }
        if (!containsProduct) {
            ShoppingCartLineEntity newShoppingCartLine = new ShoppingCartLineEntity(1, getProductEntityToAdd());
            getShoppingCartEntity().getShoppingCartLineEntities().add(newShoppingCartLine);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product added to cart", null));

        }
    }

    public void removeProductFromShoppingCart(ActionEvent event) {
        productEntityToRemove = (ProductEntity) event.getComponent().getAttributes().get("productEntityToRemove");
        for (ShoppingCartLineEntity shoppingCartLineEntity : shoppingCartLineEntities) {
            if (shoppingCartLineEntity.getProductEntity().equals(productEntityToRemove)) {
                shoppingCartLineEntities.remove(shoppingCartLineEntity);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product removed from cart", null));
                return;
            }
        }
    }

    public ShoppingCartEntity getShoppingCartEntity() {
        return shoppingCartEntity;
    }

    public void setShoppingCartEntity(ShoppingCartEntity shoppingCartEntity) {
        this.shoppingCartEntity = shoppingCartEntity;
    }

    public List<ShoppingCartLineEntity> getShoppingCartLineEntities() {
        return shoppingCartLineEntities;
    }

    public void setShoppingCartLineEntities(List<ShoppingCartLineEntity> shoppingCartLineEntities) {
        this.shoppingCartLineEntities = shoppingCartLineEntities;
    }

    public ProductEntity getProductEntityToAdd() {
        return productEntityToAdd;
    }

    public void setProductEntityToAdd(ProductEntity productEntityToAdd) {
        this.productEntityToAdd = productEntityToAdd;
    }

}
