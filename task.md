Dưới đây là prompt Markdown đã được cập nhật, **loại bỏ hoàn toàn Floating Button và quyền `SYSTEM_ALERT_WINDOW` (Hiển thị trên ứng dụng khác)**.

Phương án thay thế cho tính năng Double Tap to Lock là sử dụng **Homescreen Widget (Invisible/Transparent Widget)** hoặc **Tile trong Quick Settings**, vừa gọn nhẹ vừa không đòi hỏi quyền đè lên ứng dụng khác.

---

# Yêu cầu Phát triển: Ứng dụng Cử chỉ & Thao tác Nhanh cho Thiết bị Sony Xperia (Phiên bản Quyền Tối giản)

Bạn là một Kỹ sư Phát triển Ứng dụng Android Cao cấp (Senior Android Developer). Hãy thực thi và xây dựng ứng dụng dựa trên toàn bộ mô tả chi tiết dưới đây.

---

## 1. Mục tiêu Dự án

Điện thoại Sony Xperia có giao diện gần với Android thuần nhưng thiếu rất nhiều thao tác vuốt thông minh và tiện ích thao tác nhanh.

Ứng dụng này nhằm khỏa lấp khoảng trống đó bằng một giải pháp **gọn nhẹ, tôn trọng quyền riêng tư**:

* **KHÔNG** sử dụng Floating Button (Nút nổi).
* **KHÔNG** yêu cầu quyền "Hiển thị trên ứng dụng khác" (`SYSTEM_ALERT_WINDOW`).
* Tập trung hoàn toàn vào **Cử chỉ đa điểm** và **Widget/Tile hệ thống** để thực hiện các thao tác nhanh (Chụp màn hình, khóa màn hình, mở thông báo...).

---

## 2. Các Tính năng Cốt lõi & Mở rộng (Feature Requirements)

### 2.1. Cử chỉ Vuốt & Đa điểm (Multi-finger Gestures)

* **Thao tác 3 ngón tay:**
* Vuốt xuống: Chụp màn hình (Screenshot) / Chụp màn hình cuộn (Scroll Screenshot).
* Vuốt lên: Mở ứng dụng gần đây (Recents) / Mở Bảng thông báo / Bật Flashlight.
* Vuốt sang trái/phải: Chuyển đổi nhanh giữa các ứng dụng vừa dùng / Chuyển bài hát.


* **Thao tác 2 ngón tay:**
* Vuốt từ cạnh viền (Left/Right edge swipe): Điều chỉnh nhanh âm lượng hoặc độ sáng.
* Vuốt từ cạnh dưới (Bottom edge swipe): Tắt/Khóa màn hình ngay lập tức.


* **Tùy chọn mở rộng khác:**
* Vuốt 4 ngón tay: Thu nhỏ màn hình (Chế độ dùng 1 tay - One-handed mode).
* Chạm 3 ngón tay (3-finger tap): Mở ứng dụng tùy chỉnh / Bật Bảng Cài đặt nhanh.



### 2.2. Thao tác Nhanh (Quick Actions Shortcuts)

Bộ hành động phong phú để gán vào các cử chỉ vuốt hoặc Quick Settings Tiles:

* **Hệ thống:** Khóa màn hình (Lock screen), Chụp màn hình, Mở Bảng thông báo (Notification Panel), Mở Cài đặt nhanh (Quick Settings), Chế độ chia đôi màn hình (Split screen).
* **Nguồn & Tiện ích:** Bật/Tắt Đèn pin (Flashlight), Chế độ Không làm phiền (DND), Bật/Tắt Xoay màn hình, Bật/Tắt Bluetooth / Wi-Fi.
* **Truyền thông:** Chuyển bài hát (Next/Previous), Tăng/Giảm âm lượng, Tạm dừng/Phát nhạc.
* **Khởi chạy ứng dụng:** Chạy bất kỳ App nào cài trong máy hoặc kích hoạt Shortcut Android.

### 2.3. Khóa Nhanh Tối giản (Quick Lock Solutions - Không cần Nút Nổi)

* **Homescreen Lock Widget (Widget Khóa Màn Hình):**
* Cung cấp một Widget trong suốt/biểu tượng khóa để người dùng đặt lên màn hình chính.
* Cho phép chỉnh chạm 1 lần hoặc Chạm 2 lần (Double Tap) vào Widget để khóa màn hình ngay lập tức.


