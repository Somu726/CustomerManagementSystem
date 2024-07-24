
# Customer Management System
The Customer Management System is a simple web-based CRUD application built using JSP, Servlets, and MySQL for the backend. The frontend is designed using HTML, CSS, and JavaScript. The application allows for the management of customer information, including creating, updating, retrieving, and deleting customer records. It also supports JWT-based authentication and integrates with a remote API for synchronizing customer data.




## Features




- User Authentication: Secure login system using JWT.
 - Customer Management: Create, read, update, and delete customer information.
 - Pagination, Sorting, and Searching: Efficiently manage large sets of customer data.
- Data Synchronization: Sync customer data from a remote API, with updates for existing records.
- Simple User Interface: Basic HTML forms and tables for interaction
## Technologies Used


- Java Development Kit (JDK) 8 or higher
- Backend: Java Servlets
- Frontend: JSP (JavaServer Pages)
- Database: MySQL
- Authentication: JWT (JSON Web Token)
## Database Setup


- Install MySQL server and create a database (customer_db).
- Execute SQL scripts to create tables and insert sample data.

Sql
```sql
  
CREATE DATABASE customer_db;

   USE customer_db;

CREATE TABLE Customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    street VARCHAR(100),
    address VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(15)
);


```

Update the database connection details in DatabaseUtil.java:

java
```java
private static final String URL = "jdbc:mysql://localhost:3306/customer_db";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";

```
## Project Setup

1. Clone the repository:

```bash
  git clone https://github.com/yourusername/CustomerManagementSystem.git

```
2. Open the project in your IDE (Eclipse or IntelliJ IDEA).

3. Add the necessary JAR files for JDBC and Servlets to the project's build path.

4. Configure Tomcat in your IDE and deploy the project.

5. Go to AuthServlet file you can give your loginid and password to fetch the details

java
```java
@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginId = request.getParameter("username");
        String password = request.getParameter("password");

        if ("somu@gmail.com".equals(loginId) && "somu@123".equals(password)) {
            String token = JWTUtil.generateToken(loginId);
            response.sendRedirect("index.jsp?token=" + token);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported");
    }
}
```
you can update the logindetails as yours

6. Access the application at http://localhost:8080/CustomerManagementSystem.
    
- once you access the application give correct loginID and passowrd like

    ```
    loginId = "somu@gmail.com"
    password = "somu@123"
    ```
##  Deployment

- Ensure MySQL is running and the BlogApp database is created.
- Deploy the application on Tomcat.
- Navigate to http://localhost:8080/CustomerManagementSystem to access the application.
## TroubleShooting
- Ensure the database connection details are correct in DatabaseUtil.java.
- Verify Tomcat is running on the specified port (default is 8080).
- Check the Tomcat logs for any errors during deployment.


Feel free to adjust any part of the description to better fit your project specifics or personal preferences.
