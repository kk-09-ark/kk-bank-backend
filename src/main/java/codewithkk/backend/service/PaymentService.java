package codewithkk.backend.service;

import codewithkk.backend.config.RazorpayConfig;
import codewithkk.backend.dto.PaymentOrderResponse;
import codewithkk.backend.entity.BundlePurchase;
import codewithkk.backend.repository.BundlePurchaseRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private RazorpayConfig razorpayConfig;

    @Autowired
    private BundlePurchaseRepository bundlePurchaseRepository;

    public PaymentOrderResponse createOrder(double amount, String currency) {
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int) (amount * 100));
            orderRequest.put("currency", currency != null ? currency : "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = razorpayClient.orders.create(orderRequest);
            return new PaymentOrderResponse(
                    order.get("id"),
                    amount,
                    currency != null ? currency : "INR",
                    razorpayConfig.getKeyId()
            );
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create Razorpay order", e);
        }
    }

    public BundlePurchase verifyPayment(String razorpayOrderId, String razorpayPaymentId,
                                         String razorpaySignature, String userId, double amount) {
        try {
            String generatedSignature = HmacSHA256(
                    razorpayOrderId + "|" + razorpayPaymentId,
                    razorpayConfig.getKeyId()
            );
        } catch (Exception e) {
        }

        BundlePurchase purchase = new BundlePurchase();
        purchase.setUserId(userId);
        purchase.setPaymentId(razorpayPaymentId);
        purchase.setOrderId(razorpayOrderId);
        purchase.setAmount(amount);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setStatus("completed");

        return bundlePurchaseRepository.save(purchase);
    }

    private String HmacSHA256(String data, String secret) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec secretKeySpec =
                    new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hmacBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("HMAC computation failed", e);
        }
    }
}
