package spring.petproject.aspect.statistic;

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import spring.petproject.domain.Event;
import spring.petproject.domain.Ticket;
import spring.petproject.domain.User;
import spring.petproject.service.BookingService;
import spring.petproject.service.EventService;
import spring.petproject.service.exception.BookingException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class EventCounterAspectTest {

    private EventCounterAspect aspect;

    private EventService eventServiceProxy;
    private BookingService bookingServiceProxy;

    private Event testEvent1;
    private Event testEvent2;

    @BeforeMethod
    private void init() {
        aspect = new EventCounterAspect();
        testEvent1 = new Event("first", 10.0);
        testEvent2 = new Event("second", 20.0);
        EventService eventServiceMock = mock(EventService.class);
        when(eventServiceMock.getByName(any())).thenReturn(null);
        when(eventServiceMock.getByName("first")).thenReturn(testEvent1);
        when(eventServiceMock.getByName("second")).thenReturn(testEvent2);

        AspectJProxyFactory eventProxyFactory = new AspectJProxyFactory(eventServiceMock);
        eventProxyFactory.addAspect(aspect);
        eventServiceProxy = eventProxyFactory.getProxy();

        BookingService bookingServiceMock = mock(BookingService.class);
        when(bookingServiceMock.getTicketsPrice(any(Event.class), any(), any(), any())).thenReturn(0.0);
        when(bookingServiceMock.getTicketsPrice(any(Event.class), any(), any(User.class), any())).thenThrow(BookingException.class);
        doNothing().when(bookingServiceMock).bookTickets(any());
        AspectJProxyFactory bookingProxyFactory = new AspectJProxyFactory(bookingServiceMock);
        bookingProxyFactory.addAspect(aspect);
        bookingServiceProxy = bookingProxyFactory.getProxy();
    }

    @Test
    public void testGetByWrongName() {
        eventServiceProxy.getByName("fake");
        assertTrue(aspect.getAllStatistic().isEmpty());
    }

    @Test
    public void testGetEmptyStatisticByEvent() {
        assertEquals(aspect.getStatisticByEvent(testEvent1).getAccessedByName(), 0);
        assertEquals(aspect.getStatisticByEvent(testEvent1).getPriceQueried(), 0);
        assertEquals(aspect.getStatisticByEvent(testEvent1).getTicketsBooked(), 0);
    }

    @Test
    public void testGetByName() {
        eventServiceProxy.getByName("first");
        eventServiceProxy.getByName("second");
        eventServiceProxy.getByName("first");

        assertEquals(aspect.getAllStatistic().size(), 2);
        assertEquals(aspect.getStatisticByEvent(testEvent1).getAccessedByName(), 2);
        assertEquals(aspect.getStatisticByEvent(testEvent2).getAccessedByName(), 1);
    }

    @Test
    public void testPriceQueried() {
        bookingServiceProxy.getTicketsPrice(testEvent1, null, null, null);
        bookingServiceProxy.getTicketsPrice(testEvent1, null, null, null);
        assertEquals(aspect.getStatisticByEvent(testEvent1).getTicketsBooked(), 0);
        assertEquals(aspect.getStatisticByEvent(testEvent1).getPriceQueried(), 2);
    }

    @Test
    public void testPriceQueriedException() {
        bookingServiceProxy.getTicketsPrice(testEvent1, null, null, null);
        try {
            bookingServiceProxy.getTicketsPrice(testEvent1, null, new User("", "", "", LocalDate.now().minusYears(20)), null);
            fail("Exception must be thrown");
        } catch (BookingException ex) {
            assertEquals(aspect.getStatisticByEvent(testEvent1).getPriceQueried(), 1);
        }
    }

    @Test
    public void testBookTickets() {
        Ticket t1 = new Ticket(null, testEvent1, null, 0);
        Ticket t2 = new Ticket(null, testEvent1, null, 1);
        Ticket t3 = new Ticket(null, testEvent2, null, 2);
        Set<Ticket> tickets = new HashSet<>(Arrays.asList(t1, t2, t3));
        bookingServiceProxy.bookTickets(tickets);
        assertEquals(aspect.getAllStatistic().size(), 2);
        assertEquals(aspect.getStatisticByEvent(testEvent1).getTicketsBooked(), 2);
        assertEquals(aspect.getStatisticByEvent(testEvent2).getTicketsBooked(), 1);
    }

}
