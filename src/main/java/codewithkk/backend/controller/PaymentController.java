package codewithkk.backend.controller;

import codewithkk.backend.dto.PaymentOrderResponse;
import codewithkk.backend.dto.VerifyPaymentRequest;
import codewithkk.backend.entity.BundlePurchase;
import codewithkk.backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<PaymentOrderResponse> createOrder(@RequestBody java.util.Map<String, Object> body) {
        double amount = Double.parseDouble(body.getOrDefault("amount", 22).toString());
        String currency = (String) body.getOrDefault("currency", "INR");
        return ResponseEntity.ok(paymentService.createOrder(amount, currency));
    }

    @PostMapping("/verify")
    public ResponseEntity<BundlePurchase> verifyPayment(@RequestBody VerifyPaymentRequest request) {
        BundlePurchase purchase = paymentService.verifyPayment(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature(),
                request.getUserId(),
                request.getAmount()
        );
        return ResponseEntity.ok(purchase);
    }
}
