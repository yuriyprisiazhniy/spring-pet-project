package spring.petproject.service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import spring.petproject.dao.AbstractDomainObjectService;
import spring.petproject.domain.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;


public interface EventService extends AbstractDomainObjectService<Event> {

    /**
     * Finding event by name
     * 
     * @param name
     *            Name of the event
     * @return found event or <code>null</code>
     */
    public @Nullable Event getByName(@Nonnull String name);

    /*
     * Finding all events that air on specified date range
     * 
     * @param from Start date
     * 
     * @param to End date inclusive
     * 
     * @return Set of events
     */
     public @Nonnull
     Set<Event> getForDateRange(@Nonnull LocalDate from, @Nonnull LocalDate to);

    /*
     * Return events from 'now' till the the specified date time
     * 
     * @param to End date time inclusive
     * s
     * @return Set of events
     */
     public @Nonnull Set<Event> getNextEvents(@Nonnull LocalDateTime to);

}
