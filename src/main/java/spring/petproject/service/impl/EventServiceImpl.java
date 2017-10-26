package spring.petproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.domain.Event;
import spring.petproject.service.EventService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService{

    private final AbstractDomainObjectService<Event> eventDAO;

    @Autowired
    public EventServiceImpl(AbstractDomainObjectService<Event> dao) {
        this.eventDAO = dao;
    }

    @Nullable
    @Override
    public Event getByName(@Nonnull String name) {
        return getAll().stream().filter(e -> Objects.equals(name, e.getName())).findAny().orElse(null);
    }

    @Nonnull
    @Override
    public Set<Event> getForDateRange(@Nonnull LocalDate from, @Nonnull LocalDate to) {
        return getAll().stream().filter(event -> event.airsOnDates(from, to)).collect(Collectors.toSet());
    }

    @Nonnull
    @Override
    public Set<Event> getNextEvents(@Nonnull LocalDateTime to) {
        LocalDateTime now = LocalDateTime.now();
        return getAll().stream()
                .filter(event -> !event.getAirDates().subSet(now, to).isEmpty())
                .collect(Collectors.toSet());
    }

    @Override
    public Event save(@Nonnull Event object) {
        return eventDAO.save(object);
    }

    @Override
    public void remove(@Nonnull Event object) {
        eventDAO.remove(object);
    }

    @Override
    public Event getById(@Nonnull Long id) {
        return eventDAO.getById(id);
    }

    @Nonnull
    @Override
    public Collection<Event> getAll() {
        return eventDAO.getAll();
    }
}
