package spring.petproject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.Errors;
import spring.petproject.domain.Ticket;
import spring.petproject.domain.User;
import spring.petproject.service.validation.engine.ValidationService;

/**
 * Temporary class for launch
 */
public class AppStarter {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        /*DiscountService discountService = context.getBean(DiscountService.class);
        byte discount = discountService.getDiscount(
                new User() {{
                    setBirthday(LocalDate.now());
                }},
                new Event("King-Kong", 15),
                LocalDateTime.now(),
                2);
        System.out.println(discount);*/

        ValidationService validationService = context.getBean(ValidationService.class);
        Errors errors = validationService.validate(new User());
        System.out.println(errors.getAllErrors());
    }
}
