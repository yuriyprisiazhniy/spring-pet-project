package spring.petproject.service.impl;

import org.springframework.stereotype.Service;
import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.domain.User;
import spring.petproject.service.UserService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private AbstractDomainObjectService<User> userDAO;

    public UserServiceImpl(AbstractDomainObjectService<User> dao) {
            this.userDAO = dao;
    }

    @Override
    public User save(@Nonnull User object) {
        return userDAO.save(object);
    }

    @Override
    public void remove(@Nonnull User object) {
        userDAO.remove(object);
    }

    @Override
    public User getById(@Nonnull Long id) {
        return userDAO.getById(id);
    }

    @Nonnull
    @Override
    public Collection<User> getAll() {
        return userDAO.getAll();
    }

    @Nullable
    @Override
    public User getUserByEmail(@Nonnull String email) {
        return getAll().stream()
                .filter(u -> Objects.equals(email, u.getEmail())).findAny().orElse(null);
    }
}
