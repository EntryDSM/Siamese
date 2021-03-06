package kr.hs.entrydsm.controller;

import kr.hs.entrydsm.payload.request.LoginRequest;
import kr.hs.entrydsm.payload.response.ClubResponse;
import kr.hs.entrydsm.payload.response.LoginResponse;
import kr.hs.entrydsm.payload.response.RefreshResponse;
import kr.hs.entrydsm.security.JWTRequired;
import kr.hs.entrydsm.security.RefreshRequired;
import kr.hs.entrydsm.service.club.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/club")
public class ClubController {

    private final ClubService clubService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return clubService.login(loginRequest);
    }

    @RefreshRequired
    @PutMapping("/refresh")
    public RefreshResponse refresh() {
        return clubService.refresh();
    }

    @JWTRequired
    @GetMapping
    public ClubResponse getClub() {
        return clubService.getClub();
    }
}



