<%-- 
    Document   : index
    Created on : 2019/1/2, 下午 03:47:44
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authentication var="user" property="principal" />
<sec:authorize access="isAuthenticated()"  var="isLogin" />
<sec:authorize access="hasRole('USER')"  var="isUser" />
<sec:authorize access="hasRole('OPER')"  var="isOper" />
<sec:authorize access="hasRole('ADMIN')"  var="isAdmin" />

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=0">
        <meta name="description" content="">
        <meta name="author" content="">
        <link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />"/>

        <!-- Bootstrap core CSS -->
        <link href="<c:url value="/libs/bootstrap/bootstrap.css" />" rel="stylesheet">
        <link href="<c:url value="/libs/opensans.css" />" rel="stylesheet">
        <link href="<c:url value="/libs/jquery.mCustomScrollbar/style3.css" />" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="<c:url value="/jumbotron-narrow.css" />" rel="stylesheet">

        <link href="<c:url value="/libs/jquery.mCustomScrollbar/jquery.mCustomScrollbar.min.css" />" rel="stylesheet">

        <style>
            #gotop {
                position: fixed;
                right: 20px;
                bottom: 30px;
                padding: 10px 16px;
                font-size: 25px;
                background: rgba(0, 0, 0, 0.6);
                color: #FAFCFD;
                cursor: pointer;
                z-index: 1000;
            }
            body{
                font-family: 微軟正黑體;
            }
        </style>

        <script src="<c:url value="/libs/jQuery/jquery.js" />"></script> 
        <script src="<c:url value="/libs/bootstrap/bootstrap.js" />"></script>
        <script src="<c:url value="/libs/popper.js/popper.js" />" ></script>
        <script src="<c:url value="/libs/font-awesome/solid.js" />" ></script>
        <script defer src="<c:url value="/libs/font-awesome/fontawesome.js"/>" ></script>
        <script defer src="<c:url value="/libs/jquery.mousewheel/jquery.mousewheel.min.js" />"></script>
        <script defer src="<c:url value="/libs/jquery.mCustomScrollbar/jquery.mCustomScrollbar.min.js" />"></script>

        <script src="<c:url value="/libs/jsog/JSOG.js" />"></script>

        <script>
            $(function () {
                $("#sidebar").mCustomScrollbar({
                    theme: "minimal"
                });

                $('#dismiss, .overlay').on('click', function () {
                    // hide sidebar
                    $('#sidebar').removeClass('active');
                    // hide overlay
                    $('.overlay').removeClass('active');
                });

                $('#sidebarCollapse').on('click', function () {
                    // open sidebar
                    $('#sidebar').addClass('active');
                    // fade in the overlay
                    $('.overlay').addClass('active');
                    $('.collapse.in').toggleClass('in');
                    $('a[aria-expanded=true]').attr('aria-expanded', 'false');
                });

                $("#gotop").click(function () {
                    $("html,body").animate({
                        scrollTop: 0
                    }, 100);
                });

                $(window).scroll(function () {
                    if ($(this).scrollTop() > 200) {
                        $('#gotop').fadeIn("fast");
                    } else {
                        $('#gotop').stop().fadeOut("fast");
                    }
                });
            });
        </script>
    </head>

    <body>

        <div class="wrapper">
            <!-- Sidebar -->
            <nav id="sidebar">

                <div id="dismiss">
                    <i class="fas fa-arrow-left"></i>
                </div>

                <div class="sidebar-header">
                    <h3>拉備料儲區<br>定位系統2.0</h3>
                </div>

                <ul class="list-unstyled components">
                    <!--<p>Dummy Heading</p>-->
                    <li class="active">
                        <a href="#sub1" class="dropdown-toggle" data-toggle="collapse" aria-expanded="false">拉料</a>
                        <ul class="collapse list-unstyled show" id="sub1">
                            <!--                            <li>
                                                            <a href="layout.jsp?content=warehouse&floor_id=6#">4F</a>
                                                        </li>-->
                            <c:if test="${isAdmin}">
                                <li>
                                    <a href="layout.jsp?content=warehouse&floor_id=1#">5F</a>
                                </li>
                                <li>
                                    <a href="layout.jsp?content=warehouse&floor_id=2#">6F</a>
                                </li>
                                <li>
                                    <a href="layout.jsp?content=warehouse&floor_id=3#">7F</a>
                                </li>
                                <li>
                                    <a href="layout.jsp?content=warehouse&floor_id=5#">TEST_F</a>
                                </li>
                            </c:if>
                            <li>
                                <a href="layout.jsp?content=whPosition&map=_map_storagespace&floor_id=7#">7FDemo</a>
                            </li>
                        </ul>
                    </li>
                    <c:if test="${isAdmin}">
                        <li>
                            <a href="#sub2" class="dropdown-toggle" data-toggle="collapse" aria-expanded="true">備料</a>
                            <ul class="collapse list-unstyled show" id="sub2">
                                <!--                            <li>
                                                                <a href="layout.jsp?content=lineSchedule&floor_id=1#">5F</a>
                                                            </li>-->
                                <li>
                                    <a href="layout.jsp?content=lineSchedule&floor_id=2#">6F</a>
                                </li>
                                <li>
                                    <a href="layout.jsp?content=lineSchedule&floor_id=3#">7F</a>
                                </li>
                                <c:if test="${isAdmin}">
                                    <li>
                                        <a href="layout.jsp?content=lineSchedule&floor_id=5#">TEST_F</a>
                                    </li>
                                </c:if>
                            </ul>
                        </li>
                    </c:if>
                    <li>
                        <a href="<c:url value="http://172.20.128.223/SAP/Login.aspx " />">拉備料系統</a>
                    </li>
                    <li>
                        <a href="<c:url value="/logout" />" class="text-danger">登出</a>
                    </li>
                </ul>
            </nav>

            <!-- Page Content -->
            <div id="content">
                <nav class="navbar navbar-expand-lg navbar-light bg-light">
                    <div class="container-fluid">

                        <button type="button" id="sidebarCollapse" class="btn btn-info">
                            <i class="fas fa-align-left"></i>
                            <span>Toggle Sidebar</span>
                        </button>

                        <div class="collapse navbar-collapse" id="navbarSupportedContent">
                            <ul class="nav navbar-nav ml-auto">
                                <c:if test="${isLogin}">
                                    <li class="nav-item active">
                                        <font class="nav-link">Hello, <c:out value="${user.username}" /></font>
                                    </li>
                                    <li class="nav-item active">
                                        <a href="<c:url value="/logout" />" class="nav-link">登出</a>
                                    </li>
                                </c:if>
                                <!--                                <li class="nav-item active">
                                                                    <a class="nav-link" href="#">Page</a>
                                                                </li>
                                                                <li class="nav-item">
                                                                    <a class="nav-link" href="#">Page</a>
                                                                </li>
                                                                <li class="nav-item">
                                                                    <a class="nav-link" href="#">Page</a>
                                                                </li>
                                                                <li class="nav-item">
                                                                    <a class="nav-link" href="#">Page</a>
                                                                </li>-->
                            </ul>
                        </div>

                    </div>
                </nav>
                <div class="container-fluid">
                    <!--                    <div class="header row">
                                            <h3 class="text-muted">WS Test Page Client</h3>
                                        </div>-->
                    <div class="row">
                        <c:catch var="e">
                            <c:import url="${param.content}.jsp" />
                        </c:catch>
                        <c:if test="${!empty e}">
                            Error: page not found
                            <%--<c:out value="${e}" />--%>
                        </c:if>
                    </div>
                </div>

                <!-- Dark Overlay element -->
                <div class="overlay"></div>

                <div id="gotop">˄</div>
            </div>
    </body>
</html>

