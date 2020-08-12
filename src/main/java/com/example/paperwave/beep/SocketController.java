package com.example.paperwave.beep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class SocketController {

    @Autowired
    SimpUserRegistry userRegistry;

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