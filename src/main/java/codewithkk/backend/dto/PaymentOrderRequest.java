package codewithkk.backend.dto;

import lombok.Data;

@Data
public class PaymentOrderRequest {
    private double amount;
    private String currency;
}
