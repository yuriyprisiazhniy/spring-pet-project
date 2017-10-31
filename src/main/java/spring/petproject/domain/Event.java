package spring.petproject.domain;

import org.hibernate.annotations.SortNatural;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Event extends DomainObject {

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @SortNatural
    private SortedSet<LocalDateTime> airDates = new TreeSet<>();

    private double basePrice;

    @Enumerated(EnumType.STRING)
    private EventRating rating;

    @ElementCollection
    @SortNatural
    @CollectionTable(name = "Event_Auditoriums")
    @MapKeyColumn(name = "eventAirDate")
    @Access(AccessType.PROPERTY)
    private SortedMap<LocalDateTime, Auditorium> auditoriums = new TreeMap<>();

    protected Event() {
    }

    public Event(String name, double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    public Event(String name, double basePrice, EventRating rating, SortedMap<LocalDateTime, Auditorium> auditoriums) {
        this.name = name;
        this.basePrice = basePrice;
        this.rating = rating;
        this.auditoriums = auditoriums.entrySet().stream().collect(Collectors.toMap(
                (Map.Entry<LocalDateTime, Auditorium> e) -> e.getKey().truncatedTo(ChronoUnit.MINUTES),
                Map.Entry::getValue,
                (k, k2) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", k));
                },
                TreeMap::new));
        this.airDates = new TreeSet<>(this.auditoriums.keySet());
    }

    /**
     * Checks if event is aired on particular <code>dateTime</code> and assigns
     * auditorium to it.
     *
     * @param dateTime   Date and time of aired event for which to assign
     * @param auditorium Auditorium that should be assigned
     * @return <code>true</code> if successful, <code>false</code> if event is
     * not aired on that date
     */
    public boolean assignAuditorium(LocalDateTime dateTime, Auditorium auditorium) {
        LocalDateTime truncatedTime = dateTime.truncatedTo(ChronoUnit.MINUTES);
        if (airDates.contains(truncatedTime)) {
            auditoriums.put(truncatedTime, auditorium);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes auditorium assignment from event
     *
     * @param dateTime Date and time to remove auditorium for
     * @return <code>true</code> if successful, <code>false</code> if not
     * removed
     */
    public boolean removeAuditoriumAssignment(LocalDateTime dateTime) {
        return auditoriums.remove(dateTime.truncatedTo(ChronoUnit.MINUTES)) != null;
    }

    /**
     * Add date and time of event air
     *
     * @param dateTime Date and time to add
     * @return <code>true</code> if successful, <code>false</code> if already
     * there
     */
    public boolean addAirDateTime(LocalDateTime dateTime) {
        return airDates.add(dateTime.truncatedTo(ChronoUnit.MINUTES));
    }

    /**
     * Adding date and time of event air and assigning auditorium to that
     *
     * @param dateTime   Date and time to add
     * @param auditorium Auditorium to add if success in date time add
     * @return <code>true</code> if successful, <code>false</code> if already
     * there
     */
    public boolean addAirDateTime(LocalDateTime dateTime, Auditorium auditorium) {
        LocalDateTime truncatedTime = dateTime.truncatedTo(ChronoUnit.MINUTES);
        boolean result = airDates.add(truncatedTime);
        if (result) {
            auditoriums.put(truncatedTime, auditorium);
        }
        return result;
    }

    /**
     * Removes the date and time of event air. If auditorium was assigned to
     * that date and time - the assignment is also removed
     *
     * @param dateTime Date and time to remove
     * @return <code>true</code> if successful, <code>false</code> if not there
     */
    public boolean removeAirDateTime(LocalDateTime dateTime) {
        LocalDateTime truncatedTime = dateTime.truncatedTo(ChronoUnit.MINUTES);
        boolean result = airDates.remove(truncatedTime);
        if (result) {
            auditoriums.remove(truncatedTime);
        }
        return result;
    }

    /**
     * Checks if event airs on particular date and time
     *
     * @param dateTime Date and time to check
     * @return <code>true</code> event airs on that date and time
     */
    public boolean airsOnDateTime(LocalDateTime dateTime) {
        LocalDateTime truncatedTime = dateTime.truncatedTo(ChronoUnit.MINUTES);
        return airDates.stream().anyMatch(dt -> dt.equals(truncatedTime));
    }

    /**
     * Checks if event airs on particular date
     *
     * @param date Date to ckeck
     * @return <code>true</code> event airs on that date
     */
    public boolean airsOnDate(LocalDate date) {
        return airDates.stream().anyMatch(dt -> dt.toLocalDate().equals(date));
    }

    /**
     * Checking if event airs on dates between <code>from</code> and
     * <code>to</code> inclusive
     *
     * @param from Start date to check
     * @param to   End date to check
     * @return <code>true</code> event airs on dates
     */
    public boolean airsOnDates(LocalDate from, LocalDate to) {
        return airDates.stream()
                .anyMatch(dt -> dt.toLocalDate().compareTo(from) >= 0 && dt.toLocalDate().compareTo(to) <= 0);
    }

    /**
     * Get auditorium where event will occur in specified dateTime
     *
     * @param time time of event air
     * @return auditorium or <code>null</code> if event doesn't air in specified time
     */
    @Nullable
    public Auditorium getAuditoriumOnDateTime(LocalDateTime time) {
        return auditoriums.get(time.truncatedTo(ChronoUnit.MINUTES));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SortedSet<LocalDateTime> getAirDates() {
        return airDates;
    }

    public void setAirDates(SortedSet<LocalDateTime> airDates) {
        this.airDates = new TreeSet<>(airDates.stream()
                .map(t -> t.truncatedTo(ChronoUnit.MINUTES)).collect(Collectors.toSet())
        );
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public EventRating getRating() {
        return rating;
    }

    public void setRating(EventRating rating) {
        this.rating = rating;
    }

    public SortedMap<LocalDateTime, Auditorium> getAuditoriums() {
        return auditoriums;
    }

    public void setAuditoriums(SortedMap<LocalDateTime, Auditorium> auditoriums) {
//        this.auditoriums = auditoriums.entrySet().stream().collect(Collectors.toMap(
//                (Map.Entry<LocalDateTime, Auditorium> e) -> e.getKey().truncatedTo(ChronoUnit.MINUTES),
//                Map.Entry::getValue,
//                (k, k2) -> {
//                    throw new IllegalStateException(String.format("Duplicate key %s", k));
//                },
//                TreeMap::new));

        this.auditoriums = new TreeMap<>(auditoriums);
//        this.auditoriums = auditoriums;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Event other = (Event) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                '}';
    }
}
