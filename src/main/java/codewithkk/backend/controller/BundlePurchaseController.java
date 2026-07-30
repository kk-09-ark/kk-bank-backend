package codewithkk.backend.controller;

import codewithkk.backend.entity.BundlePurchase;
import codewithkk.backend.service.BundlePurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/bundle")
@CrossOrigin(origins = "*")
public class BundlePurchaseController {

    @Autowired
    private BundlePurchaseService bundlePurchaseService;

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
}