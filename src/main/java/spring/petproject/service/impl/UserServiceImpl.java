package spring.petproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    public UserServiceImpl(AbstractDomainObjectService<User> dao) {
            this.userDAO = dao;
    }

    @Transactional
    public User save(@Nonnull User object) {
        return userDAO.save(object);
    }

    @Transactional
    public void remove(@Nonnull User object) {
        userDAO.remove(object);
    }

    @Transactional(readOnly = true)
    public User getById(@Nonnull Long id) {
        return userDAO.getById(id);
    }

    @Nonnull
    @Transactional(readOnly = true)
    public Collection<User> getAll() {
        return userDAO.getAll();
    }

    @Nullable
    @Transactional(readOnly = true)
    public User getUserByEmail(@Nonnull String email) {
        return getAll().stream()
                .filter(u -> Objects.equals(email, u.getEmail()))
                .findAny().orElse(null);
    }
}
