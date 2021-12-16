import db.DBManager;
import db.entity.Doctor;
import db.entity.Nurse;
import db.entity.Patient;
import db.entity.Prescription;

import javax.print.Doc;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class Demo {
    public static void main(String[] args) throws ParseException {
        DBManager manager = DBManager.getInstance();
        List<Patient> patients = manager.getAllPatients();
        List<Doctor> doctors = manager.getAllDoctors();
        //patients.stream().sorted(Comparator.comparing(Patient::getFullName)).forEach(System.out::println);
        //doctors.stream().sorted(Comparator.comparing(Doctor::getFullName)).forEach(System.out::println);
        //System.out.println(manager.setDoctorToPatient(doctors.get(4), patients.get(0)));
        //List<Doctor> doctors = manager.getAllDoctors();
        //doctors.stream().sorted(Comparator.comparing(Doctor::getPatientCount).reversed()).forEach(System.out::println);
        //manager.addPrescription(new Prescription(patients.get(0), doctors.get(0), 1, "lol"));
        doctors.forEach(System.out::println);

    }
}
