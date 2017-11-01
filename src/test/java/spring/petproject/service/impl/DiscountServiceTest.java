package spring.petproject.service.impl;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import spring.petproject.domain.Discount;
import spring.petproject.domain.Event;
import spring.petproject.domain.User;
import spring.petproject.service.DiscountService;
import spring.petproject.service.DiscountStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class DiscountServiceTest {

    private DiscountService discountService = new DiscountServiceImpl();

    @BeforeClass
    public void init() {
        DiscountStrategy strategy1 = mock(DiscountStrategy.class);
        DiscountStrategy strategy2 = mock(DiscountStrategy.class);
        when(strategy1.calculateDiscount(any(), any(Event.class), any(LocalDateTime.class), anyLong(), any()))
                .thenReturn(new Discount(strategy1, "", (byte) 10));
        when(strategy1.calculateDiscount(any(), any(Event.class), any(LocalDateTime.class), eq(2L), any()))
                .thenReturn(null);
        when(strategy2.calculateDiscount(any(), any(Event.class), any(LocalDateTime.class), anyLong(), any()))
                .thenReturn(null);
        when(strategy2.calculateDiscount(any(), any(Event.class), any(LocalDateTime.class), eq(1L), any()))
                .thenReturn(new Discount(strategy2, "", (byte) 50));
        discountService.setDiscountStrategies(new HashSet<>(Arrays.asList(strategy1, strategy2)));
    }

    @DataProvider
    public Object[][] getDiscountDataProvider() {
        User testUser = new User("first", "last", "mail", LocalDate.now().minusYears(20));
        Event testEvent = new Event("test", 10.0);

        return new Object[][] {
                {testUser, testEvent, LocalDateTime.now(), 1, new TreeSet<>(Arrays.asList(1L, 2L, 3L)), (byte) 50},
                {testUser, testEvent, LocalDateTime.now(), 2, new TreeSet<>(Arrays.asList(1L, 2L, 3L)), (byte) 0},
                {null, testEvent, LocalDateTime.now(), 3, new TreeSet<>(Arrays.asList(1L, 2L, 3L)), (byte) 10},
                {null, testEvent, LocalDateTime.now(), 2, new TreeSet<>(Arrays.asList(1L, 2L, 3L)), (byte) 0},
        };
    }

    @Test(dataProvider = "getDiscountDataProvider")
    public void testGetDiscountMethod(User user, Event event, LocalDateTime time, long seat, NavigableSet<Long> tickets, byte result) {
        Discount discount = discountService.getDiscount(user, event, time, seat, tickets);
        discountService.getDiscountStrategies().forEach(strategy -> verify(strategy).calculateDiscount(user, event, time, seat, tickets));
        assertEquals(discount.getDiscount(), result);
    }
}
