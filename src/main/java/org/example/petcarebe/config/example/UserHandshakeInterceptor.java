package org.example.petcarebe.config.example;


import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class UserHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        // Lấy query param ?user=xxx
        String query = request.getURI().getQuery(); // ví dụ: "user=alice"
        if (query != null && query.startsWith("user=")) {
            String username = query.substring("user=".length());
            attributes.put("user", username);
        }
        return true; // cho phép kết nối
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
        // không cần làm gì
    }
}

