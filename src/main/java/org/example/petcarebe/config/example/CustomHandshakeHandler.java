package org.example.petcarebe.config.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(
            ServerHttpRequest request,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        // Mặc định nếu không có query param thì đặt tên là guest
        String username = "guest";

        // Lấy query param từ URL, ví dụ: /ws?user=alice
        String query = request.getURI().getQuery(); // "user=alice"
        System.out.println(request.getURI().getQuery());
        if (query != null) {
            for (String param : query.split("&")) {
                if (param.startsWith("user=")) {
                    username = param.substring("user=".length());
                    break;
                }
            }
        }

        // Trả về Principal tuỳ chỉnh
        return new StompPrincipal(username);
    }
}
