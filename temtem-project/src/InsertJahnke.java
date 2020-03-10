
/**
 * @file SimpleFormInsert.java
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SimpleFormInsert")
public class InsertJahnke extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public InsertJahnke() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String name = request.getParameter("name");
      String type = request.getParameter("type");
      String found = request.getParameter("found");

      Connection connection = null;
      String insertSql = " INSERT INTO TemTem (id, name, type, found) values (default, ?, ?, ?)";

      try {
    	  if (name != "" && type != "" && found != "") {
	         DBConnectionJahnke.getDBConnection();
	         connection = DBConnectionJahnke.connection;
	         PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
	         preparedStmt.setString(1, name);
	         preparedStmt.setString(2, type);
	         preparedStmt.setString(3, found);
	         preparedStmt.execute();
	         connection.close();
    	  }
      } catch (Exception e) {
         e.printStackTrace();
      }

      // Set response content type
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "TemTem";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h2 align=\"center\">" + title + "</h2>\n" + //
            "<ul>\n" + //

            "  <li><b>Name</b>: " + name + "\n" + //
            "  <li><b>Type</b>: " + type + "\n" + //
            "  <li><b>Found at</b>: " + found + "\n" + //

            "</ul>\n");

      out.println("<a href=/temtem-project/insert_jahnke.html>Search Data</a> <br>");
      out.println("</body></html>");
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
