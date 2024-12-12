package Controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import Models.BO.ConversionHistoryBO;
import Models.Bean.Account;
import Models.Bean.ConversionHistory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

@WebServlet("/connectServer")
@MultipartConfig
public class xuly extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ConversionHistoryBO conversionHistoryBO;

    @Override
    public void init() throws ServletException {
        super.init();
        conversionHistoryBO = new ConversionHistoryBO();
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

        List<ConversionHistory> history = conversionHistoryBO.getConversionHistory(user.getId());
        if (history.isEmpty())
            return;

        boolean areWorkAllFinished = true;
        for (ConversionHistory i : history) {
            if (i.getStatus() == 0 || i.getStatus() == -1) {
                areWorkAllFinished = false;
                break;
            }
        }
        if (!areWorkAllFinished)
            updateStatus(history);

        Gson gson = new Gson();
        String jsonResponse_history = gson.toJson(history);
        String jsonResponse_areWorkAllFinished = gson.toJson(areWorkAllFinished);

        // Create a single JSON response containing both 'status' and 'history'
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", jsonResponse_areWorkAllFinished);
        jsonResponse.addProperty("history", jsonResponse_history);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Send the combined JSON response
        response.getWriter().write(jsonResponse.toString());

    }

    private List<ConversionHistory> updateStatus(List<ConversionHistory> history) {
        if (history.isEmpty())
            return null;
        try {
            Socket soc = new Socket("localhost", 1234);
            DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            dos.writeUTF("LAYNHIEU");
            int length = 0;
            for (ConversionHistory i : history) {
                if (i.getStatus() == 0 || i.getStatus() == -1)
                    length++;
            }
            dos.writeInt(length);

            for (ConversionHistory i : history) {
                if (i.getStatus() == 1)
                    continue;

                dos.writeInt(i.getId());
                dos.writeInt(i.getUserId());

                int status = dis.readInt();
                if (status == 0) {
                    soc.close();
                    continue;
                }     
                else if (status == -1 && i.getStatus() != -1) {
                    i.setStatus(status);
                    conversionHistoryBO.update(i.getId(), i.getStatus(), "", i.getConversionTime());
                    i.setDocxPath(getDocxFileFromServer2(soc, i.getUserId(), i.getId()));
                    i.setConversionTime(new Date(System.currentTimeMillis()));
                    conversionHistoryBO.update(i.getId(), i.getStatus(), i.getDocxPath(), i.getConversionTime());
                } else if (status == 1) {
                    i.setStatus(status);
                    conversionHistoryBO.update(i.getId(), i.getStatus(), i.getDocxPath(), i.getConversionTime());
                    soc.close();
                }
            }
            return history;
        } catch (Exception e) {
            System.out.println("ngu1");
            return null;
        }
    }

    private String getDocxFileFromServer2(Socket socket, int u_id, int id) throws IOException {
        String docxPath = getServletContext().getRealPath("") + File.separator + "docx";
        File docxDir = new File(docxPath);
        if (!docxDir.exists()) {
            docxDir.mkdirs(); // Make sure the directory is created
        }

        String userPath = docxPath + File.separator + u_id;
        File userDir = new File(userPath);
        if (!userDir.exists()) {
            userDir.mkdirs(); // Make sure the directory is created
        }

        String userDocxPath = userPath + File.separator + id + ".docx";
        File docxFile = new File(userDocxPath);

        try (
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                FileOutputStream fos = new FileOutputStream(docxFile)) {

            byte[] buffer = new byte[1024]; 
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead); // Write the chunk to the output file
            }
            System.out.println("docxFile received from the server.");
            fos.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            socket.close(); // Close the socket after receiving the file
        }
        return docxFile.getAbsolutePath();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get form data

        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("account");

        ConversionHistory data = new ConversionHistory();

        // Handle file upload
        // Get the uploaded file part
        Part filePart = request.getPart("file"); // "file" is the name attribute from the <input> field

        if (filePart == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file uploaded.");
            return;
        }

        // Get the file name (just the name, not the full path)
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // Define the server's upload directory (relative path)
        String pdfPath = getServletContext().getRealPath("") + File.separator + "pdf";

        // Create the directory if it doesn't exist
        File pdfDir = new File(pdfPath);
        if (!pdfDir.exists()) {
            pdfDir.mkdirs(); // Make sure the directory is created
        }

        String userPath = pdfPath + File.separator + user.getId();
        File userDir = new File(userPath);
        if (!userDir.exists()) {
            userDir.mkdirs(); // Make sure the directory is created
        }

        // Define the full path for saving the file
        String pdfFilePath = userPath + File.separator + fileName;
        File pdfFile = new File(pdfFilePath);

        // Save the file content to the server
        try (InputStream inputStream = filePart.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(pdfFile)) {

            byte[] buffer = new byte[4096]; // Buffer size for file writing
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead); // Write bytes to the output file
            }

            // File successfully uploaded and saved
            System.out.println("File saved to: " + pdfPath);

            int id = conversionHistoryBO.getRecordLength() + 1;
            data.setId(id);
            data.setUserId(user.getId());
            data.setPdfPath(fileName);
            data.setDocxPath("");
            data.setStatus(0);
            data.setConversionTime(new Date(System.currentTimeMillis()));

            try {
                Socket socket = new Socket("localhost", 1234);
                if (socket.isConnected()) {
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                    dos.writeUTF("THEM");
                    dos.writeInt(data.getId());
                    dos.writeInt(data.getUserId());

                    sendFileToSecondServer(socket, new File(pdfFilePath));

                    conversionHistoryBO.storeConversion(data);
                    response.sendRedirect(request.getContextPath() + "/JSP/pdfToDocx/Nhap_du_lieu.jsp");
                }
            } catch (Exception ex) {
                System.out.println("ngu");
            }
        }
    }

    private void sendFileToSecondServer(Socket socket, File uploadedFile) throws IOException {
        try (
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(uploadedFile));
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream())) {

            byte[] buffer = new byte[1024]; 
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead); // Write the data to the output stream in chunks
                bos.flush(); 
            }
            bis.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close(); // Close the socket at the end of the transfer
        }
    }
}