<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<aside class="sidebar">
    <h3>Advertisement</h3>			
    <figure class="advertisement">
        <c:url var="src" value="/images/300x300.png" />
        <img src="${src}" height="120" width="120" alt="Advertisement 1" />
        <figcaption>Advertisement 1</figcaption>
    </figure>
    <figure class="advertisement">
        <img src="${src}" height="120" width="120" alt="Advertisement 2" />
        <figcaption>Advertisement 2</figcaption>
    </figure>
    <figure class="advertisement">
        <img src="${src}" height="120" width="120" alt="Advertisement 3" />
        <figcaption>Advertisement 3</figcaption>
    </figure>
</aside>