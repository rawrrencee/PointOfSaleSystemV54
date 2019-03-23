package ejb.session.stateless;

import entity.CustomerEntity;
import entity.SaleTransactionEntity;
import java.util.List;
import util.exception.CreateNewCustomerException;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;

public interface CustomerEntityControllerRemote {

    public CustomerEntity createNewCustomer(CustomerEntity newCustomer) throws InputDataValidationException, CreateNewCustomerException;

    public List<CustomerEntity> retrieveAllCustomers();

    public CustomerEntity retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginCredentialException;

}
