package spring.petproject.dao.mapstorage.domainimpl;

import org.springframework.stereotype.Repository;
import spring.petproject.dao.mapstorage.AbstractStaticStorage;
import spring.petproject.domain.Ticket;

@Repository
public class StaticTicketDAO extends AbstractStaticStorage<Ticket> {

    @Override
    protected Class<Ticket> getDomainClass() {
        return Ticket.class;
    }
}
