package com.practice.localiza.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.practice.localiza.entity.User;
import com.practice.localiza.config.JwtServiceGenerator;

@Service
public class LoginService {

	@Autowired
	private LoginRepository repository;
	@Autowired
	private JwtServiceGenerator jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;


    public String logar(Login login) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login.getUsername(),
                            login.getPassword()
                    )
            );
            User user = repository.findByUsername(login.getUsername()).get();
            String jwtToken = jwtService.generateToken(user);
            return jwtToken;
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Credenciais inválidas.");
        }
    }
}
