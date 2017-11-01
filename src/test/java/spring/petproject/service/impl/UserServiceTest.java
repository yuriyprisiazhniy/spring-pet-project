package spring.petproject.service.impl;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.domain.User;
import spring.petproject.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class UserServiceTest {

    private UserService userService;
    private List<User> testData = new ArrayList<>();

    @BeforeClass
    @SuppressWarnings("unchecked")
    public void init() {
        AbstractDomainObjectService<User> userDAO = (AbstractDomainObjectService<User>) mock(AbstractDomainObjectService.class);
        LocalDate birthday = LocalDate.now().minusYears(20);
        testData.add(new User("First", "User", "first@mail.com", birthday));
        testData.add(new User("Second", "User", "second@mail.com", birthday));
        testData.add(new User("Third", "User", "third@mail.com", birthday));
        when(userDAO.getAll()).thenReturn(testData);

        userService = new UserServiceImpl(userDAO);
    }

    @Test
    public void testGetUserByExistentEmail() {
        User ethalonUser = testData.get(1);
        User user = userService.getUserByEmail(ethalonUser.getEmail());
        assertNotNull(user, "User must be not null");
        assertEquals(user, ethalonUser);
    }

    @Test
    public void testGetUserByNotExistentEmail() {
        User user = userService.getUserByEmail("fake@mail.com");
        assertNull(user, "Return value must be null");
    }

}