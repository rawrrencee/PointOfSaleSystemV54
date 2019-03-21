/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author lawrence
 */
public class UpdateShoppingCartException extends Exception {

    public UpdateShoppingCartException() {
    }

    public UpdateShoppingCartException(String msg) {
        super(msg);
    }
}
