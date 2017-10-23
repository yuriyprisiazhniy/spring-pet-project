package spring.petproject.service.impl.discountstrategy;

import spring.petproject.domain.Discount;
import spring.petproject.domain.Event;
import spring.petproject.domain.User;
import spring.petproject.service.DiscountStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Set;

public class MultipleDiscount implements DiscountStrategy {

    private int discountMultiplier;
    private byte discountRate;
    private String discountReason;

    public MultipleDiscount() {
        this(10, (byte) 50);
    }

    public MultipleDiscount(int discountMultiplier, byte discountRate) {
        this.discountMultiplier = discountMultiplier;
        this.discountRate = discountRate;
        discountReason = String.format("User bought %ds ticket", discountMultiplier);
    }

    @Nullable
    @Override
    public Discount calculateDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime time, long seat, Set<Long> tickets) {
        if (user != null) {
            int notDiscountedTickets = user.getTickets().size() % discountMultiplier;

        }
        return null;
    }
}
