# Trang web chuyển đổi PDF sang DOCX sử dụng Java Servlet

## Giới thiệu đề tài

Đề tài này xây dựng một trang web hỗ trợ chuyển đổi file PDF sang định dạng DOCX. Hệ thống sử dụng công nghệ Java Servlet và được chia thành 2 server hoạt động song song:

1. **Server 1 - Giao diện người dùng (UI Server)**: Server này chịu trách nhiệm xử lý giao diện người dùng, tiếp nhận file PDF từ người dùng và hiển thị kết quả trả về dưới dạng file DOCX. Server này sử dụng mô hình **MVC** (Model-View-Controller) để quản lý và phân phối công việc giữa các thành phần của hệ thống.

2. **Server 2 - Server xử lý dữ liệu (Processing Server)**: Server này thực hiện công việc chuyển đổi từ PDF sang DOCX. Khi nhận được yêu cầu từ Server 1, Server 2 sẽ tiến hành xử lý file PDF và trả lại kết quả cho Server 1. Server này sẽ luôn chạy ngầm và liên tục lắng nghe các yêu cầu từ Server 1.

---

## Thiết kế hệ thống

![image](https://github.com/user-attachments/assets/b8b4bcac-e739-4188-a5ba-17447d47232d)

---

## Hướng dẫn cài đặt

Để triển khai và sử dụng hệ thống, bạn cần thực hiện các bước cài đặt sau:

### 1. Cài đặt JDK

Để phát triển ứng dụng Java, bạn cần cài đặt **JDK** (Java Development Kit). Các bước cài đặt JDK như sau:

1. Truy cập trang chính của Oracle để tải JDK tại [https://www.oracle.com/java/technologies/javase-downloads.html](https://www.oracle.com/java/technologies/javase-downloads.html).
2. Chọn phiên bản JDK phù hợp với máy tính của bạn.
3. Tải về và chạy trình cài đặt theo hướng dẫn. 

### 2. Cài đặt Eclipse IDE

Để phát triển ứng dụng Java Servlet, bạn cần cài đặt **Eclipse IDE**. Truy cập trang chủ của Eclipse tại [https://www.eclipse.org/](https://www.eclipse.org/) và tải xuống phiên bản phù hợp với hệ điều hành của bạn. Sau khi cài đặt, mở Eclipse để bắt đầu cấu hình môi trường.

**Phiên bản bắt buộc**

![image](https://github.com/user-attachments/assets/f06f195b-9729-442b-9fdb-867ef4a279e8)

### 3. Cài đặt XAMPP

Để chạy server web (Apache) và quản lý cơ sở dữ liệu (MySQL), bạn cần cài đặt **XAMPP**. Tải XAMPP tại: [https://www.apachefriends.org/](https://www.apachefriends.org/) và làm theo hướng dẫn cài đặt.

### 4. Tạo dự án Java Dynamic Web

Sau khi cài đặt Eclipse và XAMPP, bạn cần tạo một dự án **Java Dynamic Web**. Làm theo các bước sau:

**Bước 1: Tạo dự án mới**

![image](https://github.com/user-attachments/assets/aeede3b7-76bc-4846-8d09-a37cf6291ef9)

**Bước 2: Cấu hình dự án**

![image](https://github.com/user-attachments/assets/9d769bd9-9d28-4c17-bcf9-d0cb77d7ab86)

![image](https://github.com/user-attachments/assets/76d1e247-b83e-408c-aec3-c889e0fc0f70)

![image](https://github.com/user-attachments/assets/00657433-3949-4a32-bfea-ebc236ba2923)

### 5. Clone dự án
```
git clone https://github.com/stealavie/Math-Tool.git
```

---

## Chúc may mắn

Chúc bạn thành công trong việc triển khai và phát triển hệ thống! 
---
