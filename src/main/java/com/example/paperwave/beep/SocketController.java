package com.example.paperwave.beep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class SocketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    SimpUserRegistry userRegistry;

    @Autowired
    private SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/rtc-message/room/{roomId}")
    public void sendToRoom(@DestinationVariable String roomId, @Payload String message, Principal user) {
        userRegistry.findSubscriptions(s -> s.getDestination().equals(String.format("/topic/room/%s/", roomId)))
                .stream()
                .map(s -> s.getSession().getUser().getName())
                .forEach(u -> {
                    if (!u.equals(user.getName()))
                        sendingOperations.convertAndSend(String.format("/topic/room/%s/%s", roomId, u), message);
                });
    }

}
