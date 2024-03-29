/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerEntityControllerLocal;
import ejb.session.stateless.ShoppingCartControllerLocal;
import entity.CustomerEntity;
import entity.ShoppingCartEntity;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import util.exception.CreateNewCustomerException;
import util.exception.CreateNewShoppingCartException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UpdateShoppingCartException;

@Named(value = "customerManagedBean")
@RequestScoped
public class CustomerManagedBean {

    @EJB
    private ShoppingCartControllerLocal shoppingCartControllerLocal;

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    @Inject
    private ShoppingCartManagedBean shoppingCartManagedBean;

    private String email;
    private String password;

    private CustomerEntity newCustomerEntity;

    private ShoppingCartEntity currentShoppingCartEntity;

    public CustomerManagedBean() {
        newCustomerEntity = new CustomerEntity();
    }

    public void login(ActionEvent event) throws IOException {
        try {
            CustomerEntity currentCustomerEntity = customerEntityControllerLocal.customerLogin(email, password);
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentCustomerEntity", currentCustomerEntity);
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
        } catch (InvalidLoginCredentialException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage(), null));
        }
    }

    public void logout(ActionEvent event) throws IOException {
//        Long customerId = ((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity")).getCustomerId();
//        shoppingCartManagedBean.putShoppingCartIntoSession();
//        currentShoppingCartEntity = (ShoppingCartEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentShoppingCartEntity");
//        System.err.println("currentShoppingCartEntity has line number: " + currentShoppingCartEntity.getShoppingCartLineEntities().size());
//        for (int i = 0; i < currentShoppingCartEntity.getShoppingCartLineEntities().size(); i++) {
//            System.out.println("shoppingCartLine " + currentShoppingCartEntity.getShoppingCartLineEntities().get(i).getProductEntity().getName() + " / Qty: " + currentShoppingCartEntity.getShoppingCartLineEntities().get(i).getQuantity());
//        }
//        if (currentShoppingCartEntity.getShoppingCartLineEntities().size() > 0) {
//            try {
//                shoppingCartControllerLocal.createNewShoppingCart(customerId, currentShoppingCartEntity);
//            } catch (CreateNewShoppingCartException ex) {
//                System.err.println("An error has occurred while creating the shopping cart: " + ex.getMessage());
//            } catch (Exception e) {
//                System.err.println("An unexpected error has occurred while creating the shopping cart: " + e.getMessage());
//            }
//        }
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
    }

    public void createNewCustomer(ActionEvent event) {

        try {
            customerEntityControllerLocal.createNewCustomer(getNewCustomerEntity());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Your account has been created successfully.", null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating your account: " + ex.getMessage(), null));
        } catch (CreateNewCustomerException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating your account: " + ex.getMessage(), null));
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CustomerEntity getNewCustomerEntity() {
        return newCustomerEntity;
    }

    public void setNewCustomerEntity(CustomerEntity newCustomerEntity) {
        this.newCustomerEntity = newCustomerEntity;
    }

    public void setShoppingCartManagedBean(ShoppingCartManagedBean shoppingCartManagedBean) {
        this.shoppingCartManagedBean = shoppingCartManagedBean;
    }

    public ShoppingCartEntity getCurrentShoppingCartEntity() {
        return currentShoppingCartEntity;
    }

    public void setCurrentShoppingCartEntity(ShoppingCartEntity currentShoppingCartEntity) {
        this.currentShoppingCartEntity = currentShoppingCartEntity;
    }
}
