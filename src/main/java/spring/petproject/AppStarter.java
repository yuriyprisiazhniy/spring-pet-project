package spring.petproject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.dao.mapstorage.StaticMapStorage;
import spring.petproject.domain.Event;
import spring.petproject.domain.User;
import spring.petproject.service.UserService;

/**
 * Temporary class for launch
 */
public class AppStarter {

    public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        AbstractDomainObjectService<User> userService = new StaticMapStorage<>();
        userService.save(new User());
//        userService.save(new User(){{setId(1L); setFirstName("Updated");}});
//        userService.save(new User(){{setId(3L);}});
//        userService.save(new User());
//        userService.save(new User());
        AbstractDomainObjectService<Event> eventService = new StaticMapStorage<>();
        eventService.save(new Event());

    }
}
