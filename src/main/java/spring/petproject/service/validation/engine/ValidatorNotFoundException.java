package spring.petproject.service.validation.engine;


public class ValidatorNotFoundException extends RuntimeException{

    public ValidatorNotFoundException() {
    }

    public ValidatorNotFoundException(String message) {
        super(message);
    }
}
