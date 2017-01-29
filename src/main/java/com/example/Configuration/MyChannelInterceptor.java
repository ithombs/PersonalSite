package com.example.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

public class MyChannelInterceptor extends ChannelInterceptorAdapter  {
	  @Autowired
	  private JmsTemplate jmsTemplate;
	
	  @Override
	  public Message<?> preSend(Message<?> message, MessageChannel channel) {
	    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
	    StompCommand command = accessor.getCommand();
	    // ...

	    return message;
	  }
}
