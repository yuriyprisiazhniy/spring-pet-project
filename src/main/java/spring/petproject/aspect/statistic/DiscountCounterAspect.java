package spring.petproject.aspect.statistic;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spring.petproject.domain.Discount;
import spring.petproject.domain.User;
import spring.petproject.service.DiscountStrategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Component
public class DiscountCounterAspect {
    private static final Logger logger = LoggerFactory.getLogger(DiscountCounterAspect.class);

    private final Map<DiscountStrategy, Map<User, Integer>> discountStatistic = new HashMap<>();

    @AfterReturning(
            pointcut = "execution(* spring.petproject.service.DiscountService.getDiscount(..)) && args(user, ..)",
            returning = "discount"
    )
    public void calculateDiscountTimes(User user, Discount discount) {
        if (discount.getDiscount() > 0) {
            Map<User, Integer> statistic = this.discountStatistic
                    .computeIfAbsent(discount.getDiscountStrategy(), s -> new HashMap<>());
            Integer counter = statistic.merge(user, 1, (oldValue, newValue) -> ++oldValue);
            if (user != null) {
                logger.debug("For user {} applied {}-th discount {}%, reason: {}",
                        user, counter, discount.getDiscount(), discount.getReason());
            } else {
                logger.debug("Applied {}-th discount {}% for anonymous user, reason: {}",
                        counter, discount.getDiscount(), discount.getReason());
            }
        }

    }

    public Map<DiscountStrategy, Map<User, Integer>> getAllStatistic() {
        return new HashMap<>(discountStatistic);
    }

    public Map<DiscountStrategy, Integer> getStatisticByUser(User user) {
        return discountStatistic.entrySet().stream()
                .filter(e -> e.getValue().get(user) != null)
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue().get(user)));
    }
}
