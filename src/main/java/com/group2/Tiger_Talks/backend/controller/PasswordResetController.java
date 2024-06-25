package com.group2.Tiger_Talks.backend.controller;

import com.group2.Tiger_Talks.backend.service.Authentication.PasswordResetService;
import com.group2.Tiger_Talks.backend.service.DTO.ForgotPasswordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.group2.Tiger_Talks.backend.model.Utils.CROSS_ORIGIN_HOST_NAME;

@RestController
@RequestMapping("/api/logIn")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @PostMapping("passwordReset/validateEmailExist")
    public ResponseEntity<String> validateEmailExist(@RequestParam("email") String email) {
        return passwordResetService.validateEmailExist(email)
                .map(err -> ResponseEntity.status(400).body(err))
                .orElseGet(() -> ResponseEntity.ok("Email exists and is valid."));
    }

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @PostMapping("passwordReset/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ForgotPasswordDTO passwordDTO) {
        return passwordResetService.resetPassword(passwordDTO)
                .map(err -> ResponseEntity.status(404).body(err))
                .orElseGet(() -> ResponseEntity.ok("Password was successfully reset"));
    }

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @PostMapping("passwordReset/sendToken")
    public ResponseEntity<String> sendToken(@RequestParam("email") String email) {
        return passwordResetService.createAndSendResetMail(email)
                .map(err -> ResponseEntity.status(400).body(err))
                .orElseGet(() -> ResponseEntity.ok("Email was sent successfully to " + email + " for resetting your password"));
    }

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @PostMapping("passwordReset/checkToken/{token}")
    public ResponseEntity<String> validateToken(@PathVariable("token") String token) {
        return passwordResetService.validateToken(token)
                .map(err -> ResponseEntity.status(404).body(err))
                .orElseGet(() -> ResponseEntity.ok("Token validated successfully"));
    }

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @PostMapping("passwordReset/verifySecurityAnswers")
    public ResponseEntity<String> verifySecurityAnswers(
            @RequestParam("email") String email,
            @RequestParam("answer1") String answer1,
            @RequestParam("answer2") String answer2,
            @RequestParam("answer3") String answer3) {
        return passwordResetService.verifySecurityAnswers(email, answer1, answer2, answer3)
                .map(err -> ResponseEntity.status(400).body(err))
                .orElseGet(() -> ResponseEntity.ok("Security questions verified successfully."));
    }

    @CrossOrigin(origins = CROSS_ORIGIN_HOST_NAME)
    @GetMapping("passwordReset/getSecurityQuestions")
    public ResponseEntity<String[]> getSecurityQuestions(@RequestParam("email") String email) {
        return ResponseEntity.ok(passwordResetService.getSecurityQuestions(email));
    }


}
