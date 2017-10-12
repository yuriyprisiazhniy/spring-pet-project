package spring.petproject.dao.mapstorage.domainimpl;

import org.springframework.stereotype.Repository;
import spring.petproject.dao.mapstorage.AbstractStaticStorage;
import spring.petproject.domain.User;

@Repository
public class StaticUserDAO extends AbstractStaticStorage<User> {

    @Override
    protected Class<User> getDomainClass() {
        return User.class;
    }
}
