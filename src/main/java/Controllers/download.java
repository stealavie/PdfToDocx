package Controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Models.Bean.Account;

@WebServlet("/download")
public class download extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("GET");
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");
        if (user == null) {
            response.sendRedirect("index.jsp"); // If not logged in, redirect to login page
            return;
        }
        String docx_path = "";

        try {
            docx_path = request.getParameter("docx_path");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File name is missing");
            return;
        }

        System.out.println(docx_path);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + docx_path + "\"");

        // Read the file and stream it to the client
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(docx_path));
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buffer = new byte[1024]; // Buffer to hold chunks of received data
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead); // Write the chunk to the output file
            }
            bos.close();
            bis.close();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error reading the file");
        }
    }
}