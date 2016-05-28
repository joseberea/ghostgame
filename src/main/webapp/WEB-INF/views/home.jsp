<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jsp"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ghost Game</title>
<link id="bootstrap-style" href="resources/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
<link href="resources/css/custom.css" rel="stylesheet">

<script type="text/javascript">
	function putLetterAjax() {
		$("#string").append($("#letter").val());
		$.ajax({
			type : "POST",
			url : "putLetter.htm",
			dataType : 'text',
			data : {
				nextChar : $("#letter").val()
			},
			success : function(status) {
				if(status == ${stauts_not_exists}) {
					alert("No se encuentra en el diccionario");
				} else if(status == ${stauts_is_a_word}) {
					alert("Es una palabra de 4 o mas");
				} else if(status == ${stauts_continue}) {
					alert("Seguimos");
				}
				waitingDialog.hide();
			}
		});
	}
	function putLetter() {
		waitingDialog.show('Thinking ....', {
			dialogSize : 'md',
			progressType : 'info'
		});
		putLetterAjax();
	}
</script>
</head>

<body>

	<div class="navbar navbar-default navbar-fixed">
		<div class="container">
			<div class="navbar-header">
				<a href="/" class="navbar-brand">GHOST GAME CHALLENGE</a>
			</div>
		</div>
	</div>

	<div class="container-fluid">
		<h1 id="string">LETTERS: </h1>
		<form role="form" class="form-horizontal" method="POST" action="#" name="inputForm" id="inputForm" onsubmit="putLetter();return false;">
			<div class="form-group">
				<div class="col-xs-3"></div> 
				<div class="col-xs-4" style="padding-right:0px;">
					<label for="letter" class="col-xs-8 control-label">Enter letter:</label>
	            	<input type="text" id="letter" name="letter" class="form-control top-buffer-sm"	maxlength="1" 
	            		style="text-transform: uppercase;width: 40px;" required></input>
	        	</div>
				<div class="col-xs-2" style="padding-left:0px;margin-left:0px;">
	            	<button type="submit" class="btn btn-success">Submit</button>
	        	</div>	        	
	        	<div class="col-xs-3"></div> 
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
			$('#inputForm input[type=text]').on('change invalid', function() {
			    var textfield = $(this).get(0);
			    textfield.setCustomValidity('');
			    if (!textfield.validity.valid) {
			      textfield.setCustomValidity('Please enter a letter... The Ghost is waiting!');
			    }
			});
		});
	</script>
</body>
</html>
