package com.pkq.knowledgegraph.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 第一步：从请求头取 Token
        String token = extractToken(request);

        // 第二步：Token 存在且有效
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            // 从 Token 解析出用户名
            String username = jwtTokenProvider.getUsername(token);

            // 查数据库确认用户还存在（防止用户已被删除但 Token 未过期）
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 把用户信息放入安全上下文
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 第三步：不管 Token 好不好使，都放行
        // 权限判断交给 SecurityConfig 做
        filterChain.doFilter(request, response);
    }

    /**
     * 从 Authorization: Bearer xxx 中提取 Token
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);   // 去掉 "Bearer "（7 个字符）
        }
        return null;
    }
}