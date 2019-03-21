<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ page import="entity.ProductEntity" %>
<%@ page import="entity.CategoryEntity" %>
<%@ page import="entity.TagEntity" %>
<%@ page import="java.util.List" %>

<jsp:useBean id="productEntityToDelete" class="entity.ProductEntity" scope="request"/>

<!DOCTYPE html>

<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Point of Sale System (v5.4) :: System Administration :: Delete Product</title>
        
        <c:url var="href" value="/css/default.css" />
        <link rel="stylesheet" type="text/css" href="${href}" />
    </head>
    <body>
        <h1>Point of Sale System (v5.4) :: System Administration :: Delete Product</h1>
        
        <jsp:include page="/header.jsp" />
        
        <jsp:include page="/mainmenu.jsp" />
        
        <section id="content" class="content">
            
            <div>
                <h3>Delete Product</h3>
                
                <c:if test="${requestScope.message != null}">
                    <br/>
                    <c:if test="${requestScope.error != true}">
                        <span style="color: green;">${requestScope.message}</span>
                    </c:if>
                    <c:if test="${requestScope.error == true}">
                        <span style="color: red;">${requestScope.message}</span>
                    </c:if>
                    <br/>
                </c:if>                    
                
                <c:if test="${requestScope.productNotFoundException != null}">
                    <br/>
                    <span style="color:red;">${requestScope.productNotFoundException}</span>
                </c:if>
                
                <c:if test="${requestScope.productNotFoundException == null}">
                    
                    <br/>
                    <div>
                        <c:url var="href" value="/Controller?action=viewProductDetails&productId=${productEntityToDelete.productId}" />
                        <a href="${href}">Back</a>                        
                    </div>
                    <br/>
                    
                    <c:url var="action" value="/Controller?action=deleteProduct_post" />
                    <form action="${action}" method="post">

                        <table style="width: 100%;">
                            <tr>
                                <td style="width: 20%;"><b>Product ID</b></td>
                                <td style="width: 80%;">
                                    ${productEntityToDelete.productId}
                                    <input type="hidden" id="productId" name="productId" value="${productEntityToDelete.productId}" />
                                </td>
                            </tr>  
                            <tr>
                                <td><b>SKU Code</b></td>
                                <td>${productEntityToDelete.skuCode}</td>
                            </tr>                        
                            <tr>
                                <td><b>Name</b></td>
                                <td>${productEntityToDelete.name}</td>
                            </tr>                            
                            <tr>
                                <td colspan="2" style="text-align: center;">
                                    <input type="submit" value="Delete" />
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:if>
            </div>
            
        </section>
        
        <jsp:include page="/sidebar.jsp" />
        
        <jsp:include page="/footer.jsp" />
    </body>
</html>
