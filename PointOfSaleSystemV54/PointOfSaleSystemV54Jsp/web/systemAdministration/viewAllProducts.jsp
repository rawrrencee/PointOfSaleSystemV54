<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ page import="entity.ProductEntity" %>
<%@ page import="java.util.List" %>

<jsp:useBean id="productEntities" class="java.util.List<entity.ProductEntity>" scope="request"/>

<!DOCTYPE html>

<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Point of Sale System (v5.4) :: System Administration :: View All Products</title>
        
        <c:url var="href" value="/css/default.css" />
        <link rel="stylesheet" type="text/css" href="${href}" />
    </head>
    <body>
        <h1>Point of Sale System (v5.4) :: System Administration :: View All Products</h1>
        
        <jsp:include page="/header.jsp" />
        
        <jsp:include page="/mainmenu.jsp" />
        
        <section id="content" class="content">
            
            <div>
                <h3>All Products</h3>
                <table style="width: 100%;">
                    <tr>                        
                        <th>SKU Code</th>
                        <th>Product ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Quantity On Hand</th>
                        <th>Unit Price</th>
                        <th>Category</th>
                        <th>Action</th>
                    </tr>
                    
                    <c:forEach items="${productEntities}" var="productEntity">
                        
                        <tr>
                            <td>${productEntity.skuCode}</td>
                            <td>${productEntity.productId}</td>
                            <td>${productEntity.name}</td>
                            <td>${productEntity.description}</td>
                            <td>${productEntity.quantityOnHand}</td>
                            <td style="text-align: right;"><fmt:formatNumber type="currency" currencySymbol="$" groupingUsed="true" value="${productEntity.unitPrice}" /></td>
                            <td>${productEntity.categoryEntity.name}</td>                            
                            <td>
                                <c:url var="href" value="/Controller?action=viewProductDetails&productId=${productEntity.productId}" />
                                <a href="${href}">View</a>
                            </td>
                        </tr>
                        
                    </c:forEach>
                    
                </table>
            </div>
            
        </section>
        
        <jsp:include page="/sidebar.jsp" />
        
        <jsp:include page="/footer.jsp" />
    </body>
</html>
