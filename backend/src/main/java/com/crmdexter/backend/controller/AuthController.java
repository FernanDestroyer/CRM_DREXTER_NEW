package com.crmdexter.backend.controller;

import com.crmdexter.backend.dto.AuthDto;
import com.crmdexter.backend.model.Usuario;
import com.crmdexter.backend.repository.UsuarioRepository;
import com.crmdexter.backend.service.Auth.LoginService;

import java.time.Duration;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String COOKIE_NAME = "crm_dexter_token";

    private final LoginService loginService;
    private final UsuarioRepository usuarioRepository;
    private final boolean cookieSecure;

    public AuthController(
            LoginService loginService,
            UsuarioRepository usuarioRepository,
            @Value("${app.cookie.secure:true}") boolean cookieSecure
    ) {
        this.loginService = loginService;
        this.usuarioRepository = usuarioRepository;
        this.cookieSecure = cookieSecure;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto.LoginResponse> login(
            @RequestBody AuthDto.LoginRequest request
    ) {
        AuthDto.LoginResponse response =
                loginService.login(request.getEmail());

        return withSessionCookie(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthDto.LoginResponse> verifyOtp(
            @RequestBody AuthDto.OtpRequest request
    ) {
        AuthDto.LoginResponse response =
                loginService.verifyOtp(
                    request.getEmail(),
                    request.getOtp()
                );

        return withSessionCookie(response);
    }

    @GetMapping("/me")
    public UsuarioResponse me(Authentication authentication) {
        if (!isAuthenticated(authentication)) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Sesión no autenticada"
            );
        }

        Usuario usuario = usuarioRepository
            .findByEmail(authentication.getName())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Usuario no encontrado"
            ));

        return UsuarioResponse.from(usuario);
    }

    @GetMapping("/session")
    public SessionResponse session(Authentication authentication) {
        return new SessionResponse(isAuthenticated(authentication));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = createCookie("", Duration.ZERO);

        return ResponseEntity
            .noContent()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build();
    }

    private ResponseEntity<AuthDto.LoginResponse> withSessionCookie(
            AuthDto.LoginResponse response
    ) {
        boolean authenticated =
            "AUTHENTICATED".equals(response.getStatus())
            && response.getToken() != null
            && !response.getToken().isBlank();

        if (!authenticated) {
            return ResponseEntity.ok(response);
        }

        ResponseCookie cookie = createCookie(
            response.getToken(),
            Duration.ofHours(24)
        );

        // El JWT permanece únicamente en la cookie HttpOnly.
        response.setToken(null);

        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(response);
    }

    private ResponseCookie createCookie(
        String value,
        Duration maxAge
) {
    return ResponseCookie
        .from(COOKIE_NAME, value)
        .httpOnly(true)
        .secure(cookieSecure)
        .sameSite(cookieSecure ? "None" : "Lax")
        .partitioned(cookieSecure)
        .path("/")
        .maxAge(maxAge)
        .build();
}

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null
            && authentication.isAuthenticated()
            && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public record UsuarioResponse(
            String id,
            String email,
            String nombre,
            String rol,
            String estado
    ) {
        static UsuarioResponse from(Usuario usuario) {
            return new UsuarioResponse(
                usuario.getId().toString(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getRol().toLowerCase(Locale.ROOT),
                usuario.getEstado().toLowerCase(Locale.ROOT)
            );
        }
    }

    public record SessionResponse(boolean authenticated) {
    }
}
