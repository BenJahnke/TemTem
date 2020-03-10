import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SimpleFormSearch")
public class SearchJahnke extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SearchJahnke() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String nameKeyword = request.getParameter("name");
      String typeKeyword = request.getParameter("type");
      search(nameKeyword, typeKeyword, response);
   }

   void search(String nameKeyword, String typeKeyword, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Database Result";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      try {
         DBConnectionJahnke.getDBConnection();
         connection = DBConnectionJahnke.connection;

         if (nameKeyword.isEmpty() && typeKeyword.isEmpty()) {
            String selectSQL = "SELECT * FROM TemTem";
            preparedStatement = connection.prepareStatement(selectSQL);
         } 
         else if (!typeKeyword.isEmpty()){
        	 String selectSQL = "SELECT * FROM TemTem WHERE type LIKE ?";
             String type = typeKeyword + "%";
             preparedStatement = connection.prepareStatement(selectSQL);
             preparedStatement.setString(1, type);
         } 
         else if (!nameKeyword.isEmpty()){
            String selectSQL = "SELECT * FROM TemTem WHERE name LIKE ?";
            String name = nameKeyword + "%";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, name);
         }
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            String name = rs.getString("name").trim();
            String type = rs.getString("type").trim();
            String found = rs.getString("found").trim();

            if (nameKeyword.isEmpty() || name.contains(nameKeyword)) {
               out.println("Name: " + name + ", ");
               out.println("Type: " + type + ", ");
               out.println("Found at: " + found + "<br>");
            }
         }
         out.println("<a href=/temtem-project/search_jahnke.html>Search Data</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         connection.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (preparedStatement != null)
               preparedStatement.close();
         } catch (SQLException se2) {
         }
         try {
            if (connection != null)
               connection.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
