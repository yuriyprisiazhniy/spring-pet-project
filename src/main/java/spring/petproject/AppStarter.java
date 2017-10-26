package spring.petproject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.dao.mapstorage.AbstractStaticStorage;
import spring.petproject.dao.mapstorage.IdGenerator;
import spring.petproject.dao.mapstorage.domainimpl.StaticEventDAO;
import spring.petproject.dao.mapstorage.domainimpl.StaticUserDAO;
import spring.petproject.service.*;
import spring.petproject.service.impl.EventServiceImpl;
import spring.petproject.domain.*;
import spring.petproject.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Temporary class for launch
 */
public class AppStarter {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        /*BookingService bookingService = context.getBean(BookingService.class);
        AuditoriumService auditoriumService = context.getBean(AuditoriumService.class);

        LocalDateTime now = LocalDateTime.now();

        Event event = new Event("testEvent", 10.0);
        event.addAirDateTime(now, auditoriumService.getByName("Red"));
        event.addAirDateTime(now.plusHours(1), auditoriumService.getByName("Red"));
        event.addAirDateTime(now.plusHours(2), auditoriumService.getByName("Red"));
        bookingService.bookTickets(new TreeSet<>(Collections.singleton(new Ticket(null, event, now, 10L))));

        Set<Ticket> purchasedTickets = bookingService.getPurchasedTicketsForEvent(event, now);
        System.out.println(purchasedTickets);

        UserService userService = context.getBean(UserService.class);
        userService.save(new User("a", "b", "c"));
        System.out.println(userService.getAll());*/

        UserService userService = context.getBean(UserService.class);
        userService.getAll();
        userService.getUserByEmail("s");
    }
}
