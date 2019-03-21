<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<header class="header">
    <aside id="loginbox" class="loginbox">
        <c:if test="${sessionScope.isLogin != true}">
            <c:url var="action" value="/Controller?action=login_post" />
            <form action="${action}" method="post">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" size="16" required maxlength="32" />
                <label for="password">Password</label>
                <input type="password" id="password" name="password" size="16" required maxlength="32" />
                <input type="reset" value="Clear" />
                <input type="submit" value="Login" />
            </form>
            <c:if test="${requestScope.loginError != null}">
                <div style="text-align: right; color: red;">${requestScope.loginError}</div>
            </c:if>
        </c:if>
        <c:if test="${sessionScope.isLogin == true}">
            <c:url var="action" value="/Controller?action=logout_post" />
            <form action="${action}" method="post">                
                You are login as ${sessionScope.currentStaffEntity.firstName} ${sessionScope.currentStaffEntity.lastName} (Access Right: ${sessionScope.currentStaffEntity.accessRightEnum}) <input type="submit" value="Logout" />
            </form>
        </c:if>
    </aside>
</header>