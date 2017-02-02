package com.example.Controllers;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Models.ChessMatchmaking;

/*
 * Notes:
 * -The URL /personal/chessSocket is used for the player websocket connections
 */

@Controller
@RequestMapping("/personal")
public class PersonalController {
	private static final Logger log = LoggerFactory.getLogger(PersonalController.class);
	
	@Autowired
	private ChessMatchmaking cMatcher;
	
	@RequestMapping({"/", ""})
	public String personalHome(HttpSession session, Principal principal){
		log.info("From Personal Controller(/)");
		//session.setAttribute("name",principal!=null? principal.getName():"guest");
		return "PersonalHome";
	}
	
	@RequestMapping("/chessboard")
	public String chessboard(){
		log.info("ChessMatchmaking hashcode = " + cMatcher.hashCode());
		return "chessTest";
	}
	
	@RequestMapping("/chessboard2")
	public String chessboard2(){
		return "chessBoard";
	}
}
