
# Library Management System

## Các Ngôn Ngữ Có Sẵn
- 🇬🇧 [English](README.md)
- 🇻🇳 [Tiếng Việt](README.vi.md)

## Mục Lục
1. [Giới Thiệu](#giới-thiệu)
2. [Tính Năng](#tính-năng)
3. [Yêu Cầu Hệ Thống](#yêu-cầu-hệ-thống)
4. [Hướng Dẫn Sử Dụng](#hướng-dẫn-sử-dụng)
5. [Cấu Trúc Thư Mục](#cấu-trúc-thư-mục)
6. [Tác Giả](#tác-giả)

## Giới Thiệu
**Library Management System** là một ứng dụng Java được thiết kế để hỗ trợ việc quản lý thư viện, bao gồm quản lý tài liệu (sách, tạp chí, báo), người dùng (thành viên, thủ thư) và các giao dịch mượn/trả sách.

## Tính Năng
- **Quản Lý Tài Liệu:**
  - Thêm, sửa, và xóa tài liệu (sách, tạp chí, báo).
  - Tìm kiếm và xem chi tiết tài liệu.
- **Quản Lý Người Dùng:**
  - Đăng ký và đăng nhập tài khoản.
  - Quản lý thông tin thành viên và thủ thư.
  - Gia hạn thẻ thành viên.
- **Giao Dịch:**
  - Mượn và trả tài liệu.
  - Xem lịch sử giao dịch.
- **Khác:**
  - Đánh giá và nhận xét sách.
  - Giao diện dễ sử dụng với hỗ trợ CSS và FXML.

## Yêu Cầu Hệ Thống
- **Ngôn Ngữ:** Java 17 hoặc cao hơn.
- **Công Cụ Phát Triển:** Visual Studio Code
- **Thư Viện/Phụ Thuộc:**
  - JavaFX cho giao diện người dùng.
  - JUnit để kiểm thử.
  - Các dependencies khác được khai báo trong `module-info.java`.

## Hướng Dẫn Sử Dụng

1. **Tải về ứng dụng:**
   - Truy cập phần [Releases](https://github.com/Nezuko1909/Library_Management_Application/releases) của GitHub.
   - Tải về tệp `.jar` mới nhất (`Library-management-system.jar`).

2. **Chạy ứng dụng:**
   - Đảm bảo đã cài đặt Java 17 hoặc cao hơn trên hệ thống. Kiểm tra phiên bản Java bằng cách chạy:
     ```bash
     java -version
     ```
   - Di chuyển đến thư mục chứa tệp `.jar` và chạy lệnh sau:
     ```bash
     java -jar library-management-system.jar
     ```

3. **Tương tác với ứng dụng:**
   - Ứng dụng sẽ mở ra với giao diện người dùng đồ họa (GUI).
   - Sử dụng các tính năng của ứng dụng để quản lý tài liệu, người dùng và giao dịch mượn/trả.

4. **Tùy chọn: Cấu hình các tùy chọn chạy (nếu cần):**
   - Nếu gặp vấn đề với JavaFX, đảm bảo thêm các đối số chạy cần thiết:
     ```bash
     java --module-path <path-to-javafx> --add-modules javafx.controls,javafx.fxml -jar library-management-system.jar
     ```
     Thay `<path-to-javafx>` bằng thư mục cài đặt JavaFX trên hệ thống của bạn.

5. **Phản hồi và Vấn Đề:**
   - Nếu bạn gặp lỗi hoặc có đề xuất, hãy báo cáo trong phần [Issues](https://github.com/Nezuko1909/Library_Management_Application/issues) của GitHub.

## Cấu Trúc Thư Mục

Dự án được tổ chức như sau:
```markdown
library-management-system/ 
├── src/ 
│ ├── main/ 
│ │ ├── java/ 
│ │ │ ├── com.library/ 
│ │ │ │ ├── App.java # Điểm vào chính(main) của ứng dụng 
│ │ │ │ ├── controller/ # Xử lý tương tác người dùng và logic
│ │ │ │ ├── model/ # Chứa các mô hình cho tài liệu, người dùng và giao dịch 
│ │ │ │ └── service/ # Logic chính và các dịch vụ 
│ │ ├── resources/ 
│ │ │ ├── css/ # Tệp CSS để tạo kiểu cho GUI 
│ │ │ ├── fxml/ # Tệp FXML xác định giao diện người dùng 
│ │ │ └── img/ # Hình ảnh sử dụng trong ứng dụng 
│ ├── test/ 
│ │ ├── java/ 
│ │ │ ├── com.library/ # Các test case cho các thành phần khác nhau 
│ │ │ └── service/ # Các unit test cho các dịch vụ 
├── module-info.java # Cấu hình module Java 
└── README.md 
```

### Các Thư Mục Chính
- `src/main/java/com.library`:
  - **`controller/`**: Chứa các controller quản lý giao diện người dùng và logic của ứng dụng.
  - **`model/`**: Định nghĩa các mô hình cho tài liệu (sách, báo, tạp chí), người dùng và giao dịch.
  - **`service/`**: Triển khai các logic nghiệp vụ như quản lý tài liệu, người dùng và giao dịch.
- `src/main/resources`:
  - **`css/`**: Các tệp CSS để tạo kiểu cho giao diện.
  - **`fxml/`**: Các tệp FXML định nghĩa bố cục giao diện người dùng.
  - **`img/`**: Các biểu tượng và hình ảnh được sử dụng trong ứng dụng.
- `src/test/java`:
  - Chứa các tệp test JUnit để kiểm thử các controller và service.

### Các Tệp Quan Trọng
- `App.java`: Nơi đặt lớp chính(main) của ứng dụng.
- `module-info.java`: Khai báo các module Java và các dependencies của chúng.

## Tác Giả

Dự án được phát triển và bảo trì bởi:

- **thanhbt25**
- **Nghia**
- **Tv.Quyen**
- **ChauKhanhLy**

Để phản hồi hoặc đóng góp, vui lòng liên hệ với chúng tôi qua [GitHub Issues](https://github.com/Nezuko1909/Library_Management_Application/issues).
