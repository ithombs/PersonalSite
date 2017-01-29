/**
 * 
 */
var stompClient;
var chessSock;

function connect2(){
	chessSock = new SockJS('http://localhost:8080/boot2/chess');
	
	chessSock.onMessage = function(msg){console.log("From chessSocket: " + msg);}
	chessSock.onOpen = function(){console.log("Connection successful");}
	chessSock.onClose = function(){console.log("Connection successful");}
}

function connect(){
	var socket = new SockJS('/boot2/webSocketFallback');
	stompClient = Stomp.over(socket);
	//stompClient.subscribe("/topic/test1");
}

function subscribe(){
	stompClient.subscribe("/topic/test1", function(msg){console.log(msg);});
	stompClient.subscribe('/user/queue/msg', function(msg){console.log(msg);});
	stompClient.subscribe("/user/topic/msg", function(msg){console.log(msg);});
}

function sendMsg(endpoint, msg){
	stompClient.send(endpoint, {}, msg);
}