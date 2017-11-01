package spring.petproject.dao.db.domainimpl;

import org.springframework.stereotype.Repository;
import spring.petproject.dao.db.AbstractDBStorage;
import spring.petproject.domain.Event;

@Repository
public class DBEventDAO extends AbstractDBStorage<Event> {

    @Override
    protected Class<Event> getDomainClass() {
        return Event.class;
    }
}
