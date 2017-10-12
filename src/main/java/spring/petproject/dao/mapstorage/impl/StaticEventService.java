package spring.petproject.dao.mapstorage.impl;

import org.springframework.stereotype.Repository;
import spring.petproject.dao.mapstorage.AbstractStaticStorage;
import spring.petproject.domain.Event;
import spring.petproject.service.EventService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Repository
public class StaticEventService extends AbstractStaticStorage<Event> implements EventService{

    @Override
    protected Class<Event> getDomainClass() {
        return Event.class;
    }

    @Nullable
    @Override
    public Event getByName(@Nonnull String name) {
        return null;
    }

    @Nonnull
    @Override
    public Set<Event> getForDateRange(@Nonnull LocalDate from, @Nonnull LocalDate to) {
        return null;
    }

    @Nonnull
    @Override
    public Set<Event> getNextEvents(@Nonnull LocalDateTime to) {
        return null;
    }
}
