

import java.sql.Statement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	String ip,res,str,base64;
	int num,num1


	;


    /**
     * Default constructor.
     */
    public MyServlet() {
        // TODO Auto-generated constructor stub
    	super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out=response.getWriter();
		out.println("connecting to android");
		ip=request.getRemoteAddr();//returns the ip address of the client that sent the request
		System.out.println(ip);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		PrintWriter out=response.getWriter();
        response.setCharacterEncoding("UTF-8");
		try
		{
            String cycle = request.getParameter("Cycle");


            if("1".equals(cycle)){
                // if cycle no. is 1  do this
                response.setStatus(HttpServletResponse.SC_OK);

                // get client ip and mobile number
                String clientIP = request.getRemoteAddr();
                String mbNumber = request.getParameter("Data");

                // save it to database
                Connection dbConn=null;
                try
                {
                    Class.forName("com.mysql.jdbc.Driver");
                    String url="jdbc:mysql://localhost:3306/new_schema";
                    String user="root";
                    String password="shreya";
                    dbConn=DriverManager.getConnection(url, user, password);
                    Statement stmt=dbConn.createStatement();
                    String sql="UPDATE server "+"SET IPaddress = '"+clientIP+"'WHERE mobileno = '"+mbNumber+"'";
                    stmt.execute(sql);
                }
                catch(ClassNotFoundException e)
                {
                    System.out.println("cannot find class"+e);
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if(connection!=null)
                    {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // create a output stream
                OutputStreamWriter writer=new OutputStreamWriter(response.getOutputStream());

                // create response to sent
                String res = "Acknowledgement : Mobile Number Received \n Message : Hello Client";

                // write response to output stream and close stream
                writer.write(res);
                writer.flush();
                writer.close();

            }else if("2".equals(cycle)){
                // if cycle no. is 2  do this
                response.setStatus(HttpServletResponse.SC_OK);

                String dataBase64 = request.getParameter("Data");

                // do what you want do with data_bas64

                // set and send the response
            }
            else{
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
		}
		catch(IOException e)
		{
			response.getWriter().println(e);
		}

	}

}
