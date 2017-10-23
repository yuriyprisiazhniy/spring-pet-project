package spring.petproject.dao.mapstorage;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import spring.petproject.domain.Event;
import spring.petproject.domain.User;

import java.util.*;


import static org.testng.Assert.*;

public class IdGeneratorTest {

    private Map<Long, User> userStorage;
    private List<Long> generatedIds;
    private Set<Long> etalonIds;

    @BeforeClass
    public void init() {
        //create initial storage to check that generated ids doesn't clash with existing ones
        userStorage = new HashMap<>();
        User user = new User("First", "Last", "Mail");
        user.setId(2L);
        userStorage.put(user.getId(), user);

        etalonIds = new HashSet<>();
        etalonIds.add(1L);
        //skip id=2 because it already contains in userStorage
        etalonIds.add(3L);
        etalonIds.add(4L);
        etalonIds.add(5L);

        generatedIds = Collections.synchronizedList(new ArrayList<>());
    }

    @Test(threadPoolSize = 4, invocationCount = 4, priority = -1)
    public void testGenerateId() {
        Long id = IdGenerator.generateId(User.class, userStorage);
        generatedIds.add(id);
    }

    @Test
    public void testLastGeneratedIndex() {
        long userNextId = IdGenerator.generateId(User.class, userStorage);
        assertEquals(userNextId, 6L, "Next id for User must be 6");
    }

    @Test
    public void testLastGeneratedIndexForUnaffectedEntity() {
        long eventNextId = IdGenerator.generateId(Event.class, new HashMap<>());
        assertEquals(eventNextId, 1L, "Next id for Event must be 1");
    }

    @Test
    public void testGeneratedIdSequence() {
        //check for event distribution
        assertEquals(generatedIds.size(), etalonIds.size(), "Id sequences must be equal");
        assertTrue(etalonIds.containsAll(generatedIds), "Id sequences must be equal");
    }

    @AfterClass
    public void clear() {
        IdGenerator.clearIdStorage();
    }

}