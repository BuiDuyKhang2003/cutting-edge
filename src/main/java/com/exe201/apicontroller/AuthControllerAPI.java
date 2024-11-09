package com.exe201.apicontroller;

import com.nimbusds.jose.JOSEException;
import com.exe201.config.LogoutServiceConfig;
import com.exe201.dto.AuthenticationRequest;
import com.exe201.dto.RegistrationRequest;
import com.exe201.exception.CustomSuccessHandler;
import com.exe201.exception.ExceptionResponse;
import com.exe201.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication required to use other resources.")
public class AuthControllerAPI {
    private final AuthService authService;

    private final LogoutServiceConfig logoutServiceConfig;

    @Operation(summary = "Register a new account", description = "To register a new account, all information must be filled out completely and cannot be left blank." + " Upon successful registration, a verification email will be sent to the user's Gmail account.")
    @ApiResponses(value = {@ApiResponse(responseCode = "202", description = "Successfully Registered", content = @Content(examples = @ExampleObject(value = """
            {
              "message": "Successfully Register",
              "http_status": 202,
              "timestamp": "05/29/2024 21:20:36",
              "data": "Please check your email for account verification."
            }
            """))), @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ExceptionResponse.class), examples = @ExampleObject(value = """
            }
            "http_status": 400,
            "time_stamp": "05/29/2024 00:44:00",
            "error": "Field 'email' is invalid",
            "data": {
                "email": "must be a well-formed email address",
                "password": "must be at least 8 characters long"
            }"""))),})
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException, UnsupportedEncodingException {
        authService.register(request);
        return CustomSuccessHandler.responseBuilder(HttpStatus.ACCEPTED, "Successfully Register", "Please check your email for account verification.");
    }

    @Operation(summary = "Login in to the system", description = "Login into the system requires all information to be provided, " + "and validations will be performed. The response will include an access token and a refresh token")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully SignIn", content = @Content(examples = @ExampleObject(value = """
                {
                   "http_status": 200,
                   "time_stamp": "10/29/2024 11:20:03",
                   "message": "Successfully SignIn",
                   "data": {
                     "access_token": "xxxx.yyyy.zzzz",
                     "refresh_token": "xxxx.yyyy.zzzz"
                }
            """))), @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(examples = @ExampleObject(value = """
                {
                 "http_status": 401,
                 "time_stamp": "05/29/2024 21:24:57",
                 "message": "Email or Password is incorrect"
               }
            """))), @ApiResponse(responseCode = "401", description = "Account Locked", content = @Content(examples = @ExampleObject(value = """
                {
                 "http_status": 401,
                 "time_stamp": "05/29/2024 21:24:57",
                 "message": "Account is locked please contact administrator for more information"
               }
            """))), @ApiResponse(responseCode = "401", description = "Account Disabled", content = @Content(examples = @ExampleObject(value = """
                {
                 "http_status": 401,
                 "time_stamp": "05/26/2024 21:24:57",
                 "message": "Account is disabled please contact administrator for more information"
               }
            """))),})
    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> signIn(@RequestBody @Valid AuthenticationRequest request) throws JOSEException {
        return CustomSuccessHandler.responseBuilder(HttpStatus.OK, "Successfully SignIn", authService.authenticate(request));
    }

    @Operation(summary = "Enter confirmation code", description = "After registering successfully, the user will need to enter the 6-digit confirmation code sent " + "to their email to activate the account.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Account verification successfully"), @ApiResponse(responseCode = "400", description = "Account Verification Failed"), @ApiResponse(responseCode = "400", description = "Your account is already activated"), @ApiResponse(responseCode = "400", description = "This activation code is invalid as it has been revoked. Please use the latest activation code sent to your email."), @ApiResponse(responseCode = "400", description = "Activation code has expired. A new code has been sent to your email address"),})
    @PostMapping("/activate-account")
    @ResponseStatus(HttpStatus.OK)
    public void accountConfirmation(@RequestParam String token, HttpServletResponse response) throws MessagingException, UnsupportedEncodingException {
        authService.activateAccount(token, response);
    }

    @Operation(summary = "Logout of the system", description = "Logout of the system, bearer token (refresh token) is required")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Logged out successfully"), @ApiResponse(responseCode = "401", description = "No JWT token found in the request header")})
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutServiceConfig.logout(request, response, authentication);
    }

    @Operation(summary = "Refresh token if expired", description = "If the current JWT Refresh Token has expired or been revoked, you can refresh it using this method")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Generate new Refresh Token and Access Token successfully", content = @Content(examples = @ExampleObject(value = """
                {
                 "access_token": "xxxx.yyyy.zzzz",
                 "refresh_token": "xxxx.yyyy.zzzz"
               }
            """))), @ApiResponse(responseCode = "401", description = "No JWT token found in the request header"), @ApiResponse(responseCode = "401", description = "JWT token has expired and revoked")})
    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, JOSEException {
        authService.refreshToken(request, response);
    }
}
