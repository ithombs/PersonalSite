/**
 * Misc functions
 */

function test(){
	console.log("Test 1 2 3");
}

$("document").ready(function(){
	
	//Pretty up the site with small animations
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
	
	$("#projects-btn").click(function(){
		//var projects = $(".projects-div").is(":visible");
		var resume = $(".resume-div").is(":visible");
		var links = $(".rel-links-div").is(":visible");
		
		if(resume){
			$(".resume-div").fadeToggle("fast", "linear", function(){
				$(".projects-div").fadeToggle( "slow", "linear" );
			});
		} else if(links){
			$(".rel-links-div").fadeToggle("fast", "linear", function(){
				$(".projects-div").fadeToggle( "slow", "linear" );
			});
		} else{
			$(".projects-div").fadeToggle( "slow", "linear" );
		}
	});
	
	$("#resume-btn").click(function(){
		var projects = $(".projects-div").is(":visible");
		//var resume = $(".resume-div").is(":visible");
		var links = $(".rel-links-div").is(":visible");
		
		if(projects){
			$(".projects-div").fadeToggle("fast", "linear", function(){
				$(".resume-div").fadeToggle( "slow", "linear" );
			});
		} else if(links){
			$(".rel-links-div").fadeToggle("fast", "linear", function(){
				$(".resume-div").fadeToggle( "slow", "linear" );
			});
		} else{
			$(".resume-div").fadeToggle( "slow", "linear" );
		}
	});
	
	$("#links-btn").click(function(){
		var projects = $(".projects-div").is(":visible");
		var resume = $(".resume-div").is(":visible");
		//var links = $(".rel-links-div").is(":visible");
		
		if(resume){
			$(".resume-div").fadeToggle("fast", "linear", function(){
				$(".rel-links-div").fadeToggle( "slow", "linear" );
			});
		} else if(projects){
			$(".projects-div").fadeToggle("fast", "linear", function(){
				$(".rel-links-div").fadeToggle( "slow", "linear" );
			});
		} else{
			$(".rel-links-div").fadeToggle( "slow", "linear" );
		}
	});
});

