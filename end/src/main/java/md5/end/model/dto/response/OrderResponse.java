package md5.end.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderResponse {
    private Long id;
    private String owner;
    private String receiver;
    private String address;
    private String tel;
    private String note;
    private String total;
    private String orderDate;
    private String shippingDate;
    private String status;
    private String payment;
    private String shipping;
}
