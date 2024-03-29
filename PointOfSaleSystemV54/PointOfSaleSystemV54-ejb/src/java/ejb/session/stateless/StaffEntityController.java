package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteStaffException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ProductNotFoundException;
import util.exception.StaffNotFoundException;
import util.exception.UpdateStaffException;
import util.security.CryptographicHelper;



@Stateless
@Local(StaffEntityControllerLocal.class)
@Remote(StaffEntityControllerRemote.class)

public class StaffEntityController implements StaffEntityControllerLocal, StaffEntityControllerRemote
{

    @PersistenceContext(unitName = "PointOfSaleSystemV54-ejbPU")
    private EntityManager entityManager;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
        
    
    public StaffEntityController()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    // Updated in v4.2 with bean validation
    
    @Override
    public StaffEntity createNewStaff(StaffEntity newStaffEntity) throws InputDataValidationException
    {        
        Set<ConstraintViolation<StaffEntity>>constraintViolations = validator.validate(newStaffEntity);
        
        if(constraintViolations.isEmpty())
        {
            entityManager.persist(newStaffEntity);
            entityManager.flush();

            return newStaffEntity;
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public List<StaffEntity> retrieveAllStaffs()
    {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException
    {
        if(staffId == null)
        {
            throw new StaffNotFoundException("Staff ID not provided");
        }
        
        StaffEntity staffEntity = entityManager.find(StaffEntity.class, staffId);
        
        if(staffEntity != null)
        {
            return staffEntity;
        }
        else
        {
            throw new StaffNotFoundException("Staff ID " + staffId + " does not exist!");
        }
    }
    
    
    
    @Override
    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException
    {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (StaffEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new StaffNotFoundException("Staff Username " + username + " does not exist!");
        }
    }
    
    
    
    // Updated in v4.4 to include password hashing
    
    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            StaffEntity staffEntity = retrieveStaffByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + staffEntity.getSalt()));
            
            if(staffEntity.getPassword().equals(passwordHash))
            {
                staffEntity.getSaleTransactionEntities().size();                
                return staffEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(StaffNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
            
    
    @Override
    public void updateStaff(StaffEntity staffEntity) throws InputDataValidationException, StaffNotFoundException, UpdateStaffException
    {
        // Updated in v4.1 to update selective attributes instead of merging the entire state passed in from the client
        // Also check for existing staff before proceeding with the update
        
        // Updated in v4.2 with bean validation
        
        Set<ConstraintViolation<StaffEntity>>constraintViolations = validator.validate(staffEntity);
        
        if(constraintViolations.isEmpty())
        {        
            if(staffEntity.getStaffId() != null)
            {
                StaffEntity staffEntityToUpdate = retrieveStaffByStaffId(staffEntity.getStaffId());
                
                if(staffEntityToUpdate.getUsername().equals(staffEntity.getUsername()))
                {
                    staffEntityToUpdate.setFirstName(staffEntity.getFirstName());
                    staffEntityToUpdate.setLastName(staffEntity.getLastName());
                    staffEntityToUpdate.setAccessRightEnum(staffEntity.getAccessRightEnum());                
                    // Username and password are deliberately NOT updated to demonstrate that client is not allowed to update account credential through this business method
                }
                else
                {
                    throw new UpdateStaffException("Username of staff record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new StaffNotFoundException("Staff ID not provided for staff to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public void deleteStaff(Long staffId) throws StaffNotFoundException, DeleteStaffException
    {
        StaffEntity staffEntityToRemove = retrieveStaffByStaffId(staffId);
        
        if(staffEntityToRemove.getSaleTransactionEntities().isEmpty())
        {
            entityManager.remove(staffEntityToRemove);
        }
        else
        {
            // New in v4.1 to prevent deleting staff with existing sale transaction(s)
            throw new DeleteStaffException("Staff ID " + staffId + " is associated with existing sale transaction(s) and cannot be deleted!");
        }
    }
    
    
    
    // Added in v4.2
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<StaffEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}