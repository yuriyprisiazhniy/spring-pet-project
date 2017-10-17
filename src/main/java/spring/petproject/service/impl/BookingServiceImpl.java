package spring.petproject.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import spring.petproject.domain.*;
import spring.petproject.service.BookingService;
import spring.petproject.service.DiscountService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    private static final double VIP_SEAT_COST_MULTIPLIER = 2;
    private static final double HIGH_RATE_COST_MULTIPLIER = 1.2;

    private final static Set<Ticket> purchasedTickets = new HashSet<>();

    private DiscountService discountService;

    public BookingServiceImpl(DiscountService discountService) {
        this.discountService = discountService;
    }

    @Override
    public double getTicketsPrice(@Nonnull Event event, @Nonnull LocalDateTime dateTime, @Nullable User user, @Nonnull Set<Long> seats) {
        Auditorium auditorium = event.getAuditoriumOnDateTime(dateTime);
        if (auditorium == null) {
            throw new IllegalArgumentException("Event doesn't air on specified dateTime");
        }
        if (!auditorium.getAllSeats().containsAll(seats)) {
            throw new IllegalArgumentException("There are no specified seats in auditorium");
        }
        Set<Long> bookedSeats = getPurchasedTicketsForEvent(event, dateTime).stream()
                .map(Ticket::getSeat)
                .filter(seats::contains)
                .collect(Collectors.toSet());
        if (!bookedSeats.isEmpty()) {
            throw new IllegalArgumentException("Some seats already booked: " + bookedSeats);
        }
        double basePrice = event.getRating() == EventRating.HIGH
                ? event.getBasePrice() * HIGH_RATE_COST_MULTIPLIER
                : event.getBasePrice();
        long vipSeats = auditorium.countVipSeats(seats);
        double totalPrice = basePrice * (vipSeats * VIP_SEAT_COST_MULTIPLIER + seats.size() - vipSeats);
        byte discount = discountService.getDiscount(user, event, dateTime, seats.size());
        return totalPrice - totalPrice * discount / 100;
    }

    @Override
    public void bookTickets(@Nonnull Set<Ticket> tickets) {
        if (CollectionUtils.containsAny(purchasedTickets, tickets)) {
            throw new IllegalArgumentException("Some of input tickets already booked");
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
