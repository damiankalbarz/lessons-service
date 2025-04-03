package com.example.lessons_service.auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final UserRepository userRepository;


    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + principal.getName()));

        UserResponseDTO userResponse = new UserResponseDTO(
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPhoneNumber()
        );

        return ResponseEntity.ok(userResponse);
    }


    // Update user profile (like firstname, lastname, phone number)
    @PutMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody UpdateUserRequest updateRequest, Principal principal) {
        User updatedUser = service.updateUser(updateRequest, principal);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}