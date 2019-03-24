/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerEntityControllerLocal;
import ejb.session.stateless.ProductEntityControllerLocal;
import ejb.session.stateless.SaleTransactionEntityControllerLocal;
import entity.CustomerEntity;
import entity.SaleTransactionEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author lawrence
 */
@Named(value = "saleTransactionManagedBean")
@ViewScoped
public class SaleTransactionManagedBean implements Serializable {

    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    @EJB
    private SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal;

    private CustomerEntity currentCustomerEntity;
    private List<SaleTransactionEntity> saleTransactionEntities;

    private SaleTransactionEntity selectedSaleTransactionToView;

    public SaleTransactionManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        Long currentCustomerEntityId = ((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity")).getCustomerId();
        try {
            saleTransactionEntities = customerEntityControllerLocal.retrieveCustomerByCustomerId(currentCustomerEntityId).getSaleTransactionEntities();
        } catch (CustomerNotFoundException ex) {
            System.err.println("SaleTransactionManagedBean PostConstruct error. Customer not found: " + ex.getMessage());
        }
    }

    public void viewSaleTransactionEntityDetails(ActionEvent event) throws IOException {
        Long saleTransactionIdToView = (Long) event.getComponent().getAttributes().get("saleTransactionId");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("saleTransactionIdToView", saleTransactionIdToView);
    }

    public CustomerEntity getCurrentCustomerEntity() {
        return currentCustomerEntity;
    }

    public void setCurrentCustomerEntity(CustomerEntity currentCustomerEntity) {
        this.currentCustomerEntity = currentCustomerEntity;
    }

    public List<SaleTransactionEntity> getSaleTransactionEntities() {
        return saleTransactionEntities;
    }

    public void setSaleTransactionEntities(List<SaleTransactionEntity> saleTransactionEntities) {
        this.saleTransactionEntities = saleTransactionEntities;
    }

    public SaleTransactionEntity getSelectedSaleTransactionToView() {
        return selectedSaleTransactionToView;
    }

    public void setSelectedSaleTransactionToView(SaleTransactionEntity selectedSaleTransactionToView) {
        this.selectedSaleTransactionToView = selectedSaleTransactionToView;
    }

}
