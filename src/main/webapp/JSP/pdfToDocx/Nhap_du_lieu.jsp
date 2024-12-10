<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ page import="Models.Bean.Account"%>
<%@ page import="java.util.List"%>
<%@ page import="Models.Bean.ConversionHistory"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Lịch sử</title>
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

        table {
            width: 80%;
            margin-top: 20px;
            border-collapse: collapse;
        }

        table,
        th,
        td {
            border: 1px solid white;
        }

        th,
        td {
            padding: 10px;
            text-align: center;
        }

        th {
            background-color: rgba(255, 255, 255, 0.2);
        }

        td a {
            color: #ff7eb3;
            text-decoration: none;
            transition: color 0.3s ease;
        }

        td a:hover {
            color: #ff4e73;
        }

        /* Loading spinner styles */
        .loading {
            font-size: 1.5rem;
            color: #ff7eb3;
        }

        .spinner {
            border: 4px solid rgba(255, 255, 255, 0.3);
            border-top: 4px solid #ff7eb3;
            border-radius: 50%;
            width: 50px;
            height: 50px;
            animation: spin 1s linear infinite;
            margin: 20px auto;
        }

        @keyframes spin {
            0% {
                transform: rotate(0deg);
            }

            100% {
                transform: rotate(360deg);
            }
        }
    </style>
    <script>
        // Function to fetch status from the server periodically
        function fetchHistory() {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "${pageContext.request.contextPath}/connectServer", true);
            console.log(1);
            xhr.onreadystatechange = function () {
                console.log(2);
                if (xhr.readyState == 4 && xhr.status == 200) {
                    var response = JSON.parse(xhr.responseText);
                    var status = response.status;
                    var history = JSON.parse(response.history);

                    // Example: Update table with the history data
                    var tableBody = document.getElementById('table-body');
                    tableBody.innerHTML = '';  // Clear existing rows
                    if (!history || history.length === 0) {
                        // If no history, replace table with a <p> tag
                        var noData = document.createElement('p');
                        noData.textContent = 'No history found';
                        tableBody.appendChild(noData);
                    } else {
                        history.forEach(function (record, index) {
                            var row = document.createElement('tr');
                            var pdf_path = document.createElement('td');
                            pdf_path.textContent = record.pdf_path;

                            var tdStatus = document.createElement('td');
                            if (record.status === 0 || record.status === -1) {
                                tdStatus.textContent = "Đang xử lý";
                            } else if (record.status === 1) {
                                tdStatus.textContent = "Đã hoàn thành";
                            }

                            var conversion_time = document.createElement('td');
                            conversion_time.textContent = record.conversion_time;

                            var tdDetails = document.createElement('td');
                            // add form to submit
                            if (record.status !== 0 || record.status !== -1) {
                                var form = document.createElement('form');
                                form.method = 'GET';
                                form.action = '${pageContext.request.contextPath}/download';
                                var input = document.createElement('input');
                                input.type = 'hidden';
                                input.name = 'docx_path';
                                input.value = record.docx_path;
                                var button = document.createElement('button');
                                button.type = 'submit';
                                button.textContent = 'Tải';
                                form.appendChild(input);
                                form.appendChild(button);
                                tdDetails.appendChild(form);
                            }

                            row.appendChild(pdf_path);
                            row.appendChild(tdStatus);
                            row.appendChild(conversion_time);
                            row.appendChild(tdDetails);

                            tableBody.appendChild(row);
                        });
                    }
                    if (status === true) {
                        clearInterval(statusInterval);
                    }

                }
            };
            xhr.send();
        }

        // Poll every 1 seconds for status update
        var statusInterval = setInterval(fetchHistory, 1000);
    </script>
</head>

<body>
    <% // Retrieve session and user object Account user=(Account) session.getAttribute("user"); %>
        <form action="${pageContext.request.contextPath}/connectServer" method="POST" enctype="multipart/form-data">
            <label for="file">Chọn file:</label>
            <input type="file" id="file" name="file" accept=".pdf" required /><br><br>
            <input type="submit" value="Chuyển đổi" />
        </form>

        <h1>Lịch sử chuyển đổi</h1>

        <!-- Table for displaying the data -->
        <table>
            <thead>
                <tr>
                    <th>Tên PDF</th>
                    <th>Trạng thái</th>
                    <th>Thời gian chuyển đổi</th>
                    <th>Tải xuống</th>
                </tr>
            </thead>
            <tbody id="table-body">
                <!-- The table rows will be dynamically added here by JavaScript -->
            </tbody>
        </table>
</body>

</html>
