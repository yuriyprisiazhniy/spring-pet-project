package spring.petproject.service.impl;

import org.testng.annotations.*;
import spring.petproject.domain.*;
import spring.petproject.service.BookingService;
import spring.petproject.service.DiscountService;
import spring.petproject.service.exception.BookingException;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class BookingServiceTest {

    private BookingService bookingService;
    private DiscountService discountServiceMock;

    private Event testEvent;
    private User testUser;
    private LocalDateTime testDateTime;

    @BeforeClass
    public void init() {
        testDateTime = LocalDateTime.of(2017, 10, 24, 12, 0);
        discountServiceMock = mock(DiscountService.class);
        when(discountServiceMock.getDiscount(any(), any(),any(), anyLong(), any())).thenReturn((byte) 0);
        when(discountServiceMock.getDiscount(any(User.class), any(),any(), eq(2L), any())).thenReturn((byte) 30);

        initTestEntities();
    }

    @BeforeMethod
    public void beforeMethod() {
        initTestEntities();
    }

    private void initTestEntities() {
        Auditorium a1 = new Auditorium();
        a1.setName("Red");
        a1.setNumberOfSeats(10);
        a1.setVipSeats(new HashSet<>(Arrays.asList(1L, 2L, 3L)));
        Auditorium a2 = new Auditorium();
        a2.setName("Green");
        a2.setNumberOfSeats(5);
        a2.setVipSeats(Collections.singleton(2L));

        testEvent = new Event("First", 10.0);
        testEvent.addAirDateTime(testDateTime, a1);
        testEvent.addAirDateTime(testDateTime.plusHours(1), a1);
        testEvent.addAirDateTime(testDateTime.plusHours(2), a2);
        testEvent.setRating(EventRating.MID);

        testUser = new User("First", "Last", "mail");
        testUser.setBirthday(testDateTime.toLocalDate().minusYears(20));

        bookingService = new BookingServiceImpl(discountServiceMock);
    }

    @DataProvider
    public Object[][] getTicketPriceDataProvider() {
        return new Object[][] {
                {testEvent, testDateTime.plusHours(1), testUser, new TreeSet<>(Arrays.asList(1L, 2L)), 34},
                {testEvent, testDateTime.plusHours(1), null, new TreeSet<>(Arrays.asList(1L, 2L)), 40},
                {testEvent, testDateTime.plusHours(1), testUser, new TreeSet<>(Arrays.asList(1L, 3L)), 40},
                {testEvent, testDateTime.plusHours(1), testUser, new TreeSet<>(Arrays.asList(1L, 4L, 5L)), 40},
                {testEvent, testDateTime.plusHours(1), null, new TreeSet<>(Arrays.asList(1L, 4L, 5L)), 40},
                {testEvent, testDateTime.plusHours(1), null, new TreeSet<>(Collections.emptySet()), 0}
        };
    }

    @Test(dataProvider = "getTicketPriceDataProvider")
    public void testGetTicketPriceSimpleConditions(Event event, LocalDateTime dateTime, User user, NavigableSet<Long> seats, double result) {
        double ticketsPrice = bookingService.getTicketsPrice(event, dateTime, user, seats);
        assertEquals(ticketsPrice, result);
    }

    @Test(expectedExceptions = BookingException.class, expectedExceptionsMessageRegExp = "Event doesn't air on specified dateTime")
    public void testGetTicketPriceWrongTime() {
        bookingService.getTicketsPrice(testEvent, testDateTime.plusHours(5), testUser, new TreeSet<>(Arrays.asList(1L)));
    }

    @Test(expectedExceptions = BookingException.class, expectedExceptionsMessageRegExp = "There are no specified seats in auditorium")
    public void testGetTicketPriceWrongSeat() {
        bookingService.getTicketsPrice(testEvent, testDateTime.plusHours(2), testUser, new TreeSet<>(Arrays.asList(5L, 6L)));
    }

    @Test(expectedExceptions = BookingException.class, expectedExceptionsMessageRegExp = ".*already booked.*")
    public void testGetTicketPriceAlreadyBooked() {
        Ticket t1 = new Ticket(testUser, testEvent, testDateTime.plusHours(2), 5L);
        Ticket t2 = new Ticket(testUser, testEvent, testDateTime.plusHours(2), 6L);
        bookingService.bookTickets(new HashSet<>(Arrays.asList(t1, t2)));
        bookingService.getTicketsPrice(testEvent, testDateTime.plusHours(2), testUser, new TreeSet<>(Arrays.asList(4L, 5L)));
    }

    @Test
    public void testGetPurchasedTickets() {
        Ticket t1 = new Ticket(testUser, testEvent, testDateTime.plusHours(2), 5L);
        Ticket t2 = new Ticket(testUser, testEvent, testDateTime.plusHours(2), 6L);
        bookingService.bookTickets(new HashSet<>(Arrays.asList(t1, t2)));
        Set<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(testEvent, testDateTime.plusHours(2));
        assertEquals(purchasedTicketsForEvent.size(), 2);
        assertTrue(purchasedTicketsForEvent.containsAll(Arrays.asList(t1, t2)));
    }

    @Test
    public void testGetPurchasedTicketsWrongTime() {
        Ticket t1 = new Ticket(testUser, testEvent, testDateTime.plusHours(2), 5L);
        bookingService.bookTickets(new HashSet<>(Collections.singleton(t1)));
        Set<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(testEvent, testDateTime.plusHours(6));
        assertTrue(purchasedTicketsForEvent.isEmpty());
    }

    @Test(expectedExceptions = BookingException.class, expectedExceptionsMessageRegExp = ".*already booked.*")
    public void testBookAlreadyBookedTicket() {
        Ticket t1 = new Ticket(null, testEvent, testDateTime.plusHours(2), 5L);
        bookingService.bookTickets(new HashSet<>(Collections.singleton(t1)));
        assertEquals(bookingService.getPurchasedTicketsForEvent(testEvent, testDateTime.plusHours(2)).size(), 1);
        bookingService.bookTickets(new HashSet<>(Collections.singleton(t1)));
    }

    @Test
    public void testBookedTicketSavedInUser() {
        assertTrue(testUser.getTickets().isEmpty());
        Ticket t1 = new Ticket(testUser, testEvent, testDateTime.plusHours(2), 5L);
        Ticket t2 = new Ticket(null, testEvent, testDateTime.plusHours(2), 6L);
        Ticket t3 = new Ticket(testUser, testEvent, testDateTime.plusHours(2), 4L);
        bookingService.bookTickets(new HashSet<>(Arrays.asList(t1, t2, t3)));
        assertEquals(testUser.getTickets().size(), 2);
        assertEquals(bookingService.getPurchasedTicketsForEvent(testEvent, testDateTime.plusHours(2)).size(), 3);
    }

    //TODO no ticket validation for now
    @Test(enabled = false, expectedExceptions = BookingException.class)
    public void testBookNotValidTicket() {
        Ticket t1 = new Ticket(testUser, null, null, 5L);
        bookingService.bookTickets(new TreeSet<>(Collections.singleton(t1)));
    }

}
