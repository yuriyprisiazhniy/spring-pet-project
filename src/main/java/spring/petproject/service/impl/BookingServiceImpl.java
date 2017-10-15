package spring.petproject.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import spring.petproject.domain.*;
import spring.petproject.service.BookingService;
import spring.petproject.service.DiscountService;
import spring.petproject.service.validation.engine.ValidationException;
import spring.petproject.service.validation.engine.ValidationService;

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
    private ValidationService validationService;

    public BookingServiceImpl(DiscountService discountService) {
        this.discountService = discountService;
    }

    @Override
    public double getTicketsPrice(@Nonnull Event event, @Nonnull LocalDateTime dateTime, @Nullable User user, @Nonnull Set<Long> seats) {
        Errors validationErrors = null;
        for (Long seat : seats) {
            validationErrors = validationService.validate(new Ticket(user, event, dateTime, seat), validationErrors);
        }
        if (validationErrors != null && validationErrors.hasErrors()) {
            throw new ValidationException("Invalid input data: " +
                    validationErrors.getAllErrors().stream()
                            .map(e -> e.getDefaultMessage())
                            .reduce((s, s2) -> s.concat(", ").concat(s2))
                            .orElse("No details"));
        }

        Auditorium auditorium = event.getAuditoriumOnDateTime(dateTime);
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

    public void setValidationService(ValidationService validationService) {
        this.validationService = validationService;
    }
}
