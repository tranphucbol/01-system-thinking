
# Mục lục

- [Mục lục](#m%E1%BB%A5c-l%E1%BB%A5c)
- [1. Định lý CAP](#1-%C4%91%E1%BB%8Bnh-l%C3%BD-cap)
  - [Giới thiệu](#gi%E1%BB%9Bi-thi%E1%BB%87u)
  - [Phát biểu định lý](#ph%C3%A1t-bi%E1%BB%83u-%C4%91%E1%BB%8Bnh-l%C3%BD)
  - [Phân loại](#ph%C3%A2n-lo%E1%BA%A1i)
  - [Các mẫu nhất quán (Consistency patterns)](#c%C3%A1c-m%E1%BA%ABu-nh%E1%BA%A5t-qu%C3%A1n-consistency-patterns)
- [2. Eventual consistency](#2-eventual-consistency)
- [3. Throughput và Latency](#3-throughput-v%C3%A0-latency)
  - [Định nghĩa](#%C4%91%E1%BB%8Bnh-ngh%C4%A9a)
- [4. Scale Database](#4-scale-database)
  - [Replication](#replication)
    - [Master-salve replication](#master-salve-replication)
    - [Master-master replication](#master-master-replication)
  - [Sharding](#sharding)
- [5. Task Queue khác gì Message Queue?](#5-task-queue-kh%C3%A1c-g%C3%AC-message-queue)
  - [Message Queue](#message-queue)
  - [Task Queue](#task-queue)
  - [Sự khác nhau](#s%E1%BB%B1-kh%C3%A1c-nhau)
- [6. Load Balancer](#6-load-balancer)
- [7. Kiến trúc bên trong **nginx**](#7-ki%E1%BA%BFn-tr%C3%BAc-b%C3%AAn-trong-nginx)
  - [NGINX là gì?](#nginx-l%C3%A0-g%C3%AC)
  - [Vì sao nginx dùng single thread?](#v%C3%AC-sao-nginx-d%C3%B9ng-single-thread)
- [8. Caching](#8-caching)
  - [Vai trò của cache](#vai-tr%C3%B2-c%E1%BB%A7a-cache)
  - [Các thuật toán apply cho cache](#c%C3%A1c-thu%E1%BA%ADt-to%C3%A1n-apply-cho-cache)
    - [Least reccently used (LRU)](#least-reccently-used-lru)
    - [Least frequent used (LFU)](#least-frequent-used-lfu)
- [9. Redis](#9-redis)
  - [Kiến trúc của Redis](#ki%E1%BA%BFn-tr%C3%BAc-c%E1%BB%A7a-redis)
  - [Các kiểu dữ liệu của Redis](#c%C3%A1c-ki%E1%BB%83u-d%E1%BB%AF-li%E1%BB%87u-c%E1%BB%A7a-redis)
  - [Khi nào dùng cấu trúc hyperloglog](#khi-n%C3%A0o-d%C3%B9ng-c%E1%BA%A5u-tr%C3%BAc-hyperloglog)
  - [Cách đặt tên cho key](#c%C3%A1ch-%C4%91%E1%BA%B7t-t%C3%AAn-cho-key)
- [Reference](#reference)

# 1. Định lý CAP

## Giới thiệu

Trong một thế giới hoàn hảo, sẽ chỉ có một mô hình nhất quán dữ liệu: khi có một cập nhật xảy ra thì tất cả người đọc đều nhìn thấy cập nhật đó. Vào những năm 70, nhiều kĩ thuật đang cố gắng đạt được sự trong suốt về phân tán - tức là người dùng chỉ nhìn thấy một hệ thống duy nhát thay vì nhiều hệ thống cộng tác với nhau. Nhiều hệ thống trong thời kì này đi theo cách tiếp cận là thà đánh hỏng toàn bộ hệ thống chứ không phá vỡ tính trong suốt.

Vào thời điểm giữa những năm 90, với sự ra đời của các hệ thống Internet lớn hơn. Người ta bắt đầu xem xét lại những các làm trên. Ở thời điểm hiện tại, tính sẵn sàng của hệ thống là tài sản quý giá nhất. **Định lý CAP** là một lý thuyết trong khoa học máy tính dược phát biểu bởi giáo sư **Eric Allen Brewer** của trường Đại Học California thuộc Berkeley. Ông đồng thời cũng là chủ tịch danh dự mảng hạ tầng của Goolge.

## Phát biểu định lý

**Định lý CAP** nêu ra ba yếu tố của hệ thống chia sẻ dữ liệu.

- **Consistency** (tính nhất quán): mỗi lần đọc dữ liệu sẽ nhận được nội dung mới nhất hoặc lỗi
- **Availability** (tính sẵn sàng): mỗi một resquest sẽ được một reponse không phải lỗi, nhưng không đảm bảo là lưu trữ mới nhất
- **Partition Tolerance** (tính chịu đựng phân mảnh): Hệ thống tiếp tục hoạt động bất chấp lượng tùy ý các thông điệp / gói tin bị mất hoặc trì hoãn do trục trặc giữ các nút

Một hệ thống lưu trữ phân tán chỉ có thể đồng thời đảm bảo 2 trong 3 yếu tố trên. Tuy nhiên, một hệ thống không chịu đựng phân đoạn mạng có thể đạt được **Consitency** và **Availability** và thường thực hiện bằng các giao thức nhân bản. Để đạt được điều này, hệ thống client và hệ thống lưu trữ phải ở trong cùng một môi trường. Để đạt được điều này, hệ thống client và hệ thống lưu trữ phải ở trong cùng một môi trường, trong một số tình huống nhất định chúng sẽ suy sụp cùng nhau, và vì vậu client không nhận ra phân đoạn. Một nhận xét quan trọng là trong các hệ thống phân tán ở qui mô lớn thì việc mạng bị phân đoạn là một thực tế, do đó **Consistency** và **Availability** không thể đạt được cùng nhau.

## Phân loại

Cho nên, Định lý CAP phân loại các hệ thống thành 2 loại khác nhau:

- **CP (Consistent and Partition Tolerant):** Đợi một respone từ node được phân đoạn có thể gây ra timeout error. **CP** là một lựa chọn tốt nếu bạn cần một ứng dụng đọc ghi guyên tử.
- **AP (Availability and Partition Tolerant):** Respone trả vè phiên bản gần nhất của dữ liệu có trên node đó, có thể không phải là mới nhất. Việc ghi có thể tốn một lượng thời gian cho việc lan truyền. **AP** sẽ tốt đối với các ứng dụng cần `eventual consistency` hoặc các ứng dụng yêu cầu hoạt động mặc cho có lỗi từ ngoài.

**Định lý CAP** thể hiện được sự **không hoàn hảo** của mọi hệ thống. Để lựa chọn được một cặp tính chất phải đánh đổi một cặp tính chất quan trọng khác.

## Các mẫu nhất quán (Consistency patterns)

- **Weak consistency (Nhất quán yếu)**
- **Eventual consistency (Nhất quán đến cuối cùng)**
- **Strong consistency (Nhất quán mạnh)**

# 2. Eventual consistency

Đây là một dạng đặc biệt trong **Weak consistency**. Hệ thống đảm bảo rằng nếu không có thêm cập nhật nào nữa, thì cuối cùng tất cả các truy nhập sẽ trả về giá trị cập nhật cuối cùng. Khi sử dụng nhiều bản sao, có một write request đến một bản sao (insert, update, delete) thì chúng phải làm sao cho các bản sao khác cũng nhận được request tương ứng. Việc đồng bộ giữa các bản sao có thể một ít thời gian, trong thời gian các bản sao động bộ khi có một read request đến thì sẽ nhận được kết quả trả về cũ hơn.

Có thể hiểu như khi bạn đăng một post lên facebook thì bài post sẽ được đăng ngay lập tức. Bạn có thể thấy bài post được đăng. Nhưng người khác có thể mất vài giây để thấy bài đăng của bạn.

Hệ thống phổ biến dùng mô hình này là **DNS (Domain Name System)**. Các cập nhật đối với tên miền được phân tán dựa vào mẫu đã được cấu hình trước và kếp với với cache. Cuối cùng mọi khách hàng đều nhìn thấy cập nhật.

# 3. Throughput và Latency

## Định nghĩa

**Throughput** (Thông lượng): là số hànScale Databaseh động được thực hiện hay số kết quả được đưa ra trong một đơn vị thời gian.Scale Database
Scale Database
**Latency** (Độ trễ): là thời gian để tScale Databasehực hiện một số hành động hoặc để tạo ra một số kết quả.

# 4. Scale Database

## Replication

Replication (nhân bản), là một phiên bản giống hệt database đang tồn tại. Điều hữu ích là ta có thể phân tích các bản sao mà không sử dụng đến cơ sở dữ liệu chính, hoặc chỉ đơn giản là một phương tiện để mở rộng ra.

### Master-salve replication

Server master phục vụ đọc và ghi, nhân bản các dữ liệu được ghi ra slave - nơi mà dữ liệu chỉ được đọc. Các slave có thể nhân bả ra các slave khác (dạng cây). Nếu master sập, hệ thống sẽ ở trạng thái chỉ đọc cho đến khi một slave được thăng lên làm master hoặc master được khôi phục.

### Master-master replication

Tất cả server đều hỗ trợ đọc ghi, các server là ngang hàng về việc ghi. Nếu một master sập, hệ thống vẫn tiếp tục đọc ghi. Tuy nhiên, vấn đề có nhiều master, sẽ dẫn đến sự thiếu nhất quán trong việc ghi và đồng bộ giữa các server. Giải quyết xung đột sẽ càng phức tạp nếu thêm server ghi.

## Sharding

**Sharding** phân tán dữ liệu xuyên suốt các database khác nhau như mỗi database giữa mỗi database giữa một phần dữ liệu. Đối với một hệ thống có dữ liệu lên đến hàng triệu dòng, việc query dữ liệu trở nên vô cùng chậm chập. Việc chia nhỏ bảng, làm giảm số lượng index, giúp gia tăng tốc độ truy vấn.

![](/img/sharding.jpg)

# 5. Task Queue khác gì Message Queue?

## Message Queue

Message queue nhận, giữ, và vận chuyển tin nhắn. Nếu một tác vụ mất nhiều thời gian để xử lý một các đồng bộ, bạn có thể dùng một message queue với luồng công việc. Ứng dụng sẽ publish các tác vụ và các worker sẽ pick up làm các tác vụ đó. Message Queue là lưu trữ message tuần tự. 

Trong môi trường phân tán, sẽ có nhiều ứng dụng xử lý song song trên cufngm ột message queue. Khi đó message vào trước và ra trước nhưng khong chắc chắn là sẽ được xử lý xong trước tiên. Vì ngay khi message được dequeue và message kế tiếp cũng được dequeue ra ngay lập tức. Neus quá tình xử lý có message thứ nhất mà lâu hơn message thứ hai, thì thứ tự xử lý sẽ không còn như mong muốn. Giả sử có hai message tương ứng xảy ra lần lượt là: Xác nhận đơn hàng và hủy đơn hàng. Các message này được gửi vào queue. Messsage hủy được xử lý xong trước, lúc này message xác nhận đơn hàng mới được xử lý do phải làm việc lâu hơn, thì xảy ra lỗi do đơn hàng đã bị hủy.

## Task Queue

Các Task queue nhận các task và những dữ liệu liên quan, chạy chúng, sau đó chuyển đi các kết quả của chúng. Task queue hỗ trợ lập lịch, sắp xếp các công việc có liên quan vào cùng một chỗ để thực hiện chung, có thể chạy các công việc đòi hỏi tính toán nhiều ở trong nền.

## Sự khác nhau

Có thể thấy rằng các **Message Queue** sẽ xử lý các message tuần tự đã được đẩy vào. **Task Queue** sẽ nhận các task và dử liệu liên quan, sau đó sẽ lập lịch và xử lý các công việc giống nhau hoặc gần giống nhau cùng một lúc.

# 6. Load Balancer
BAc
**Load balancer** là một phương pháp phân phối khối lượng tải trên nhiều máy tính để có thể sử dụng tối ưu các nguồn lực, tối đa hóa thông lượng, giảm thời gian đáp ứng và tránh tình trạng quá tải trên máy chủ.

**Các lợi ích khi sử dụng phương pháp cân bằng tải:**

- Tăng khả năng đap ứng, tình trạng quá tải trên máy chủ.
- Tăng độ tinh cậy và khả năng dự phòng cho hệ thống.
- Tăng tính bảo mật cho hệ thống.

# 7. Kiến trúc bên trong **nginx**

## NGINX là gì?

**NGINX** là một web server mạnh mẽ và sử dig **kiến trúc đơn luồng**, hướng sự kiện (event-driven), không đồng bộ (asynchronous) và có khả năng mở rộng. Ngay cả khi bạn không cần phải xử hý hàng ngàn request truy vấn đống thời. Các tính năng mạnh mẽ của **NGINX**: load balancing, reverse proxy, ...

**NGINX** có một `master process` (thực hiện các hoạt động đặc quyền như độc cấu hình và liên kết với các cổng) và một số worker và các process trợ giúp.

## Vì sao nginx dùng single thread?

Các phổ biến để thiết kể một ứng dụng mạng là gán một thread hoặc một process cho mỗi connection. Cách thiết kể này đơn giản và dễ cài đặt, nhưng nó không thể scale khi ứng dụng phải xử lý hàng ngàn kết nối đồng thời.

Cách tiếp cận **process-per-connection** hay **thread-per-connection**, thì các connection khi không có bất cứ một events nào xảy ra, thì connection sẽ bị blocking, dẫn đến hao phí tài nguyên, hệ thốBAcng vẫn tốn chi phí cho việc **context switch**. Còn trong **NGINX** mỗi **worker process** sẽ tương đương với một CPU core. Một worker sẽ không bao giờ bBAcị block trên network traffic, chờ cho client phản hồi. Khi một client phản hồi, sau khi xử lý xong phản hồi nó sẽ chuyển ngay sang client khác đang chờBAc được xử lý, hoặc tiếp nhận một connection của client khác.
BAc
Việc sử dụng **single thread** là để tránh các BAcvấn đề trong **multiprocess** và **multithread** đang gặp phải là blocking và context switch, khiến cho hệ thống chậm và khó scale hơn khi mỗi kết nối lạiBAc ứng với thread.

# 8. Caching

## Vai trò của cache

**Caching** cải thiện thời gian tải trang và giảm thiểu việc phải tương tác với database của server. Cở sở dữ liệu thường hoạt động tốt nhờ việc phân phối đồng đề các lần đọc ghi trên các phân vùng của nó. Các item phổ biến sẽ làm mất cân bằng phân phối, gây nghẽn cổ chai. Đưa một bộ nhớ cache ở phía trước cơ sở dữ liệu có thể giải quyết vấn đề tải không đều và sự tăng đột biến lượng truy cập.

Có thể thấy được tác dụng lớn của cache. Giả sử mỗi giây ta nhận được 100 requesr, mỗi request sẽ mất 1s để chờ query database. Database sẽ dễ dàng bị quá tải. Sử dụng cache để lưu lại kết quả của câu query, lúc này dữ liệu được lưu trong RAM, thời gian truy xuất chỉ còn 50-100ms, không cần phải truy cập vào database. Hệ thống được giảm tải, còn người dùng lại nhận được kết quả nhanh hơn rất nhiều.

## Các thuật toán apply cho cache

### Least reccently used (LRU)

Loại bỏ items lâu nhất chưa được sử dụng trong quá khứ.

### Least frequent used (LFU)

Thuật toán sẽ đếm các items nào thường xuyên được dùng nhất. Những items được sử dụng ít nhất thường được loại bỏ trước tiên.

# 9. Redis

## Kiến trúc của Redis

**Redis** (REmote DIctionary Server) là cơ sở dự liệu **NoSQL**, lưu trữ dữ liệu với dạng **KEY-VALUE**. Lưu trữ **KEY-VALUE** là một hệ thống lưu trữ dữ liệu dưới dạng cặp khóa và giá trị. Redis sẽ lưu dữ liệu theo dạng KEY-VALUE trong RAM. Vì tủy xuất dữ liệu trên RAM, nên tốc độ query dữ liệu của Redis là cực kỳ nhanh, có thể lên đến 110000 lệnh SETs mỗi giây, 81000 GETs mỗi giây. Dù là **NoSQL** database, nhưng các operation ở Redis vẫn có tính chất **atomic**, tức là nếu có từ clients trở lên cùng vào database, Redis server sẽ luôn cập nhật giá trị data mỗi khi có thay đổi. Redis có thể được dùng trong nhiều trường hợp khác nhau: caching, message-queue,...

## Các kiểu dữ liệu của Redis

- **STRING:** string, integer hoặc float. Redis có thể làm việc với cả string, từng phần của string, cũng như tăng/giảm gía trị của integer, float.
- **LIST:** danh sách liên kết của strings. Redis hỗ trợ các thao tác push, pop từ của 2 phía của list, trim dựa theo offset, đọc 1 hoặc nhiều items của list, tìm kiếm và xóa giá trị.
- **SET:** tập hợp các string (không được sắp xếp). Redis hỗ trợ các thao tác thêm, đọc, xóa từng phần tử, kiểm tra sự xuất hiện của phần tử trong tập hợp. Ngoài ra Redis còn hỗ trợ các phép toán tập hợp, gồm intersect/union/diffrence.
- **HASH:** lưu trữ hash table của các cặp key-value, trong đó key được sắp xếp ngẫu nhiên, không theo thứ tự nào cả. Redis hỗ trợ các thao tác thêm, đọc, xóa từng phàn tử, cũng như đọc tất cả giá trị.
- **ZSET (sorted set):** là 1 danh sách, trong đó mỗi phần tử là map của 1 string(member) và 1 floating-point number (score), danh sách được sắp xếp theo score này. Redis hỗ trợ thao tác thêm, đọc, xóa từng phần tử, lấy ra các phần tử dựa theo range của score hoặc string.

## Khi nào dùng cấu trúc hyperloglog

**Hyperloglog** là một cấu trúc dữ liệu xác xuất để ước tính các thành phần duy nhất trong một tập dữ liệu. Khi cần ước tính các thành phần duy nhất của dữ liệu thì **HyperLogLog** lựa chọn bởi sự tối ưu bộ nhớ khi thực hiện. Khi một tập dữ liệu rất lớn thì việc lưu trữ để đếm theo cách thông thường là bất khả thi. Trong trường hợp xấu nhất **Redis** sử dụng **12KB**.
 
## Cách đặt tên cho key

- **Key không được quá dài.** Nếu key quá dài sẽ rất khó để nhớ và sẽ tốn nhiều chi phí trong việc tìm kiếm hơn. Việc đặt key quá dài dẫn đến việc tiêu tốn tài nguyên
- **Key không được quá ngắn.** Nếu key như `u500frd`, có thể thay thế bằng `user:500:friend`. Cách sau sẽ dễ đọc howng và không gian được thêm vào so với cái trước cũng khá ít. Mặc dù key ngắn hơn rõ ràng sẽ tiêu tốn ít bộ nhớ hơn một chút, việc đặt key là phải tìm sự cân bằng phù hợp.
- **Cố gắng gắn bó với schema**. Chẳng hạn như `object-type:id`, `user-chat:123`.
- **Kích thước khóa tối đa được phép là 512MB**. Nhưng có lẽ chắc bao giờ phải dùng tới như vậy ^^.

# Reference

- [CAP theorem](https://medium.com/eway/database-101-p1-%C4%91%E1%BB%8Bnh-l%C3%BD-cap-7260adf8b02f)
- [Master-Salve Replication](https://viblo.asia/p/gioi-thieu-ve-mysql-replication-master-slave-bxjvZYwNkJZ)
- [Sharding](https://viblo.asia/p/shard-database-voi-activerecord-turntable-l0rvmx3kGyqA)
- [Task Queue](https://www.fullstackpython.com/task-queues.html)
- [Message Queue](https://techblog.vn/van-thu-tu-messge-trong-viec-xu-ly-bat-dong-bo-dua-tren-message-queue)
- [Nginx](https://www.nginx.com/blog/inside-nginx-how-we-designed-for-performance-scale/)
- [Thuật toán LRU và LFU](https://en.wikipedia.org/wiki/Cache_replacement_policies#Least-frequently_used_(LFU))
- [Redis](http://qnimate.com/overview-of-redis-architecture/)
- [Redis](https://medium.com/vunamhung/redis-l%C3%A0-g%C3%AC-t%C3%ACm-hi%E1%BB%83u-v%E1%BB%81-c%C6%A1-s%E1%BB%9F-d%E1%BB%AF-li%E1%BB%87u-redis-60dd267f53ad)
- [Redis-Key](https://redis.io/topics/data-types-intro)