package spring.petproject.service.validation.validator;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import spring.petproject.domain.Ticket;

public class TicketValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        return Ticket.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "event", null, "Event must be specified.");
    }
}
