package hello;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SendingService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void send(Greeting greeting) {
        messagingTemplate.convertAndSend("/topic/greetings", greeting);
    }

    public void sendToUser(String user, Greeting greeting) {
        messagingTemplate.convertAndSendToUser(user, "/queue/reply", greeting);
    }

    @Scheduled(fixedDelay = 5000, initialDelay = 20000L)
    public void sendScheduled() {
        String now = new Date().toString();
        Greeting g = new Greeting(now);
        send(g);
        sendToUser("song", g);
    }
}
