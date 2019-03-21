<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ page import="entity.ProductEntity" %>
<%@ page import="entity.CategoryEntity" %>
<%@ page import="entity.TagEntity" %>
<%@ page import="java.util.List" %>

<jsp:useBean id="productEntityToUpdate" class="entity.ProductEntity" scope="request"/>

<jsp:useBean id="categoryEntities" class="java.util.List<entity.CategoryEntity>" scope="request"/>
<jsp:useBean id="tagEntities" class="java.util.List<entity.TagEntity>" scope="request"/>

<!DOCTYPE html>

<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Point of Sale System (v5.4) :: System Administration :: Update Product</title>
        
        <c:url var="href" value="/css/default.css" />
        <link rel="stylesheet" type="text/css" href="${href}" />
    </head>
    <body>
        <h1>Point of Sale System (v5.4) :: System Administration :: Update Product</h1>
        
        <jsp:include page="/header.jsp" />
        
        <jsp:include page="/mainmenu.jsp" />
        
        <section id="content" class="content">
            
            <div>
                <h3>Update Product</h3>
                
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
                        <c:url var="href" value="/Controller?action=viewProductDetails&productId=${productEntityToUpdate.productId}" />
                        <a href="${href}">Back</a>                        
                    </div>
                    <br/>
                    
                    <c:url var="action" value="/Controller?action=updateProduct_post" />
                    <form action="${action}" method="post">

                        <table style="width: 100%;">
                            <tr>
                                <td style="width: 20%;"><b>Product ID</b></td>
                                <td style="width: 80%;">
                                    ${productEntityToUpdate.productId}
                                    <input type="hidden" id="productId" name="productId" value="${productEntityToUpdate.productId}" />
                                </td>
                            </tr>  
                            <tr>
                                <td><b>SKU Code</b></td>
                                <td><input id="skuCode" name="skuCode" type="text" placeholder="PROD999" required value="${productEntityToUpdate.skuCode}" /></td>
                            </tr>                        
                            <tr>
                                <td><b>Name</b></td>
                                <td><input id="name" name="name" type="text" placeholder="Product 1" required value="${productEntityToUpdate.name}" /></td>
                            </tr>
                            <tr>
                                <td><b>Description</b></td>
                                <td><input id="description" name="description" type="text" placeholder="Product 1" required value="${productEntityToUpdate.description}" /></td>
                            </tr>
                            <tr>
                                <td><b>Quantity On Hand</b></td>
                                <td><input id="quantityOnHand" name="quantityOnHand" type="number" min="0" max="1000000" placeholder="99" required value="${productEntityToUpdate.quantityOnHand}" /></td>
                            </tr>
                            <tr>
                                <td><b>Reorder Quantity</b></td>
                                <td><input id="reorderQuantity" name="reorderQuantity" type="number" min="0" max="1000000" placeholder="99" required value="${productEntityToUpdate.reorderQuantity}" /></td>
                            </tr>
                            <tr>
                                <td><b>Unit Price</b></td>
                                <td><input id="unitPrice" name="unitPrice" type="number" min="0" max="1000000" placeholder="10.00" required value="${productEntityToUpdate.unitPrice}" /></td>
                            </tr>
                            <tr>
                                <td><b>Product Rating</b></td>
                                <td><input id="productRating" name="productRating" type="range" min="1" max="5" required value="${productEntityToUpdate.productRating}" /></td>
                            </tr>
                            <tr>
                                <td><b>Category</b></td>
                                <td>
                                    <select id="categoryId" name="categoryId">
                                        <option value="" selected disabled >-- Select a Category --</option>

                                        <c:forEach items="${categoryEntities}" var="categoryEntity">

                                            <c:if test="${categoryEntity.categoryId != productEntityToUpdate.categoryEntity.categoryId}">
                                                <option value="${categoryEntity.categoryId}">${categoryEntity.name}</option>
                                            </c:if>
                                            <c:if test="${categoryEntity.categoryId == productEntityToUpdate.categoryEntity.categoryId}">
                                                <option selected value="${categoryEntity.categoryId}">${categoryEntity.name}</option>
                                            </c:if>

                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td><b>Tag(s)</b></td>
                                <td>
                                    <ul style="margin-left: 20px;">
                                        <c:forEach items="${productEntityToUpdate.tagEntities}" var="tagEntity">
                                            <li>${tagEntity.name}</li>
                                        </c:forEach>
                                    </ul>
                                    
                                    <br/>
                                    
                                    <select id="tagIds" name="tagIds" multiple>

                                        <c:forEach items="${tagEntities}" var="tagEntity">

                                            <option value="${tagEntity.tagId}">${tagEntity.name}</option>

                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" style="text-align: center;">
                                    <input type="reset" value="Clear" /> 
                                    <input type="submit" value="Update" />
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