* **Quick Settings Tile (Ô Cài đặt nhanh):**
* Thêm ô "Khóa màn hình" hoặc "Chụp màn hình" trực tiếp vào thanh Cài đặt nhanh (Quick Settings) trên Android.



---

## 3. Giao diện Người dùng (UI/UX Design Concept)

Thiết kế tuân theo ngôn ngữ **Material You (Material Design 3)**, hỗ trợ Dynamic Color theo hệ thống Sony Xperia.

### 3.1. Cấu trúc Màn hình (Screen Hierarchy)

1. **Home / Dashboard Screen:**
* Card trạng thái: Hiển thị trạng thái Bật/Tắt của **Accessibility Service** (Cấp quyền duy nhất cần thiết).
* Hướng dẫn nhanh cách thêm Widget hoặc Quick Settings Tile.


2. **Gesture Mapping Screen (Cấu hình Cử chỉ):**
* Phân loại: `Cử chỉ 3 ngón` | `Cử chỉ 2 ngón` | `Cử chỉ 4 ngón`.
* Danh sách rõ ràng cho phép chọn Trigger (Ví dụ: *Vuốt 3 ngón xuống*) ➔ Action (Ví dụ: *Chụp màn hình*).


3. **App Exclusion List (Danh sách loại trừ):**
* Cho phép chọn các ứng dụng (như Game, Camera) để tự động tạm dừng cử chỉ vuốt nhằm tránh chạm nhầm khi chơi game hay thao tác phức tạp.



---

## 4. Kiến trúc Kỹ thuật & Logic Xử lý (Technical Architecture & Logic)

### 4.1. Android Services & Permissions (Tối giản quyền)

* **Cần duy nhất: Accessibility Service (`AccessibilityService`)**
* Lắng nghe sự kiện cảm ứng đa điểm toàn hệ thống.
* Thực thi các hành động hệ thống native như `GLOBAL_ACTION_LOCK_SCREEN`, `GLOBAL_ACTION_TAKE_SCREENSHOT`, `GLOBAL_ACTION_NOTIFICATIONS`, `GLOBAL_ACTION_RECENTS`.
* **Đảm bảo mở khóa vân tay:** Sử dụng cơ chế khóa màn hình qua Accessibility API (`GLOBAL_ACTION_LOCK_SCREEN` trên Android 9+) giúp màn hình tắt hoàn toàn mà vẫn mở khóa lại được bằng Vân tay mà không bị bắt nhập PIN/Mật khẩu.


* **CẤM SỬ DỤNG:** `SYSTEM_ALERT_WINDOW` (Quyền đè lên app khác).

### 4.2. Luồng Logic Xử lý Cử chỉ (Gesture Detection Flow)

```text
[Touch Event Received via AccessibilityService] 
        │
        ├── Check Active App in Exclusion List? 
        │       ├── YES ➔ Pass event through (Ignore)
        │       └── NO  ➔ Continue
        │
        ├── Count Active Pointers (Fingers)
        │       ├── 2 Fingers ➔ Track Delta X / Delta Y
        │       ├── 3 Fingers ➔ Track Direction Vector (Up/Down/Left/Right)
        │       └── Other ➔ Ignore
        │
        └── Match Trigger Threshold? (Distance > Min_Swipe_Distance)
                ├── YES ➔ Trigger Assigned Action (Haptic feedback + Execute)
                └── NO  ➔ Reset Touch State

```

---

## 5. Tiêu chí Đánh giá Tối thượng (Definition of Done)

1. **Không yêu cầu bất kỳ quyền vẽ trên ứng dụng khác (`SYSTEM_ALERT_WINDOW`)**.
2. Khi vuốt 3 ngón tay xuống, chụp màn hình kích hoạt ngay lập tức với độ trễ dưới 50ms.
3. Khóa màn hình qua Gesture/Widget/Tile xong vẫn mở khóa lại bình thường bằng cảm biến vân tay trên dòng máy Sony Xperia.
4. Giao diện trực quan, cài đặt cực kỳ đơn giản (chỉ cần bật Accessibility Service duy nhất 1 lần).
