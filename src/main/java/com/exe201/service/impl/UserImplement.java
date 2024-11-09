package com.exe201.service.impl;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.exe201.model.Token;
import com.exe201.model.User;
import com.exe201.repository.RoleRepository;
import com.exe201.repository.TokenRepository;
import com.exe201.repository.UserRepository;
import com.exe201.dto.CustomUserProfile;
import com.exe201.dto.UserProfileDto;
import com.exe201.dto.UserProfileResponse;
import com.exe201.exception.CustomSuccessHandler;
import com.exe201.service.JwtService;
import com.exe201.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserImplement implements UserService {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<Object> getUserInformation(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No JWT token found in the request header");
        }

        final Token accessToken = tokenRepository.findByAccessToken(token).orElse(null);
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
        }

        JWTClaimsSet decryptedClaims;
        try {
            decryptedClaims = jwtService.decryptJwt(token);
        } catch (JOSEException | ParseException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to decrypt JWT token");
        }

        String username = decryptedClaims.getSubject();
        var user = userRepository.findByEmail(username).orElse(null);
        if (user == null || !jwtService.isEncryptedTokenValid(decryptedClaims, user) || accessToken.isRevoked() || accessToken.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token has expired or been revoked");
        }

        return CustomSuccessHandler.responseBuilder(HttpStatus.OK, "Successfully retrieved user information", user);
    }

    @Override
    public ResponseEntity<Object> getAllUsersByAdmin(int pageNo, int pageSize, String sortBy, String sortDir, String email) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<User> UserContent = userRepository.findByEmail(email, pageable);
        List<User> UserList = UserContent.getContent();
        List<CustomUserProfile> content = UserList.stream().map(UserProfile -> modelMapper.map(UserProfile, CustomUserProfile.class)).collect(Collectors.toList());

        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setContent(content);
        userProfileResponse.setPageNo(UserContent.getNumber());
        userProfileResponse.setPageSize(UserContent.getSize());
        userProfileResponse.setTotalElements(UserContent.getTotalElements());
        userProfileResponse.setTotalPages(UserContent.getTotalPages());
        userProfileResponse.setLast(UserContent.isLast());
        return CustomSuccessHandler.responseBuilder(HttpStatus.OK, "Successfully retrieved user information", userProfileResponse);
    }

    @Override
    public ResponseEntity<Object> updateUserProfileByAdmin(UUID id, CustomUserProfile customUserProfile) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {

            if (customUserProfile.getFirstName() != null) {
                user.setFirstName(customUserProfile.getFirstName());
            }
            if (customUserProfile.getLastName() != null) {
                user.setLastName(customUserProfile.getLastName());
            }
            if (customUserProfile.getAddress() != null) {
                user.setAddress(customUserProfile.getAddress());
            }
            if (customUserProfile.getEmail() != null) {
                user.setEmail(customUserProfile.getEmail());
            }
            if (customUserProfile.isAccountNotLocked() != user.isAccountNotLocked()) {
                user.setAccountNotLocked(customUserProfile.isAccountNotLocked());
            }

            if (customUserProfile.getGender() != null) {
                user.setGender(customUserProfile.getGender());
            }

            if (customUserProfile.getPhone() != null) {
                user.setPhone(customUserProfile.getPhone());
            }

            if (customUserProfile.getProfilePicture() != null) {
                user.setProfilePic(customUserProfile.getProfilePicture());
            }

            if (customUserProfile.getDob() != null) {
                user.setDob(customUserProfile.getDob());
            }

            if (customUserProfile.getRole()!=null){
                if (customUserProfile.getRole().equals("USER")) {
                    user.setRole(roleRepository.findById(1L).orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized")));
                }
            }

            User updateUser = userRepository.save(user);
            return CustomSuccessHandler.responseBuilder(HttpStatus.OK, "user profile update successfully", updateUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed");
        }
    }

    public String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    @Override
    public UserProfileDto updateUserInformation(UUID id, UserProfileDto userProfileDto) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setFirstName(userProfileDto.getFirstName());
            user.setLastName(userProfileDto.getLastName());
            user.setAddress(userProfileDto.getAddress());
            user.setGender(userProfileDto.getGender());
            user.setDob(userProfileDto.getDob());
            user.setPhone(userProfileDto.getPhone());
            User updateUser = userRepository.save(user);
            return mapToUserProfileDto(updateUser);
        } else {
            return null;
        }
    }

    @Override
    public void updateUserProfilePicture(UUID id, String profilePictureUrl) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setProfilePic(profilePictureUrl);
            User updatedUser = userRepository.save(user);
            mapToUserProfileDto(updatedUser);
        }
    }

    @Override
    public ResponseEntity<Object> getNumberOfUsersByRoleId(String roleName) {

        Long roleId = null;
        if (roleName != null) {
            if (roleName.trim().equalsIgnoreCase("USER")) {
                roleId = 1L;
            }
        }

        if (roleId == null) {
            return CustomSuccessHandler.responseBuilder(HttpStatus.BAD_REQUEST, "Invalid role name", null);
        }

        long count = userRepository.countUserWithRoleId(roleId);
        return CustomSuccessHandler.responseBuilder(HttpStatus.OK, "Number of users retrieve successfully", count);
    }

//    @Override
//    public boolean saveDeviceToken(UUID userId, String token) {
//        Optional<User> userOptional = userRepository.findById(userId);
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            user.setDeviceToken(token);
//            userRepository.save(user);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public String getDeviceTokenByUserId(UUID userId) {
//        Optional<User> userOptional = userRepository.findById(userId);
//        return userOptional.map(User::getDeviceToken).orElse(null);
//    }

    private UserProfileDto mapToUserProfileDto(User user) {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(user.getUserId());
        userProfileDto.setFirstName(user.getFirstName());
        userProfileDto.setLastName(user.getLastName());
        userProfileDto.setAddress(user.getAddress());
        userProfileDto.setGender(user.getGender());
        userProfileDto.setDob(user.getDob());
        userProfileDto.setPhone(userProfileDto.getPhone());

        return userProfileDto;
    }
}
