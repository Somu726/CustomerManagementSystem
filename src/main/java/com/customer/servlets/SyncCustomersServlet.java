package com.customer.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.customer.model.Customer;
import com.customer.utils.DatabaseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;




@WebServlet("/SyncCustomers")
public class SyncCustomersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apiUrl = "http://localhost:9090/customerManagementSystem/listCustomers";
        String token = (String) request.getSession().getAttribute("token");
        if (token == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token found. Please authenticate first.");
            return;
        }

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                String jsonResponse = new BufferedReader(new InputStreamReader(inputStream))
                                      .lines().collect(Collectors.joining("\n"));
                
                // Log the JSON response
                System.out.println("API Response: " + jsonResponse);

                ObjectMapper mapper = new ObjectMapper();
                List<Customer> customers = mapper.readValue(jsonResponse, 
                        mapper.getTypeFactory().constructCollectionType(List.class, Customer.class));

                syncCustomersToDatabase(customers);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(conn.getResponseCode(), "Failed to fetch customers from API");
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while syncing customers");
        }
    }

    private void syncCustomersToDatabase(List<Customer> customers) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            for (Customer customer : customers) {
                if (customerExists(customer.getId(), connection)) {
                    updateCustomer(customer, connection);
                } else {
                    insertCustomer(customer, connection);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean customerExists(int customerId, Connection connection) throws Exception {
        String sql = "SELECT COUNT(*) FROM Customer WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, customerId);
        ResultSet resultSet = statement.executeQuery();
        boolean exists = false;
        if (resultSet.next()) {
            exists = resultSet.getInt(1) > 0;
        }
        resultSet.close();
        statement.close();
        return exists;
    }

    private void updateCustomer(Customer customer, Connection connection) throws Exception {
        String sql = "UPDATE Customer SET first_name = ?, last_name = ?, street = ?, address = ?, city = ?, state = ?, email = ?, phone = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, customer.getFirstName());
        statement.setString(2, customer.getLastName());
        statement.setString(3, customer.getStreet());
        statement.setString(4, customer.getAddress());
        statement.setString(5, customer.getCity());
        statement.setString(6, customer.getState());
        statement.setString(7, customer.getEmail());
        statement.setString(8, customer.getPhone());
        statement.setInt(9, customer.getId());
        statement.executeUpdate();
        statement.close();
    }

    private void insertCustomer(Customer customer, Connection connection) throws Exception {
        String sql = "INSERT INTO Customer (id, first_name, last_name, street, address, city, state, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, customer.getId());
        statement.setString(2, customer.getFirstName());
        statement.setString(3, customer.getLastName());
        statement.setString(4, customer.getStreet());
        statement.setString(5, customer.getAddress());
        statement.setString(6, customer.getCity());
        statement.setString(7, customer.getState());
        statement.setString(8, customer.getEmail());
        statement.setString(9, customer.getPhone());
        statement.executeUpdate();
        statement.close();
    }
}


/*
@WebServlet("/syncCustomers")
public class SyncCustomersServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = authenticateUser();
        if (token != null) {
            try {
                // Fetch customers from the remote API
                URL url = new URL("https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + token);

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder content = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    // Parse the JSON response
                    Gson gson = new Gson();
                    Customer[] customers = gson.fromJson(content.toString(), Customer[].class);

                    // Update the database with the fetched customers
                    Connection dbConnection = DatabaseUtil.getConnection();
                    for (Customer customer : customers) {
                        String selectSQL = "SELECT * FROM Customer WHERE id = ?";
                        PreparedStatement selectStmt = dbConnection.prepareStatement(selectSQL);
                        selectStmt.setInt(1, customer.getId());
                        ResultSet rs = selectStmt.executeQuery();

                        if (rs.next()) {
                            String updateSQL = "UPDATE Customer SET first_name = ?, last_name = ?, street = ?, address = ?, city = ?, state = ?, email = ?, phone = ? WHERE id = ?";
                            PreparedStatement updateStmt = dbConnection.prepareStatement(updateSQL);
                            updateStmt.setString(1, customer.getFirstName());
                            updateStmt.setString(2, customer.getLastName());
                            updateStmt.setString(3, customer.getStreet());
                            updateStmt.setString(4, customer.getAddress());
                            updateStmt.setString(5, customer.getCity());
                            updateStmt.setString(6, customer.getState());
                            updateStmt.setString(7, customer.getEmail());
                            updateStmt.setString(8, customer.getPhone());
                            updateStmt.setInt(9, customer.getId());
                            updateStmt.executeUpdate();
                        } else {
                            String insertSQL = "INSERT INTO Customer (id, first_name, last_name, street, address, city, state, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement insertStmt = dbConnection.prepareStatement(insertSQL);
                            insertStmt.setInt(1, customer.getId());
                            insertStmt.setString(2, customer.getFirstName());
                            insertStmt.setString(3, customer.getLastName());
                            insertStmt.setString(4, customer.getStreet());
                            insertStmt.setString(5, customer.getAddress());
                            insertStmt.setString(6, customer.getCity());
                            insertStmt.setString(7, customer.getState());
                            insertStmt.setString(8, customer.getEmail());
                            insertStmt.setString(9, customer.getPhone());
                            insertStmt.executeUpdate();
                        }
                    }
                    dbConnection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported.");
    }

    private String authenticateUser() {
        try {
            URL url = new URL("https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{\"login_id\":\"test@sunbasedata.com\",\"password\":\"Test@123\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                TokenResponse tokenResponse = gson.fromJson(content.toString(), TokenResponse.class);
                return tokenResponse.getToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class TokenResponse {
        private String token;

        public String getToken() {
            return token;
        }
    }
}
*/