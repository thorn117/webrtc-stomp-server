package com.example.paperwave.beep;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;


@Component
public class WebSocketEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleWebSocketConnectListener(final SessionConnectedEvent event) {
        String sessionId = Objects.requireNonNull(event.getMessage().getHeaders().get("simpSessionId")).toString();
        LOGGER.info("New session with ID {}", sessionId);
    }

    @EventListener
    public void handleSubscription(final SessionSubscribeEvent event) {
        String destination = Objects.requireNonNull(event.getMessage().getHeaders().get("simpDestination")).toString();
        LOGGER.info("New subscription to {}", destination);
    }
}
