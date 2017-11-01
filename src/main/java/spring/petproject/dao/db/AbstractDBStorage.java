package spring.petproject.dao.db;

import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.domain.DomainObject;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;
import java.util.List;

public abstract class AbstractDBStorage<T extends DomainObject> implements AbstractDomainObjectService<T> {


    @PersistenceContext
    private EntityManager em;

    @Override
    public T save(@Nonnull T entity) {
        Long entityId = entity.getId();
        if (entityId == null) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }

    @Override
    public void remove(@Nonnull T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    public T getById(@Nonnull Long id) {
        return em.find(getDomainClass(), id);
    }

    @Nonnull
    @Override
    public Collection<T> getAll() {
        Class<T> domainClass = getDomainClass();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(domainClass);
        query.select(query.from(domainClass));
        return em.createQuery(query).getResultList();
    }

    protected abstract Class<T> getDomainClass();
}
