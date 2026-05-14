import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/display")
public class DisplayServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String dbUrl  = "jdbc:mysql://localhost:3306/clicker";
        String dbUser = "root";
        String dbPass = "xxxx"; // your MySQL password

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body style='font-family:Arial; padding:30px;'>");
        out.println("<h2>Q1. Who is the coolest Marvel Hero?</h2>");
        out.println("<ul>");
        out.println("<li>A. Captain America</li>");
        out.println("<li>B. Ironman</li>");
        out.println("<li>C. Black Widow</li>");
        out.println("<li>D. Thor</li>");
        out.println("</ul>");
        out.println("<h3>Results:</h3>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);

            // Bar chart
            String sql = "SELECT choice, COUNT(*) AS count FROM responses WHERE questionNo=1 GROUP BY choice";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            String[] labels = {"a", "b", "c", "d"};
            int[] counts = new int[4];

            while (rs.next()) {
                String choice = rs.getString("choice").toLowerCase();
                int count = rs.getInt("count");
                for (int i = 0; i < labels.length; i++) {
                    if (labels[i].equals(choice)) {
                        counts[i] = count;
                    }
                }
            }

            String[] colors = {"#4285F4", "#EA4335", "#000000", "#FBBC05"};
            String[] names = {"A. Captain America", "B. Ironman", "C. Black Widow", "D. Thor"};

            int max = 1;
            for (int c : counts) if (c > max) max = c;

            out.println("<div style='display:flex; align-items:flex-end; height:300px; gap:40px; padding:20px; border-bottom:2px solid black;'>");
            for (int i = 0; i < 4; i++) {
                int barHeight = (counts[i] == 0) ? 0 : (int)((double)counts[i] / max * 250);
                out.println("<div style='display:flex; flex-direction:column; align-items:center;'>");
                out.println("<span style='margin-bottom:5px; font-weight:bold;'>" + counts[i] + "</span>");
                out.println("<div style='width:80px; height:" + barHeight + "px; background-color:" + colors[i] + ";'></div>");
                out.println("<span style='margin-top:8px; font-size:12px; text-align:center;'>" + names[i] + "</span>");
                out.println("</div>");
            }
            out.println("</div>");

            // Comments section
            out.println("<h3 style='margin-top:30px;'>Student Comments:</h3>");
            String commentSql = "SELECT userID, comment, timestamp FROM comments ORDER BY timestamp DESC";
            ResultSet commentRs = stmt.executeQuery(commentSql);

            out.println("<table border='1' style='border-collapse:collapse; width:60%;'>");
            out.println("<tr><th style='padding:8px;'>User ID</th><th style='padding:8px;'>Comment</th><th style='padding:8px;'>Time</th></tr>");

            boolean hasComments = false;
            while (commentRs.next()) {
                hasComments = true;
                out.println("<tr>");
                out.println("<td style='padding:8px;'>" + commentRs.getString("userID") + "</td>");
                out.println("<td style='padding:8px;'>" + commentRs.getString("comment") + "</td>");
                out.println("<td style='padding:8px;'>" + commentRs.getString("timestamp") + "</td>");
                out.println("</tr>");
            }

            if (!hasComments) {
                out.println("<tr><td colspan='3' style='padding:8px; text-align:center;'>No comments yet</td></tr>");
            }

            out.println("</table>");
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        }

        out.println("</body></html>");
    }
}