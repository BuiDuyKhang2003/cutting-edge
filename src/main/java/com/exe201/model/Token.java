package com.exe201.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.exe201.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "token", indexes = {
        @Index(name = "idx_access_token", columnList = "access_token"),
        @Index(name = "idx_refresh_token", columnList = "refresh_token"),
})
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("access_token")
    @Column(name = "access_token",length = 450)
    private String accessToken;

    @JsonProperty("refresh_token")
    @Column(name = "refresh_token",length = 350)
    private String refreshToken;

    @JsonProperty("token_type")
    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean expired;

    private boolean revoked;

}
