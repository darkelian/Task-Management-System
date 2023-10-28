package com.demo.demo.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.demo.demo.Dtos.LoginRequest;
import com.demo.demo.Dtos.StandardResponseDTO;
import com.demo.demo.Security.JwtGenerador;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtGenerador jwtGenerador;

    @PostMapping("/login")
    public ResponseEntity<StandardResponseDTO> authenticate(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Login request received: {}", loginRequest);
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generar JWT
            String token = jwtGenerador.generateToken(authentication);
            logger.info("Login successful. JWT generated: {}", token);
            return ResponseEntity.ok(new StandardResponseDTO().FullSuccess(token));
            
        } catch (BadCredentialsException e) {
            logger.warn("Login failed. Bad credentials: {}", loginRequest);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponseDTO().FailSuccess("Credenciales inválidas"));
        } catch (Exception e) {
            logger.error("Login error: {}", e.getMessage(), e); 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StandardResponseDTO().FailSuccess("Usuario no encontrado"));
        }
    }
}
