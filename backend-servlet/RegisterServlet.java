import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String userID = request.getParameter("userID");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

        String dbUrl  = "jdbc:mysql://localhost:3306/clicker";
        String dbUser = "root";
        String dbPass = "xxxx"; // your MySQL password

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);

            // Check if userID already exists
            String checkSql = "SELECT * FROM users WHERE userID='" + userID + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(checkSql);

            if (rs.next()) {
                out.println("User ID already exists!");
            } else {
                String insertSql = "INSERT INTO users (userID, password, phone) VALUES ('"
                        + userID + "', '" + password + "', '" + phone + "')";
                stmt.executeUpdate(insertSql);
                out.println("success");
            }
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        }
    }
}