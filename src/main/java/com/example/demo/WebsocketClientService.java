package com.example.demo;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class WebsocketClientService {
    private WebSocketClient client;
    private String clientId;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    public void connect() throws URISyntaxException {
        client = new WebSocketClient(new URI("ws://localhost:8888")) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Connected to server");
            }

            @Override
            public void onMessage(String message) {
                try {
                    JSONObject json = new JSONObject(message);
                    if (json.has("client_id")) {
                        clientId = json.getString("client_id");
                        messageQueue.add("Assigned client ID: " + clientId);
                    } else {
                        messageQueue.add("Received: " + message);
                    }
                } catch (Exception e) {
                    messageQueue.add("Received: " + message);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                messageQueue.add("Disconnected from server: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                messageQueue.add("WebSocket error: " + ex.getMessage());
            }
        };

        client.connect();
    }

    public void sendMessage(String target, String message) {
        if (client != null && client.isOpen()) {
            JSONObject json = new JSONObject();
            json.put("target", target);
            json.put("message", message);
            client.send(json.toString());
            messageQueue.add("Sent to " + target + ": " + message);
        } else {
            messageQueue.add("Not connected to server");
        }
    }

    public String getClientId() {
        return clientId;
    }

    public String getNextMessage() throws InterruptedException {
        return messageQueue.take();
    }
}