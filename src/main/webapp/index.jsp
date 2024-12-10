<%@ page language="java" import="javax.servlet.http.HttpSession" %>
<%@ page import="Models.Bean.Account" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<%
    // Get the session object and check for a logged-in user
    session = request.getSession(false);
    Account loggedInUser = (session != null) ? (Account) session.getAttribute("account") : null;
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Math Tools</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(135deg, #89f7fe, #66a6ff);
            color: white;
            text-align: center;
            margin: 0;
            padding: 0;
            height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            position: relative;
        }

        header {
            position: absolute;
            top: 10px;
            right: 10px;
        }

        .auth-buttons {
            display: flex;
            gap: 10px;
        }

        .auth-buttons button {
            background: #ff7eb3;
            color: white;
            font-size: 0.9rem;
            border: none;
            padding: 8px 15px;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .auth-buttons button:hover {
            background: #ff4e73;
            transform: scale(1.05);
        }

        h1 {
            font-size: 2.5rem;
            margin-bottom: 20px;
        }

        p {
            font-size: 1.2rem;
            margin-bottom: 30px;
        }

        button {
            background: #ff7eb3;
            color: white;
            font-size: 1.2rem;
            border: none;
            padding: 15px 30px;
            margin: 10px;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        button:hover {
            background: #ff4e73;
            transform: scale(1.05);
        }
    </style>
</head>
<%@ page session="true" %> <!-- Ensures the session is available in the JSP -->

    <body>
        <header>
            <% if (loggedInUser !=null) { %>
                <!-- If the user is logged in, display a welcome message and a logout button -->
                <span>Welcome, <%= loggedInUser.getUsername() %></span>
                <form method="GET" action="${pageContext.request.contextPath}/logoutServlet" style="display:inline;">
                    <button type="submit">Đăng xuất</button>
                </form>
                <% } else { %>
                    <!-- If the user is not logged in, show the sign in and sign up buttons -->
                    <button
                        onclick="window.location.href='${pageContext.request.contextPath}/JSP/Authentication/login.jsp'">Đăng
                        nhập</button>
                    <button
                        onclick="window.location.href='${pageContext.request.contextPath}/JSP/Authentication/signup.jsp'">Đăng
                        kí</button>
                    <% } %>
        </header>

        <h1>Chào mừng đến bộ chuyển đổi PDF sang DOCX</h1>
        <button onclick="selectFunction('pdfConverter')">Bộ chuyển đổi pdf</button>
        <script>
            function selectFunction(func) {
                window.location.href = "${pageContext.request.contextPath}/loginServlet?function=" + func;
            }
        </script>
    </body>

</html>
