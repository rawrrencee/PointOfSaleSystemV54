<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ page import="entity.ProductEntity" %>

<jsp:useBean id="productEntityToView" class="entity.ProductEntity" scope="request"/>

<!DOCTYPE html>

<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Point of Sale System (v5.4) :: System Administration :: View Product Details</title>
        
        <c:url var="href" value="/css/default.css" />
        <link rel="stylesheet" type="text/css" href="${href}" />
    </head>
    <body>
        <h1>Point of Sale System (v5.4) :: System Administration :: View Product Details</h1>
        
        <jsp:include page="/header.jsp" />
        
        <jsp:include page="/mainmenu.jsp" />
        
        <section id="content" class="content">
            
            <div>
                <h3>View Product Details</h3>
                
                <c:if test="${requestScope.productNotFoundException != null}">
                    <br/>
                    <span style="color:red;">${requestScope.productNotFoundException}</span>
                </c:if>
                
                <c:if test="${requestScope.productNotFoundException == null}">
                    <br/>
                    <div>
                        <c:url var="href" value="/Controller?action=viewAllProducts" />
                        <a href="${href}">Back</a> | 
                        <c:url var="href" value="/Controller?action=updateProduct&productId=${productEntityToView.productId}" />
                        <a href="${href}">Update</a> | 
                        <c:url var="href" value="/Controller?action=deleteProduct&productId=${productEntityToView.productId}" />
                        <a href="${href}">Delete</a>
                    </div>
                    <br/>

                    <table style="width: 100%;">
                        <tr>
                            <td style="width: 20%;"><b>Product ID</b></td>
                            <td style="width: 80%;">${productEntityToView.productId}</td>
                        </tr>
                        <tr>
                            <td><b>SKU Code</b></td>
                            <td>${productEntityToView.skuCode}</td>
                        </tr>
                        <tr>
                            <td><b>Name</b></td>
                            <td>${productEntityToView.name}</td>
                        </tr>
                        <tr>
                            <td><b>Description</b></td>
                            <td>${productEntityToView.description}</td>
                        </tr>
                        <tr>
                            <td><b>Quantity On Hand</b></td>
                            <td>${productEntityToView.quantityOnHand}</td>
                        </tr>
                        <tr>
                            <td><b>Reorder Quantity</b></td>
                            <td>${productEntityToView.reorderQuantity}</td>
                        </tr>
                        <tr>
                            <td><b>Unit Price</b></td>
                            <td><fmt:formatNumber type="currency" currencySymbol="$" groupingUsed="true" value="${productEntityToView.unitPrice}" /></td>
                        </tr>
                        <tr>
                            <td><b>Product Rating</b></td>
                            <td>${productEntityToView.productRating}</td>
                        </tr>
                        <tr>
                            <td><b>Category</b></td>
                            <td>${productEntityToView.categoryEntity.name}</td>
                        </tr>
                        <tr>
                            <td><b>Tag(s)</b></td>
                            <td>
                                <ul style="margin-left: 20px;">
                                    <c:forEach items="${productEntityToView.tagEntities}" var="tagEntity">
                                        <li>${tagEntity.name}</li>
                                    </c:forEach>
                                </ul>
                            </td>
                        </tr>
                    </table>
                </c:if>
            </div>
            
        </section>
        
        <jsp:include page="/sidebar.jsp" />
        
        <jsp:include page="/footer.jsp" />
    </body>
</html>
