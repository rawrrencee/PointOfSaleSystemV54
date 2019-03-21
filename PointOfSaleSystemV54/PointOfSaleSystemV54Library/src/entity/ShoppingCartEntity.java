/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author lawrence
 */
@Entity
public class ShoppingCartEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingCartId;
    
    @OneToMany
    private List<ShoppingCartLineEntity> shoppingCartLineEntities;

    @OneToOne
    private CustomerEntity customerEntity;

    public ShoppingCartEntity() {
        shoppingCartLineEntities = new ArrayList<>();
    }

    public ShoppingCartEntity(List<ShoppingCartLineEntity> shoppingCartLineEntities, CustomerEntity customerEntity) {
        this.shoppingCartLineEntities = shoppingCartLineEntities;
        this.customerEntity = customerEntity;
    }

    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (shoppingCartId != null ? shoppingCartId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ShoppingCartEntity)) {
            return false;
        }
        ShoppingCartEntity other = (ShoppingCartEntity) object;
        if ((this.shoppingCartId == null && other.shoppingCartId != null) || (this.shoppingCartId != null && !this.shoppingCartId.equals(other.shoppingCartId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ShoppingCartEntity[ id=" + shoppingCartId + " ]";
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public List<ShoppingCartLineEntity> getShoppingCartLineEntities() {
        return shoppingCartLineEntities;
    }

    public void setShoppingCartLineEntities(List<ShoppingCartLineEntity> shoppingCartLineEntities) {
        this.shoppingCartLineEntities = shoppingCartLineEntities;
    }

}
