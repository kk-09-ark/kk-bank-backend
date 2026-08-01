package codewithkk.backend.controller;

import codewithkk.backend.entity.BundlePurchase;
import codewithkk.backend.entity.User;
import codewithkk.backend.repository.UserRepository;
import codewithkk.backend.service.BundlePurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bundle")
@CrossOrigin(origins = "*")
public class BundlePurchaseController {

    @Autowired
    private BundlePurchaseService bundlePurchaseService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/purchase")
    public BundlePurchase savePurchase(@RequestBody BundlePurchase purchase) {
        return bundlePurchaseService.savePurchase(purchase);
    }

    @GetMapping("/{userId}")
    public Optional<BundlePurchase> getPurchase(@PathVariable String userId) {
        return bundlePurchaseService.getPurchaseByUserId(userId);
    }

    @GetMapping("/check/{userId}")
    public boolean hasPurchased(@PathVariable String userId) {
        return bundlePurchaseService.hasPurchased(userId);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> myPurchase() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails userDetails)) {
            return ResponseEntity.status(401).build();
        }
        Optional<User> userOpt = userRepository.findByEmail(userDetails.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        Optional<BundlePurchase> purchase =
                bundlePurchaseService.getPurchaseByUserId(userOpt.get().getId());
        Map<String, Object> body = new HashMap<>();
        body.put("purchased", purchase.isPresent());
        body.put("purchase", purchase.orElse(null));
        return ResponseEntity.ok(body);
    }
}
