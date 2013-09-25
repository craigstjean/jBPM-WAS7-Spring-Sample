<!DOCTYPE html>
<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib prefix="tiles" uri="/WEB-INF/tld/tiles-jsp.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<meta charset="utf-8" />
	<title>Test</title>
</head>
<body>
	<div id="header" class="header">
		<tiles:insertAttribute name="header" />
	</div>
	
	<div class="row">
		<tiles:insertAttribute name="content" />
	</div>
</body>
</html>
