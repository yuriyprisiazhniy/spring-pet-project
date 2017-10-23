package spring.petproject.dao.mapstorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.petproject.domain.DomainObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class IdGenerator {
    private static final Logger logger = LoggerFactory.getLogger(IdGenerator.class);

    private static final Map<Class<? extends DomainObject>, AtomicLong> domainCounters = new ConcurrentHashMap<>();

    private IdGenerator(){}

    public static synchronized <T extends DomainObject> Long generateId(Class<T> clazz, Map<Long, T> storage) {
        AtomicLong domainCounter = domainCounters.get(clazz);
        if (domainCounter == null) {
            domainCounter = new AtomicLong(0);
        }
        Long generatedId = domainCounter.incrementAndGet();
        while (storage.containsKey(generatedId)) {
            generatedId = domainCounter.incrementAndGet();
        }
        domainCounters.put(clazz, domainCounter);
        return generatedId;
    }

    public static void clearIdStorage() {
        domainCounters.clear();
        logger.info("IdGenerator sequence cleaned.");
    }
}
