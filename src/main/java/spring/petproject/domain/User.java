package spring.petproject.domain;

import org.hibernate.annotations.SortNatural;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
public class User extends DomainObject {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthday;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    @SortNatural
    private SortedSet<Ticket> tickets = new TreeSet<>();

    @ElementCollection
    private List<String> additionalInformation = new ArrayList<>();

    public User(String firstName, String lastName, String email, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
    }

    protected User() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SortedSet<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(SortedSet<Ticket> tickets) {
        this.tickets = tickets;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public List<String> getAdditionalInformation() {
        return new ArrayList<>(additionalInformation);
    }

    public void addAdditionalInformation(String info) {
        if (!StringUtils.isEmpty(info))
            additionalInformation.add(info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, birthday);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return birthday != null ? birthday.equals(user.birthday) : user.birthday == null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
