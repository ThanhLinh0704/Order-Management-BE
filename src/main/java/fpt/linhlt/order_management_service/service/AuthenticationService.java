package fpt.linhlt.order_management_service.service;

import com.nimbusds.jose.JOSEException;
import fpt.linhlt.order_management_service.dto.request.AuthenticationRequest;
import fpt.linhlt.order_management_service.dto.request.LogoutRequest;
import fpt.linhlt.order_management_service.dto.response.AuthenticationResponse;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
    void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;
}