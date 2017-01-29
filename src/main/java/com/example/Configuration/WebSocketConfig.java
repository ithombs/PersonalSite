/*package com.example.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;



@Configuration
@EnableWebSocketMessageBroker
@EnableJms
public class WebSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/webSocket");
        //config.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/webSocketFallback").withSockJS().setInterceptors(httpSessionIdHandshakeInterceptor());
    }
    
    @Autowired
	private WebSocketHandler subProtocolWebSocketHandler;

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
			@Override
			public WebSocketHandler decorate(WebSocketHandler webSocketHandler) {
				return new WebSocketSessionCapturingHandlerDecorator(webSocketHandler);
			}
		});
	}
	

	@Bean
	public HttpSessionIdHandshakeInterceptor httpSessionIdHandshakeInterceptor() {
		return new HttpSessionIdHandshakeInterceptor();
	}
    
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
      registration.setInterceptors(new MyChannelInterceptor());
    }
    
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
      registration.setInterceptors(new MyChannelInterceptor());
    }
    
}*/