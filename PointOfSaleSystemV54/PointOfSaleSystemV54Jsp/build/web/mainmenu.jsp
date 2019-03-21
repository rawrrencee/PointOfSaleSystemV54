<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<section id="mainmenu" class="mainmenu">
    <h3>Main Menu</h3>
    <nav>
        <br/>
        <c:url var="href" value="/index.jsp" />
        <a href="${href}">Home</a>
        <br/><br/>
        <c:if test="${sessionScope.isLogin == true}">
            <c:url var="href" value="/Controller?action=logout_post" />
            <a href="${href}">Logout</a>
            <br/><br/>
            <h4 class="menuHeader1">Cashier Operation</h4>				
            <ul class="secondaryMenu">
                <c:url var="href" value="/Controller?action=checkout" />
                <li><a href="${href}">Checkout</a></li>
                <c:url var="href" value="/Controller?action=voidRefund" />
                <li><a href="${href}">Void/Refund</a></li>
                <c:url var="href" value="/Controller?action=viewMySaleTransactions" />
                <li><a href="${href}">View My Sale Transactions</a></li>
            </ul>
            <br/>
            <h4 class="menuHeader1">System Administration</h4>
            <h5 class="menuHeader2">Staff</h5>
            <ul class="secondaryMenu">
                <c:url var="href" value="/Controller?action=createNewStaff" />
                <li><a href="${href}">Create New Staff</a></li>                
                <c:url var="href" value="/Controller?action=viewAllStaffs" />
                <li><a href="${href}">View All Staffs</a></li>
            </ul>
            <h5 class="menuHeader2">Product</h5>
            <ul class="secondaryMenu">
                <c:url var="href" value="/Controller?action=createNewProduct" />
                <li><a href="${href}">Create New Product</a></li>                
                <c:url var="href" value="/Controller?action=viewAllProducts" />
                <li><a href="${href}">View All Products</a></li>
            </ul>
            <br/><br/>
        </c:if>
    </nav>
</section>