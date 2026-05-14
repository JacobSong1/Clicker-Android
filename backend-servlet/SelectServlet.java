import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/select")
public class SelectServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Step 5: Get the choice from URL parameter e.g. ?choice=a
        String choice = request.getParameter("choice");

        // Database connection details — adjust if needed
        String dbUrl  = "jdbc:mysql://localhost:3306/clicker";
        String dbUser = "root";
        String dbPass = "xxxx"; // your MySQL password if any

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);

            // Hardcode questionNo as 1 for now
            String sqlstr = "INSERT INTO responses (questionNo, choice) VALUES (1, '"
                            + choice + "')";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlstr);
            conn.close();

            // Send a simple confirmation back
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("Choice '" + choice + "' recorded! Thank you.");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}