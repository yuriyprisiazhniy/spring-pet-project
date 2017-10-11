package spring.petproject.dao.mapstorage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.domain.DomainObject;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractStaticStorage<T extends DomainObject> implements AbstractDomainObjectService<T> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractStaticStorage.class);

    private static final Map<Class<? extends DomainObject>, Map<Long, DomainObject>> storage;
    private static final AtomicLong idCounter;

    static {
        storage = new HashMap<>();
        idCounter = new AtomicLong(1);
        logger.info("Static map storage initialized: {}", storage);
    }

    @Override
    public T save(@Nonnull T object) {
        Class<? extends DomainObject> concreteClass = object.getClass();
        Map<Long, DomainObject> domainStorage = storage.get(concreteClass);
        if (domainStorage == null) {
            domainStorage = new HashMap<>();
        }

        Long objectId = object.getId();
        DomainObject oldValue;
        if (objectId == null) {
            //Id generator cause collisions
            long assignedId = idCounter.getAndIncrement();
            object.setId(assignedId);
            oldValue = domainStorage.put(assignedId, object);
            if (oldValue != null) {
                logger.warn("Object {} was replaced with new one {}. Looks like Id collision!", oldValue, object);
            } else {
                logger.info("New Object added with assigned Id: {}", assignedId);
            }
        } else {
            oldValue = domainStorage.put(objectId, object);
            if (oldValue != null) {
                logger.info("Object {} was updated with new value: {}", oldValue, object);
            } else {
                logger.info("New Object: {} added", object);
            }
        }
        storage.put(concreteClass, domainStorage);

        return object;
    }

    @Override
    public void remove(@Nonnull T object) {

    }
}
