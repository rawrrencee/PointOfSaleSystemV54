/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.ShoppingCartEntity;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.CreateNewShoppingCartException;
import util.exception.CustomerNotFoundException;
import util.exception.ShoppingCartNotFoundException;
import util.exception.UpdateShoppingCartException;

/**
 *
 * @author lawrence
 */
@Stateless
@Local(ShoppingCartControllerLocal.class)
@Remote(ShoppingCartControllerRemote.class)

public class ShoppingCartController implements ShoppingCartControllerRemote, ShoppingCartControllerLocal {

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    @PersistenceContext(unitName = "PointOfSaleSystemV54-ejbPU")
    private EntityManager em;

    @Override
    public ShoppingCartEntity createNewShoppingCart(Long customerId, ShoppingCartEntity newShoppingCart) throws CreateNewShoppingCartException {
        try {
            CustomerEntity customerEntity = customerEntityControllerLocal.retrieveCustomerByCustomerId(customerId);
            newShoppingCart.setCustomerEntity(customerEntity);
            em.persist(newShoppingCart);
            customerEntity.setShoppingCartEntity(newShoppingCart);
            em.flush();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                try {
                    updateShoppingCart(customerId, newShoppingCart);
                } catch (UpdateShoppingCartException e) {
                    throw new CreateNewShoppingCartException("An error has occurred when updating the shopping cart: " + e.getMessage());
                }
            } else {
                throw new CreateNewShoppingCartException("An unexpected error has occurred: " + ex.getMessage());
            }
        } catch (Exception ex) {
            throw new CreateNewShoppingCartException("An unexpected error has occurred!" + ex.getMessage());
        }

        return newShoppingCart;
    }

    @Override
    public ShoppingCartEntity updateShoppingCart(Long customerId, ShoppingCartEntity newShoppingCart) throws UpdateShoppingCartException {
        try {
            CustomerEntity customerEntity = customerEntityControllerLocal.retrieveCustomerByCustomerId(customerId);
            ShoppingCartEntity oldShoppingCart = customerEntity.getShoppingCartEntity();
            em.remove(oldShoppingCart);
            customerEntity.setShoppingCartEntity(newShoppingCart);
            em.persist(newShoppingCart);
            em.flush();
        } catch (CustomerNotFoundException ex) {
            throw new UpdateShoppingCartException("Customer not found when updating shopping cart: " + ex.getMessage());
        } catch (Exception ex) {
            throw new UpdateShoppingCartException("An unexpected error has occurred: " + ex.getMessage());
        }
        return newShoppingCart;
    }

    @Override
    public ShoppingCartEntity retrieveShoppingCartById(Long shoppingCartId) throws ShoppingCartNotFoundException {

        if (shoppingCartId == null) {
            throw new ShoppingCartNotFoundException("Shopping Cart ID not provided!");
        }

        ShoppingCartEntity shoppingCartEntity = em.find(ShoppingCartEntity.class, shoppingCartId);

        if (shoppingCartEntity != null) {
            shoppingCartEntity.getShoppingCartLineEntities().size();
            return shoppingCartEntity;
        } else {
            throw new ShoppingCartNotFoundException("Shopping Cart ID " + shoppingCartId + " does not exist!");
        }
    }
    
    @Override
    public ShoppingCartEntity retrieveShoppingCartByCustomerId(Long customerId) throws ShoppingCartNotFoundException {
        try {
            CustomerEntity customerEntity = customerEntityControllerLocal.retrieveCustomerByCustomerId(customerId);
            ShoppingCartEntity shoppingCartEntity = customerEntity.getShoppingCartEntity();
            return shoppingCartEntity;
        } catch (CustomerNotFoundException ex) {
            throw new ShoppingCartNotFoundException("Shopping cart of customer does not exist: " + ex.getMessage());
        } catch (Exception ex) {
            throw new ShoppingCartNotFoundException("An unexpected error has occurred: " + ex.getMessage());
        }
    }

}
