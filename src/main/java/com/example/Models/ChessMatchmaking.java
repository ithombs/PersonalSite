package com.example.Models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


@Service
@Scope(value = "singleton")
@EnableScheduling
public class ChessMatchmaking {
	private static final Logger log = LoggerFactory.getLogger(ChessMatchmaking.class);
	
	private final Map<String, WebSocketSession> playerPool;
	//private final Map<String, ChessBoard> games;
	
	public ChessMatchmaking(){
		log.info("ChessMatchaker created! - " + this.hashCode());
		
		playerPool = new HashMap<String, WebSocketSession>();
		//games = new HashMap<String, ChessBoard>();
	}
	
	public void addPlayerToPool(WebSocketSession wsSession){
		String username = (String)wsSession.getAttributes().get("username");
		
		//Don't allow a player to connect more than once so their game isn't transfered to the second session
		if(!playerPool.containsKey(username)){
			playerPool.put(username, wsSession);
		}
	}
	
	public void removePlayerFromPool(String username){
		playerPool.remove(username);
	}
	
	public void findMatch(WebSocketSession searchingPlayer){
		Iterator<WebSocketSession> players = playerPool.values().iterator();
    	boolean foundMatch = false;
    	
    	while(players.hasNext())
    	{
    		WebSocketSession s = players.next();
    		//String status = (String)s.getAttributes().get("status");
    		if(!searchingPlayer.getAttributes().get("username").equals(s.getAttributes().get("username")))
    		{
    			String user, opponent;
    			s.getAttributes().put("status", "inGame");
    			searchingPlayer.getAttributes().put("status", "inGame");
    			
    			user = (String)searchingPlayer.getAttributes().get("username");
    			opponent = (String)s.getAttributes().get("username");
    			log.info("User: "  +user + " --- Opponent: " + opponent);
    			
    			try
    			{
    				Random r = new Random();
    				if(r.nextInt(2) == 0)
    				{
    					s.getAttributes().put("side", Side.WHITE);
    					searchingPlayer.getAttributes().put("side", Side.BLACK);
    				}
    				else
    				{
    					s.getAttributes().put("side", Side.BLACK);
    					searchingPlayer.getAttributes().put("side", Side.WHITE);
    				}

    				//put opponents into each others map
    				searchingPlayer.getAttributes().put("opponent", opponent);
    				s.getAttributes().put("opponent", user);
    				
    				//Send to the client that they connected to a match
    				s.sendMessage(new TextMessage("connnected"));
    				searchingPlayer.sendMessage(new TextMessage("connected") );
    				
    				//Send opponent name and SIDE
    				searchingPlayer.sendMessage(new TextMessage("opponent:"+ opponent));
    				searchingPlayer.sendMessage(new TextMessage("side:" + searchingPlayer.getAttributes().get("side").toString()));
    				s.sendMessage(new TextMessage("opponent:"+ user));
    				s.sendMessage(new TextMessage("side:" + s.getAttributes().get("side").toString()));
    			}
    			catch(Exception e)
    			{
    				log.info("Error finding opponent for Chess game: " + e.getMessage());
    			}
    			//this.removePlayerFromPool(user);
    			this.removePlayerFromPool(opponent);
    			foundMatch = true;
    			break;
    		}
    	}
    	
    	if(!foundMatch){
    		this.addPlayerToPool(searchingPlayer);
    	}
	}
	
	@Scheduled(fixedDelay = 10000)
	public void purgePlayers(){
		Iterator<String> playerNames = playerPool.keySet().iterator();
		//log.info("---Purging player pool---");
		while(playerNames.hasNext()){
			String player = playerNames.next();
			WebSocketSession wsSession = playerPool.get(player);
			if(!wsSession.isOpen()){
				log.info("Removing disconnected player - " + player);
				playerPool.remove(player);
			}
		}
	}
}
