package ro.msg.edu.jbugs.userManagement.business.control;

import ro.msg.edu.jbugs.userManagement.business.control.UserManagementBean;
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

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserPersistenceManagerBeanTest {



    @InjectMocks
    UserManagementBean userManagementBean;

    @Mock
    UserPersistenceManager userPersistenceManager;

    @Test
    public void generateUsername_expectedMarini() {
        String username = userManagementBean.generateUsername("Ion", "Marin");
        assertEquals("marini", username);
    }

    @Test
    public void generateUsername_expectedIonion() {
        String username = userManagementBean.generateUsername("Ion", "Ion");
        assertEquals("ionion", username);
    }
    @Test
    public void generateUsername_expectedPetric() {
        String username = userManagementBean.generateUsername("Calin", "Petrindean");
        assertEquals("petric", username);
    }

    @Test
    public void generateUsername_expectedba0000() {
        String username = userManagementBean.generateUsername("a", "b");
        assertEquals("ba0000", username);
    }

    @Test
    public void createSuffix_expectedEmpty(){

        when(userPersistenceManager.findUsersNameStartingWith(any(String.class))).thenReturn(new ArrayList<>());
        String suffix = userManagementBean.createSuffix("dorel0");
        assertEquals( "",suffix);

    }

    @Test
    public void createSuffix_expected4(){


        when(userPersistenceManager.findUsersNameStartingWith(any(String.class)))
                .thenReturn(
                        new ArrayList<String>(){{
                            add("dorel0");
                            add("dorel01");
                            add("dorel02");
                            add("dorel03");
                        }}
                );
        String suffix = userManagementBean.createSuffix("dorel0");
        assertEquals( "4",suffix);

    }

    @Test
    public void createSuffix_expected7(){


        when(userPersistenceManager.findUsersNameStartingWith(any(String.class)))
                .thenReturn(
                        new ArrayList<String>(){{
                            add("dorel0");
                            add("dorel06");
                        }}
                );
        String suffix = userManagementBean.createSuffix("dorel0");
        assertEquals("7",suffix);

    }

    @Test
    public void createSuffix_expected1(){


        when(userPersistenceManager.findUsersNameStartingWith(any(String.class)))
                .thenReturn(
                        new ArrayList<String>(){{
                            add("marini");
                        }}
                );
        String suffix = userManagementBean.createSuffix("marini");
        assertEquals( "1",suffix);
    }

    @Test
    public void testLogin_wrongUsername() {
        when(userPersistenceManager.getUserForUsername(any(String.class)))
                .thenReturn(null);
        try {
            userManagementBean.login("a", "s");
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

        when(userPersistenceManager.getUserForUsername(any(String.class)))
                .thenReturn(user);

        try{
            UserDTO userDTO = userManagementBean.login("salut","secret");
            assertEquals(userDTO.getUsername(),user.getUsername());
        } catch(BusinessException e){
            fail("Shouldn't reach this point");
        }
    }

    @Test
    public void testCreateUser_Success(){
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Cristi");
        userDTO.setLastName("Borcea");
        userDTO.setEmail("dinamo@msggroup.com");
        userDTO.setPhoneNumber("1234456667");
        userDTO.setPassword("IloveSteaua");
        try{
        UserDTO createdUser = userManagementBean.createUser(userDTO);
        assertEquals(userDTO.getFirstName(),createdUser.getFirstName());
        assertEquals(userDTO.getLastName(),createdUser.getLastName());
        assertEquals(userDTO.getEmail(),createdUser.getEmail());
        assertEquals("borcec",createdUser.getUsername());
        } catch (BusinessException e){
            fail("Should not reach this point");
        }
    }
}