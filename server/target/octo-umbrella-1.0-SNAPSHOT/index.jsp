<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  org.octoteam.octoproject.Entities.User: Alexander
  Date: 17/02/16
  Time: 22:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <h1>Octo Project 0.1</h1>
    <p>Your ip: <%= request.getRemoteAddr().toString() %></p>
    <p>Server time: <%= (new Date()).toString() %></p>

    <h2>Auth service</h2>

    <h3>[rest/auth/signin] Sign in form:</h3>
    <form action="rest/auth/signin" method="post">
        Login:<br>
        <input type="text" name="login"><br>

        Password:<br>
        <input type="password" name="password"><br>

        <input type="submit" value="Login">
    </form>

    <h3>[rest/auth/signup] Sign up form:</h3>
    <form action="rest/auth/signup" method="post">
        Name:<br>
        <input type="text" name="name"><br>

        Login:<br>
        <input type="text" name="login"><br>

        Password:<br>
        <input type="password" name="password"><br>

        <input type="submit" value="Register">
    </form>

    <h3>[rest/auth/signout] Session close form:</h3>
    <form action="rest/auth/signout" method="post">
        Token:<br>
        <input type="text" name="token"><br>

        <input type="submit" value="Close">
    </form>

    <h2>Users service</h2>

    <h3>[rest/users/get] User get(detail info):</h3>
    <form action="rest/users/get" method="post">
        User id:<br>
        <input type="number" name="user_id"><br>

        <input type="submit" value="Go!">
    </form>

    <h3>[rest/users/search] User search(list now):</h3>
    <form action="rest/users/search" method="post">
        <input type="submit" value="Go!">
    </form>

    <h3>[rest/users/update] User info update:</h3>
    <form action="rest/users/update" method="post">
        Token:<br>
        <input type="text" name="token"><br>

        New name:<br>
        <input type="text" name="name"><br>

        Old password:<br>
        <input type="password" name="old_password"><br>

        New password:<br>
        <input type="password" name="new_password"><br>

        Avatar:<br>
        <input type="text" name="avatar_file"><br>

        <input type="submit" value="Go!">
    </form>

    <h3>[rest/users/follow] Follow user:</h3>
    <form action="rest/users/follow" method="post">
        Token:<br>
        <input type="text" name="token"><br>

        User id to follow:<br>
        <input type="number" name="user_id"><br>

        <input type="submit" value="Go!">
    </form>

    <h3>[rest/users/unfollow] Unfollow user:</h3>
    <form action="rest/users/unfollow" method="post">
        Token:<br>
        <input type="text" name="token"><br>

        User id to unfollow:<br>
        <input type="number" name="user_id"><br>

        <input type="submit" value="Go!">
    </form>

</body>
</html>
