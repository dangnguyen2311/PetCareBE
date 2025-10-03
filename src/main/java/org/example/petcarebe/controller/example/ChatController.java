package org.example.petcarebe.controller.example;

import org.example.petcarebe.model.example.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate template;

    public ChatController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage message) {
        message.setContent("joined the chat!");
        return message;
    }

//    @MessageMapping("/chat.private")
//    public void sendPrivate(ChatMessage message) {
//        template.convertAndSendToUser(
//                message.getReceiver(),
//                "/queue/private",
//                message
//        );
//    }
    @MessageMapping("/chat.private")
    public void sendPrivate(ChatMessage message) {
        // gửi tin riêng
        template.convertAndSendToUser(
                message.getReceiver(),
                "/queue/private",
                message
        );
    }

    // không Principal
//    @MessageMapping("/chat.private")
//    public void sendPrivate(ChatMessage message) {
//        template.convertAndSend(
//                "/user/" + message.getReceiver() + "/queue/private",
//                message
//        );
//    }
}
