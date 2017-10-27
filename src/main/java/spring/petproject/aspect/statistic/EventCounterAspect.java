package spring.petproject.aspect.statistic;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.petproject.domain.Event;
import spring.petproject.domain.Ticket;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Aspect
@Component
public class EventCounterAspect {
    private static final Logger logger = LoggerFactory.getLogger(EventCounterAspect.class);

    private final Map<Event, EventStatistic> eventStatistic = new HashMap<>();

    @AfterReturning(
            pointcut = "execution(* spring.petproject.service.EventService.getByName(*))",
            returning = "event"
    )
    public void countAccessEventByNameTimes(Event event) {
        if (event != null) {
            EventStatistic statistic = eventStatistic.computeIfAbsent(event, e -> new EventStatistic());
            statistic.accessedByName++;
            logger.debug("Event {} accessed by name {} times", event, statistic.accessedByName);
        }
    }

    @AfterReturning(
            pointcut = "execution(* spring.petproject.service.BookingService.getTicketsPrice(..)) && args(event, ..))"
    )
    public void countPriceQueriedTimes(Event event) {
        EventStatistic statistic = eventStatistic.computeIfAbsent(event, e -> new EventStatistic());
        statistic.priceQueried++;
        logger.debug("Price queried for event {} {} times", event, statistic.priceQueried);
    }

    @AfterReturning(
            pointcut = "execution(* spring.petproject.service.BookingService.bookTickets(..)) && args(tickets))"
    )
    public void countTicketBookedTimes(Set<Ticket> tickets) {
        tickets.forEach(ticket -> {
            Event event = ticket.getEvent();
            EventStatistic statistic = eventStatistic.computeIfAbsent(event, e -> new EventStatistic());
            statistic.ticketsBooked++;
            logger.debug("For event {} booked {} tickets", event, statistic.ticketsBooked);
        });
    }

    public EventStatistic getStatisticByEvent(Event e) {
        return eventStatistic.get(e);
    }

    public Map<Event, EventStatistic> getAllStatistic() {
        return new HashMap<>(eventStatistic);
    }

    public final static class EventStatistic {
        private int accessedByName;
        private int priceQueried;
        private int ticketsBooked;

        private EventStatistic(int accessedByName, int priceQueried, int ticketsBooked) {
            this.accessedByName = accessedByName;
            this.priceQueried = priceQueried;
            this.ticketsBooked = ticketsBooked;
        }

        private EventStatistic() {
        }

        public int getAccessedByName() {
            return accessedByName;
        }


        public int getPriceQueried() {
            return priceQueried;
        }


        public int getTicketsBooked() {
            return ticketsBooked;
        }

        @Override
        public String toString() {
            return "EventStatistic{" +
                    "accessedByName=" + accessedByName +
                    ", priceQueried=" + priceQueried +
                    ", ticketsBooked=" + ticketsBooked +
                    '}';
        }
    }
}
