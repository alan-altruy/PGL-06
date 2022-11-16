package be.ac.umons.g06.model;

import java.util.Objects;

public class Bank implements User {

    private final String swift;
    private final String name;

    public Bank(String swift, String name) {
        this.swift = swift;
        this.name = name;
    }

    @Override
    public String getId() {
        return swift;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Bank other = (Bank) o;
        return swift.equals(other.swift) && name.equals(other.name);
    }

    @Override
    public String toString() {
        return "be.ac.umons.g06.model.Bank : { name = " + name + ", swift = " + swift + " }";
    }

    @Override
    public int hashCode() {
        return Objects.hash(swift);
    }
}
