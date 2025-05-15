package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebSocketController {

    @Autowired
    private WebsocketClientService websocketClientService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("clientId", websocketClientService.getClientId());
        return "index";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String target,
                              @RequestParam String message,
                              Model model) {
        websocketClientService.sendMessage(target, message);
        model.addAttribute("clientId", websocketClientService.getClientId());
        model.addAttribute("message", "Message sent to " + target);
        return "index";
    }

    // Add this endpoint to fetch messages
    @GetMapping("/messages")
    @ResponseBody
    public String getMessage() throws InterruptedException {
        return websocketClientService.getNextMessage();
    }
}