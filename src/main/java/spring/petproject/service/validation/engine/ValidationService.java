package spring.petproject.service.validation.engine;


import com.sun.istack.internal.NotNull;
import org.springframework.validation.Errors;

import javax.annotation.Nullable;

/**
 * Service for storing and unify validation
 * with Spring's {@link org.springframework.validation.Validator}
 * interfaces registered in context
 */
public interface ValidationService {

    /**
     * Perform validation on given object
     * @param object validated object
     * @return object with errors if any
     * @throws ValidatorNotFoundException if there are no defined validator for such class
     */
    Errors validate(Object object);

    /**
     * Perform validation on given object
     * @param object validated object
     * @param errors error container
     * @return object with aggregated errors
     * @throws ValidatorNotFoundException if there are no defined validator for such class
     */
    @NotNull Errors validate(Object object, @Nullable Errors errors);
}
