<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table>
	<tr>
		<td><label>Select User:</label></td>
		<td colspan="4">
			<form id="userForm" action="<c:url value="/" />" method="get">
				<select name="user" onchange="document.getElementById('userForm').submit();">
					<option value=""></option>
					<c:forEach var="userId" items="${users}">
						<option value="${userId}" <c:if test="${user eq userId}"> selected="selected" </c:if>>${userId}</option>
					</c:forEach>
				</select>
			</form>
		</td>
	</tr>
	<tr>
		<td><label>Current User:</label></td>
		<td>${user}</td>
		<td width="50"></td>
		<td><label>User Groups:</label></td>
		<td>TODO</td>
	</tr>
</table>

<hr>
