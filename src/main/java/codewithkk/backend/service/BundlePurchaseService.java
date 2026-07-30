package codewithkk.backend.service;

import codewithkk.backend.entity.BundlePurchase;
import codewithkk.backend.repository.BundlePurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BundlePurchaseService {

    @Autowired
    private BundlePurchaseRepository bundlePurchaseRepository;

    public BundlePurchase savePurchase(BundlePurchase purchase) {
        return bundlePurchaseRepository.save(purchase);
    }

    public Optional<BundlePurchase> getPurchaseByUserId(String userId) {
        return bundlePurchaseRepository.findByUserId(userId);
    }

    public boolean hasPurchased(String userId) {
        return bundlePurchaseRepository.existsByUserId(userId);
    }
}