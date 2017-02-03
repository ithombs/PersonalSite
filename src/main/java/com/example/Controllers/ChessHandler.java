package com.example.Controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.Models.Side;
import com.example.Models.ChessPiece.*;
import com.example.Models.ChessAI;
import com.example.Models.ChessBoard;
import com.example.Models.ChessMatchmaking;
import com.example.Models.ChessPiece;
import com.example.Models.Role;

public class ChessHandler  extends TextWebSocketHandler{
	private static final Logger log = LoggerFactory.getLogger(ChessHandler.class);
	
	//NOTE: Replace this user map with something more robust if this is ever used as more than a simple demo
	private static final Map<String, WebSocketSession> connectedPlayers = new HashMap<String, WebSocketSession>();
	
	@Autowired
	ChessMatchmaking cMatcher;
	
	public ChessHandler(){
		
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession wsSession){
		//TODO: Add in a Query String for use when a client connects from someplace other than the web
		//TODO: Alternatively, just authenticate from the client as normal
		String username = "";
		//UserDetails currentUser = (UserDetails) ((Authentication) WebSocketSession.getPrincipal()).getPrincipal();
		
		if(wsSession.getPrincipal() != null){
			wsSession.getAttributes().put("username", wsSession.getPrincipal().getName());
			username = wsSession.getPrincipal().getName();
		}else{
			username = "guest-" + wsSession.getId();
			wsSession.getAttributes().put("username", username);
		}
		log.info("Websocket WebSocketSession created - " + username);
		//currentUser.getAuthorities().forEach(ga -> log.info(ga.getAuthority()));
		connectedPlayers.put(username, wsSession);
		String type = wsSession.getUri().getQuery();
		log.info("Query Info: "+ type);
		
		//log.info("ChessMatchmaking hashcode = " + cMatcher.hashCode());
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession wsSession, CloseStatus status){
		String username = (String)wsSession.getAttributes().get("username");
		log.info("Websocket WebSocketSession closed - " + username);
		connectedPlayers.remove(username);
	}
	
	
	@Override
    public void handleTextMessage(WebSocketSession wsSession, TextMessage msg) {
		log.info("Message recieved: " + msg.getPayload());
		log.info(wsSession.getAttributes().get("username").toString());
        
        //int id = -1, row = -1, col = -1;
        String message = msg.getPayload();
        
        /*
         * A client connects by sending a message of "connect:USERNAME".
         * 
         * Note: USERNAME containing guest-[websocketID] means a user is not logged in.
         */
        //String gameType = wsSession.getAttributes().get("type").toString();
        
    	if(message.startsWith("connect"))
        {
    		String type = message.split(":")[1];
        	log.info("Player entered queue - " + type);
        	
        	initPlayer(wsSession, type);
        	initGame(wsSession, type);
        }
        else if(message.startsWith("recon"))
        { 
        	reconnect(wsSession, message.split(":")[1]);
        }
        //The pipe character denotes an attempted move from a client
        else if(message.contains("|"))
        {
        	if(wsSession.getAttributes().get("status").equals("inGame")){
        		recieveMove(wsSession, message);
        	}
        }
        //Send the move list data to the reconnecting opponent
        else if(message.contains("md"))
        {
        	recSendMoveList(wsSession, message);
        }
        else if(message.startsWith("surr"))
        {
        	surrender(wsSession, message);
        }
        //Two portions to send data to an ADMIN for the game count and player count
    	//TODO: Pull these out of the chess moves portion
        else if(message.startsWith("gameCount"))
        {
        	adminGameCount(wsSession);
        }
        else if(message.startsWith("playerCount"))
        {
        	adminPlayerCount(wsSession);
        }
    }
	
	//--------------------------------------------------------------------------------------------------------------------------
	
