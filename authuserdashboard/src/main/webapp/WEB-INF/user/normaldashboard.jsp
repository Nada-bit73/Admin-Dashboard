<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isErrorPage="true"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="windows-1256">
<title>Normal User Dashboard</title>
<link rel="stylesheet" type="text/css" href="/css/style.css">
<script type="text/javascript" src="/js/app.js"></script>
<!-- for Bootstrap CSS -->
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
<!-- YOUR own local CSS -->
<link rel="stylesheet" href="/css/main.css" />
<!-- For any Bootstrap that uses JS or jQuery-->
<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>

</head>
<body>
	<div class="d-flex justify-content-center container">
		<div class="align-items-center">
			<div class="row ml-5 mt-1 ">
				<div class="span6">
					<div class="row ml-5 mt-2 mr-3 ">
						<h1 class="form-heading mb-5 mr-3">
							Welcome <c:out value="${userFirstName}"></c:out> !
						</h1>
						<!-- ----------------------------------------------------------- -->
						<div class="row mb-3">
							<h4>
								First Name:
								<c:out value="${user.firstName}" />
							</h4>
						</div>

						<div class="row mb-3">
							<h4>
								Last Name:
								<c:out value="${user.lastName}" />
							</h4>
						</div>

						<div class="row mb-3">
							<h4>
								Email:
								<c:out value="${user.email}" />
							</h4>
						</div>

						<div class="row mb-3">
							<h4>
								Sign Up Date:
								<c:out value="${user.createdAt}" />
							</h4>
						</div>
						
						<div class="row mb-3">
							<h4>
								Last Sign In:
								<c:out value="${user.lastSignin}" />
							</h4>
						</div>

						
					
						<form class="form align-items-right ml-4 mt-5" action="/logout"
							method="GET">
							<button type="submit" class="btn btn-link text-right">logout</button>
						</form>

					</div>

				</div>

			</div>
		</div>
	</div>
</body>
</html>