<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, com.customer.utils.DatabaseUtil" %>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    String firstName = "";
    String lastName = "";
    String street = "";
    String address = "";
    String city = "";
    String state = "";
    String email = "";
    String phone = "";
    try (Connection connection = DatabaseUtil.getConnection()) {
        String sql = "SELECT * FROM Customer WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            firstName =   rs.getString("first_name");
            lastName =   rs.getString("last_name");
            street =   rs.getString("street");
            address =  rs.getString("address");
            city =  rs.getString("city");
            state =  rs.getString("state");
            email =  rs.getString("email");
            phone =   rs.getString("phone");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Student</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 50%;
            margin: 50px auto;
            background: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            color: #333;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        label {
            margin: 10px 0 5px;
            font-weight: bold;
        }
        input[type="text"], input[type="email"], input[type="number"] {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        input[type="submit"] {
            margin-top: 20px;
            padding: 10px;
            background-color: #5cb85c;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #4cae4c;
        }
        .back-link {
            display: block;
            margin-top: 20px;
            text-align: center;
        }
    </style>
</head>
<<body>
    <h1>Update Customer</h1>
    <form action="updateCustomer" method="post">
        <input type="hidden" name="id" value="<%= id %>">
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" value="$<%= firstName %>" required><br><br>
        
        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" value="<%= lastName %>" required><br><br>
        
        <label for="street">Street:</label>
        <input type="text" id="street" name="street" value="<%= street %>" required><br><br>
        
        <label for="address">Address:</label>
        <input type="text" id="address" name="address" value="<%= address %>" required><br><br>
        
        <label for="city">City:</label>
        <input type="text" id="city" name="city" value="<%= city %>" required><br><br>
        
        <label for="state">State:</label>
        <input type="text" id="state" name="state" value="<%= state %>" required><br><br>
        
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="<%= email %>" required><br><br>
        
        <label for="phone">Phone:</label>
        <input type="text" id="phone" name="phone" value="<%= phone %>" required><br><br>
        
        <input  type="submit" value = "Update customer">
    </form>
    <a class="back-link" href="listCustomers">Back to Customer List</a>
</body>
</html>
