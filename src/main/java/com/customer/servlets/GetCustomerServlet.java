package com.customer.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.customer.model.Customer;
import com.customer.utils.DatabaseUtil;


@WebServlet("/GetCustomer")
public class GetCustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            int customerId = Integer.parseInt(idParam);
            Customer customer = getCustomerById(customerId);
            if (customer != null) {
                request.setAttribute("showCustomerDetails", true);
                request.setAttribute("customerId", customer.getId());
                request.setAttribute("customerFirstName", customer.getFirstName());
                request.setAttribute("customerLastName", customer.getLastName());
                request.setAttribute("customerStreet", customer.getStreet());
                request.setAttribute("customerAddress", customer.getAddress());
                request.setAttribute("customerCity", customer.getCity());
                request.setAttribute("customerState", customer.getState());
                request.setAttribute("customerEmail", customer.getEmail());
                request.setAttribute("customerPhone", customer.getPhone());
            } else {
                request.setAttribute("showCustomerDetails", false);
                request.setAttribute("errorMessage", "Customer does not exist.");
            }
            request.getRequestDispatcher("listCustomers.jsp").forward(request, response);
        } else {
            request.setAttribute("showCustomerDetails", false);
            request.setAttribute("errorMessage", "Invalid customer ID.");
            request.getRequestDispatcher("listCustomers.jsp").forward(request, response);
        }
    }

    private Customer getCustomerById(int customerId) {
        Customer customer = null;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM Customer WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setFirstName(resultSet.getString("first_name"));
                customer.setLastName(resultSet.getString("last_name"));
                customer.setStreet(resultSet.getString("street"));
                customer.setAddress(resultSet.getString("address"));
                customer.setCity(resultSet.getString("city"));
                customer.setState(resultSet.getString("state"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhone(resultSet.getString("phone"));
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customer;
    }
}

/*@WebServlet("/getCustomer")
public class GetCustomerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int customerId = Integer.parseInt(request.getParameter("id"));

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM Customer WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setFirstName(resultSet.getString("first_name"));
                customer.setLastName(resultSet.getString("last_name"));
                customer.setStreet(resultSet.getString("street"));
                customer.setAddress(resultSet.getString("address"));
                customer.setCity(resultSet.getString("city"));
                customer.setState(resultSet.getString("state"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhone(resultSet.getString("phone"));

                request.setAttribute("customers", customer);
                RequestDispatcher dispatcher = request.getRequestDispatcher("listCustomers.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while retrieving the customer.");
        }
    }
}
*/
