package com.customer.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.customer.model.Customer;
import com.customer.utils.DatabaseUtil;


@WebServlet("/listCustomers")
public class ReadCustomersServlet extends HttpServlet {
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchQuery = request.getParameter("search");
		List<Customer> customers = new ArrayList<>();
//    	int page = Integer.parseInt(request.getParameter("page"));
//      int size = Integer.parseInt(request.getParameter("size"));

        try (Connection conn = DatabaseUtil.getConnection()){
        		String sql = "SELECT * FROM Customer ";
                if (searchQuery != null && !searchQuery.isEmpty()) {
                    sql += " WHERE first_name LIKE ? OR email LIKE ? "
                    		+ " OR city LIKE ? OR phone LIKE ? OR state LIKE ? ";
                }

                PreparedStatement stmt = conn.prepareStatement(sql);
                if (searchQuery != null && !searchQuery.isEmpty()) {
                    stmt.setString(1, "%" + searchQuery + "%");
                    stmt.setString(2, "%" + searchQuery + "%");
                    stmt.setString(3, "%" + searchQuery + "%");
                    stmt.setString(4, "%" + searchQuery + "%");
                    stmt.setString(5, "%" + searchQuery + "%");
                }
             
                /*try (Connection connection = DatabaseUtil.getConnection()) {
                    StringBuilder query = new StringBuilder("SELECT * FROM Customer ");

                    if (search != null && !search.isEmpty()) {
                        query.append(" AND (first_name LIKE ? OR last_name LIKE ? OR email LIKE ?)");
                    }
                    if (sort != null && !sort.isEmpty() && order != null && !order.isEmpty()) {
                        query.append(" ORDER BY ").append(sort).append(" ").append(order);
                    }
                    

                    PreparedStatement statement = connection.prepareStatement(query.toString());

                    int paramIndex = 1;
                    if (search != null && !search.isEmpty()) {
                        String searchPattern = "%" + search + "%";
                        statement.setString(paramIndex++, searchPattern);
                        statement.setString(paramIndex++, searchPattern);
                        statement.setString(paramIndex++, searchPattern);
                    }
					*/
            ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                  int id =   rs.getInt("id");
                  String firstName =   rs.getString("first_name");
                  String lastName =   rs.getString("last_name");
                  String street =   rs.getString("street");
                  String address =  rs.getString("address");
                  String city =  rs.getString("city");
                  String state =  rs.getString("state");
                  String email =  rs.getString("email");
                  String phone =   rs.getString("phone");
                  customers.add(new Customer(id,firstName,lastName,street, address,city,state,email,phone));
                }
            
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("listCustomers.jsp").forward(request, response);
    }
}



