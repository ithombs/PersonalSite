package com.example.Models;

import java.util.Random;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

//@ServerEndpoint("/aiOpp")
public class AIserver {
	
	@OnOpen
    public void onOpen(Session session) 
	{

		ChessBoard c = new ChessBoard();
		session.getUserProperties().put("game", c);
		int level = Integer.parseInt(session.getQueryString());
		session.getUserProperties().put("level", level);
		
		
		Random r = new Random();
		if(r.nextInt(2) == 0)
		{
			session.getUserProperties().put("side", Side.BLACK);
			
			try
			{
				session.getBasicRemote().sendText("side:" + session.getUserProperties().get("side").toString());
				//ChessAI ai = new ChessAI((Integer)session.getUserProperties().get("level"), c, session);
				session.getUserProperties().put("game", c);
			}
			catch(Exception e)
			{
				System.err.println("AIserver - onOpen: " + e.getMessage());
			}
		}
		else
		{
			try
			{
				session.getUserProperties().put("side", Side.WHITE);
				session.getBasicRemote().sendText("side:" + session.getUserProperties().get("side").toString());
			}
			catch(Exception e)
			{
				
			}
			
		}
		
		
		
	}
	
	@OnMessage
	public void onMessage(String message, Session session)
	{
		int id = -1, row = -1, col = -1;
		
	 	if(message.contains("|"))
        {
        	String[] msg = message.split("\\|");

        	try
            {
            	id = Integer.parseInt(msg[0]);
            	row = Integer.parseInt(msg[1]);
            	col = Integer.parseInt(msg[2]);
            	ChessPiece previousPos;
            	//String opponent = (String)session.getUserProperties().get("opponent");
            	boolean correctSide = false;
            	Side userSide = (Side)session.getUserProperties().get("side");
            	ChessBoard b = (ChessBoard)session.getUserProperties().get("game");
            	previousPos = b.getPiece(id);
            	
            	int previRow = previousPos.getRow();
            	int previCol = previousPos.getCol();
            	
            	if(userSide == b.getTurn())
            		correctSide = true;
            	
            	
            	if(correctSide && b.receiveMove(id, row, col))
            	{
            		session.getUserProperties().put("game", b);
            		
        			session.getBasicRemote().sendText("move:"+message);
        			
        			session.getBasicRemote().sendText("ml1:" + previRow+ "|" + previCol);
        			session.getBasicRemote().sendText("ml2:" + row+ "|" + col);
        			
        			//Check if the move ended the game
        			if(b.getPossibleMoves(Side.WHITE).size() == 0 || b.getPossibleMoves(Side.BLACK).size() == 0)
        			{
        				session.getBasicRemote().sendText("gameOver:" + userSide.toString());
        				session.getUserProperties().put("winner", "You");
        			}
        			
        			//Do AI move here
        			
        			//ChessAI ai = new ChessAI((Integer)session.getUserProperties().get("level"), b, session);
        			
        			
            	}
            	else //Move the piece back on the player's screen if the move was bad.
            	{
            		System.out.println("bad move, returning piece.");
            		System.out.println(id + "|" + b.getPiece(id).getRow() + "|" + b.getPiece(id).getCol());
            		session.getBasicRemote().sendText("-Bad Move-");
            		session.getAsyncRemote().sendText("move:" +id + "|" + b.getPiece(id).getRow() + "|" + b.getPiece(id).getCol() + "|-1");
            	}
            }
            catch(Exception e)
            {
            	System.out.println("Error parsing move data: " + e.toString());
            	e.printStackTrace();
            	System.out.println(id + " " + row + " " + col);
            }
        	
        }
	 	else if(message.startsWith("surr"))
	 	{
	 		try
	 		{
	 			session.getBasicRemote().sendText("gameOver: computer");
	 		}
	 		catch(Exception e)
	 		{
	 			System.err.println("AIserver - surrender: " + e.getMessage());
	 		}
	 	}
	}
	
	@OnClose
	public void onClose(Session session) throws InterruptedException
	{
		System.out.println("Session " +session.getId()+" has ended");
		for(Thread t : Thread.getAllStackTraces().keySet())
		{
			if(t.getName().equals("TestThread"))
				t.join();
		}
	}

}
