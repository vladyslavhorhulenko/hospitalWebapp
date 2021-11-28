package db.entity;

import java.util.ArrayList;
import java.util.List;

public abstract class Medic extends Person {
    private List<Patient> patients;

    public Medic(int id, String name, String surname, List<Patient> patients) {
        super(id, name, surname);
        this.patients = patients;
    }

    public Medic(int id, String name, String surname) {
        super(id, name, surname);
        this.patients = new ArrayList<>();
    }

    public Medic(String name, String surname) {
        super(name, surname);
        this.patients = new ArrayList<>();
    }

    public int getPatientCount() {
        return this.patients.size();
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public List<Patient> getPatients() {
        return this.patients;
    }
}