	private void initPlayer(WebSocketSession WebSocketSession, String gameType)
	{
		if(gameType.equals("human")){
			WebSocketSession.getAttributes().put("status", "open");
			WebSocketSession.getAttributes().put("type", "human");
	        WebSocketSession.getAttributes().put("game", new ChessBoard());
		}else{
			WebSocketSession.getAttributes().put("status", "inGame");
			WebSocketSession.getAttributes().put("type", "ai");
	        WebSocketSession.getAttributes().put("game", new ChessBoard());
		}
	}
//	private void initAdmin(WebSocketSession WebSocketSession)
//	{
//		//ADMIN functions - send the amount of players connected and the amount of games being played
//		UserDetails currentUser = (UserDetails) ((Authentication) WebSocketSession.getPrincipal()).getPrincipal();
//		String gameC = "";
//		String playerC = "";
//		if(currentUser != null && currentUser.getAuthorities().contains(Role.ADMIN)){
//	    		int gameCount = 0;
//	    		int playerCount = connectedPlayers.size();
//	    		WebSocketSession.getAttributes().put("status", "admin");
//	    		WebSocketSession.getAttributes().put("username", "admin");
//	  
//	    		
//	    		Iterator<WebSocketSession> players = connectedPlayers.values().iterator();
//	    		while(players.hasNext()){
//	    			WebSocketSession player = players.next();
//	    			String status = (String)player.getAttributes().get("status");
//	    			if(status != null && status.equals("inGame"))
//	    				gameCount++; 
//	    		}
//	    		/*
//	    		for(WebSocketSession s : connectedPlayers.values().)
//	    		{
//	    			String status = (String)s.getAttributes().get("status");
//	    			if(status != null && status.equals("inGame"))
//	    				gameCount++;
//	    		}
//	    		*/
//	    		//send the amount of people in game and the amount of games to the admin
//	    		WebSocketSession.sendMessage(new TextMessage("gameCount:"+ gameCount/2));
//	    		WebSocketSession.sendMessage(new TextMessage("playerCount:"+ playerCount));
//	    		gameC = 
//		}else{
//				log.info("Non-ADMIN attempting to get admin stats from ChessHandler");
//				
//		}
//		
//		try{
//			
//		}catch(Exception e){
//			
//		}
//	}
	
	/*
	 * Set up a game if a user is connected, or set up user to be ready for a game to start on the next connection
	 */
	private void initGame(WebSocketSession wsSession, String message)
	{
    	//log.info("username: " +(String)wsSession.getAttributes().get("username"));
    	if(message.startsWith("ai")){
    		try{
    			int aiLevel = Integer.parseInt(message.substring(3, 4));
        		wsSession.getAttributes().put("level", aiLevel);
        		
        		
        		Random r = new Random();
        		if(r.nextInt(2) == 0)
        		{
        			wsSession.getAttributes().put("side", Side.BLACK);
        			
        			try
        			{
        				wsSession.sendMessage(new TextMessage("side:" + Side.BLACK.toString()));
        				ChessAI ai = new ChessAI((Integer)wsSession.getAttributes().get("level"), (ChessBoard)wsSession.getAttributes().get("game"), wsSession);
        				//wsSession.getUserProperties().put("game", c);
        			}
        			catch(Exception e)
        			{
        				log.info("AI Opponent problem - initGame: " + e.getMessage());
        			}
        		}
        		else
        		{
        			try
        			{
        				wsSession.getAttributes().put("side", Side.WHITE);
        				wsSession.sendMessage(new TextMessage("side:" + Side.WHITE.toString()));
        			}
        			catch(Exception e)
        			{
        				log.info("AI Opponent problem - initGame: " + e.getMessage());
        			}
        			wsSession.getAttributes().put("status", "inGame");
        		}
    		}catch(Exception e){
    			log.info("Error parsing AI level - " + e);
    		}
    	}else{
    		//Find a match for a human game
    		//cMatcher.addPlayerToPool(wsSession);
        	cMatcher.findMatch(wsSession);
    	}
	}
	
