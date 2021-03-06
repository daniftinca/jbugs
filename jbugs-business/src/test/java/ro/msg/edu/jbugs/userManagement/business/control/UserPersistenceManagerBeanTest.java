package ro.msg.edu.jbugs.userManagement.business.control;

import ro.msg.edu.jbugs.userManagement.business.exceptions.BusinessException;
import ro.msg.edu.jbugs.userManagement.business.exceptions.ExceptionCode;
import ro.msg.edu.jbugs.userManagement.persistence.dao.UserPersistenceManager;
import ro.msg.edu.jbugs.userManagement.persistence.entity.User;
import ro.msg.edu.jbugs.userManagement.business.dto.UserDTO;
import ro.msg.edu.jbugs.userManagement.business.utils.Encryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserPersistenceManagerBeanTest {



    @InjectMocks
    private UserManagementController userManagementController;

    @Mock
    private UserPersistenceManager userPersistenceManager;

    @Test
    public void generateUsername_expectedMarini() {
        when(userPersistenceManager.getUserByUsername(any(String.class)))
                .thenReturn(
                        Optional.empty()
                );
        String username = userManagementController.generateUsername("Ion", "Marin");

        assertEquals("marini", username);
    }


    @Test
    public void generateUsername_expectediftind(){
        when(userPersistenceManager.getUserByUsername(any(String.class)))
                .thenReturn(
                        Optional.empty()
                );
        assertEquals("iftind",userManagementController.generateUsername("Dan","Iftinca"));
    }

    @Test
    public void generateUsername_expectedionion(){
        when(userPersistenceManager.getUserByUsername(any(String.class)))
                .thenReturn(
                        Optional.empty()
                );
        assertEquals("ionion",userManagementController.generateUsername("Ion","Ion"));
    }

    @Test
    public void generateUsername(){
        when(userPersistenceManager.getUserByUsername(any(String.class)))
                .thenReturn(
                        Optional.empty()
                );
        assertEquals("ba0000",userManagementController.generateUsername("a","b"));
    }

    @Test
    public void testLogin_wrongUsername() {
        when(userPersistenceManager.getUserByUsername(any(String.class)))
                .thenReturn(Optional.empty());
        try {
            userManagementController.login("a", "s");
            fail("Shouldn't reach this point");
        } catch (BusinessException e){
            assertEquals(ExceptionCode.USERNAME_NOT_VALID,e.getExceptionCode());
        }
    }

    @Test
    public void testLogin_Success() {
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("salut");
        when(user.getPassword()).thenReturn(Encryptor.encrypt("secret"));

        when(userPersistenceManager.getUserByUsername(any(String.class)))
                .thenReturn(Optional.of(user));

        try{
            UserDTO userDTO = userManagementController.login("salut","secret");
            assertEquals(userDTO.getUsername(),user.getUsername());
        } catch(BusinessException e){
            fail("Shouldn't reach this point");
        }
    }

    @Test
    public void testCreateUser_Success(){
        when(userPersistenceManager.getUserByEmail(any(String.class)))
                .thenReturn(Optional.empty());


        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Cristi");
        userDTO.setLastName("Borcea");
        userDTO.setEmail("dinamo@msggroup.com");
        userDTO.setPhoneNumber("1234456667");
        userDTO.setPassword("IloveSteaua");
        try{
        UserDTO createdUser = userManagementController.createUser(userDTO);
        assertEquals(userDTO.getFirstName(),createdUser.getFirstName());
        assertEquals(userDTO.getLastName(),createdUser.getLastName());
        assertEquals(userDTO.getEmail(),createdUser.getEmail());
        assertEquals("borcec",createdUser.getUsername());
        } catch (BusinessException e){
            fail("Should not reach this point");
        }
    }
}