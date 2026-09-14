package fpt.linhlt.order_management_service.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import fpt.linhlt.order_management_service.dto.request.AuthenticationRequest;
import fpt.linhlt.order_management_service.dto.request.LogoutRequest;
import fpt.linhlt.order_management_service.dto.response.AuthenticationResponse;
import fpt.linhlt.order_management_service.entity.InvalidatedToken;
import fpt.linhlt.order_management_service.entity.User;
import fpt.linhlt.order_management_service.exception.AppException;
import fpt.linhlt.order_management_service.exception.ErrorCode;
import fpt.linhlt.order_management_service.repository.InvalidatedTokenRepository;
import fpt.linhlt.order_management_service.repository.UserRepository;
import fpt.linhlt.order_management_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

        UserRepository userRepository;
        PasswordEncoder passwordEncoder;
        InvalidatedTokenRepository tokenRepository;

        @NonFinal
        @Value("${app.jwt.secret}")
        protected String SIGNER_KEY;

        @Override
        public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
                User user = userRepository.findByEmail(authenticationRequest.getEmail());
                if (user == null) {
                        throw new AppException(ErrorCode.EMAIL_EXISTED);
                }
                boolean result = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPasswordHash());
                if (!result) {
                        throw new AppException(ErrorCode.UNAUTHENTICATED);
                }
                return AuthenticationResponse.builder()
                        .token(generationToken(user))
                        .authenticated(true)
                        .build();
        }


        @Override
        public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
                var signToken = verifyToken(logoutRequest.getToken());
                String jit = signToken.getJWTClaimsSet().getJWTID();
                Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
                InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                        .id(jit)
                        .expiryTime(expiryTime)
                        .build();
                tokenRepository.save(invalidatedToken);
        }

        private SignedJWT verifyToken(String token) throws ParseException, JOSEException {
                SignedJWT signedJWT = SignedJWT.parse(token);
                JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
                var verified = signedJWT.verify(verifier);
                Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
                if(!(verified && expiration.after(new Date()))) {
                        throw new AppException(ErrorCode.UNAUTHENTICATED);
                }
                if (tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
                        throw new AppException(ErrorCode.UNAUTHENTICATED);
                }
                return signedJWT;
        }

        private String generationToken(User userEntity) {
                JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
                JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                        .subject(userEntity.getFullName())
                        .issuer("linhlt.fpt")
                        .issueTime(new Date())
                        .expirationTime(new Date(
                                Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                        ))
                        .claim("scope", buildScope(userEntity))
                        .claim("userId", userEntity.getId())
                        .jwtID(UUID.randomUUID().toString())
                        .build();
                Payload payload = new Payload(jwtClaimsSet.toJSONObject());
                JWSObject jwsObject = new JWSObject(jwsHeader, payload);
                try {
                        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
                        return jwsObject.serialize();
                } catch (JOSEException e) {
                        throw new RuntimeException(e);
                }
        }

        private String buildScope(User userEntity) {
                StringJoiner scopeJoiner = new StringJoiner(" ");
                if (userEntity.getRole() != null) {
                        scopeJoiner.add("ROLE_" + userEntity.getRole().getCode());
                }
                return scopeJoiner.toString();
        }


}