	/*
	 * Reconnecting to a game
	 */
	private void reconnect(WebSocketSession wsSession, String msg)
	{
		String username = wsSession.getAttributes().get("username").toString();
		if(!username.equals("guest-" + wsSession.getId()))
    	{
			//wsSession.getAttributes().put("username",msg);
    		
    		try
    		{
    			//Check each WebSocketSession to see if the connecting user is already in a game (RECONNECTION)
	    		Iterator<WebSocketSession> players = connectedPlayers.values().iterator();
	    		
	        	while(players.hasNext())
	        	{
	        		WebSocketSession s = players.next();
	        		log.info((String)s.getAttributes().get("opponent"));
	        		String opponentName = (String)s.getAttributes().get("opponent");
	        		
	        		if(s.getId().equals(wsSession.getId()))
	        			continue;
	        		if(opponentName.equals(msg));
	        		{
	        			log.info("---RECONNECTING---");
	        			
	        			ChessBoard b = (ChessBoard)s.getAttributes().get("game");
	        			Side opp = (Side)s.getAttributes().get("side");
	        			
	        			wsSession.getAttributes().put("game", b);
	        			wsSession.getAttributes().put("opponent", s.getAttributes().get("username"));
	        			if(opp == Side.WHITE)
	        				wsSession.getAttributes().put("side", Side.BLACK);
	        			else
	        				wsSession.getAttributes().put("side", Side.WHITE);
	        			
	        			
	        			//Send all the info to the client
	        			wsSession.sendMessage(new TextMessage((String)wsSession.getAttributes().get("username")));
	        			wsSession.sendMessage(new TextMessage("side:" +wsSession.getAttributes().get("side").toString()));
	        			wsSession.sendMessage(new TextMessage("opponent:" + wsSession.getAttributes().get("opponent").toString()));
	        			
	        			//Transmit already done moves to the reconnecting client
	        			for(ChessPiece c : b.getMoveList())
	        			{
	        				
	        				wsSession.sendMessage(new TextMessage("move:" +c.getID() + "|" + c.getRow() + "|" + c.getCol()));
	        			}
	        			s.sendMessage(new TextMessage("oppRecon"));
	        			return;
	        		}
	        	}
	        	//Opponent was not found - close connection
	        	wsSession.close();
        	
    		}
    		catch(Exception e)
    		{
    			log.info("recon: " + e.getMessage());
    		}
    	}
    	else
    	{
    		//TODO: Remove this or remember why it's here.
    		try
    		{
    			wsSession.close();
    		}
    		catch(Exception e)
    		{
    			log.info("Reconn - guest: " + e.getMessage());
    		}
    	}
	}
	
	/*
	 * Receive and deal with a move from a user/client
	 */
	private void recieveMove(WebSocketSession wsSession, String message)
	{
		String[] msg = message.split("\\|");
		int id = -1, row = -1, col = -1;
		
    	try
        {
        	id = Integer.parseInt(msg[0]);
        	row = Integer.parseInt(msg[1]);
        	col = Integer.parseInt(msg[2]);
        	
        	ChessPiece previousPos;
        	String opponent = (String)wsSession.getAttributes().get("opponent");
        	boolean correctSide = false;
        	Side userSide = (Side)wsSession.getAttributes().get("side");
        	ChessBoard b = (ChessBoard)wsSession.getAttributes().get("game");
        	previousPos = b.getPiece(id);
        	
        	int previRow = previousPos.getRow();
        	int previCol = previousPos.getCol();
        	
        	if(userSide == b.getTurn())
        		correctSide = true;
        	
        	
        	if(correctSide && b.receiveMove(id, row, col))
        	{
        		wsSession.getAttributes().put("game", b);
        		
        		String gameType = wsSession.getAttributes().get("type").toString();
        		if(gameType.equals("ai")){
        			wsSession.sendMessage(new TextMessage("move:"+message));
        			wsSession.sendMessage(new TextMessage("ml1:" + previRow+ "|" + previCol));
        			wsSession.sendMessage(new TextMessage("ml2:" + row+ "|" + col));
        			
        			if(b.getPossibleMoves(Side.WHITE).size() == 0 || b.getPossibleMoves(Side.BLACK).size() == 0)
        			{
        				wsSession.sendMessage(new TextMessage("gameOver:" + userSide.toString()));
        				wsSession.getAttributes().put("winner", wsSession.getAttributes().get("username"));
        			}else{
        				ChessAI ai = new ChessAI((Integer)wsSession.getAttributes().get("level"), (ChessBoard)wsSession.getAttributes().get("game"), wsSession);
        			}
        		}else{
        			Iterator<WebSocketSession> players = connectedPlayers.values().iterator();
            		while(players.hasNext())
                	{
            			WebSocketSession s = players.next();
                		if(s.getAttributes().get("username").equals(opponent))
                		{
                			s.getAttributes().put("game", b);
                			s.sendMessage(new TextMessage("move:" +message));
                			wsSession.sendMessage(new TextMessage("move:"+message));
                			
                			
                			s.sendMessage(new TextMessage("ml1:" + previRow+ "|" + previCol));
                			s.sendMessage(new TextMessage("ml2:" + row+ "|" + col));
                			wsSession.sendMessage(new TextMessage("ml1:" + previRow+ "|" + previCol));
                			wsSession.sendMessage(new TextMessage("ml2:" + row+ "|" + col));
                			
                			//Check if the move ended the game
                			if(b.getPossibleMoves(Side.WHITE).size() == 0 || b.getPossibleMoves(Side.BLACK).size() == 0)
                			{
                				wsSession.sendMessage(new TextMessage("gameOver:" + userSide.toString()));
                				s.sendMessage(new TextMessage("gameOver:" + userSide.toString()));
                				
                				wsSession.getAttributes().put("winner", wsSession.getAttributes().get("username"));
                				s.getAttributes().put("winner", wsSession.getAttributes().get("username"));
                			}
                		}
                	}
        		}
        	}
        	else //Move the piece back on the player's screen if the move was bad.
        	{
        		//log.info("bad move, returning piece.");
        		//log.info(id + "|" + b.getPiece(id).getRow() + "|" + b.getPiece(id).getCol());
        		wsSession.sendMessage(new TextMessage("-Bad Move-"));
        		wsSession.sendMessage(new TextMessage("move:" +id + "|" + b.getPiece(id).getRow() + "|" + b.getPiece(id).getCol() + "|-1"));
        	}
        }
        catch(Exception e)
        {
        	log.info("Error parsing move data: " + e.toString());
        	e.printStackTrace();
        	log.info(id + " " + row + " " + col);
        }
	}
	
