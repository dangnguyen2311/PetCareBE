package org.example.petcarebe.config;

import org.example.petcarebe.config.example.CustomHandshakeHandler;
import org.example.petcarebe.config.example.UserHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final UserHandshakeInterceptor interceptor;
    private final CustomHandshakeHandler handshakeHandler;

    public WebSocketConfig(UserHandshakeInterceptor interceptor, CustomHandshakeHandler handshakeHandler) {
        this.interceptor = interceptor;
        this.handshakeHandler = handshakeHandler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple memory-based message broker
        config.enableSimpleBroker("/topic", "/queue");
        // Set application destination prefix
        config.setApplicationDestinationPrefixes("/app");
        // Set user destination prefix for private messages
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register STOMP endpoint with SockJS fallback
        registry.addEndpoint("/ws")
                // sau bỏ 2 dưới dòng này
//                .addInterceptors(new UserHandshakeInterceptor())
//                .setHandshakeHandler(new CustomHandshakeHandler())
                .addInterceptors(interceptor)
                .setHandshakeHandler(handshakeHandler)
                .setAllowedOriginPatterns("*")
                .withSockJS();
        
        // Register endpoint without SockJS for native WebSocket clients
//        registry.addEndpoint("/ws-native")
//                .setAllowedOriginPatterns("*");
        // ✅ Cho Android (native)
        registry.addEndpoint("/ws-native")
                .addInterceptors(interceptor)
                .setHandshakeHandler(handshakeHandler)
                .setAllowedOriginPatterns("*");
        registry.addEndpoint("/ws-native/websocket")
                .addInterceptors(interceptor)
                .setHandshakeHandler(handshakeHandler)
                .setAllowedOriginPatterns("*");
    }

}
