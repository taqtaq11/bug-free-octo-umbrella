<%--
  Created by IntelliJ IDEA.
  User: Alexander
  Date: 24/02/16
  Time: 20:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Angular test</title>

    <script src="angular.min.js"></script>
</head>
<body>
    <h1>Hello, angular!</h1>

    <div ng-app="">
        <p>Name : <input type="text" ng-model="name"></p>
        <h1>Hello {{name}}</h1>
    </div>
</body>
</html>
