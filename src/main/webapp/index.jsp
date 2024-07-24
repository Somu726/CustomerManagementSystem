<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer Management</title>
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
        .link {
            display: block;
            text-align: center;
            margin: 20px 0;
            padding: 10px;
            background-color: #5cb85c;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
        }
        .link:hover {
            background-color: #4cae4c;
        }
        .logout {
            display: block;
            text-align: center;
            margin: 20px 0;
            padding: 10px;
            background-color: #d9534f;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
        }
        .logout:hover {
            background-color: #c9302c;
        }
    </style>
    <script>
        function getQueryParam(param) {
            const urlParams = new URLSearchParams(window.location.search);
            return urlParams.get(param);
        }

        function logout() {
       
            localStorage.removeItem('authToken');
           
            window.location.href = 'login.jsp';
        }

        window.onload = function() {
            const token = getQueryParam('token');
            if (token) {
                
                localStorage.setItem('authToken', token);
            }
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Customer Management System</h2>
        <a class="link" href="listCustomers">View Customers</a>
        <a class="link" href="createCustomers.jsp">Add Customer</a>
        <a class="logout" href="javascript:void(0);" onclick="logout()">Logout</a>
    </div>
</body>
</html>
