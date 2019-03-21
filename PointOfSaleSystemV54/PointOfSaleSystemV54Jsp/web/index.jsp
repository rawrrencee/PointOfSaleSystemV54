<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Point of Sale System (v5.4) :: Home</title>
        
        <c:url var="href" value="/css/default.css" />
        <link rel="stylesheet" type="text/css" href="${href}" />
    </head>
    <body>
        <h1>Point of Sale System (v5.4) :: Home</h1>
        
        <jsp:include page="/header.jsp" />
        
        <jsp:include page="/mainmenu.jsp" />
        
        <section id="content" class="content">
            <c:if test="${sessionScope.isLogin != true}">
                <article>
                    <h3>Welcome</h3>
                    <p>
                        This version of the retail Point-Of-Sale (POS) system includes the following enhancements:                    
                    </p>
                    <p>
                        <ul style="margin-left: 20px;">
                            <li>Enhancement of existing use cases to meet v5.0 expanded logical data model.</li>
                            <li>Implementation of new use cases in v5.0 (except customer related use cases).</li>
                        </ul>
                    </p>
                </article>
            </c:if>
            <c:if test="${sessionScope.isLogin == true}">
                <article>
                    <h3>Message Of The Day</h3>                    
                    <p>
                        <table style="width: 600px;">
                            <tr>
                                <th>Date</th>
                                <th>Title</th>
                                <th>Message</th>
                            </tr>                            
                            <c:forEach items="${sessionScope.messageOfTheDayEntities}" var="motd">
                                <tr>
                                    <td><fmt:formatDate type="date" value="${motd.messageDate}" pattern="dd/MM/yyyy" /></td>
                                    <td>${motd.title}</td>
                                    <td>${motd.message}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </p>
                </article>
            </c:if>
        </section>
        
        <jsp:include page="/sidebar.jsp" />
        
        <jsp:include page="/footer.jsp" />
    </body>
</html>
