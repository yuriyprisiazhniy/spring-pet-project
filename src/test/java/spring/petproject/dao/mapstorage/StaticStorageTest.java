package spring.petproject.dao.mapstorage;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import spring.petproject.dao.mapstorage.domainimpl.StaticEventDAO;
import spring.petproject.dao.mapstorage.domainimpl.StaticUserDAO;
import spring.petproject.domain.Event;
import spring.petproject.domain.User;

import java.util.Arrays;

import static org.testng.Assert.*;

public class StaticStorageTest {

    private StaticUserDAO userDAO = new StaticUserDAO();
    private StaticEventDAO eventDAO = new StaticEventDAO();

    @BeforeMethod
    public void beforeMethod() {
        AbstractStaticStorage.clearStaticStorage(true);
    }

    @Test
    public void testSaveNewEntity() {
        User user = new User("First", "Last", "first@mail.com");
        userDAO.save(user);
        assertEquals(userDAO.getAll().size(), 1, "User storage must contain one element");
    }

    @Test
    public void testSaveNewEntityIdAssignment() {
        User user = new User("First", "Last", "first@mail.com");
        userDAO.save(user);
        assertNotNull(user.getId(), "Id must be assigned");
    }

    @Test
    public void testSaveNewEntityStorageIsolation() {
        User user = new User("First", "Last", "first@mail.com");
        userDAO.save(user);
        assertTrue(eventDAO.getAll().isEmpty(), "Entity saving must not affect other entity storage");
    }

    @Test
    public void testSaveEntityWithSameId() {
        User user = new User("First", "Last", "first@mail.com");
        userDAO.save(user);
        User secondUser = new User("Second", "User", "second@mail.com");
        secondUser.setId(1L);
        User savedUser = userDAO.save(secondUser);
        assertEquals(userDAO.getAll().size(), 1, "Second user must update existent one");
        assertEquals(savedUser, secondUser, "Second user must update existent one");
    }

    @Test
    public void testSaveSameEntityWithoutId() {
        User user = new User("First", "Last", "first@mail.com");
        User user1 = new User("First", "Last", "first@mail.com");
        userDAO.save(user);
        userDAO.save(user1);
        assertEquals(userDAO.getAll().size(), 2, "Second user must be added");
    }

    @Test
    public void testRemoveEntityWithId() {
        User user = new User("First", "Last", "first@mail.com");
        userDAO.save(user);
        userDAO.remove(user);
        assertTrue(userDAO.getAll().isEmpty(), "Storage must be empty");
    }

    @Test
    public void testRemoveEntityWithoutId() {
        User user = new User("First", "Last", "first@mail.com");
        User user1 = new User("First", "Last", "first@mail.com");
        assertEquals(user, user1);
        userDAO.save(user);
        userDAO.remove(user1);
        assertFalse(userDAO.getAll().isEmpty(), "User must not be deleted");
    }

    @Test
    public void testRemoveOtherEntityWithSameId() {
        User user = new User("First", "Last", "first@mail.com");
        User user1 = new User("Changed", "Changed", "changed@mail.com");
        userDAO.save(user);
        user1.setId(user.getId());
        userDAO.remove(user1);
        assertFalse(userDAO.getAll().isEmpty(), "User must not be deleted");
    }

    @Test
    public void testGetByNotExistentId() {
        User user = new User("First", "Last", "first@mail.com");
        userDAO.save(user);
        assertNull(userDAO.getById(2L), "Get by not existent id must return null");
    }

    @Test
    public void testGetByExistentId() {
        User user = new User("First", "Last", "first@mail.com");
        userDAO.save(user);
        User secondUser = userDAO.getById(1L);
        assertEquals(secondUser, user, "Users must be equals");
    }

    @Test
    public void testEmptyGetAll() {
        assertTrue(userDAO.getAll().isEmpty(), "Must be empty storage");
    }

    @Test
    public void testGetAll() {
        User user = new User("First", "Last", "first@mail.com");
        User user1 = new User("Changed", "Changed", "changed@mail.com");
        userDAO.save(user);
        userDAO.save(user1);
        assertTrue(userDAO.getAll().containsAll(Arrays.asList(user, user1)), "Storage must contains all entities");
    }

    @Test
    public void testClearStorageForAllDomains() {
        User user = new User("First", "Last", "first@mail.com");
        Event event = new Event("test", 10);
        userDAO.save(user);
        eventDAO.save(event);
        AbstractStaticStorage.clearStaticStorage(true);
        assertTrue(userDAO.getAll().isEmpty(), "User storage must be cleaned");
        assertTrue(eventDAO.getAll().isEmpty(), "Event storage must be cleaned");
    }

    @Test
    public void testClearStorageResetIdGeneratorSequence() {
        User user = new User("First", "Last", "first@mail.com");
        userDAO.save(user);
        assertEquals(user.getId(), Long.valueOf(1), "Id must be 1");
        AbstractStaticStorage.clearStaticStorage(true);
        User user1 = new User("First", "Last", "first@mail.com");
        userDAO.save(user1);
        assertEquals(user1.getId(), Long.valueOf(1), "Id must be 1 again");
    }

    @Test
    public void testClearStorageWithoutResetIdGeneratorSequence() {
        User user = new User("First", "Last", "first@mail.com");
        userDAO.save(user);
        assertEquals(user.getId(), Long.valueOf(1), "Id must be 1");
        AbstractStaticStorage.clearStaticStorage(false);
        User user1 = new User("First", "Last", "first@mail.com");
        userDAO.save(user1);
        assertNotEquals(user1.getId(), 1L, "Id must have other value");
    }

}
