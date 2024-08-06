<%--
  Created by IntelliJ IDEA.
  User: Saba
  Date: 5/30/2023
  Time: 10:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Try Again</title>
</head>
<body>
<h1>Please Try Again</h1>
<p>Either your username or password is incorrect. Please try again.</p>
<form action="login" method="post">
    <label for="user-name-label"> User Name: </label>
    <input type="text" id="user-name-label" name="username"><br><br>
    <label for="password-label">Password: </label>
    <input type="text" id="password-label" name="password">
    <input type="submit" value="Login">
    <br>
</form>
<a href="create.jsp"> Create New Account</a>

</body>
</html>
