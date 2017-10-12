package spring.petproject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.dao.mapstorage.AbstractStaticStorage;
import spring.petproject.dao.mapstorage.domainimpl.StaticEventDAO;
import spring.petproject.dao.mapstorage.domainimpl.StaticUserDAO;
import spring.petproject.service.UserService;
import spring.petproject.service.impl.EventServiceImpl;
import spring.petproject.domain.*;
import spring.petproject.service.EventService;
import spring.petproject.service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeSet;

/**
 * Temporary class for launch
 */
public class AppStarter {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        UserService userService = context.getBean(UserService.class);
        EventService eventService = context.getBean(EventService.class);

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user3.setId(2L);
        user3.setFirstName("Changed");
        user3.setEmail("mail");
        User user4 = new User();
        user4.setId(3L);
        User user5 = new User();

        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
        userService.save(user4);
        userService.save(user5);

        Event event = new Event();
        Event event1 = new Event();
        event1.setId(2L);
        Event event2 = new Event();
        eventService.save(event);
        eventService.save(event1);
        eventService.save(event2);

        System.out.println("All events: " + eventService.getAll());
        System.out.println("All users: " + userService.getAll());

        System.out.println("User by email: " + userService.getUserByEmail("mail"));

        System.out.println("All storage content:");
        Map<Class<? extends DomainObject>, Map<Long, ? extends DomainObject>> allStorageContent = AbstractStaticStorage.getAllStorageContent();
        for (Class cl : allStorageContent.keySet()) {
            System.out.print(cl.getSimpleName() + " : ");
            System.out.println(allStorageContent.get(cl));
        }

    }
}
