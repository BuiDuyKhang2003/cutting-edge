package com.exe201.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for account information update")
public class UserProfileDto {

    @JsonIgnore
    @JsonProperty("user_id")
    private UUID id;

    @Schema(description = "User's first name", example = "Bui Duy")
    @JsonProperty("first_name")
    private String firstName;

    @Schema(description = "User's last name", example = "Khang")
    @JsonProperty("last_name")
    private String lastName;

    @Schema(description = "User's address", example = "123 Main St, Springfield")
    @NotBlank(message = "Address cannot be blank")
    private String address;

    @Schema(description = "User's gender", example = "Male")
    private String gender;

    @Schema(description = "User's date of birth", example = "26-10-2003")
    private LocalDate dob;

    @Schema(description = "User's phone number", example = "(+84)877643231")
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Please enter a valid(+84) phone number")
    private String phone;

}
