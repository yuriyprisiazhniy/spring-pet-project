package spring.petproject;

import spring.petproject.dao.mapstorage.domainimpl.StaticEventDAO;
import spring.petproject.dao.mapstorage.domainimpl.StaticUserDAO;
import spring.petproject.service.UserService;
import spring.petproject.service.impl.EventServiceImpl;
import spring.petproject.domain.*;
import spring.petproject.service.EventService;
import spring.petproject.service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * Temporary class for launch
 */
public class AppStarter {

    public static void main(String[] args) {
//        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

       /* UserService userService = new UserServiceImpl();
        EventService eventService = new EventServiceImpl();

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user3.setId(2L);
        user3.setFirstName("Changed");
        user3.setEmail("mail");
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
        event1.setId(2L);
        Event event2 = new Event();
        eventService.save(event);
        eventService.save(event1);
        eventService.save(event2);

        System.out.println("All events: " + eventService.getAll());
        System.out.println("All users: " + userService.getAll());

        AbstractDomainObjectService<Ticket> ticketService = new AbstractStaticStorage<Ticket>() {
            @Override
            protected Class<Ticket> getDomainClass() {
                return Ticket.class;
            }
        };
        Ticket ticket = new Ticket(user1, event1, LocalDateTime.now(), 2);
        ticketService.save(ticket);
        System.out.println(ticketService.getAll());

        System.out.println("User by email: " + userService.getUserByEmail("mail"));*/

       EventService eventService = new EventServiceImpl(new StaticEventDAO());
       LocalDateTime now = LocalDateTime.now();

       Event event = new Event();
       event.setName("First");
       event.setAirDates(new TreeSet<>(Arrays.asList(now, now.plusDays(1), now.plusDays(2))));
       eventService.save(event);

        Event event1 = new Event();
        LocalDateTime shifted = now.plusHours(1);
        event1.setName("Second");
        event1.setAirDates(new TreeSet<>(Arrays.asList(shifted, shifted.plusDays(1))));
        eventService.save(event1);

        System.out.println(eventService.getNextEvents(now.plusHours(7)));

        UserService userService = new UserServiceImpl(new StaticUserDAO());
        userService.save(new User(){{setFirstName("First");}});
        System.out.println(userService.getAll());
    }
}
