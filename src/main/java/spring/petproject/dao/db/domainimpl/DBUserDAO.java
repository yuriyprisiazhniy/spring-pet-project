package spring.petproject.dao.db.domainimpl;

import org.springframework.stereotype.Repository;
import spring.petproject.dao.db.AbstractDBStorage;
import spring.petproject.domain.User;

@Repository
public class DBUserDAO extends AbstractDBStorage<User> {
    
    @Override
    protected Class<User> getDomainClass() {
        return User.class;
    }
}
