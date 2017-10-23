package spring.petproject.service.impl.discountstrategy;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import spring.petproject.domain.*;
import spring.petproject.service.DiscountStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.testng.Assert.*;

public class DiscountStrategyTest {

    private DiscountStrategy birthdayStrategy = new BirthdayDiscount();
    private DiscountStrategy multipleStrategy = new MultipleDiscount();


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

        return new Object[][]{
                {birthdayStrategy, new User("First", "Last", "mail"){{
                    setBirthday(LocalDate.now().minusDays(6));
                }}, testEvent, now, 5, null},
                {birthdayStrategy, new User("First", "Last", "mail"){{
                    setBirthday(LocalDate.now().minusDays(5));
                }}, testEvent, now, 5, new Discount("", (byte) 5)},
                {birthdayStrategy, new User("First", "Last", "mail"){{
                    setBirthday(LocalDate.now().minusYears(20));
                }}, testEvent, now, 5, new Discount("", (byte) 5)},
                {birthdayStrategy, new User("First", "Last", "mail"){{
                    setBirthday(now.toLocalDate());
                }}, testEvent, now, 6, new Discount("", (byte) 5)},
                {birthdayStrategy, new User("First", "Last", "mail"){{
                    setBirthday(LocalDate.now().plusDays(3));
                }}, testEvent, now, 8, new Discount("", (byte) 5)},
                {birthdayStrategy, new User("First", "Last", "mail"){{
                    setBirthday(LocalDate.now().minusYears(4).plusDays(8));
                }}, testEvent, now, 5, null},
                {birthdayStrategy, null, testEvent, now, 5, null}
        };
    }

    @Test(dataProvider = "birthdayStrategyDataProvider")
    public void testDiscountStrategy(DiscountStrategy strategy, User user, Event event,
                                     LocalDateTime time, long numberOfTickets, Discount result) {
        Discount discount = strategy.calculateDiscount(user, event, time, numberOfTickets);
        assertEquals(discount, result);
    }

}