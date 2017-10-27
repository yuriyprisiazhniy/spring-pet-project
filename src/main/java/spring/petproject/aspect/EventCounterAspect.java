package spring.petproject.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.petproject.domain.Event;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class EventCounterAspect {
    private static final Logger logger = LoggerFactory.getLogger(EventCounterAspect.class);

    private final Map<Event, Statistic> eventStatistic = new HashMap<>();

    @AfterReturning(
            pointcut = "execution(* spring.petproject.service.EventService.getByName(*))",
            returning = "event"
    )
    public void countAccessEventByName(Event event) {
        if (event != null) {
            eventStatistic.putIfAbsent(event, new Statistic());
            Statistic statistic = eventStatistic.get(event);
            statistic.accessedByName++;
            logger.debug("Event accessed by name {} times", statistic.accessedByName);
        }
    }


    public Statistic getStatisticByEvent(Event e) {
        return eventStatistic.get(e);
    }

    public Map<Event, Statistic> getAllStatistic() {
        return eventStatistic;
    }

    public static class Statistic {
        private int accessedByName;
        private int priceQueried;
        private int ticketsBooked;

        private Statistic(int accessedByName, int priceQueried, int ticketsBooked) {
            this.accessedByName = accessedByName;
            this.priceQueried = priceQueried;
            this.ticketsBooked = ticketsBooked;
        }

        private Statistic() {
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
            return "Statistic{" +
                    "accessedByName=" + accessedByName +
                    ", priceQueried=" + priceQueried +
                    ", ticketsBooked=" + ticketsBooked +
                    '}';
        }
    }
}
