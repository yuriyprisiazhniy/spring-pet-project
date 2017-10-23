package spring.petproject.service;

import spring.petproject.domain.Discount;
import spring.petproject.domain.Event;
import spring.petproject.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Set;

public interface DiscountStrategy {

    /**
     * Calculate discount based on input parameters
     * @param user
     *            User that buys tickets. Can be <code>null</code>
     * @param event
     *            Event that tickets are bought for
     * @param time
     *            The date and time event will be aired
     * @param seat
     *            Seat that user buys
     * @param tickets
     *            Tickets that user buys
     * @return discount value in %
     */
    @Nullable Discount calculateDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime time, long seat, Set<Long> tickets);
}
