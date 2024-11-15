package com.exe201.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "email_token")
public class EmailToken {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "verification_token")
    private String token;

    @JsonProperty("created_at")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonProperty("expired_at")
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @JsonProperty("validate_at")
    @Column(name = "validate_at")
    private LocalDateTime validateAt;

    @JsonProperty("revoked_token")
    @Column(name = "revoked_token")
    private boolean revokedToken;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

