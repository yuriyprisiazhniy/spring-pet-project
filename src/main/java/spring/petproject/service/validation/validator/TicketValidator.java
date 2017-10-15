package spring.petproject.service.validation.validator;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import spring.petproject.domain.Auditorium;
import spring.petproject.domain.Event;
import spring.petproject.domain.Ticket;

public class TicketValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        return Ticket.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "event", "", "Event must be specified");
        ValidationUtils.rejectIfEmpty(e, "dateTime", "", "Air time must be specified");
        Ticket ticket = (Ticket) target;
        Event event = ticket.getEvent();
        Auditorium auditorium = event.getAuditoriumOnDateTime(ticket.getDateTime());
        if (auditorium == null) {
            e.reject("", "Event doesn't air on specified dateTime");
        } else if (!auditorium.getAllSeats().contains(ticket.getSeat())){
            e.reject("", "There are no specified seat #" + ticket.getSeat() + " in auditorium");
        }
    }
}
