package spring.petproject.service.validation.engine;


public class ValidatorNotFoundException extends ValidationException{

    public ValidatorNotFoundException() {
    }

    public ValidatorNotFoundException(String message) {
        super(message);
    }
}
