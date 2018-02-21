package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class GreetingController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage helloMessage) throws Exception {
        return new Greeting("Hello, " + helloMessage.getName());
    }

    @MessageMapping("/whistle")
    public Greeting whistle(HelloMessage message) throws Exception {
        Greeting g = new Greeting("Hey, " + message.getName() + message.getTo());
        messagingTemplate.convertAndSendToUser("kim", "/queue/reply", g);
        return g;
    }

    @MessageExceptionHandler
    @SendToUser(value = "/queue/errors")
    public String handleException(Exception exception) {
        // ...
        return exception.getMessage();
    }
}
