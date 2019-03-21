<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Point of Sale System (v5.4) :: System Administration :: Create New Staff</title>
        
        <c:url var="href" value="/css/default.css" />
        <link rel="stylesheet" type="text/css" href="${href}" />
    </head>
    <body>
        <h1>Point of Sale System (v5.4) :: System Administration :: Create New Staff</h1>
        
        <jsp:include page="/header.jsp" />
        
        <jsp:include page="/mainmenu.jsp" />
        
        <section id="content" class="content">
            
            <div>
                <h3>Create New Staff</h3>
                
            </div>
            
        </section>
        
        <jsp:include page="/sidebar.jsp" />
        
        <jsp:include page="/footer.jsp" />
    </body>
</html>
