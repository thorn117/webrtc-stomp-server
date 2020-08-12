# webrtc-stomp-server

This is a Signaling server for a webRTC video chat based on Spring Boot. It uses the STOMP protocol.

Check out the [demo](https://webrtcvideochat.paperwave.xyz/).

A client in vanilla js is available here: [webrtc-stomp-client-js](https://github.com/thorn117/webrtc-stomp-client-js).

## Run

Modify the `server.address` and `server.port` in the `application.properties` to your needs. An self-signed-cert is included. Do not forget to allow access from the browser by opening the server address (e.g. https://192.168.0.80:9110).
