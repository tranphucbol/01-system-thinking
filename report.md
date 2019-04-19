### 1. Định lý CAP

#### Giới thiệu
Trong một thế giới hoàn hảo, sẽ chỉ có một mô hình nhất quán dữ liệu: khi có một cập nhật xảy ra thì tất cả người đọc đều nhìn thấy cập nhật đó. Vào những năm 70, nhiều kĩ thuật đang cố gắng đạt được sự trong suốt về phân tán - tức là người dùng chỉ nhìn thấy một hệ thống duy nhát thay vì nhiều hệ thống cộng tác với nhau. Nhiều hệ thống trong thời kì này đi theo cách tiếp cận là thà đánh hỏng toàn bộ hệ thống chứ không phá vỡ tính trong suốt.

Vào thời điểm giữa những năm 90, với sự ra đời của các hệ thống Internet lớn hơn. Người ta bắt đầu xem xét lại những các làm trên. Ở thời điểm hiện tại, tính sẵn sàng của hệ thống là tài sản quý giá nhất. **Định lý CAP** là một lý thuyết trong khoa học máy tính dược phát biểu bởi giáo sư **Eric Allen Brewer** của trường Đại Học California thuộc Berkeley. Ông đồng thời cũng là chủ tịch danh dự mảng hạ tầng của Goolge.

#### Phát biểu định lý
**Định lý CAP** nêu ra ba yếu tố của hệ thống chia sẻ dữ liệu.
* **Consistency** (tính nhất quán): mỗi lần đọc dữ liệu sẽ nhận được nội dung mới nhất hoặc lỗi
* **Availability** (tính sẵn sàng): mỗi một resquest sẽ được một reponse không phải lỗi, nhưng không đảm bảo là lưu trữ mới nhất
* **Partition Tolerance** (tính chịu đựng phân mảnh): Hệ thống tiếp tục hoạt động bất chấp lượng tùy ý các thông điệp / gói tin bị mất hoặc trì hoãn do trục trặc giữ các nút

Một hệ thống lưu trữ phân tán chỉ có thể đồng thời đảm bảo 2 trong 3 yếu tố trên. Tuy nhiên, một hệ thống không chịu đựng phân đoạn mạng có thể đạt được **Consitency** và **Availability** và thường thực hiện bằng các giao thức nhân bản. Để đạt được điều này, hệ thống client và hệ thống lưu trữ phải ở trong cùng một môi trường. Để đạt được điều này, hệ thống client và hệ thống lưu trữ phải ở trong cùng một môi trường, trong một số tình huống nhất định chúng sẽ suy sụp cùng nhau, và vì vậu client không nhận ra phân đoạn. Một nhận xét quan trọng là trong các hệ thống phân tán ở qui mô lớn thì việc mạng bị phân đoạn là một thực tế, do đó **Consistency** và **Availability** không thể đạt được cùng nhau.

#### Phân loại
Cho nên, Định lý CAP phân loại các hệ thống thành 2 loại khác nhau:
* **CP (Consistent and Partition Tolerant):** Đợi một respone từ node được phân đoạn có thể gây ra timeout error. **CP** là một lựa chọn tốt nếu bạn cần một ứng dụng đọc ghi guyên tử.
* **AP (Availability and Partition Tolerant):** Respone trả vè phiên bản gần nhất của dữ liệu có trên node đó, có thể không phải là mới nhất. Việc ghi có thể tốn một lượng thời gian cho việc lan truyền. **AP** sẽ tốt đối với các ứng dụng cần `eventual consistency` hoặc các ứng dụng yêu cầu hoạt động mặc cho có lỗi từ ngoài.

**Định lý CAP** thể hiện được sự **không hoàn hảo** của mọi hệ thống. Để lựa chọn được một cặp tính chất phải đánh đổi một cặp tính chất quan trọng khác.

#### Các mẫu nhất quán (Consistency patterns)
* **Weak consistency (Nhất quán yếu)**
* **Eventual consistency (Nhất quán đến cuối cùng)**
* **Strong consistency (Nhất quán mạnh)**

### 2. Eventual consistency
Đây là một dạng đặc biệt trong **Weak consistency**. Hệ thống đảm bảo rằng nếu không có thêm cập nhật nào nữa, thì cuối cùng tất cả các truy nhập sẽ trả về giá trị cập nhật cuối cùng. Khi sử dụng nhiều bản sao, có một write request đến một bản sao (insert, update, delete) thì chúng phải làm sao cho các bản sao khác cũng nhận được request tương ứng. Việc đồng bộ giữa các bản sao có thể một ít thời gian, trong thời gian các bản sao động bộ khi có một read request đến thì sẽ nhận được kết quả trả về cũ hơn.

Có thể hiểu như khi bạn đăng một post lên facebook thì bài post sẽ được đăng ngay lập tức. Bạn có thể thấy bài post được đăng. Nhưng người khác có thể mất vài giây để thấy bài đăng của bạn.

Hệ thống phổ biến dùng mô hình này là **DNS (Domain Name System)**. Các cập nhật đối với tên miền được phân tán dựa vào mẫu đã được cấu hình trước và kếp với với cache. Cuối cùng mọi khách hàng đều nhìn thấy cập nhật.