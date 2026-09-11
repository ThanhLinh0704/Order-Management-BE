package fpt.linhlt.order_management_service.service.impl;

import fpt.linhlt.order_management_service.dto.request.LoginRequest;
import fpt.linhlt.order_management_service.dto.response.LoginResponse;
import fpt.linhlt.order_management_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final AuthenticationManager authenticationManager;
        private final JwtEncoder jwtEncoder;

        @Override
        public LoginResponse login(LoginRequest request) {
                Authentication authentication = authenticationManager.authenticate(
                                UsernamePasswordAuthenticationToken.unauthenticated(
                                                request.getEmail(),
                                                request.getPassword()));

                List<String> roles = new ArrayList<>();
                for (GrantedAuthority authority : authentication.getAuthorities()) {
                        String role = authority.getAuthority();
                        if (role.startsWith("ROLE_")) {
                                roles.add(role.substring(5));
                        }
                }

                JwtClaimsSet claims = JwtClaimsSet.builder()
                        .subject(authentication.getName())
                        .claim("roles", roles)
                        .build();

                JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
                String token = jwtEncoder.encode(
                        JwtEncoderParameters.from(header, claims)
                ).getTokenValue();
                return new LoginResponse(token);

        }
}