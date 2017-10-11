package spring.petproject.dao.mapstorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.petproject.domain.DomainObject;
import spring.petproject.dao.AbstractDomainObjectService;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class StaticMapStorage<T extends DomainObject> implements AbstractDomainObjectService<T> {

    private static final Logger LOG = LoggerFactory.getLogger(StaticMapStorage.class);

    private static final Map<Long, DomainObject> storage;
    private static final AtomicLong idCounter;

    static {
        storage = new HashMap<>();
        idCounter = new AtomicLong(1);
        LOG.info("Static map storage initialized");
    }

    @Override
    public T save(@Nonnull T object) {
        Long objectId = object.getId();
        DomainObject oldValue;
        if (objectId == null) {
            long assignedId = idCounter.getAndIncrement();
            object.setId(assignedId);
            if ((oldValue = storage.put(assignedId, object)) != null) {
                LOG.warn("Object {} was replaced with new one {}. Looks like Id collision!", oldValue, object);
            } else {
                LOG.info("New Object added with assigned Id: {}", assignedId);
            }
        } else {
            if ((oldValue = storage.put(objectId, object)) != null) {
                LOG.info("Object {} was updated", oldValue);
            } else {
                LOG.info("New Object added");
            }
        }
        return object;
    }

    @Override
    public void remove(@Nonnull T object) {

    }

    @Override
    public T getById(@Nonnull Long id) {
        return null;
    }

    @Nonnull
    @Override
    public Collection<T> getAll() {
//        return storage.values().stream().filter(el -> {
//            T tmp;
//
//        }).collect(Collectors.toList());
        return null;
    }
}
