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
	var thinking = '<fmt:message key="text.thinking"/>';
	var say = '<fmt:message key="text.say"/>';
	var lose = '<fmt:message key="text.lose"/>';
	var win = '<fmt:message key="text.win"/>';
	
	function matchesWord() {
		var matches = '<fmt:message key="text.matches"/>';
		$('#modalBody').html(matches);
		$('#modalDialog').modal('show');
		$('#modalWord').html($("#string").html());
	}
	
	function matchesShortWord() {
		var matchesShorter = '<fmt:message key="text.matchesShorter"/>';
		var draw = '<fmt:message key="text.draw"/>';
		$('#modalLabel').html(draw);
		$('#modalBody').html(matchesShorter);
		$('#modalDialog').modal('show');
		$('#modalWord').html($("#string").html());
	}
	
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
					$('#modalLabel').html(win);
					matchesWord();
				} else if(status == ${stauts_continue}) {
					$("#humanForm").show();
					$("#ghostTurn").hide();
					$("#messageGhost").css('display', 'inline');
					$("#messageGhost").html(say + " '" + letter + "'");
				} else if(status == ${stauts_draw}) {
					matchesShortWord();
				}
				waitingDialog.hide();
			}
		});
	}
	
	function ghostLetter() {
		waitingDialog.show(thinking, {
			dialogSize : 'md',
			progressType : 'info'
		});
		ghostLetterAjax();
	}

	function humanLetterAjax() {
		var notExists = '<fmt:message key="text.notExists"/>';
		$("#string").append($("#letter").val());
		$("#messageGhost").css('display', 'none');
		$("#messageHuman").css('display', 'inline');
		$("#messageHuman").html(say + " '" + $("#letter").val() + "'");
		$.ajax({
			type : "POST",
			url : "humanLetter.htm",
			dataType : 'text',
			data : {
				nextChar : $("#letter").val()
			},
			success : function(status) {
				if(status == ${stauts_not_exists}) {
					$('#modalLabel').html(lose);
					$('#modalBody').html(notExists);
					$('#modalWord').html($("#string").html());
					$('#modalDialog').modal('show');
				} else if(status == ${stauts_is_a_word}) {
					$('#modalLabel').html(lose);
					matchesWord();
				} else if(status == ${stauts_continue}) {
					$("#humanForm").hide();
					$("#ghostTurn").show();
				} else if(status == ${stauts_draw}) {
					matchesShortWord();
				}
				waitingDialog.hide();
			}
		});
	}
	
	function humanLetter() {
		waitingDialog.show(thinking, {
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
				<a href="/" class="navbar-brand"><fmt:message key="title.title"/></a>
			</div>
		</div>
	</div>
	
	<!-- JUMBOTRON WITH STRING RESULT -->
	<div class="jumbotron">
    	<div class="container">
    		<div class="col-md-6 left-aligned">
    			<img src="resources/img/ghost.png" class="player-img">
    			<div id="messageGhost" class="alert alert-dismissible alert-success message-alert"></div>
    		</div>
    		<div class="col-md-6 right-aligned">
	            <div id="messageHuman" class="alert alert-dismissible alert-success message-alert"></div>
    			<img src="resources/img/player.png" class="player-img">
    		</div>
	      	<div class="center-aligned">
	        	<h1 id="string"><fmt:message key="label.string"/>:&nbsp;</h1>
      		</div>
    	</div>
    </div>	

	<!-- HUMAN & GHOST FORM -->	
	<div class="container">
		<div class="col-md-4 col-md-offset-4 center-aligned">
			<form id="humanForm" class="form-inline human-form" onsubmit="humanLetter();return false;" data-toggle="validator">
			  <div class="form-group">
				<label for="letter"><fmt:message key="label.getLetter"/>:</label>
				<input type="text" class="form-control human-input" id="letter" maxlength="1" pattern="[a-zA-Z]" required>
		        <button type="submit" class="btn btn-success"><fmt:message key="label.submit"/></button>
			  </div>
			</form>
			<button id="ghostTurn" class="btn btn-success" onclick="ghostLetter()"><fmt:message key="label.ghostTurn"/></button>
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
	        <button type="button" class="btn btn-success" data-dismiss="modal"><fmt:message key="label.playAgain"/></button>
	      </div>
	    </div>
	  </div>
	</div>


	<div class="navbar navbar-default navbar-fixed-bottom footer">
		<div class="container">
			<div class="col-md-12 navbar-text left-aligned">
				<p class="text-muted"><fmt:message key="footer.footer"/></p>
			</div>
		</div>
	</div>

	<script src="resources/js/jquery-1.9.1.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/bootstrapvalidator.min.js"></script>
	<script src="resources/js/waiting-dialog.js"></script>
	<script type="text/javascript">
		$( document ).ready(function() {
			$('#modalDialog').on('hidden.bs.modal', function () {
				location.reload(true);
			})
			$('#humanForm input[type=text]').on('change invalid', function() {
				var errorLetter = '<fmt:message key="error.letter"/>';
			    var textfield = $(this).get(0);
			    textfield.setCustomValidity('');
			    if (!textfield.validity.valid) {
			      textfield.setCustomValidity(errorLetter);
			    }
			});
			$("#ghostTurn").hide();
		});
	</script>
</body>
</html>
