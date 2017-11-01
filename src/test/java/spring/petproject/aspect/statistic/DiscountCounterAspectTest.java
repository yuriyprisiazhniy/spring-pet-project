package spring.petproject.aspect.statistic;

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import spring.petproject.domain.Discount;
import spring.petproject.domain.User;
import spring.petproject.service.DiscountService;
import spring.petproject.service.DiscountStrategy;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class DiscountCounterAspectTest {

    private DiscountCounterAspect aspect;

    private DiscountService discountServiceProxy;

    private User testUser;
    private DiscountStrategy discountStrategy1;
    private DiscountStrategy discountStrategy2;

    @BeforeMethod
    private void init() {
        discountStrategy1 = mock(DiscountStrategy.class);
        discountStrategy2 = mock(DiscountStrategy.class);
        testUser = new User("first", "last", "mail", LocalDate.now().minusYears(20));
        DiscountService discountServiceMock = mock(DiscountService.class);
        when(discountServiceMock.getDiscount(eq(testUser), any(), any(), anyLong(), any()))
                .thenReturn(new Discount());
        when(discountServiceMock.getDiscount(eq(testUser), any(), any(), eq(1L), any()))
                .thenReturn(new Discount(discountStrategy1, "test discount 10%", (byte) 10));
        when(discountServiceMock.getDiscount(eq(testUser), any(), any(), eq(2L), any()))
                .thenReturn(new Discount(discountStrategy2, "test discount 20%", (byte) 20));
        when(discountServiceMock.getDiscount(isNull(), any(), any(), eq(3L), any()))
                .thenReturn(new Discount(discountStrategy2, "anonymous discount 50%", (byte) 50));
        aspect = new DiscountCounterAspect();
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(discountServiceMock);
        proxyFactory.addAspect(aspect);
        discountServiceProxy = proxyFactory.getProxy();

    }

    @Test
    public void testEmptyDiscount() {
        Discount discount = discountServiceProxy.getDiscount(testUser, null, null, 24L, null);
        assertTrue(aspect.getAllStatistic().isEmpty());
    }

    @Test
    public void testDiscountStrategy1() {
        discountServiceProxy.getDiscount(testUser, null, null, 1L, null);
        assertEquals(aspect.getAllStatistic().size(), 1);
        assertTrue(aspect.getAllStatistic().containsKey(discountStrategy1));
        Map<DiscountStrategy, Integer> userStatistic = aspect.getStatisticByUser(testUser);
        assertEquals(userStatistic.get(discountStrategy1), Integer.valueOf(1));
    }

    @Test
    public void testAllStrategies() {
        discountServiceProxy.getDiscount(testUser, null, null, 1L, null);
        discountServiceProxy.getDiscount(testUser, null, null, 2L, null);
        discountServiceProxy.getDiscount(null, null, null, 3L, null);
        Map<DiscountStrategy, Map<User, Integer>> allStatistic = aspect.getAllStatistic();
        assertEquals(allStatistic.size(), 2);
        assertTrue(allStatistic.keySet().containsAll(Arrays.asList(discountStrategy1, discountStrategy2)));
        assertEquals(allStatistic.get(discountStrategy2).size(), 2);
        assertEquals(aspect.getStatisticByUser(testUser).size(), 2);
    }

    @Test
    public void testMultipleSameStrategies() {
        discountServiceProxy.getDiscount(testUser, null, null, 1L, null);
        discountServiceProxy.getDiscount(testUser, null, null, 1L, null);
        Map<DiscountStrategy, Integer> userStatistic = aspect.getStatisticByUser(testUser);
        assertEquals(userStatistic.get(discountStrategy1), Integer.valueOf(2));
        assertNull(userStatistic.get(discountStrategy2));
    }

}
