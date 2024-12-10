<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(135deg, #66a6ff, #89f7fe);
            text-align: center;
            margin: 0;
            padding: 0;
            height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }

        .container {
            margin-top: 100px;
            padding: 20px;
            background-color: white;
            width: 300px;
            margin-left: auto;
            margin-right: auto;
            border: 1px solid #ccc;
            border-radius: 10px;
        }

        input {
            width: 90%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        button {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            background-color: #4CAF50;
            color: white;
            cursor: pointer;
            margin: 10px 0;
        }

        button:hover {
            background-color: #45a049;
        }

        .link {
            color: #4CAF50;
            text-decoration: none;
            font-size: 0.9em;
        }

        .error {
            color: red;
            font-size: 0.9em;
        }
    </style>
</head>

<body>
    <div class="container">
        <h2>Đăng nhập</h2>
        <!-- Display error message if there is one -->
        <c:if test="${not empty errorMessage}">
            <p class="error">${errorMessage}</p>
        </c:if>
        <form action="${pageContext.request.contextPath}/loginServlet" method="POST">
            <input type="text" name="username" id="username" placeholder="Tên đặng nhập" required>
            <input type="password" name="password" id="password" placeholder="Mật khẩu" required>
            <button type="submit">Đăng nhập</button>
        </form>
        <p>Không có tài khoản? <a href="${pageContext.request.contextPath}/signupServlet" class="link">Tạo mới</a>
        </p>
    </div>
</body>

</html>