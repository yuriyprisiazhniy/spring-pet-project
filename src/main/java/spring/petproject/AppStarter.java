package spring.petproject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.dao.mapstorage.AbstractStaticStorage;
import spring.petproject.domain.Event;
import spring.petproject.domain.User;
import spring.petproject.service.UserService;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Temporary class for launch
 */
public class AppStarter {

    public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        AbstractDomainObjectService<User> userService = new AbstractStaticStorage<User>() {
            @Override
            public User getById(@Nonnull Long id) {
                return null;
            }

            @Nonnull
            @Override
            public Collection<User> getAll() {
                return null;
            }
        };
        AbstractDomainObjectService<Event> eventService = new AbstractStaticStorage<Event>() {
            @Override
            public Event getById(@Nonnull Long id) {
                return null;
            }

            @Nonnull
            @Override
            public Collection<Event> getAll() {
                return null;
            }
        };

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user3.setId(2L);
        user3.setFirstName("Changed");
        User user4 = new User();
        user4.setId(3L);
        User user5 = new User();

        System.out.println("Returned object: " + userService.save(user1));
        userService.save(user2);
        userService.save(user3);
        userService.save(user4);
        userService.save(user5);

        Event event = new Event();
        Event event1 = new Event();
        event1.setId(1L);
        eventService.save(event);
        eventService.save(event1);

    }
}
