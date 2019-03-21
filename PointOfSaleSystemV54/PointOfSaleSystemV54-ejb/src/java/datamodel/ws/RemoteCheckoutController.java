package datamodel.ws;

import ejb.session.stateful.CheckoutControllerLocal;
import java.util.Date;



public class RemoteCheckoutController 
{
    private String sessionKey;
    private CheckoutControllerLocal checkoutControllerLocal;
    private Date expiryDateTime;

    
    
    public RemoteCheckoutController() 
    {
    }

    
    
    public RemoteCheckoutController(String sessionKey, CheckoutControllerLocal checkoutControllerLocal, Date expiryDateTime) 
    {
        this.sessionKey = sessionKey;
        this.checkoutControllerLocal = checkoutControllerLocal;
        this.expiryDateTime = expiryDateTime;
    }

    
    
    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public CheckoutControllerLocal getCheckoutControllerLocal() {
        return checkoutControllerLocal;
    }

    public void setCheckoutControllerLocal(CheckoutControllerLocal checkoutControllerLocal) {
        this.checkoutControllerLocal = checkoutControllerLocal;
    }

    public Date getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(Date expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }
}