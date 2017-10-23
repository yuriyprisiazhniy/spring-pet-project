package spring.petproject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.dao.mapstorage.AbstractStaticStorage;
import spring.petproject.dao.mapstorage.IdGenerator;
import spring.petproject.dao.mapstorage.domainimpl.StaticEventDAO;
import spring.petproject.dao.mapstorage.domainimpl.StaticUserDAO;
import spring.petproject.service.AuditoriumService;
import spring.petproject.service.DiscountService;
import spring.petproject.service.UserService;
import spring.petproject.service.impl.EventServiceImpl;
import spring.petproject.domain.*;
import spring.petproject.service.EventService;
import spring.petproject.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Temporary class for launch
 */
public class AppStarter {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        DiscountService discountService = context.getBean(DiscountService.class);
    }
}
