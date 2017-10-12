package spring.petproject.dao.mapstorage.impl;

import org.springframework.stereotype.Repository;
import spring.petproject.dao.mapstorage.AbstractStaticStorage;
import spring.petproject.domain.DomainObject;
import spring.petproject.domain.User;
import spring.petproject.service.UserService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

@Repository
public class StaticUserRepository extends AbstractStaticStorage<User> implements UserService {

    @Override
    protected Class<User> getDomainClass() {
        return User.class;
    }

    @Nullable
    @Override
    public User getUserByEmail(@Nonnull String email) {
        return getAll().stream()
                .filter(u -> Objects.equals(email, u.getEmail())).findAny().orElse(null);
    }
}
