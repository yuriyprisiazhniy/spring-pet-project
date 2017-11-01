package spring.petproject.dao.db.domainimpl;

import org.springframework.stereotype.Repository;
import spring.petproject.dao.db.AbstractDBStorage;
import spring.petproject.domain.Ticket;

@Repository
public class DBTicketDAO extends AbstractDBStorage<Ticket> {
    
    @Override
    protected Class<Ticket> getDomainClass() {
        return Ticket.class;
    }
}
