package spring.petproject.service.exception;

public class DomainValidationException extends RuntimeException {

    public DomainValidationException() {
    }

    public DomainValidationException(String message) {
        super(message);
    }
}
