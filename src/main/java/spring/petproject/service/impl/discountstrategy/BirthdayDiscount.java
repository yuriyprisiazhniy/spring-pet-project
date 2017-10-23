package spring.petproject.service.impl.discountstrategy;

import spring.petproject.domain.Discount;
import spring.petproject.domain.Event;
import spring.petproject.domain.User;
import spring.petproject.service.DiscountStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public class BirthdayDiscount implements DiscountStrategy {

    private int datesDifference;
    private byte discountRate;
    private String discountReason;

    public BirthdayDiscount() {
        this(5, (byte) 5);
    }

    public BirthdayDiscount(int datesDifference, byte discountRate) {
        this.datesDifference = datesDifference;
        this.discountRate = discountRate;
        discountReason = String.format("User has birthday within %d days of air date", datesDifference);
    }

    @Override
    public Discount calculateDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime time, long seat, Set<Long> tickets) {
        if (user == null)
            return null;
        long rangeBetweenEvents = Math.abs(ChronoUnit.DAYS.between(user.getBirthday().with(Year.from(time)), time));
        return rangeBetweenEvents <= datesDifference
                ? new Discount(discountReason, discountRate)
                : null;
    }
}
