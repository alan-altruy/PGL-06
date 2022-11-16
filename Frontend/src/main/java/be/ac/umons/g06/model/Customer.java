package be.ac.umons.g06.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class Customer implements User {

    private final String id;
    private final LocalDate birthdate;
    private final String name;

    public Customer(String name, String nationalRegistrationNumber, LocalDate birthdate) {
        this.id = nationalRegistrationNumber;
        this.birthdate = birthdate;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public int getAge() {
        Period period = Period.between(birthdate, LocalDate.now());
        return period.getYears();
    }

    public boolean isAdult() {
        return getAge() >= 18;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Customer other = (Customer) o;
        return id.equals(other.id);
    }

    @Override
    public String toString() {
        return "be.ac.umons.g06.model.User : { name = " + name + ", nrn = " + id + ", birthdate = " + birthdate + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, birthdate, name);
    }
}
