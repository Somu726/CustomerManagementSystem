<%@ page import="java.util.List" %>
<%@ page import="com.customer.model.Customer" %>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 80%;
            margin: 50px auto;
            background: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            color: #333;
        }
        .search-bar, .fetch-bar {
            margin-bottom: 20px;
            text-align: center;
        }
        .search-bar input[type="text"], .fetch-bar input[type="text"] {
            padding: 10px;
            width: 300px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .search-bar input[type="submit"], .fetch-bar input[type="submit"], .fetch-bar input[type="button"] {
            padding: 10px 15px;
            background-color: #5cb85c;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .search-bar input[type="submit"]:hover, .fetch-bar input[type="submit"]:hover, .fetch-bar input[type="button"]:hover {
            background-color: #4cae4c;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 15px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .actions a {
            text-decoration: none;
            color: #337ab7;
            margin: 0 5px;
        }
        .actions a:hover {
            text-decoration: underline;
        }
        .add-new {
            display: block;
            width: 150px;
            margin: 0 auto;
            padding: 10px;
            text-align: center;
            background-color: #5cb85c;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
        }
        .add-new:hover {
            background-color: #4cae4c;
        }
    </style>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div class="container">
        <h2>Customer List</h2>
        <button onclick="location.href='createCustomers.jsp'">Add New Customer</button>
        <button onclick="syncCustomers()">Sync</button>
        <div class="search-bar">
            <form action="listCustomers" method="get">
                <input type="text" name="search" placeholder="Search by firstname or email or city or phone">
                <input type="submit" value="Search">
            </form>
        </div>
        <div class="fetch-bar">
            <form action="GetCustomer" method="get">
                <input type="text" name="id" placeholder="Enter Customer ID">
                <input type="submit" value="Get Customer">
            </form>
        </div>
        <a class="Logout" href="login.jsp">Back to Login</a>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (errorMessage != null) { %>
            <div class="error-message"><%= errorMessage %></div>
        <% } %>
        <table>
            <thead>
                <tr>
                    <th>Id</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Street</th>
                    <th>Address</th>
                    <th>City</th>
                    <th>State</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    List<Customer> customers = (List<Customer>) request.getAttribute("customers");
                    if (customers != null) {
                        for (Customer customer : customers) {
                %>
                <tr>
                    <td><%= customer.getId() %></td>
                    <td><%= customer.getFirstName() %></td>
                    <td><%= customer.getLastName() %></td>
                    <td><%= customer.getStreet() %></td>
                    <td><%= customer.getAddress() %></td>
                    <td><%= customer.getCity() %></td>
                    <td><%= customer.getState() %></td>
                    <td><%= customer.getEmail() %></td>
                    <td><%= customer.getPhone() %></td>
                    <td class="actions">
                        <a href="updateCustomer.jsp?id=<%= customer.getId() %>">Update</a>
                        <a href="deleteCustomer?id=<%= customer.getId() %>">Delete</a>
                    </td>
                </tr>
                <% 
                        }
                    } 
                %>
            </tbody>
        </table>
        <% Boolean showCustomerDetails = (Boolean) request.getAttribute("showCustomerDetails"); %>
        <% if (showCustomerDetails != null && showCustomerDetails) { %>
            <div id="customerDetails">
                <h2>Customer Details</h2>
                <p><strong>ID:</strong> <span id="customerId"><%= request.getAttribute("customerId") %></span></p>
                <p><strong>First Name:</strong> <span id="customerFirstName"><%= request.getAttribute("customerFirstName") %></span></p>
                <p><strong>Last Name:</strong> <span id="customerLastName"><%= request.getAttribute("customerLastName") %></span></p>
                <p><strong>Street:</strong> <span id="customerStreet"><%= request.getAttribute("customerStreet") %></span></p>
                <p><strong>Address:</strong> <span id="customerAddress"><%= request.getAttribute("customerAddress") %></span></p>
                <p><strong>City:</strong> <span id="customerCity"><%= request.getAttribute("customerCity") %></span></p>
                <p><strong>State:</strong> <span id="customerState"><%= request.getAttribute("customerState") %></span></p>
                <p><strong>Email:</strong> <span id="customerEmail"><%= request.getAttribute("customerEmail") %></span></p>
                <p><strong>Phone:</strong> <span id="customerPhone"><%= request.getAttribute("customerPhone") %></span></p>
                <a class="back-link" href="listCustomers">Back to Customers List</a>
            </div>
        <% } %>
    </div>

    <script>
        // Function to fetch customer details
        function fetchCustomerDetails(customerId) {
            if (customerId) {
                window.location.href = 'GetCustomer?id=' + customerId;
            } else {
                alert('Please provide a customer ID');
            }
        }

        // Show customer details if available
        window.onload = function() {
            var customerDetails = document.getElementById('customerDetails');
            if (customerDetails.innerHTML.trim() !== "") {
                customerDetails.style.display = 'block';
            }

            // Retrieve and store token from URL parameter
            var urlParams = new URLSearchParams(window.location.search);
            var token = urlParams.get('token');
            if (token) {
                localStorage.setItem('token', token);
            }
        }

        // Sync customers using the stored token
        function syncCustomers() {
    fetch('/Authenticate', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                return fetch('/SyncCustomers', { method: 'POST' });
            } else {
                throw new Error('Authentication failed');
            }
        })
        .then(response => {
            if (response.ok) {
                alert('Customers synchronized successfully');
            } else {
                alert('Failed to synchronize customers');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

    </script>
</body>
</html>
