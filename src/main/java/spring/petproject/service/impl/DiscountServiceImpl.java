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
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class DiscountServiceImpl implements DiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountServiceImpl.class);

    private Set<DiscountStrategy> discountStrategies = Collections.emptySet();

    @Override
    public byte getDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime, long numberOfTickets) {
        byte result = 0;
        Optional<Discount> discount = discountStrategies.stream()
                .map(strategy -> strategy.calculateDiscount(user, event, airDateTime, numberOfTickets))
                .max(Discount::compareTo);
        if (discount.isPresent()) {
            result = discount.get().getDiscount();
            logger.info("Applied discount {}%. Reason: {}", result, discount.get().getReason());
        }
        return result;
    }

    public Set<DiscountStrategy> getDiscountStrategies() {
        return discountStrategies;
    }

    public void setDiscountStrategies(Set<DiscountStrategy> discountStrategies) {
        this.discountStrategies = discountStrategies;
    }
}
