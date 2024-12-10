<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tạo tài khoản</title>
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
            margin-top: 50px;
            padding: 20px;
            background-color: white;
            width: 300px;
            margin-left: auto;
            margin-right: auto;
            border: 1px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        h2 {
            margin-bottom: 20px;
            color: #4CAF50;
        }

        input {
            width: 90%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }

        button {
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            background-color: #4CAF50;
            color: white;
            cursor: pointer;
            margin-top: 10px;
        }

        button:hover {
            background-color: #45a049;
        }

        .link {
            color: #4CAF50;
            text-decoration: none;
            font-size: 0.9em;
        }

        p {
            margin-top: 15px;
        }

        .error {
            color: red;
            font-size: 0.9em;
        }
    </style>
    <script>
        // JavaScript function to validate password and confirm password
        function check() {
            var password = document.form.password.value;
            var confirm_password = document.form.confirm_password.value;

            // If passwords don't match, show an error and prevent form submission
            if (password !== confirm_password) {
                document.getElementById("error-message").innerHTML = "Mật khẩu không trùng!";
                document.getElementById("error-message").style.display = "block";
                return false;  // Prevent form submission
            } else {
                document.getElementById("error-message").style.display = "none";
                return true;  // Allow form submission
            }
        }
    </script>
</head>

<body>
    <div class="container">
        <h2>Tạo tài khoản</h2>
        <!-- Display error message if there is one -->
        <c:if test="${not empty errorMessage}">
            <p class="error">${errorMessage}</p>
        </c:if>

        <!-- Form to create an account -->
        <form action="${pageContext.request.contextPath}/signupServlet" name="form" method="post"
            onsubmit="return check()">
            <input type="text" name="username" placeholder="Tên đăng nhập" required>
            <input type="password" name="password" placeholder="Mật khẩu" required>
            <input type="password" name="confirm_password" placeholder="Xác nhận mật khẩu" required onblur="check()">

            <!-- Error message for password mismatch -->
            <p id="error-message" class="error" style="display:none;"></p>

            <button type="submit">Đăng kí</button>
        </form>
        <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/loginServlet" class="link">Đăng nhập</a></p>
    </div>
</body>

</html>
