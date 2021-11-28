package db.entity;

import java.sql.Timestamp;

public class Prescription {
    private int id;
    private Doctor doctor;
    private Patient patient;
    private String data;
    private int type;
    private Timestamp time;
    private boolean isActive;
    private Timestamp executionTime;
    private int executor;

    public Prescription() {}

    public Prescription(int id, Patient patient, Doctor doctor, int type, String data, Timestamp time, boolean isActive,
                        Timestamp executionTime, int executor) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.type = type;
        this.data = data;
        this.time = time;
        this.isActive = isActive;
        this.executionTime = executionTime;
        this.executor = executor;
    }


    public Prescription(int id, Patient patient, Doctor doctor, int type, String data, Timestamp time) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.type = type;
        this.data = data;
        this.time = time;
        this.isActive = true;
    }

    public Prescription(Patient patient, Doctor doctor, int type, String data) {
        this.patient = patient;
        this.doctor = doctor;
        this.type = type;
        this.data = data;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getData() {
        return data;
    }

    public int getType() {
        return type;
    }

    public Timestamp getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public Timestamp getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Timestamp executionTime) {
        this.executionTime = executionTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getExecutor() {
        return executor;
    }

    public void setExecutor(int executor) {
        this.executor = executor;
    }

    @Override
    public String toString() {
        return "Пацієнт: " + this.patient.getFullName() + ". Лікар: " + this.doctor.getFullName() + ". Призначення: " +this.getData();
    }

}
