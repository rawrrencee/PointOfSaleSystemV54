/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerEntityControllerLocal;
import ejb.session.stateless.ShoppingCartControllerLocal;
import entity.ProductEntity;
import entity.ShoppingCartEntity;
import entity.ShoppingCartLineEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

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

    private BigDecimal totalPrice;

    private ProductEntity productEntityToAdd;
    private ProductEntity productEntityToRemove;
    private ProductEntity productEntityToModify;

    public ShoppingCartManagedBean() {
        shoppingCartEntity = new ShoppingCartEntity();
        shoppingCartLineEntities = new ArrayList<>();
        shoppingCartEntity.setShoppingCartLineEntities(shoppingCartLineEntities);
        totalPrice = BigDecimal.ZERO;
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
                totalPrice = totalPrice.add(productEntityToAdd.getUnitPrice());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product already added to cart, quantity increased", null));
                return;
            }
        }
        if (!containsProduct) {
            ShoppingCartLineEntity newShoppingCartLine = new ShoppingCartLineEntity(1, getProductEntityToAdd());
            getShoppingCartEntity().getShoppingCartLineEntities().add(newShoppingCartLine);
            totalPrice = totalPrice.add(productEntityToAdd.getUnitPrice());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product added to cart", null));

        }
    }

    public void removeProductFromShoppingCart(ActionEvent event) {
        productEntityToRemove = (ProductEntity) event.getComponent().getAttributes().get("productEntityToRemove");
        for (ShoppingCartLineEntity shoppingCartLineEntity : shoppingCartLineEntities) {
            if (shoppingCartLineEntity.getProductEntity().equals(productEntityToRemove)) {
                Integer quantity = shoppingCartLineEntity.getQuantity();
                shoppingCartLineEntities.remove(shoppingCartLineEntity);
                totalPrice = totalPrice.subtract(productEntityToRemove.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product removed from cart", null));
                return;
            }
        }
    }

    public void increaseProductQuantity(ActionEvent event) {
        productEntityToModify = (ProductEntity) event.getComponent().getAttributes().get("productEntityToModify");
        for (ShoppingCartLineEntity shoppingCartLineEntity : shoppingCartLineEntities) {
            if (shoppingCartLineEntity.getProductEntity().equals(productEntityToModify)) {
                shoppingCartLineEntity.setQuantity(shoppingCartLineEntity.getQuantity() + 1);
                totalPrice = totalPrice.add(productEntityToModify.getUnitPrice());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product quantity increased", null));
                return;
            }
        }
    }

    public void decreaseProductQuantity(ActionEvent event) {
        productEntityToModify = (ProductEntity) event.getComponent().getAttributes().get("productEntityToModify");
        for (int i =0; i < shoppingCartLineEntities.size(); i++) {
            if (shoppingCartLineEntities.get(i).getProductEntity().equals(productEntityToModify)) {
                if (shoppingCartLineEntities.get(i).getQuantity() > 0) {
                    shoppingCartLineEntities.get(i).setQuantity(shoppingCartLineEntities.get(i).getQuantity() - 1);
                    totalPrice = totalPrice.subtract(productEntityToModify.getUnitPrice());
                    if (shoppingCartLineEntities.get(i).getQuantity() == 0) {
                        shoppingCartLineEntities.remove(i);
                        return;
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product quantity decreased", null));
                } else {
                    return;
                }
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

}
