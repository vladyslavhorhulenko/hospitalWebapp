package db.entity;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends Medic{
    private int category;
    private List<Nurse> nurses;

    public Doctor(int id, String name, String surname, List<Patient> patients, int category) {
        super(id, name, surname, patients);
        this.category = category;
    }

    public Doctor(int id, String name, String surname, int category) {
        super(id, name, surname);
        this.category = category;
    }

    public Doctor(String name, String surname, int category) {
        super(name, surname);
        this.category = category;
    }

    public List<Nurse> getNurses() {
        return nurses;
    }

    public void setNurses(List<Nurse> nurses) {
        this.nurses = nurses;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return this.getFullName() + " " + this.getCategory();
    }
}
