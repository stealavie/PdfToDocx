import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainServer {
    static BlockingQueue<Socket> queue = new LinkedBlockingQueue<Socket>();
    static BlockingQueue<user> queueUsers = new LinkedBlockingQueue<user>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(1234);
            System.out.println("Server is running on port 1234");
            new Thread(() -> {
                while (true) {
                    try {
                        Socket socket = server.accept();
                        queue.add(socket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
            new Thread(new clientHandler(queue, queueUsers)).start();
            new Thread(new DataHandler(queueUsers)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class clientHandler implements Runnable {
    BlockingQueue<user> queueUsers;
    BlockingQueue<Socket> queue;

    public clientHandler(BlockingQueue<Socket> queue, BlockingQueue<user> queueUsers) {
        this.queue = queue;
        this.queueUsers = queueUsers;
    }

    @Override
    public synchronized void run() {
        while (true) {
            if (!queue.isEmpty()) {
                try {
                    Socket socket = queue.take();
                    System.out.println("Client connected");
                    DataInputStream dis = new DataInputStream(socket.getInputStream());

                    String message = dis.readUTF();
                    if (message.equals("THEM")) {
                        int id = dis.readInt();
                        int id_u = dis.readInt();

                        if (!queueUsers.isEmpty()) {
                            for (user u : queueUsers) {
                                if (u.id == id && u.id_u == id_u) {
                                    return;
                                }
                            }
                        }
                        // Receive the file from the first server (JSP/Servlet)
                        File folder = new File("pdf");
                        if (!folder.exists()) {
                            folder.mkdirs();
                        }

                        String subfolderPath = folder.getAbsolutePath() + File.separator + id_u; // Use File.separator

                        File subfolder = new File(subfolderPath);
                        if (!subfolder.exists()) {
                            subfolder.mkdirs(); // Create subfolder (e.g., "1") if it doesn't exist
                        }

                        String pdfPath = subfolderPath + File.separator + id + ".pdf";
                        File pdfFile = new File(pdfPath);
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pdfFile));
                        int c;
                        do {
                            c = dis.readInt();
                            bos.write(c);
                        } while (c != -1);
                        bos.close();

                        user u = new user(id, id_u, pdfFile.getAbsolutePath());
                        queueUsers.add(u);
                        System.out.println("User added");
                        dis.close();

                        socket.close();

                    } else if (message.equals("LAYNHIEU")) {
                        int lengthHistory = dis.readInt();
                        System.out.println("lengthHistory: " + lengthHistory);
                        if (queueUsers.isEmpty() || lengthHistory == 0) {
                            return;
                        }
                        for (int i = 0; i < lengthHistory; i++) {
                            int id = dis.readInt();
                            int id_u = dis.readInt();
                            user u1 = null;
                            for (user u : queueUsers) {
                                if (u.id == id && u.id_u == id_u) {
                                    u1 = u;
                                    break;
                                }
                            }
                            if (u1 != null) {
                                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                                dos.writeInt(u1.status);
                                dos.flush();

                                if (u1.status == 1) {
                                    System.out.println("1");
                                    u1.isSent = 1;
                                }
                                if (u1.status == -1) {
                                    sendDocxBacktoServer(dos, u1.docxPath);
                                    u1.status = 1;
                                    System.out.println("docxFile sent back to the client.");
                                }

                                System.out.println("status sent back to the client.");
                                if (u1.status == 1 && u1.isSent == 1) {
                                    queueUsers.remove(u1);
                                    System.out.println("User removed");
                                }
                            }
                        }
                        dis.close();
                        socket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendDocxBacktoServer(DataOutputStream dos, String docxPath) {
        try {
            File docxFile = new File(docxPath);
            int c;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(docxFile));
            do {
                c = bis.read();
                dos.writeInt(c);
                dos.flush();
            } while (c != -1);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

class DataHandler implements Runnable {
    BlockingQueue<user> queueUsers;

    public DataHandler(BlockingQueue<user> queueUsers) {
        this.queueUsers = queueUsers;
    }

    @Override
    public void run() {
        while (true) {
            if (!queueUsers.isEmpty()) {
                try {
                    for (user i : queueUsers) {
                        if (i.status == 1) {
                            continue;
                        }
                        File folder = new File("result");
                        if (!folder.exists()) {
                            folder.mkdirs();
                        }

                        String subfolderPath = folder.getAbsolutePath() + File.separator + i.id_u; // Use File.separator

                        File subfolder = new File(subfolderPath);
                        if (!subfolder.exists()) {
                            subfolder.mkdirs(); // Create subfolder (e.g., "1") if it doesn't exist
                        }

                        String docxPath = subfolderPath + File.separator + i.id + ".docx";
                        File docxFile = new File(docxPath);
                        if (convertPdfToDocx(new File(i.pdfPath), docxFile.getAbsolutePath())) {
                            i.docxPath = docxFile.getAbsolutePath();
                            i.status = -1;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean convertPdfToDocx(File pdfFile, String docxFilePath) {
        try {
            // Example using Aspose.PDF for conversion
            com.aspose.pdf.Document pdfDocument = new com.aspose.pdf.Document(pdfFile.getAbsolutePath());
            pdfDocument.save(docxFilePath, com.aspose.pdf.SaveFormat.DocX);
            pdfDocument.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

class user {
    public int id, id_u, status, isSent;
    public String pdfPath, docxPath;

    public user(int id, int id_u, String pdfPath) {
        this.id = id;
        this.id_u = id_u;
        this.pdfPath = pdfPath;
        this.status = 0;
        this.isSent = 0;
        this.docxPath = "";
    }
}
