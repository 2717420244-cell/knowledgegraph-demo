package com.pkq.knowledgegraph.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;        // JWT 字符串
    private String tokenType;    // "Bearer"
    private Long expiresIn;      // 过期时间（毫秒）
    private String username;     // 用户名

    public static LoginResponse of(String token, long expiresIn, String username) {
        return new LoginResponse(token, "Bearer", expiresIn, username);
    }
}