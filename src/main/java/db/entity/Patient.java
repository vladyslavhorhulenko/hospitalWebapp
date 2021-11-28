package db.entity;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;

public class Patient extends Person {
    private LocalDate birthday;
    private LocalDate registrDate;

    public Patient(int id, String name, String surname, LocalDate birthday, LocalDate registrDate) {
        super(id, name, surname);
        this.birthday = birthday;
        this.registrDate = registrDate;
    }

    public Patient(String name, String surname, LocalDate birthday) {
        super(name, surname);
        this.birthday = birthday;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    @Override
    public String toString() {
        return getFullName() + " " + getBirthday();
    }
}
