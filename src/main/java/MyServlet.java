import db.DBManager;
import db.entity.Doctor;
import db.entity.Person;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@WebServlet("/doctors")
public class MyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        DBManager manager = DBManager.getInstance();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<Doctor> doctors = manager.getAllDoctors();
        try(PrintWriter writer = response.getWriter()) {
            doctors.stream().sorted(Comparator.comparing(Person::getFullName)).forEach(writer::println);
        }
    }
}
