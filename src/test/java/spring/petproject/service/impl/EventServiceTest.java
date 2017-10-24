package spring.petproject.service.impl;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.domain.Auditorium;
import spring.petproject.domain.Event;
import spring.petproject.domain.EventRating;
import spring.petproject.service.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class EventServiceTest {

    private EventService eventService;
    private List<Event> testData = new ArrayList<>();
    private LocalDateTime testDateTime;

    @BeforeClass
    @SuppressWarnings("unchecked")
    public void init() {
        testDateTime = LocalDateTime.of(2017, 10, 24, 12, 0);

        AbstractDomainObjectService<Event> eventDAO = (AbstractDomainObjectService<Event>) mock(AbstractDomainObjectService.class);

        Auditorium red = new Auditorium();
        red.setName("red");
        red.setNumberOfSeats(5);
        Auditorium green = new Auditorium();
        green.setName("green");
        green.setNumberOfSeats(10);
        green.setVipSeats(new HashSet(Arrays.asList(1L, 2L, 3L)));

        Event event1 = new Event("first", 10.0);
        event1.addAirDateTime(testDateTime.minusDays(1), red);
        event1.addAirDateTime(testDateTime, red);
        event1.addAirDateTime(testDateTime.plusDays(1), green);

        Event event2 = new Event("second", 20.0);
        event2.addAirDateTime(testDateTime.plusHours(2), green);
        event2.addAirDateTime(testDateTime.plusHours(4), green);
        event2.addAirDateTime(testDateTime.plusHours(2).plusDays(2), green);
        testData = Arrays.asList(event1, event2);
        when(eventDAO.getAll()).thenReturn(testData);
        eventService = new EventServiceImpl(eventDAO);
    }

    @DataProvider
    public Object[][] getForDateRangeDataProvider() {
        LocalDate date = testDateTime.toLocalDate();
        return new Object[][] {
                {date.minusDays(10), date.plusDays(10), new HashSet<>(testData)},
                {date, date.plusDays(1), new HashSet<>(testData)},
                {date.plusDays(1), date.plusDays(2), new HashSet<>(testData)},
                {date.plusDays(2), date.plusDays(3), Collections.singleton(testData.get(1))},
                {date.plusDays(3), date.plusDays(2), Collections.emptySet()},
                {date.minusDays(2), date.minusDays(1), Collections.singleton(testData.get(0))},
        };
    }

    @Test
    public void testFindEventByName() {
        Event ethalonEvent = testData.get(0);
        Event event = eventService.getByName(ethalonEvent.getName());
        assertNotNull(event);
        assertEquals(event, ethalonEvent);
    }

    @Test
    public void testFindEventByNotExistentName() {
        Event event = eventService.getByName("fake");
        assertNull(event);
    }

    @Test(dataProvider = "getForDateRangeDataProvider")
    public void testGetForDateRange(LocalDate from, LocalDate to, Set<Event> result) {
        Set<Event> events = eventService.getForDateRange(from, to);
        assertEquals(events, result);
    }

}
