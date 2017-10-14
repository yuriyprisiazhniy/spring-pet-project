package spring.petproject.service.validation.engine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ValidatorRegistry implements ValidationService, ApplicationContextAware, InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(ValidatorRegistry.class);

    private ApplicationContext context;
    private final Set<Validator> validators =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Validator> beans = context.getBeansOfType(Validator.class, false, true);
        if (beans.size() > 0) {
            logger.info("Registered {} validators", beans.size());
            validators.addAll(beans.values());
        } else
            logger.info("Validators not found");
    }

    @Override
    public Errors validate(Object target) {
        Errors errors;
        if (target == null) {
            errors = new MapBindingResult(new HashMap<>(), "null");
            errors.reject("Null object");
            return errors;
        }
        errors = prepareBindingResultObject(target);
        Class<?> clazz = target.getClass();
        Set<Validator> appropriateValidators = validators.stream()
                .filter(validator -> validator.supports(clazz)).collect(Collectors.toSet());
        if (appropriateValidators.isEmpty()) {
            throw new ValidatorNotFoundException("Cannot find validator for "
                    + target.getClass().getName()
                    + " class");
        }
        for (Validator validator : appropriateValidators) {
            validator.validate(target, errors);
        }
        return errors;
    }

    private Errors prepareBindingResultObject(Object target) {
        return new MapBindingResult(new HashMap<>(), target.getClass().getName());
    }
}
