<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ page import="entity.CategoryEntity" %>
<%@ page import="entity.TagEntity" %>
<%@ page import="java.util.List" %>

<jsp:useBean id="categoryEntities" class="java.util.List<entity.CategoryEntity>" scope="request"/>
<jsp:useBean id="tagEntities" class="java.util.List<entity.TagEntity>" scope="request"/>

<!DOCTYPE html>

<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Point of Sale System (v5.4) :: System Administration :: Create New Product</title>
        
        <c:url var="href" value="/css/default.css" />
        <link rel="stylesheet" type="text/css" href="${href}" />
    </head>
    <body>
        <h1>Point of Sale System (v5.4) :: System Administration :: Create New Product</h1>
        
        <jsp:include page="/header.jsp" />
        
        <jsp:include page="/mainmenu.jsp" />
        
        <section id="content" class="content">
            
            <div>
                <h3>Create New Product</h3>
                
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
                    
                <br/>
                    
                <c:url var="action" value="/Controller?action=createNewProduct_post" />
                <form action="${action}" method="post">
                              
                    <table style="width: 100%;">
                        <tr>
                            <td style="width: 20%;"><b>SKU Code</b></td>
                            <td style="width: 80%;"><input id="skuCode" name="skuCode" type="text" placeholder="PROD999" required /></td>
                        </tr>                        
                        <tr>
                            <td><b>Name</b></td>
                            <td><input id="name" name="name" type="text" placeholder="Product 1" required /></td>
                        </tr>
                        <tr>
                            <td><b>Description</b></td>
                            <td><input id="description" name="description" type="text" placeholder="Product 1" required /></td>
                        </tr>
                        <tr>
                            <td><b>Quantity On Hand</b></td>
                            <td><input id="quantityOnHand" name="quantityOnHand" type="number" min="0" max="1000000" placeholder="99" required /></td>
                        </tr>
                        <tr>
                            <td><b>Reorder Quantity</b></td>
                            <td><input id="reorderQuantity" name="reorderQuantity" type="number" min="0" max="1000000" placeholder="99" required /></td>
                        </tr>
                        <tr>
                            <td><b>Unit Price</b></td>
                            <td><input id="unitPrice" name="unitPrice" type="number" min="0" max="1000000" placeholder="10.00" required /></td>
                        </tr>
                        <tr>
                            <td><b>Product Rating</b></td>
                            <td><input id="productRating" name="productRating" type="range" min="1" max="5" required /></td>
                        </tr>
                        <tr>
                            <td><b>Category</b></td>
                            <td>
                                <select id="categoryId" name="categoryId">
                                    <option value="" selected disabled >-- Select a Category --</option>

                                    <c:forEach items="${categoryEntities}" var="categoryEntity">

                                        <option value="${categoryEntity.categoryId}">${categoryEntity.name}</option>

                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td><b>Tag(s)</b></td>
                            <td>
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
                                <input type="submit" value="Create" />
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            
        </section>
        
        <jsp:include page="/sidebar.jsp" />
        
        <jsp:include page="/footer.jsp" />
    </body>
</html>
