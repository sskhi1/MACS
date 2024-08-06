<%--
  Created by IntelliJ IDEA.
  User: Saba
  Date: 5/30/2023
  Time: 10:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<h1>Welcome to Homework 5</h1>
<p>Please log in.</p>
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
