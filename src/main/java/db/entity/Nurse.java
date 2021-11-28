package db.entity;

import java.util.ArrayList;
import java.util.List;

public class Nurse extends Medic {
    private Doctor doctor;

    public Nurse(int id, String name, String surname, List<Patient> patients, Doctor doctor) {
        super(id, name, surname, patients);
        this.doctor = doctor;
    }

    public Nurse(int id, String name, String surname, Doctor doctor) {
        super(id, name, surname);
        this.doctor = doctor;
        takeDoctorsPatients();
    }

    public Nurse(String name, String surname, Doctor doctor) {
        super(name, surname);
        this.doctor = doctor;
        takeDoctorsPatients();
    }

    public Nurse(String name, String surname) {
        super(name, surname);
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void takeDoctorsPatients() {
        if (this.doctor != null)
            this.setPatients(doctor.getPatients());
    }

    @Override
    public String toString() {
        return this.getFullName() + " лікар: " + this.getDoctor();
    }
}
