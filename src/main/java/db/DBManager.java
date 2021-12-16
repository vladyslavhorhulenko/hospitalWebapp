package db;

import db.entity.*;

import javax.print.Doc;
import javax.swing.*;
import javax.xml.transform.Result;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBManager {
    String url = "";
    private static DBManager manager = null;

    private DBManager() {
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(new File("D:\\JavaProjects\\webapp\\app.properties")));
            Properties prop = new Properties();
            prop.load(in);
            url = prop.getProperty("connection.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DBManager getInstance() {
        if (manager == null)
            manager = new DBManager();
        return manager;
    }

    public List<Patient> getAllPatients() {
        String sql = "SELECT * FROM patient";
        List<Patient> patients = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                patients.add(new Patient(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        LocalDate.parse(rs.getDate(4).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDate.parse(rs.getDate(5).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                ));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public List<Doctor> getAllDoctors() {
        String sql = "SELECT * FROM doctor";
        List<Doctor> doctors = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            ResultSet docResSet = statement.executeQuery(sql);
            while (docResSet.next()) {
                Doctor doctor = new Doctor(
                        docResSet.getInt(1),
                        docResSet.getString(2),
                        docResSet.getString(3),
                        docResSet.getInt(4));
                String patientSql = "SELECT * FROM patient_doctor WHERE doctor_id = ?";
                List<Patient> patients = new ArrayList<>();
                PreparedStatement pStatement = connection.prepareStatement(patientSql);
                pStatement.setInt(1, docResSet.getInt(1));
                pStatement.execute();
                ResultSet docPatientsResSet = pStatement.getResultSet();
                while (docPatientsResSet.next()) {
                    patients.add(getPatientById(docPatientsResSet.getInt(1)));
                }
                doctor.setPatients(patients);
                doctors.add(doctor);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // returns null if a new patient has not been added
    public Patient addPatient(Patient patient) {
        String sql = "INSERT INTO patient(patient_name, patient_surname, patient_birthday)" +
                "VALUES(?, ?, ?)";
        Patient insertedPatient = null;
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, patient.getName());
            statement.setString(2, patient.getSurname());
            statement.setDate(3, Date.valueOf(patient.getBirthday()));
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    insertedPatient = new Patient(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            LocalDate.parse(rs.getDate(4).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            LocalDate.parse(rs.getDate(5).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    );
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedPatient;
    }

    // returns null if a new doctor has not been added
    public Doctor addDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctor(doctor_name, doctor_surname, doctor_category)" +
                "VALUES(?, ?, ?)";
        Doctor insertedDoctor = null;
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, doctor.getName());
            statement.setString(2, doctor.getSurname());
            statement.setInt(3, doctor.getCategory());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    insertedDoctor = new Doctor(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4)
                    );
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedDoctor;
    }

    // returns null if a patient has not been searched
    public Patient getPatientById(int patientId) {
        String sql = "SELECT * FROM patient WHERE patient_id = ?";
        Patient patient = null;
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, patientId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                patient = new Patient(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        LocalDate.parse(rs.getDate(4).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDate.parse(rs.getDate(5).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                );
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    // returns null if a doctor has not been searched
    public Doctor getDoctorById(int doctorId) {
        String sql = "SELECT * FROM doctor WHERE doctor_id = ?";
        Doctor doctor = null;
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, doctorId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                doctor = new Doctor(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4)
                );
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
    }

    public boolean isDoctorsPatient(Doctor doctor, Patient patient) {
        String sql = "SELECT patient_doctor.patient_id FROM patient_doctor WHERE patient_doctor.doctor_id = ?";
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, doctor.getId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExistsCurrentDoctorsCategory(Doctor doctor, Patient patient) {
        String sql = "SELECT patient_doctor.patient_id FROM patient_doctor INNER JOIN doctor ON doctor.doctor_id = patient_doctor.doctor_id WHERE doctor.doctor_category = ?";
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, doctor.getCategory());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setDoctorToPatient(Doctor doctor, Patient patient) {
        if (isDoctorsPatient(doctor, patient) || isExistsCurrentDoctorsCategory(doctor, patient))
            return false;
        String sql = "INSERT INTO patient_doctor values(?, ?)";
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, patient.getId());
            statement.setInt(2, doctor.getId());
            int affectedRows = statement.executeUpdate();
            connection.close();
            if (affectedRows > 0)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Prescription addPrescription(Prescription prescr) {
         if (!(isDoctorsPatient(prescr.getDoctor(), prescr.getPatient())))
             return null;
         String sql = "INSERT INTO prescription(patient_id, doctor_id, prescr_type, prescr_data) VALUES(?, ?, ?, ?)";
         try {
             Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             statement.setInt(1, prescr.getPatient().getId());
             statement.setInt(2, prescr.getDoctor().getId());
             statement.setInt(3, prescr.getType());
             statement.setString(4, prescr.getData());
             int affectedRows = statement.executeUpdate();
             if (affectedRows > 0) {
                 ResultSet rs = statement.getGeneratedKeys();
                 if (rs.next()) {
                     return new Prescription(
                             rs.getInt(1),
                             getPatientById(rs.getInt(2)),
                             getDoctorById(rs.getInt(3)),
                             rs.getInt(4),
                             rs.getString(5),
                             rs.getTimestamp(6));
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return null;
    }

    public List<Prescription> getPatientPrescriprions(Patient patient) {
        String sql = "SELECT * FROM prescription WHERE patient_id = ?";
        List<Prescription> prescriptions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, patient.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                prescriptions.add(new Prescription(
                      rs.getInt(1),
                      getPatientById(rs.getInt(2)),
                      getDoctorById(rs.getInt(3)),
                      rs.getInt(4),
                      rs.getString(5),
                      rs.getTimestamp(6),
                      rs.getBoolean(7),
                      rs.getTimestamp(8),
                      rs.getInt(9)
                ));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptions;
    }

    public int getPrescrLevel(Prescription prescr) {
        String sql = "SELECT prescription_type.type_level FROM prescription JOIN prescription_type " +
                "ON prescription.prescr_type = prescription_type.type_id WHERE prescription.prescr_id = ?";
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, prescr.getId());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Nurse addNurse(Nurse nurse) {
        String sql = "INSERT INTO nurse(nurse_name, nurse_surname)" +
                "VALUES(?, ?)";
        Nurse insertedNurse = null;
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, nurse.getName());
            statement.setString(2, nurse.getSurname());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    insertedNurse = new Nurse(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            null
                    );
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedNurse;
    }

    public List<Nurse> getAllNurses() {
        String sql = "SELECT * FROM nurse";
        List<Nurse> nurses = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            ResultSet docResSet = statement.executeQuery(sql);
            while (docResSet.next()) {
                nurses.add(new Nurse(
                        docResSet.getInt(1),
                        docResSet.getString(2),
                        docResSet.getString(3),
                        getDoctorById(docResSet.getInt(4))
                ));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nurses;
    }

    public Nurse setNurseToDoctor(Nurse nurse, Doctor doctor) {
        String sql = "UPDATE nurse SET nurse_doctor = ? WHERE nurse_id = ?";
        try {
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, doctor.getId());
            statement.setInt(2, nurse.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    nurse = new Nurse(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            doctor
                    );
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nurse;
    }

    public boolean isMedicAbleToExecutePrescr(Prescription prescr, Medic medic) {
        if (medic instanceof Doctor)
            if (!(((Doctor) medic).getCategory() == prescr.getDoctor().getCategory()))
                return false;
        if (medic instanceof Nurse)
            if (!(((Nurse) medic).getDoctor().getCategory() == prescr.getDoctor().getCategory()) || getPrescrLevel(prescr) != 2)
                return false;
        return true;
    }

    public boolean executePrescription(Prescription prescr, Medic medic) {
        if (prescr.isActive() && isMedicAbleToExecutePrescr(prescr, medic)) {
            String sql = "UPDATE prescription SET prescr_is_active = false, prescr_execution_time = CURRENT_TIMESTAMP, " +
                    "prescr_executor = ? WHERE prescr_id = ?";
            try {
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, medic.getId());
                statement.setInt(2, prescr.getId());
                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    ResultSet rs = statement.getGeneratedKeys();
                    if (rs.next()) {
                        prescr.setActive(rs.getBoolean(7));
                        prescr.setExecutionTime(rs.getTimestamp(8));
                        prescr.setExecutor(rs.getInt(9));
                    }
                }
                connection.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
