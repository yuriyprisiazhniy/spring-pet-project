package spring.petproject.service.exception;

public class BookingException extends DomainValidationException {

    public BookingException() {
    }

    public BookingException(String message) {
        super(message);
    }
}
