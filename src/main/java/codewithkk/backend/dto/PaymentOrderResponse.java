package codewithkk.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentOrderResponse {
    private String orderId;
    private double amount;
    private String currency;
    private String key;
}
