package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import util.enumeration.AccessRightEnum;
import util.exception.DeleteStaffException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.UpdateStaffException;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class StaffEntityControllerTest
{
    private static StaffEntityControllerRemote staffEntityControllerRemote;
    
    
    
    public StaffEntityControllerTest()
    {
    }
    
    
    
    @BeforeClass
    public static void setUpClass() 
    {
        staffEntityControllerRemote = lookupStaffEntityControllerRemote();
    }
    
    
    
    @AfterClass
    public static void tearDownClass() 
    {
    }
    
    
    
    @Before
    public void setUp() 
    {
    }
    
    
    
    @After
    public void tearDown() 
    {
    }
    
    
    
    @Test
    public void test01CreateNewStaff01() throws InputDataValidationException
    {
        StaffEntity expectedSaffEntity = new StaffEntity("One", "Cashier", AccessRightEnum.CASHIER, "cashier1", "password");
        StaffEntity actualStaffEntity = staffEntityControllerRemote.createNewStaff(expectedSaffEntity);
        
        assertNotNull(actualStaffEntity.getStaffId());
        assertEquals(2l, actualStaffEntity.getStaffId().longValue());
        assertEquals(expectedSaffEntity.getUsername(), actualStaffEntity.getUsername());
    }
    
    
    
    // How can we improve this test case further given that EJBException is a very generic exception class?
    @Test(expected = EJBException.class)
    public void test02CreateNewStaff02() throws InputDataValidationException
    {
        StaffEntity expectedSaffEntity = new StaffEntity("One", "Cashier", AccessRightEnum.CASHIER, "cashier1", "password");
        StaffEntity actualStaffEntity = staffEntityControllerRemote.createNewStaff(expectedSaffEntity);     
    }
    
    
    
    @Test(expected = InputDataValidationException.class)
    public void test03CreateNewStaff03() throws InputDataValidationException
    {
        StaffEntity expectedSaffEntity = new StaffEntity("Two", "Cashier", AccessRightEnum.CASHIER, "cas", "password");
        StaffEntity actualStaffEntity = staffEntityControllerRemote.createNewStaff(expectedSaffEntity);     
    }
    
    
    
    @Test
    public void test04RetrieveAllStaffs01()
    {
        List<StaffEntity> actualStaffEntities = staffEntityControllerRemote.retrieveAllStaffs();
        
        assertFalse(actualStaffEntities.isEmpty());
        assertEquals(2, actualStaffEntities.size());
    }
    
    
    
    @Test
    public void test05RetrieveStaffByStaffId01() throws StaffNotFoundException
    {
        StaffEntity actualStaffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(1l);
        
        assertEquals(1l, actualStaffEntity.getStaffId().longValue());
    }
    
    
    
    @Test(expected = StaffNotFoundException.class)
    public void test06RetrieveStaffByStaffId02() throws StaffNotFoundException
    {
        StaffEntity actualStaffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(3l);
    }
    
    
    
    @Test
    public void test07StaffLogin01() throws InvalidLoginCredentialException
    {
        StaffEntity actualStaffEntity = staffEntityControllerRemote.staffLogin("manager", "password");
        
        assertEquals("manager", actualStaffEntity.getUsername());
    }
    
    
    
    @Test(expected = InvalidLoginCredentialException.class)
    public void test08StaffLogin02() throws InvalidLoginCredentialException
    {
        StaffEntity actualStaffEntity = staffEntityControllerRemote.staffLogin("manager", "123456");
    }
    
    
    
    @Test
    public void test09UpdateStaff01() throws InputDataValidationException, StaffNotFoundException, UpdateStaffException
    {
        StaffEntity actualStaffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(1l);
        actualStaffEntity.setFirstName("Default First Name");
        staffEntityControllerRemote.updateStaff(actualStaffEntity);
        actualStaffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(1l);
        
        assertEquals("Default First Name", actualStaffEntity.getFirstName());
    }
    
    
    
    @Test(expected = StaffNotFoundException.class)
    public void test10UpdateStaff02() throws InputDataValidationException, StaffNotFoundException, UpdateStaffException
    {
        StaffEntity actualStaffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(1l);
        actualStaffEntity.setStaffId(10l);
        actualStaffEntity.setFirstName("Default First Name");
        staffEntityControllerRemote.updateStaff(actualStaffEntity);        
    }
    
    
    
    @Test(expected = UpdateStaffException.class)
    public void test11UpdateStaff03() throws InputDataValidationException, StaffNotFoundException, UpdateStaffException
    {
        StaffEntity actualStaffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(1l);
        actualStaffEntity.setUsername("manager1");
        actualStaffEntity.setFirstName("Default First Name");
        staffEntityControllerRemote.updateStaff(actualStaffEntity);        
    }
    
        
    
    @Test(expected = InputDataValidationException.class)
    public void test12UpdateStaff04() throws InputDataValidationException, StaffNotFoundException, UpdateStaffException
    {
        StaffEntity actualStaffEntity = staffEntityControllerRemote.retrieveStaffByStaffId(1l);
        actualStaffEntity.setFirstName(null);
        staffEntityControllerRemote.updateStaff(actualStaffEntity);        
    }
    
    
    @Test(expected = StaffNotFoundException.class)
    public void test13DeleteStaff01() throws StaffNotFoundException, DeleteStaffException
    {
        staffEntityControllerRemote.deleteStaff(2l);
        staffEntityControllerRemote.retrieveStaffByStaffId(2l);
    }
    
    
    
    @Test(expected = StaffNotFoundException.class)
    public void test14DeleteStaff02() throws StaffNotFoundException, DeleteStaffException
    {
        staffEntityControllerRemote.deleteStaff(3l);
    }
    
    
    
    @Test
    public void test15RetrieveAllStaffs02()
    {
        List<StaffEntity> staffEntities = staffEntityControllerRemote.retrieveAllStaffs();
        
        assertFalse(staffEntities.isEmpty());
        assertEquals(1, staffEntities.size());
    }
    
        
    
    private static StaffEntityControllerRemote lookupStaffEntityControllerRemote()
    {
        try 
        {
            Context c = new InitialContext();
            
            return (StaffEntityControllerRemote) c.lookup("java:global/PointOfSaleSystemV44/PointOfSaleSystemV44-ejb/StaffEntityController!ejb.session.stateless.StaffEntityControllerRemote");
        }
        catch (NamingException ne)
        {
            throw new RuntimeException(ne);
        }
    }
}