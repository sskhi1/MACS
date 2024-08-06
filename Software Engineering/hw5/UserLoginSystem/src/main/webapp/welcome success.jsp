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
    <title>Welcome Success</title>
</head>
<body>
<h1>Welcome <%= request.getParameter("username")%></h1>
</body>
</html>