	/*
	 * Send move list to a reconnecting opponent
	 */
	private void recSendMoveList(WebSocketSession wsSession, String msg)
	{
		String username = (String)wsSession.getAttributes().get("username");
    	
    	try
    	{
    		//get opponent and give them the move data that was sent from the client
    		Iterator<WebSocketSession> players = connectedPlayers.values().iterator();
    		while(players.hasNext())
    		{
    			WebSocketSession s = players.next();
    			String opponent = (String)s.getAttributes().get("opponent");
    			
    			if(username.equals(opponent))
    			{
    				log.info("SENDING MOVE LIST --- " + username + " to " + opponent);
    				s.sendMessage(new TextMessage("md:" +msg.substring(3)));
    			}
    		}
    	}
    	catch(Exception e)
    	{
    		log.info("md: " + e.getMessage());
    	}
	}
	
	/*
	 * Surrender and quit a game
	 */
	private void surrender(WebSocketSession wsSession, String message)
	{
		String user = wsSession.getAttributes().get("username").toString();
    	
    	Iterator<WebSocketSession> players = connectedPlayers.values().iterator();
    	while(players.hasNext())
    	{
    		WebSocketSession s = players.next();
    		if(s.getAttributes().get("opponent") != null && s.getAttributes().get("opponent").equals(user))
    		{
    			try
    			{
    				wsSession.sendMessage(new TextMessage("gameOver:" + s.getAttributes().get("username").toString()));
    				s.sendMessage(new TextMessage("gameOver:" + s.getAttributes().get("username").toString()));
    				
    				wsSession.getAttributes().put("winner", s.getAttributes().get("username"));
    				s.getAttributes().put("winner", s.getAttributes().get("username"));
    				
    				wsSession.getAttributes().put("surr", true);
    				s.getAttributes().put("surr", true);
    			}
    			catch(Exception e)
    			{
    				log.info("ReceiveMessage - surr: " + e.getMessage());
    			}
    			
    		}
    	}
	}
	
	//send the number of games in progress to the admin user
	private void adminGameCount(WebSocketSession wsSession)
	{
		try
    	{
    		int gameCount = 0;
    		Iterator<WebSocketSession> players = connectedPlayers.values().iterator();
        	while(players.hasNext())
    		{
        		WebSocketSession s = players.next();
    			String status = (String)s.getAttributes().get("status");
    			if(status != null && status.equals("inGame"))
    				gameCount++;
    		}
    		//send the amount of people in game and the amount of games to the admin
    		wsSession.sendMessage(new TextMessage("gameCount:"+ gameCount/2));
    	}
    	catch(Exception e)
    	{
    		log.info("WebSockServer - gameCount: " + e.getMessage());
    	}
	}
	
	//send the number of player connected to the server to the admin user
	private void adminPlayerCount(WebSocketSession wsSession)
	{
		try
    	{
    		int playerCount = connectedPlayers.size();
        	wsSession.sendMessage(new TextMessage("playerCount:"+ playerCount));
    	}
    	catch(Exception e)
    	{
    		log.info("WebSockServer - playerCount: " + e.getMessage());
    	}
	}
}

