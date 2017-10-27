package spring.petproject.service.impl.discountstrategy;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import spring.petproject.domain.*;
import spring.petproject.service.DiscountStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.testng.Assert.*;

public class DiscountStrategyTest {

    private DiscountStrategy birthdayStrategy = new BirthdayDiscount();
    //each 5th ticket
    private DiscountStrategy multipleStrategy = new MultipleDiscount(5, (byte) 50);


    @DataProvider
    public Object[][] birthdayStrategyDataProvider() {
        Event testEvent = new Event("test", 10.1);
        testEvent.setRating(EventRating.HIGH);
        LocalDateTime now = LocalDateTime.now();
        Auditorium auditorium = new Auditorium();
        auditorium.setName("Red");
        testEvent.addAirDateTime(now, auditorium);
        testEvent.addAirDateTime(now.plusDays(1), auditorium);
        testEvent.addAirDateTime(now.plusDays(2), auditorium);
        NavigableSet<Long> tickets = new TreeSet<>();
        tickets.addAll(Arrays.asList(1L,2L,3L,4L,5L));

        return new Object[][]{
                {birthdayStrategy, new User("First", "Last", "mail"){{
                    setBirthday(LocalDate.now().minusDays(6));
                }}, testEvent, now, 1, tickets, null},
                {birthdayStrategy, new User("First", "Last", "mail"){{
                    setBirthday(LocalDate.now().minusDays(5));
                }}, testEvent, now, 1, tickets, new Discount(birthdayStrategy, "", (byte) 5)},
                {birthdayStrategy, new User("First", "Last", "mail"){{
                    setBirthday(LocalDate.now().minusYears(20));
                }}, testEvent, now, 1, tickets, new Discount(birthdayStrategy, "", (byte) 5)},
                {birthdayStrategy, new User("First", "Last", "mail"){{
                    setBirthday(now.minusYears(20).minusDays(5).toLocalDate());
                }}, testEvent, now, 1, tickets, new Discount(birthdayStrategy, "", (byte) 5)},
                {birthdayStrategy, new User("First", "Last", "mail"){{
                    setBirthday(LocalDate.now().plusDays(3));
                }}, testEvent, now, 1, tickets, new Discount(birthdayStrategy, "", (byte) 5)},
                {birthdayStrategy, new User("First", "Last", "mail"){{
                    setBirthday(LocalDate.now().minusYears(4).plusDays(8));
                }}, testEvent, now, 1, tickets, null},
                {birthdayStrategy, null, testEvent, now, 1, tickets, null}
        };
    }

    @DataProvider
    public Object[][] multipleStrategyDataProvider() {
        Event testEvent = new Event("test", 10.1);
        testEvent.setRating(EventRating.HIGH);
        LocalDateTime now = LocalDateTime.now();
        User testUser = new User("first", "last", "mail");
        TreeSet<Ticket> userTickets = new TreeSet<>();
        userTickets.add(new Ticket(testUser, testEvent, now, 1L));
        userTickets.add(new Ticket(testUser, testEvent, now, 2L));
        userTickets.add(new Ticket(testUser, testEvent, now, 3L));
        testUser.setTickets(userTickets);

        NavigableSet<Long> pack10Seats =  new TreeSet<>(Arrays.asList(4L, 5L, 6L, 7L, 8L, 9L, 10L));

        return new Object[][] {
                {multipleStrategy, testUser, testEvent, now, 4L, new TreeSet<>(Collections.singleton(4L)), null},
                {multipleStrategy, testUser, testEvent, now, 4L, new TreeSet<>(Arrays.asList(4L, 5L)), null},
                {multipleStrategy, testUser, testEvent, now, 4L, new TreeSet<>(Arrays.asList(5L, 4L)), null},
                {multipleStrategy, testUser, testEvent, now, 5L, new TreeSet<>(Arrays.asList(4L, 5L)),
                        new Discount(multipleStrategy, "", (byte) 50)},
                {multipleStrategy, null, testEvent, now, 4L, new TreeSet<>(Arrays.asList(1L, 2L, 3L, 4L, 5L)), null},
                {multipleStrategy, null, testEvent, now, 5L, new TreeSet<>(Arrays.asList(1L, 2L, 3L, 4L, 5L)),
                        new Discount(multipleStrategy, "", (byte) 50)},
                {multipleStrategy, testUser, testEvent, now, 6L, pack10Seats, null},
                {multipleStrategy, testUser, testEvent, now, 5L, pack10Seats,
                        new Discount(multipleStrategy, "", (byte) 50)},
                {multipleStrategy, testUser, testEvent, now, 10L, pack10Seats,
                        new Discount(multipleStrategy, "", (byte) 50)}
        };
    }

    @Test(dataProvider = "birthdayStrategyDataProvider")
    public void testBirthdayStrategy(DiscountStrategy strategy, User user, Event event,
                                     LocalDateTime time, long seat, NavigableSet<Long> tickets, Discount result) {
        Discount discount = strategy.calculateDiscount(user, event, time, seat, tickets);
        assertEquals(discount, result);
    }

    @Test(dataProvider = "multipleStrategyDataProvider")
    public void testMultipleStrategy(DiscountStrategy strategy, User user, Event event,
                                     LocalDateTime time, long seat, NavigableSet<Long> tickets, Discount result) {
        Discount discount = strategy.calculateDiscount(user, event, time, seat, tickets);
        assertEquals(discount, result);
    }

}