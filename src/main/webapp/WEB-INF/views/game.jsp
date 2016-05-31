<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jsp"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Ghost Game</title>
<link id="bootstrap-style" href="resources/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
<link href="resources/css/custom.css" rel="stylesheet">

<script type="text/javascript">
	function ghostLetterAjax() {
		$("#messageHuman").css('display', 'none');
		$.ajax({
			type : "GET",
			url : "ghostLetter.htm",
			success : function(response) {
				var status = response.status;
				var letter = response.letter;
				$("#string").append(letter);
				if(status == ${stauts_is_a_word}) {
					$('#modalLabel').html("You win ....");
					$('#modalBody').html("The string matches a word!");
					$('#modalDialog').modal('show');
					$('#modalWord').html($("#string").html());
				} else if(status == ${stauts_continue}) {
					$("#humanForm").show();
					$("#ghostTurn").hide();
					$("#messageGhost").css('display', 'inline');
					$("#messageGhost").html("I say '" + letter + "'");
				} else if(status == ${stauts_draw}) {
					$('#modalLabel').html("Draw ....");
					$('#modalBody').html("The string matches a word, but is shorter than 4!");
					$('#modalDialog').modal('show');
					$('#modalWord').html($("#string").html());
				}
				waitingDialog.hide();
			}
		});
	}
	
	function ghostLetter() {
		waitingDialog.show('Thinking ....', {
			dialogSize : 'md',
			progressType : 'info'
		});
		ghostLetterAjax();
	}

	function humanLetterAjax() {
		$("#string").append($("#letter").val());
		$("#messageGhost").css('display', 'none');
		$("#messageHuman").css('display', 'inline');
		$("#messageHuman").html("I say '" + $("#letter").val() + "'");
		$.ajax({
			type : "POST",
			url : "humanLetter.htm",
			dataType : 'text',
			data : {
				nextChar : $("#letter").val()
			},
			success : function(status) {
				if(status == ${stauts_not_exists}) {
					$('#modalLabel').html("You lose ....");
					$('#modalBody').html("Sorry the string is not in the dictionary!");
					$('#modalWord').html($("#string").html());
					$('#modalDialog').modal('show');
				} else if(status == ${stauts_is_a_word}) {
					$('#modalLabel').html("You lose ....");
					$('#modalBody').html("The string matches a word!");
					$('#modalDialog').modal('show');
					$('#modalWord').html($("#string").html());
				} else if(status == ${stauts_continue}) {
					$("#humanForm").hide();
					$("#ghostTurn").show();
				} else if(status == ${stauts_draw}) {
					$('#modalLabel').html("Draw ....");
					$('#modalBody').html("The string matches a word, but is shorter than 4!");
					$('#modalDialog').modal('show');
					$('#modalWord').html($("#string").html());
				}
				waitingDialog.hide();
			}
		});
	}
	
	function humanLetter() {
		waitingDialog.show('Thinking ....', {
			dialogSize : 'md',
			progressType : 'info'
		});
		humanLetterAjax();
	}
</script>
</head>

<body>
	<!-- NAVBAR -->
	<div class="navbar navbar-default navbar-fixed">
		<div class="container">
			<div class="navbar-header">
				<a href="/" class="navbar-brand">GHOST GAME CHALLENGE</a>
			</div>
		</div>
	</div>
	
	<!-- JUMBOTRON WITH STRING RESULT -->
	<div class="jumbotron">
    	<div class="container">
    		<div class="col-md-6" style="text-align: left;">
    			<img src="http://2.bp.blogspot.com/-2Bo9UrNuuHI/UmvHqQw_uaI/AAAAAAAAEWs/Ekmz7VUys6Y/s1600/ghosty.png" style="max-height: 80px;">
    			<div id="messageGhost" class="alert alert-dismissible alert-success" style="display: none; padding-right: 20px;"></div>
    		</div>
    		<div class="col-md-6" style="text-align: right;">
	            <div id="messageHuman" class="alert alert-dismissible alert-success" style="display: none; padding-right: 20px;"></div>
    			<img src="http://2014hs.igem.org/wiki/images/a/aa/Team_icon.png" style="max-height: 80px; display: inline">
    		</div>
	      	<div style="text-align: center;">
	        	<h1 id="string">String: </h1>
      		</div>
    	</div>
    </div>	

	<!-- HUMAN & GHOST FORM -->	
	<div class="container">
		<div class="col-md-4 col-md-offset-4" style="text-align: center;">
			<form id="humanForm" class="form-inline" onsubmit="humanLetter();return false;" style="padding-bottom: 60px;">
			  <div class="form-group">
				<label for="letter">Enter your next letter:</label>
				<input type="text" class="form-control" id="letter" maxlength="1" 
		        	style="text-transform: uppercase;width: 40px;display: inline;" required>
		        <button type="submit" class="btn btn-success">Submit</button>
			  </div>
			</form>
			<button id="ghostTurn" class="btn btn-success" onclick="ghostLetter()">Ghost turn</button>
		</div>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="modalDialog"  data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-md">
	    <div class="modal-content">
	      <div id="modalLabel" class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h2 id="modalTitle" class="modal-title"></h2>
	      </div>
	      <div class="modal-body">
	        <h2 id="modalWord"></h2>
	        <h2 id="modalBody"></h2>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-success" data-dismiss="modal">Let's play again!</button>
	      </div>
	    </div>
	  </div>
	</div>


	<div class="navbar navbar-default navbar-fixed-bottom footer">
		<div class="container">
			<div class="col-sm-12 navbar-text">
				<p class="text-muted">JoseManuelBereaLozano</p>
			</div>
		</div>
	</div>

	<script src="resources/js/jquery-1.9.1.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script 
		src="//oss.maxcdn.com/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
	<script src="resources/js/waiting-dialog.js"></script>
	<script type="text/javascript">
		$( document ).ready(function() {
			$('#modalDialog').on('hidden.bs.modal', function () {
				location.reload(true);
			})
			$('#humanForm input[type=text]').on('change invalid', function() {
			    var textfield = $(this).get(0);
			    textfield.setCustomValidity('');
			    if (!textfield.validity.valid) {
			      textfield.setCustomValidity('Please enter a letter... The Ghost is waiting!');
			    }
			});
			$("#ghostTurn").hide();
		});
	</script>
</body>
</html>
