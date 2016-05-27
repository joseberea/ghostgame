<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jsp"%>
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
		<label class="control-label" for="inputLetter">Enter letter:</label>
		<input type="text" class="form-control" id="inputLetter" maxlength="1"
			style='text-transform: uppercase';> <a href="#" class="btn btn-success" onclick="waitingDialog.show('Thinking ....', {dialogSize: 'md', progressType: 'info'});waitingDialog.hide()">Submit</a>
	</div>

	<div class="navbar navbar-default navbar-fixed-bottom footer">
		<div class="container">
			<div class="col-sm-12 navbar-text">
				<p class="text-muted">Jose Manuel Berea Lozano</p>
			</div>
		</div>
	</div>

	<script src="resources/js/jquery-1.9.1.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/waiting-dialog.js"></script>
</body>
</html>
