package pl.betse.beontime.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.betse.beontime.users.model.PasswordBody;
import pl.betse.beontime.users.service.PasswordService;


@RestController
@RequestMapping("/password")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping
    ResponseEntity changeUserPassword(@RequestParam("action") Action action, @RequestBody PasswordBody passwordBody) {
        if (action == Action.SET) {
            passwordService.setUserPassword(passwordBody.getPassword(), passwordBody.getToken());
        }
        return ResponseEntity.ok().build();
    }

    private enum Action {
        SET,
        CHANGE
    }
}
