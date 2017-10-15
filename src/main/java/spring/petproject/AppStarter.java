package spring.petproject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import spring.petproject.domain.Event;
import spring.petproject.domain.Ticket;
import spring.petproject.domain.User;
import spring.petproject.service.BookingService;
import spring.petproject.service.DiscountService;
import spring.petproject.service.validation.engine.ValidationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Temporary class for launch
 */
public class AppStarter {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        BookingService bookingService = context.getBean(BookingService.class);
        double price = bookingService.getTicketsPrice(
                new Event("King-Kong", 15),
                LocalDateTime.now(),
                new User() {{
                    setBirthday(LocalDate.now());
                }},
                new HashSet(){{add(2L); add(3L);}});

        System.out.println(price);

    }
}
