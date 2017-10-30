package spring.petproject.aspect;


import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import spring.petproject.domain.Ticket;
import spring.petproject.domain.User;
import spring.petproject.service.BookingService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static org.mockito.Mockito.*;
import static org.mockito.Matchers.any;
import static org.testng.Assert.assertEquals;


public class LuckyWinnerAspectTest {

    private LuckyWinnerAspect aspect;

    private BookingService bookingServiceProxy;

    @BeforeMethod
    private void init(){
        Random randomMock = mock(Random.class);
        when(randomMock.nextDouble()).thenReturn(0.9);
        aspect = new LuckyWinnerAspect(randomMock);
        BookingService bookingServiceMock = mock(BookingService.class);
        doNothing().when(bookingServiceMock).bookTickets(any());

        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(bookingServiceMock);
        proxyFactory.addAspect(aspect);
        bookingServiceProxy = proxyFactory.getProxy();
    }


    @Test
    public void testLucky() {
        User user1 = spy(new User("first", "last", "mail"));
        User user2 = spy(new User("second", "last", "mail"));
        Ticket t1 = new Ticket(user1, null, LocalDateTime.now(), 1L);
        Ticket t2 = new Ticket(user2, null, LocalDateTime.now(), 2L);
        bookingServiceProxy.bookTickets(new HashSet<>(Arrays.asList(t1, t2)));
        verify(user1).addAdditionalInformation(any());
        verify(user2).addAdditionalInformation(any());
        assertEquals(user1.getAdditionalInformation().size(), 1);
        assertEquals(user2.getAdditionalInformation().size(), 1);
    }

}
