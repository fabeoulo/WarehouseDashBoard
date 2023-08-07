<%-- 
    Document   : index
    Created on : 2019/1/2, 下午 03:47:44
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />"/>

        <title>${initParam.pageTitle}</title>

        <script>

        </script>
    </head>

    <body>

        <div class="container">
            <div class="header">
                <h3 class="text-muted">WS Test Page Client</h3>
            </div>

            <div>
                <ul>
<%--                
                    <li>
                        <a href="<c:url value="/pages/layout.jsp?content=warehouse&floor_id=6#" />">4F首頁</a>
                    </li>
--%>
                    <li>
                        <a href="<c:url value="/pages/layout.jsp?content=warehouse&floor_id=1#" />">5F首頁</a>
                    </li>
                    <li>
                        <a href="<c:url value="/pages/layout.jsp?content=warehouse&floor_id=2#" />">6F首頁</a>
                    </li>
                    <li>
                        <a href="<c:url value="/pages/layout.jsp?content=warehouse&floor_id=3#" />">7F首頁</a>
                    </li>
                    <li>
                        <a href="<c:url value="/pages/layout.jsp?content=whPosition&floor_id=7#" />">7FDemo</a>
                    </li>
                </ul>
            </div>

        </div>
        <!-- /container -->
    </body>
</html>

