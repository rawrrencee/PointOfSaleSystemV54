/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author lawrence
 */
@Entity
public class ShoppingCartLineEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingCartLineId;
    
    private Integer quantity;
    
    @ManyToOne
    private ProductEntity productEntity;

    public ShoppingCartLineEntity() {
    }

    public ShoppingCartLineEntity(Integer quantity, ProductEntity productEntity) {
        this.quantity = quantity;
        this.productEntity = productEntity;
    }

    public Long getShoppingCartLineId() {
        return shoppingCartLineId;
    }

    public void setShoppingCartLineId(Long shoppingCartLineId) {
        this.shoppingCartLineId = shoppingCartLineId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (shoppingCartLineId != null ? shoppingCartLineId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ShoppingCartLineEntity)) {
            return false;
        }
        ShoppingCartLineEntity other = (ShoppingCartLineEntity) object;
        if ((this.shoppingCartLineId == null && other.shoppingCartLineId != null) || (this.shoppingCartLineId != null && !this.shoppingCartLineId.equals(other.shoppingCartLineId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ShoppingCartLineEntity[ id=" + shoppingCartLineId + " ]";
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }
    
}
