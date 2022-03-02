package com.csci310.db.services;

import com.csci310.TestSetupUtils;
import com.csci310.db.entities.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;

import com.csci310.db.repositories.UserRepository;
import com.csci310.spring.APIApplication;

import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = APIApplication.class)
public class UserServiceTest {
    private TestSetupUtils utils = new TestSetupUtils();

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetUserById_none() {
        // Setup
        UUID id = UUID.randomUUID();

        // Mocks
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Assertions
        Assert.assertTrue(userService.getUserById(id).isEmpty());
    }

    @Test
    public void testGetUserById_some() {
        // Setup
        UUID id = UUID.randomUUID();

        // Mocks
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(utils.makeUser("uname", "pword")));

        // Assertions
        Optional<User> result = userService.getUserById(id);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getUsername(), "uname");
    }

    @Test
    public void testIsUsernameTaken_true() {
        // Setup
        String username = "user1";

        // Mocks
        Mockito.when(userRepository.findByUsername(userService.encrypt(username))).thenReturn(Optional.of(utils.makeUser(username, "test")));

        // Assertions
        Assert.assertTrue((userService.isUsernameTaken(username)));
    }

    @Test
    public void testIsUsernameTaken_false() {
        // Setup
        String username = "uniqueUsername";

        // Mocks
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Assertions
        Assert.assertFalse((userService.isUsernameTaken(username)));
    }

    @Test
    public void testGetUserByUsername_some() {
        // Setup
        String username = "user1";

        // Mocks
        Mockito.when(userRepository.findByUsername(userService.encrypt(username))).thenReturn(Optional.of(utils.makeUser(username, "test")));

        // Assertions
        User result = userService.getUserByUsername(username);
        Assert.assertNotNull(result);
        Assert.assertEquals(userService.encrypt(username), result.getUsername());
    }

    @Test
    public void testGetUserByUsername_none() {
        // Setup
        String username = "user1";

        // Mocks
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Assertions
        User result = userService.getUserByUsername(username);
        Assert.assertNull(result);
    }

    @Test
    public void testDidAuthenticateUser_goodLogin() {
        // Setup
        String username = "user1";
        String password = "testPassword";

        // Mocks
        Mockito.when(userRepository.findByUsernameAndPassword(userService.encrypt(username), userService.encrypt(password))).thenReturn(Optional.of(utils.makeUser(username, password)));

        // Assertions
        Assert.assertTrue(userService.didAuthenticateUser(username, password));
    }

    @Test
    public void testDidAuthenticateUser_badLogin() {
        // Setup
        String username = "user1";
        String password = "testPassword";

        // Mocks
        Mockito.when(userRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.empty());

        // Assertions
        Assert.assertFalse(userService.didAuthenticateUser(username, password));
    }

    @Test
    public void testPersistUser() {
        // Setup
        String username = "user1";
        String password = "password";
        User user = new User(username, password);
        // Invocation
        userService.persistUser(user);
    }

    @Test
    public void testPasswordsMatch() {
        String pw1 = "pw", pw2 = "pw";
        Assert.assertTrue(userService.passwordsMatch(pw1, pw2));

        pw2 = "does not match";
        Assert.assertFalse(userService.passwordsMatch(pw1, pw2));

        pw1 = "pass";
        pw2 = "Pass";
        Assert.assertFalse(userService.passwordsMatch(pw1, pw2));
    }

    @Test
    public void testEncrypt() {
        Assert.assertEquals("uv!", userService.encrypt("hi!"));
        Assert.assertEquals("abc", userService.encrypt("nop"));
        Assert.assertNull(userService.encrypt(null));
    }

    @Test
    public void testDecrypt() {
        Assert.assertEquals("hi!", userService.decrypt("uv!"));
        Assert.assertEquals("Nn", userService.decrypt("Aa"));
        Assert.assertNull(userService.decrypt(null));
    }

    @Test
    public void testDeleteUserById() {
        // create a user first
        String username = "user2";
        String password = "password";
        User user = new User(username, password);
        userService.persistUser(user);

        // Invocation
        //delete user
        userService.deleteUserById(user.getId());

    }

    @Test
    public void testDeleteUserByUsername() {
        // create a user first
        String username = "user4";
        String password = "password";
        User user = new User(username, password);
        userService.persistUser(user);

        // Invocation
        //delete user
        userService.deleteUserByUsername(username);

    }
}
