/**
 * Misc functions
 */

function test(){
	console.log("Test 1 2 3");
}

$("document").ready(function(){
	$("#dev-btn").click(function(){
		var devVis = $("#dev-blurb").is(":visible");
		var musVis = $("#music-blurb").is(":visible");
		console.log(devVis);
		if(!devVis){
			if(musVis){
				$("#music-blurb").fadeToggle( "slow", "linear", function(){
					$("#dev-blurb").fadeToggle( "slow", "linear" );
				});
			}else{
				$("#dev-blurb").fadeToggle( "slow", "linear" );
			}
			
		}
	});

	$("#music-btn").click(function(){
		var musVis = $("#music-blurb").is(":visible");
		var devVis = $("#dev-blurb").is(":visible");
		console.log(musVis);
		if(!musVis){
			if(devVis){
				$("#dev-blurb").fadeToggle( "fast", "linear", function(){
					$("#music-blurb").fadeToggle( "slow", "linear" );
				});
			}else{
				$("#music-blurb").fadeToggle( "slow", "linear" );
			}
		}
	});
});

