package spring.petproject.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.petproject.domain.Discount;
import spring.petproject.domain.Event;
import spring.petproject.domain.User;
import spring.petproject.service.DiscountService;
import spring.petproject.service.DiscountStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.*;

public class DiscountServiceImpl implements DiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountServiceImpl.class);

    private Set<DiscountStrategy> discountStrategies = Collections.emptySet();

    @Override
    public byte getDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime, long seat, NavigableSet<Long> tickets) {
        byte result = 0;
        Optional<Discount> discount = discountStrategies.stream()
                .map(strategy -> strategy.calculateDiscount(user, event, airDateTime, seat, tickets))
                .filter(Objects::nonNull)
                .max(Discount::compareTo);
        if (discount.isPresent()) {
            result = discount.get().getDiscount();
            logger.info("Applied discount {}% for user {}. Reason: {}", result, user, discount.get().getReason());
        }
        return result;
    }

    public Set<DiscountStrategy> getDiscountStrategies() {
        return new HashSet<>(discountStrategies);
    }

    public void setDiscountStrategies(@Nonnull Set<DiscountStrategy> discountStrategies) {
        this.discountStrategies = discountStrategies;
        if (!discountStrategies.isEmpty())
            logger.info("Registered {} discount strategies", discountStrategies.size());
    }
}
