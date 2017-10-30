package spring.petproject.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.petproject.domain.Ticket;
import spring.petproject.domain.User;

import java.util.Random;
import java.util.Set;

@Aspect
@Component
public class LuckyWinnerAspect {

    private static final Logger logger = LoggerFactory.getLogger(LuckyWinnerAspect.class);
    private static final double LUCKY_RATE = 0.8;

    //TODO refactor when price will be assigned to Ticket
    @Before("execution(* spring.petproject.service.BookingService.bookTickets(..)) && args(tickets)")
    public void checkLucky(Set<Ticket> tickets) {
        tickets.stream()
                .filter(t -> t.getUser() != null)
                .filter(t -> new Random().nextDouble() > LUCKY_RATE)
                .forEach(ticket -> {
                            ticket.getUser()
                                    .addAdditionalInformation(String.format("Lucky ticket %d. Free of charge.", ticket.getSeat()));
                            logger.info("User {} bought lucky ticket {}", ticket.getUser(), ticket.getSeat());
                        });
    }

}
