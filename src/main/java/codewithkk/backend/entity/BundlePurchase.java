package codewithkk.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bundle_purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BundlePurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    private String paymentId;

    private String orderId;

    private double amount;

    private LocalDateTime purchaseDate;

    private String status;
}
