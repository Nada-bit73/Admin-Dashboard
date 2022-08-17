<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"       "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="windows-1256">
<title>Super Admin Dashboard</title>
<!-- for Bootstrap CSS -->
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
<!-- YOUR own local CSS -->
<link rel="stylesheet" href="/css/main.css" />
<script type="text/javascript" src="js/base.js"></script>

<!-- For any Bootstrap that uses JS or jQuery-->
<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>

</head>
<body>
	<div class="d-flex justify-content-center m-4 container">
		<div class="align-items-center">
			<div class="row ml-5 mt-1 ">
				<div class="span6">
					<div class="row ml-5 mt-2 mr-3">
						<h1 class="form-heading mb-5 mr-3">
							Welcome
							<c:out value="${userFirstName}"></c:out>
							!
						</h1>
						<div class="col-md-3">
							<a href="/logout">logout</a>
						</div>

					</div>
					<div class="row ml-5 mt-2 mr-3">
						<table class="table table-hover table-dark mt-4">
							<thead>
								<tr>
									<th colspan="1">Name</th>
									<th colspan="1">Email</th>
									<th colspan="1">Role</th>
									<th colspan="2">Action</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="item" items="${users}">
									<tr>
										<td colspan="1"><c:out value="${item.firstName}" /> <c:out
												value="${item.lastName}" /></td>
										<td colspan="1"><c:out value="${item.email}" /></td>
										<td colspan="1"><c:out value="${item.role}" /></td>
										<td colspan="1">
											<form action="/${item.id}/deleteSuperAdmin" method="POST">
												<input type="hidden" name="_method" value="DELETE">
												<input type="submit" value="delete"
													class="btn btn-link mb-1">
											</form> <c:if test="${item.role != 'admin'}">
												<form action="/${item.id}/makeadmin/superAdmin" method="POST">
													<input type="submit" value="make-admin"
														class="btn btn-link mb-1">
												</form>
											</c:if>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>

			</div>
		</div>
	</div>
</body>
</html>