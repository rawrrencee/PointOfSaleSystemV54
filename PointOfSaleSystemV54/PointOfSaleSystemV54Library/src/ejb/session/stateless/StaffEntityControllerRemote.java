package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import util.exception.DeleteStaffException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.UpdateStaffException;



public interface StaffEntityControllerRemote
{
    StaffEntity createNewStaff(StaffEntity newStaffEntity) throws InputDataValidationException;
    
    List<StaffEntity> retrieveAllStaffs();
    
    StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException;
    
    StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException;

    StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;

    void updateStaff(StaffEntity staffEntity) throws InputDataValidationException, StaffNotFoundException, UpdateStaffException;
    
    void deleteStaff(Long staffId) throws StaffNotFoundException, DeleteStaffException;
}
