/*package com.example.Controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;


 * NOTES:
 * -Javascript side must connect with 'var socket = new SockJS('/boot2/webSocketFallback')
 * -No other websocket related URLs require 'boot2'
 
@Controller
public class WebSocketController {
	private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private JmsMessagingTemplate jmsMsgTmp;
	
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
    public WebSocketController(SimpMessagingTemplate template) {
        this.messagingTemplate = template;
    }
	
	@MessageMapping("/direct2")
	@SendToUser("/queue/msg")
	public String sendDirect2(){
		return "{'msg': 'from direct2'}";
	}

	@MessageMapping("/direct")
	public String sendSchedData(Principal p){
		
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
			    .create(SimpMessageType.MESSAGE);
			headerAccessor.setSessionId("Ian2");
			headerAccessor.setLeaveMutable(true);
		
		log.info("schedu message");
		try{
			//jmsTemplate.convertAndSend("/topic/test1", "hahahahah");
			
			//messagingTemplate.convertAndSendToUser("Ian", "/queue/msg", "{'msg':'Sent from direct'}");
			this.messagingTemplate.convertAndSendToUser("Ian", "/queue/msg", "{'msg':'Sent from direct'}");
			this.messagingTemplate.convertAndSendToUser("Ian", "/topic/msg", "{'msg':'Sent from direct'}");
			//this.messagingTemplate.convertAndSend("/Ian/queue/msg", "{'msg':'Sent from direct'}");
			//this.messagingTemplate.convertAndSend("/user/Ian/queue/msg", "{'msg':'Sent from direct'}");
		}catch(Exception e){
			log.info("message error");
		}
		return "test";
	}
	
	@MessageMapping("/test") // URL to connect to is /webSocket/test
    @SendTo("/topic/test1")
    public String greeting(SimpMessageHeaderAccessor headerAccessor, String message, MessageHeaders messageHeaders) throws Exception {
        //Thread.sleep(1000); // simulated delay
		this.messagingTemplate.convertAndSendToUser("Ian", "/topic/msg", "{'msg':'Sent from direct'}");
        log.info("From the WebSocketController - " + headerAccessor.getSessionId());
        return "Return message from the server!";
    }
}
*/