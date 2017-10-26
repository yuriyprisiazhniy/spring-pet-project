package spring.petproject.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

//@Aspect
@Component
public class TestAspect {

//    @Before("execution(* spring.petproject.service.UserService.*(..))")
    public void logMethod(JoinPoint joinPoint) {
        System.out.println("Before method " + joinPoint.getSignature().getName());
    }
}
