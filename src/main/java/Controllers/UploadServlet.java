package Controllers;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.Socket;
import java.net.InetAddress;

public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the uploaded file
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();

        // Save the uploaded file locally
        File uploadedFile = new File("uploads/" + fileName);
        try (InputStream fileContent = filePart.getInputStream();
             OutputStream outStream = new FileOutputStream(uploadedFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileContent.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        }

        // Establish a TCP connection to the second server (ServerSocket)
        try (Socket socket = new Socket(InetAddress.getByName("localhost"), 12345)) {
            // Send the file to the second server (ServerSocket)
            try (OutputStream socketOut = socket.getOutputStream();
                 FileInputStream fileInput = new FileInputStream(uploadedFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInput.read(buffer)) != -1) {
                    socketOut.write(buffer, 0, bytesRead);
                }
            }

            // Receive the DOCX file from the second server
            File convertedDocx = new File("converted_output.docx");
            try (InputStream socketIn = socket.getInputStream();
                 FileOutputStream fileOutput = new FileOutputStream(convertedDocx)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = socketIn.read(buffer)) != -1) {
                    fileOutput.write(buffer, 0, bytesRead);
                }
            }

            // Allow the user to download the DOCX file
            response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "attachment; filename=" + convertedDocx.getName());
            try (FileInputStream fileInputStream = new FileInputStream(convertedDocx);
                 OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}

