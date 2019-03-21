package util.security;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;



public class PasswordCallbackHandler implements CallbackHandler
{
    private String username;
    private char[] password;

    
    
    public PasswordCallbackHandler(String username, char[] password)
    {
        super();
        
        this.username = username;
        this.password = password;
    }

    
    
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
    {
        if(callbacks == null)
        {
            return;
        }

        for (int i = 0; i < callbacks.length; i++) 
        {
            if (callbacks[i] instanceof NameCallback) 
            {
                NameCallback nc = (NameCallback) callbacks[i];
                nc.setName(username);
            }
            else if (callbacks[i] instanceof PasswordCallback) 
            {
                PasswordCallback pc = (PasswordCallback) callbacks[i];
                pc.setPassword(password);
            } 
            else 
            {
                throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
            }
        }
    }
}