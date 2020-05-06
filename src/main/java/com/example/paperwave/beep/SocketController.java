package com.example.paperwave.beep;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class SocketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    SimpUserRegistry userRegistry;
    @Autowired
    private SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/rtc-message/room/{roomId}")
    public void sendToRoom(@DestinationVariable String roomId, @Payload String message, Principal user) {
        userRegistry.findSubscriptions(s -> s.getDestination().equals("/app/topic/room/" + roomId))
                .stream()
                .map(s -> s.getSession().getUser().getName())
                .forEach(u -> {
                    if (!u.equals(user.getName()))
                        sendingOperations.convertAndSend(String.format("/topic/room/%s/%s", roomId, u), message);
                });
    }

    @MessageMapping("/signaling/{roomId}/{userId}")
    @SendTo("/topic/room/{roomId}/{userId}")
    public String wireUserSignaling(@DestinationVariable String roomId, @DestinationVariable String userId, @Payload String message) {
        return message;
    }

    @SubscribeMapping("/topic/room/{roomId}")
    public Map<String, Set<String>> handleRoomSubscription(@DestinationVariable String roomId, Principal user) {
        return Map.of("userIds",
                userRegistry.findSubscriptions(s -> s.getDestination().equals("/app/topic/room/" + roomId))
                        .stream()
                        .map(s -> s.getSession().getUser().getName())
                        .filter(s -> !s.equals(user.getName()))
                        .collect(Collectors.toSet()));
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class UserData {
    private String name;
}
