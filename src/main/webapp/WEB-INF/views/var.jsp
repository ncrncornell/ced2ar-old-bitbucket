<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>CED2AR</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="styles/main.css" />
<link rel="stylesheet" type="text/css" href="//netdna.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="//netdna.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css" />
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

</head>
<body>
	<div class="row">
		<div class="col-sm-12" style="background-color: #B40404;">
			<div class="row">
				<h1 style="color: #FFFFFF">
					&nbsp;&nbsp;&nbsp;&nbsp;CED<sup>2</sup>AR
				</h1>
			</div>
		</div>
	</div>
	<h1>Variable Details:</h1>
	<h2>	${varname}</h2>
	
	<c:forEach var="field" items="${details}">
		<h3>${field.key}</h3>
		<p>${field.value}</p>	
	</c:forEach>
	<p>
		<a href="${baseURI}/ced2ar-rdb/codebooks/${handle}/"
	                   title="View Codebook">Back to ${handle}            
        </a>
	</p>
	<p>
		<a href="${baseURI}/ced2ar-rdb/codebooks/${handle}/vars"
	                   title="View Codebook">Back to ${handle} Variables            
        </a>
	</p>
</body>
</html>