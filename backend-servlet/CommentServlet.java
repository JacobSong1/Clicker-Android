import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String userID = request.getParameter("userID");
        String comment = request.getParameter("comment");

        String dbUrl  = "jdbc:mysql://localhost:3306/clicker";
        String dbUser = "root";
        String dbPass = "xxxx"; // your MySQL password

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);

            String sql = "INSERT INTO comments (userID, comment) VALUES ('"
                    + userID + "', '" + comment + "')";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conn.close();

            out.println("success");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        }
    }
}