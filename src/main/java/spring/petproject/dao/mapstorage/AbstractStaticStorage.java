package spring.petproject.dao.mapstorage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.domain.DomainObject;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract storage based on single static map for all domain objects.
 * Uniqueness of objects is supported by id's uniqueness for each type of domain object.
 * @param <T> - concrete type of Domain Object provided by children
 * @see #getDomainClass()
 */
public abstract class AbstractStaticStorage<T extends DomainObject> implements AbstractDomainObjectService<T> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractStaticStorage.class);

    private static final Map<Class<? extends DomainObject>, Map<Long, ? extends DomainObject>> storage;
    static {
        storage = new ConcurrentHashMap<>();
        logger.info("Static map storage initialized");
    }

    @Override
    public T save(@Nonnull T object) {
        Map<Long, T> domainStorage = getDomainStorage();
        Long objectId = object.getId();
        DomainObject oldValue;
        if (objectId == null) {
            long assignedId = IdGenerator.generateId(getDomainClass(), domainStorage);
            object.setId(assignedId);
            oldValue = domainStorage.put(assignedId, object);
            if (oldValue != null) {
                logger.warn("Object {} was replaced with new one {}. Looks like Id collision!", oldValue, object);
            } else {
                logger.info("New {} added with assigned Id: {}", getDomainClass().getSimpleName(), assignedId);
            }
        } else {
            oldValue = domainStorage.put(objectId, object);
            if (oldValue != null) {
                logger.info("Object {} was updated with new value: {}", oldValue, object);
            } else {
                logger.info("New Object: {} added", object);
            }
        }
        storage.put(getDomainClass(), domainStorage);

        return object;
    }

    @Override
    public void remove(@Nonnull T object) {
        Map<Long, T> domainStorage = getDomainStorage();
        if (domainStorage.remove(object.getId(), object)) {
            logger.info("Object: {} removed", object);
        } else {
            logger.info("There are no such object in the storage");
        }
    }

    @Override
    public T getById(@Nonnull Long id) {
        Map<Long, T> domainStorage = getDomainStorage();
        return domainStorage.get(id);
    }

    @Nonnull
    @Override
    public Collection<T> getAll() {
        Map<Long, T> domainStorage = getDomainStorage();
        return domainStorage.values();
    }

    protected abstract Class<T> getDomainClass();

    private @Nonnull Map<Long, T> getDomainStorage() {
        Class<T> domainClass = getDomainClass();
        Map<Long, T> domainStorage = (Map<Long, T>) storage.get(domainClass);
        return domainStorage != null ? domainStorage : new HashMap<>();
    }

    /**
     * Temp method for debug purposes
     */
    //TODO: remove this method
    @Deprecated
    public static Map<Class<? extends DomainObject>, Map<Long, ? extends DomainObject>> getAllStorageContent() {
        return new HashMap<>(storage);
    }
}
