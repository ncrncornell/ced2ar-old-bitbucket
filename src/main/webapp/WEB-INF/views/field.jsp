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
	<div class="row">
		<div class="col-sm-12">&nbsp;</div>
	</div>
	<div class="row">

		<div class="col-sm-12">
			<h1>Fields</h1>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-12">&nbsp;</div>
	</div>
	<table class="table table-striped">
		<tr>
			<th></th>
			<th>Codebook</th>
			<th></th>
			<th></th>
		</tr>
		<!--  Process -->
		<c:forEach var="field" items="${fields}">
			<tr>
				<td></td>
				<td>${field.displayName}</td>
				<!--  td>
					<a class="btn btn-default" href="codebooks/${codebook.handle}/filedesc">
						FileDesc
					</a> 
					<a class="btn btn-default" href="codebooks/${codebook.handle}/docdesc">
						DocDesc
					</a> 
				</td-->
			</tr>
		</c:forEach>
		<!--  END Process -->
	</table>

	<div class="row">
		<div class="col-sm-6">
	
		</div>
		<div class="col-sm-6">
			<a href="search">Search Variables</a>

		</div>
	</div>


</body>
</html>