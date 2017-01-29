package com.example.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.Controllers.ChessHandler;

@Configuration
@EnableWebSocket
public class WebSocketBasicConfig implements WebSocketConfigurer  {
	    @Override
	    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	        registry.addHandler(chessHandler(), "/chess").withSockJS().setInterceptors(httpInterceptor());
	    }

	    @Bean
	    public WebSocketHandler chessHandler() {
	        return new ChessHandler();
	    }
	    
	    @Bean
	    public HttpSessionIdHandshakeInterceptor httpInterceptor(){
	    	return new HttpSessionIdHandshakeInterceptor();
	    }
	}

