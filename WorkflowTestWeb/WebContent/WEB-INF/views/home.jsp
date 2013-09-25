<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<p><label>Tasks</label></p>
<table>
	<tr>
		<th>Process Id</th>
		<th>Name</th>
		<th>Subject</th>
		<th>Description</th>
		<th>Status</th>
		<th>&nbsp;</th>
	</tr>
	<c:forEach var="task" items="${tasks}">
		<tr>
			<td>${task.processId}</td>
			<td>${task.name}</td>
			<td>${task.subject}</td>
			<td>${task.description}</td>
			<td>${task.status}</td>
			<td>
				<form action="<c:url value="/task/${task.id}/startAndComplete" />" method="post">
					<input type="submit" value="Complete">
				</form>
			</td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="6" align="right">
			<form action="<c:url value="/process/new" />" method="post">
				<input type="submit" value="New Reservation Process">
			</form>
		</td>
	</tr>
</table>
