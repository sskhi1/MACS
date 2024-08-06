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
    <title>Create Account</title>
</head>
<body>
<h1>Create New Account</h1>
<p>Please enter proposed name and password.</p>
<form action="Create" method="post">
    <label for="user-name-label"> User Name: </label>
    <input type="text" id="user-name-label" name="username"><br><br>
    <label for="password-label">Password: </label>
    <input type="text" id="password-label" name="password">
    <input type="submit" value="create"><br><br>
</form>

</body>
</html>
