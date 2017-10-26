package spring.petproject.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import spring.petproject.domain.*;
import spring.petproject.service.BookingService;
import spring.petproject.service.DiscountService;
import spring.petproject.service.exception.BookingException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    private static final double VIP_SEAT_COST_MULTIPLIER = 2;
    private static final double HIGH_RATE_COST_MULTIPLIER = 1.2;

    private final Set<Ticket> purchasedTickets = new HashSet<>();

    private DiscountService discountService;

    @Autowired
    public BookingServiceImpl(DiscountService discountService) {
        this.discountService = discountService;
    }

    @Override
    public double getTicketsPrice(@Nonnull Event event, @Nonnull LocalDateTime dateTime, @Nullable User user, @Nonnull NavigableSet<Long> seats) {
        Auditorium auditorium = event.getAuditoriumOnDateTime(dateTime);
        if (auditorium == null) {
            throw new BookingException("Event doesn't air on specified dateTime");
        }
        if (!auditorium.getAllSeats().containsAll(seats)) {
            throw new BookingException("There are no specified seats in auditorium");
        }
        Set<Long> bookedSeats = getPurchasedTicketsForEvent(event, dateTime).stream()
                .map(Ticket::getSeat)
                .filter(seats::contains)
                .collect(Collectors.toSet());
        if (!bookedSeats.isEmpty()) {
            throw new BookingException("Some seats already booked: " + bookedSeats);
        }

        double eventBasePrice = event.getRating() == EventRating.HIGH
                ? event.getBasePrice() * HIGH_RATE_COST_MULTIPLIER
                : event.getBasePrice();
        return seats.stream().mapToDouble(seat -> {
            double seatPrice = auditorium.getVipSeats().contains(seat)
                    ? eventBasePrice * VIP_SEAT_COST_MULTIPLIER
                    : eventBasePrice;
            byte discount = discountService.getDiscount(user, event, dateTime, seat, seats);
            return seatPrice - seatPrice * discount / 100;
        }).sum();

    }

    @Override
    public void bookTickets(@Nonnull Set<Ticket> tickets) {
        if (CollectionUtils.containsAny(purchasedTickets, tickets)) {
            throw new BookingException("Some of input tickets already booked");
        }
        purchasedTickets.addAll(tickets);
        logger.info("Booked {} tickets", tickets.size());
        tickets.stream().filter(ticket -> ticket.getUser() != null)
                .forEach(ticket -> {
                    User user = ticket.getUser();
                    user.getTickets().add(ticket);
                });
    }

    @Nonnull
    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime) {
        return purchasedTickets.stream()
                .filter(ticket -> event.equals(ticket.getEvent()) && dateTime.equals(ticket.getDateTime()))
                .collect(Collectors.toSet());
    }
}
