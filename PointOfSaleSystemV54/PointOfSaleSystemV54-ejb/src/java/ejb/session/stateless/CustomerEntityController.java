package ejb.session.stateless;

import entity.CustomerEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewCustomerException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.security.CryptographicHelper;

@Stateless
@Local(CustomerEntityControllerLocal.class)
@Remote(CustomerEntityControllerRemote.class)

public class CustomerEntityController implements CustomerEntityControllerLocal, CustomerEntityControllerRemote {

    @PersistenceContext(unitName = "PointOfSaleSystemV54-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerEntityController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CustomerEntity createNewCustomer(CustomerEntity newCustomerEntity) throws InputDataValidationException, CreateNewCustomerException {
        Set<ConstraintViolation<CustomerEntity>> constraintViolations = validator.validate(newCustomerEntity);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newCustomerEntity);
                em.flush();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewCustomerException("Customer with the same email address already exists!");
                } else {
                    throw new CreateNewCustomerException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new CreateNewCustomerException("An unexpected error has occurred: " + ex.getMessage());
            }
            
            return newCustomerEntity;
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<CustomerEntity> retrieveAllCustomers() {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c");

        return query.getResultList();
    }

    @Override
    public CustomerEntity retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException {

        if (customerId == null) {
            throw new CustomerNotFoundException("Customer ID not provided!");
        }

        CustomerEntity customerEntity = em.find(CustomerEntity.class, customerId);

        if (customerEntity != null) {
            return customerEntity;
        } else {
            throw new CustomerNotFoundException("Customer ID " + customerId + " does not exist!");
        }
    }

    @Override
    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);

        try {
            return (CustomerEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Email " + email + " does not exist!");
        }
    }

    @Override
    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginCredentialException {
        try {
            CustomerEntity customerEntity = retrieveCustomerByEmail(email);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + customerEntity.getSalt()));

            if (customerEntity.getPassword().equals(passwordHash)) {
                customerEntity.getSaleTransactionEntities().size();
                return customerEntity;
            } else {
                throw new InvalidLoginCredentialException("Email does not exist or invalid password!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Email does not exist or invalid password!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomerEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
