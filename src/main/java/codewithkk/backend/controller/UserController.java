package codewithkk.backend.controller;

import codewithkk.backend.dto.UserProfileResponse;
import codewithkk.backend.entity.User;
import codewithkk.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<User> updateProfile(@PathVariable String userId, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(userId, user));
    }
}
