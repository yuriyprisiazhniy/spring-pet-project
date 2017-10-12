package spring.petproject.dao.mapstorage.domainimpl;

import org.springframework.stereotype.Repository;
import spring.petproject.dao.mapstorage.AbstractStaticStorage;
import spring.petproject.domain.Event;

@Repository
public class StaticEventDAO extends AbstractStaticStorage<Event> {

    @Override
    protected Class<Event> getDomainClass() {
        return Event.class;
    }
}
