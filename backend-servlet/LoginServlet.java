import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String userID = request.getParameter("userID");
        String password = request.getParameter("password");

        String dbUrl  = "jdbc:mysql://localhost:3306/clicker";
        String dbUser = "root";
        String dbPass = "xxxx"; // your MySQL password

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);

            String sql = "SELECT * FROM users WHERE userID='" + userID 
                       + "' AND password='" + password + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                out.println("success");
            } else {
                out.println("Invalid userID or password!");
            }
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        }
    }
}