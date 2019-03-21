/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ShoppingCartEntity;
import javax.ejb.Remote;
import util.exception.CreateNewShoppingCartException;
import util.exception.ShoppingCartNotFoundException;
import util.exception.UpdateShoppingCartException;

/**
 *
 * @author lawrence
 */
public interface ShoppingCartControllerRemote {

    public ShoppingCartEntity retrieveShoppingCartById(Long shoppingCartId) throws ShoppingCartNotFoundException;

    public ShoppingCartEntity createNewShoppingCart(Long customerId, ShoppingCartEntity newShoppingCart) throws CreateNewShoppingCartException;

    public ShoppingCartEntity updateShoppingCart(Long customerId, ShoppingCartEntity newShoppingCart) throws UpdateShoppingCartException;

    public ShoppingCartEntity retrieveShoppingCartByCustomerId(Long customerId) throws ShoppingCartNotFoundException;

}
