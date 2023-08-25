<%-- 
    Document   : map_storagespace_
    Created on : 2023年8月17日, 上午11:12:50
    Author     : Justin.Yeh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.UUID" %>
<!DOCTYPE html>
<html>
    <head>
        <c:set var="userSitefloor" value="${param.sitefloor}" />
        <c:if test="${(userSitefloor == null) || (userSitefloor == '' || userSitefloor < 1 || userSitefloor > 7)}">
            <c:redirect url="index.jsp" />
        </c:if>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${userSitefloor}F儲區定位圖 - ${initParam.pageTitle}</title>
        <!--<link rel="stylesheet" href="<c:url value="/webjars/jquery-ui-themes/1.12.1/redmond/jquery-ui.min.css" />" >-->
        <link rel="stylesheet" href="<c:url value="/css/tooltipster.bundle.min.css"/>" >
        <style>
            body{
                font-family: 微軟正黑體;
            }
            .draggable {
                width: 25px;
                height: 20px;
                padding: 0.3em;
                float: left;
                /*background-color: red;*/
                margin: 0px;
                cursor: default;
                text-align: center;
            }
            #generateArea{
                height: 20px;
            }
            .alarm{
                background-color: #0066FF;
                /*color: white;*/
            }
            .normal{
                background-color: greenyellow;
                /*color: white;*/
            }
            .abnormal{
                background-color: yellow;
            }
            .offLine{
                background-color: white;
            }
            #goback{
                cursor: pointer;
                color: blue;
            }
            .mapTitle{
                padding: 3px;
                background-color: white;
                font-size: 30px;
                float: left;
                border: 5px red solid;
                cursor: none;
                overflow: auto;
            }
            .groupTitle{
                padding: 3px;
                background-color: white;
                font-size: 32px;
                float: left;
                border:5px green solid;
                cursor: default;
                overflow: auto;
            }
            .clearWiget{
                clear: both;
            }
            #mapGroup{
                width: 1200px;
                height: 500px;
                background-image: url(../images/ss_${userSitefloor}f.png);
                background-repeat: no-repeat;
                -o-background-size: 100% 100%, auto;
                -moz-background-size: 100% 100%, auto;
                -webkit-background-size: 100% 100%, auto;
                background-size: 100% 100%, auto;
                background-position:center center;
                border:5px red solid;
                /*讓最外層div不要隨視窗變動而改變(不然裏頭的子div會跑掉)*/
                position: absolute;
            }
            /*            body {
                            padding-top: 70px;
                             Required padding for .navbar-fixed-top. Remove if using .navbar-static-top. Change if height of navigation changes. 
                        }*/
            .modal.fade.ui-draggable-dragging {
                -moz-transition: none;
                -o-transition: none;
                -webkit-transition: none;
                transition: none;
            }
            #wigetInfo{
                border-bottom: 5px red solid;
                border-right: 5px red solid;
                background-color: white;
                display: inline-block;
                overflow: hidden;
            }
            .titleWiget{
                cursor: pointer;
            }
            .divCustomBg{
                background-size: 100% 100%, auto;
                background-repeat: no-repeat;
            }
            .ui-helper {
                /*width: 100% !important;*/
                float: left;
            }
            .blub-size{
                width: 15px;
                height: 15px;
            }
            .blub-empty{
                background-image: url(../images/blub-icon/Gray_Light_Icon.png);
                /*background-color: red;*/
            }
            .blub-normal{
                background-image: url(../images/blub-icon/Green_Light_Icon.png);
                cursor: pointer;
            }
            .blub-alarm{
                background-image: url(../images/blub-icon/Blue_Light_Icon.png);
                cursor: pointer;
            }
            .blub-abnormal{
                background-image: url(../images/blub-icon/Yellow_Light_Icon.png);
            }
            .blub-target{
                background-image: url(../images/blub-icon/Red_Light_Icon.png);
            }
            .suggestMsg{
                background-color: white;
                color: red;
                margin: 5px;
                padding: 5px;
            }
            .adjustPosition{
                position: absolute;
            }
            #infoArea {
                background: white;
                margin: 0;
                padding: 0.5em 0.5em 0.5em 0.5em;
                position: absolute;
                /*                position: fixed;*/
                right: 0px;
                top: 0px;
                overflow: auto;
                height: 20%;
                width: 35%;
                border-width: 3px;
                border-style: solid;
                border-color: #FFAC55;
                opacity: 0.8;
                font-size: 12px;
                /*display: none;*/
            }
            #log-toggle{
                right: 0px;
                top: 0px;
                padding: 0.2em;
                border-width: 1px;
                border-style: solid;
                border-color: red;
                cursor: pointer;
            }
            /*            .rotate {
                            -webkit-transform: rotateX(180deg);
                            transform: rotateX(180deg);
                        }*/
        </style>
        <script src="<c:url value="/webjars/jquery/1.12.4/jquery.min.js" />"></script>
        <script src="<c:url value="/js/jquery-ui-1.10.0.custom.min.js"/>"></script>
        <script src="<c:url value="/js/reconnecting-websocket.min.js"/>"></script>
        <script src="<c:url value="/js/jquery.fullscreen-min.js"/>"></script>
        <script src="<c:url value="/js/tooltipster.bundle.min.js"/>"></script>
        <script src="<c:url value="/js/ss-setting/${userSitefloor}f.js"/>"></script>
        <script>
            var maxProductivity = 200;

            $(function () {
                var log = $("#log");
                var infoArea = $("#infoArea");

                initMapInfo();
                initTitleGroup();
                initTestGroup();
                initBabGroup();
                initFqcGroup();

                var testChildElement = $("#testArea>.testWiget div");
                var babChildElement = $("#babArea>.babWiget div");
                var fqcChildElement = $("#fqcArea>.fqcWiget div");

                testObjectInit();
                babObjectInit();
                fqcObjectInit();

                $("#titleArea>div").not(".clearWiget").addClass("groupTitle");

                var dragableWiget = $("#mapGroup > div:not(#wigetInfo) > div");

                dragableWiget.addClass("adjustPosition");
                dragableWiget.not(".clearWiget").addClass("ui-helper").draggable({
                    drag: function (e) {
//                        return false;
                    }
                });

                var timeout;

                $('.groupTitle').on('click', function () {
                    infoArea.show();
                    var id = $(this).attr("id");
                    var st = id.replace("_title", "");

                    //if you already have a timout, clear it
                    if (typeof timeout != 'undefined') {
                        clearTimeout(timeout);
                    }

                    //start new time, to perform ajax stuff in 500ms
                    timeout = setTimeout(function () {
                        getDetails(st);
                    }, 500);
                });

                $("#log-toggle").click(function () {
                    infoArea.hide();
                });

                function initMapInfo() {
                    $("#mapInfo").append("<div></div>");
                    $("#mapInfo>div")
                            .attr("id", "map_title")
                            .addClass("titleWiget mapTitle")
                            .html('${initParam.pageTitle}' + ': ' + mapInfo.titleName)
                            .css({left: mapInfo.x + pXa, top: mapInfo.y + pYa});
                }

                function initTitleGroup() {
                    for (var i = 0; i < titleGroup.length; i++) {
                        var groupStatus = titleGroup[i];
                        $("#titleArea").append("<div></div>");
                        $("#titleArea>div")
                                .eq(i)
                                .attr("id", groupStatus.lineName + "_title")
                                .addClass("titleWiget")
                                .html(groupStatus.lineName)
                                .css({left: groupStatus.x + pXa, top: groupStatus.y + pYa});
                    }
                }

                function initTestGroup() {
                    for (var i = 0; i < testGroup.length; i++) {
                        $("#testArea").append("<div></div>");
                        var thisDiv = $("#testArea>div").eq(i);
                        var groupStatus = testGroup[i];
                        var style = {left: groupStatus.x + pXa, top: groupStatus.y + pYa};
                        if ('straight' in groupStatus) {
                            style.width = "10px";
                        }
                        thisDiv.addClass("testWiget").attr("name", groupStatus.ssPrefix).css(style);
                        if ('reverse' in groupStatus) {
                            thisDiv.attr("reverse", true);
                        }
                        for (var j = 0, k = groupStatus.people; j < k; j++) {
                            thisDiv.append("<div></div>");
                        }
                    }
                }

                function initBabGroup() {
                    for (var i = 0; i < babGroup.length; i++) {
                        $("#babArea").append("<div></div>");
                        var groupStatus = babGroup[i];
                        for (var j = 0, k = groupStatus.people; j < k; j++) {
                            var style = {left: groupStatus.x + pXa, top: groupStatus.y + pYa};
                            if ('straight' in groupStatus) {
                                style.width = "10px";
                            }
                            $("#babArea>div")
                                    .eq(i)
                                    .append("<div></div>")
                                    .addClass("babWiget")
                                    .attr("id", groupStatus.lineName)
                                    .css(style);
                            if ('reverse' in groupStatus) {
                                $("#babArea>div")
                                        .eq(i)
                                        .attr("reverse", true);
                            }

                        }
                    }
                }

                function initFqcGroup() {
                    for (var i = 0; i < fqcGroup.length; i++) {
                        $("#fqcArea").append("<div></div>");
                        var groupStatus = fqcGroup[i];
                        for (var j = 0, k = groupStatus.people; j < k; j++) {
                            $("#fqcArea>div")
                                    .eq(i)
                                    .append("<div></div>")
                                    .addClass("fqcWiget")
                                    .attr("id", groupStatus.lineName)
                                    .css({left: groupStatus.x + pXa, top: groupStatus.y + pYa});
                        }
                    }
                }

                function initWiget(obj) {
                    obj.addClass("blub-empty")
                            .removeClass("blub-alarm blub-normal blub-abnormal blub-target")
                            .removeAttr("title")
                            .off("click").off("mouseover");
                }

                function testObjectInit() {
                    var loopCount = minTestTableNo;
                    $(".testWiget").each(function () {
                        var ssPrefix = $(this).attr("name");
                        if ($(this).attr("reverse")) {
                            var childAmount = $(this).children().length;
                            var startCount = loopCount + childAmount - 1;

                            $(this).children().each(function () {
                                $(this).attr({"id": "draggable" + ssPrefix + startCount + "_" + sitefloor + "f"})
                                        .addClass("draggable blub-empty divCustomBg blub-size")
                                        .html(startCount)
                                        .tooltipster({// init tooltipster here.
                                            contentAsHTML: true, // This enables HTML content in the tooltip
                                            interactive: true,
                                            contentCloning: false, // if you use a single element as content for several tooltips, set this option to true
                                            updateAnimation: null
                                        });
                                startCount--;
                            });
                            loopCount += childAmount;
                        } else {
                            $(this).children().each(function () {
                                $(this).attr({"id": "draggable" + ssPrefix + loopCount + "_" + sitefloor + "f"})
                                        .addClass("draggable blub-empty divCustomBg")
                                        .html(loopCount)
                                        .tooltipster({// init tooltipster here.
                                            contentAsHTML: true, // This enables HTML content in the tooltip
                                            interactive: true,
                                            contentCloning: false, // if you use a single element as content for several tooltips, set this option to true
                                            updateAnimation: null
                                        });
                                loopCount++;
                            });
                        }
                    });
                }

                function babObjectInit() {
                    $(".babWiget").each(function () {
                        var lineName = $(this).attr("id");
                        if ($(this).attr("reverse")) {
                            var childAmount = 1;
                            $(this).children().each(function () {
                                $(this).attr({"id": (lineName + "-S-" + childAmount)})
                                        .addClass("draggable blub-empty divCustomBg")
                                        .tooltipster({updateAnimation: null});
                                childAmount++;
                            });
                        } else {
                            var childAmount = $(this).children().length;
                            $(this).children().each(function () {
                                $(this).attr({"id": (lineName + "-S-" + childAmount)})
                                        .addClass("draggable blub-empty divCustomBg")
                                        .tooltipster({updateAnimation: null});
                                childAmount--;
                            });
                        }
                    });
                }

                function fqcObjectInit() {
                    $(".fqcWiget").each(function () {
                        var lineName = $(this).attr("id");
                        var childAmount = $(this).children().length;
                        $(this).children().each(function () {
                            $(this).attr({"id": (lineName + "_" + childAmount)})
                                    .addClass("draggable blub-empty divCustomBg")
                                    .tooltipster({updateAnimation: null});
                            childAmount--;
                        });
                    });
                }

                function testDataToWiget(obj) {
                    initWiget(testChildElement);
                    for (var k = 0, l = obj.length; k < l; k++) {
                        var ss = obj[k];

                        var alarmSignal = ss.map.sign;
                        var signalClass;
                        switch (alarmSignal) {
                            case 0:
                                signalClass = "blub-normal";
                                break;
                            case 1:
                                signalClass = "blub-alarm";
                                break;
                            case - 1:
                                signalClass = "blub-empty";
                                break;
                        }

                        var thisDiv = $(".testWiget #draggable" + ss.map.ssName + "_" + sitefloor + "f");
                        thisDiv.removeClass("blub-empty")
                                .addClass(signalClass)
                                .tooltipster('content', ('儲區: ' + ss.map.ssName + '<BR>工單:<BR>' + ss.map.pos))
                                .on("click", parent.testClick)
                                .on("mouseover", function () {
                                    console.log(' 值:' + thisDiv.html());
                                });
                    }

//                    initWiget(testChildElement);
//                    var tagMap = new Map(Object.entries(obj));
//                    tagMap.forEach((value, key) => {
//                        var alarmSignal = value.map.sign;
//                    });
                }

                function babDataToWiget(obj) {
                    initWiget(babChildElement);
                    babChildElement.html("");
                    if (obj != null) {
                        var babData = obj.data;
                        if (babData != null) {
                            for (var k = 0, l = babData.length; k < l; k++) {
                                var people = babData[k];

                                var childElement = $("#" + people.tagName);
                                if (childElement.length) {
                                    childElement.removeClass("blub-empty blub-target");

                                    if ("ismax" in people) {
                                        childElement.addClass((people.ismax ? "blub-alarm" : "blub-normal"))
                                                .html(people.station)
                                                .tooltipster('content', "Time:" + people.diff + "秒");
                                    } else {
                                        childElement.addClass("blub-target")
                                                .html(people.station);
                                    }
                                }
                            }
                        }
                    }
                }

                function fqcDataToWiget(obj) {
                    initWiget(fqcChildElement);
                    fqcChildElement.html("");
                    if (obj != null) {
                        var fqcData = obj.data;
                        if (fqcData != null) {
                            for (var k = 0, l = fqcData.length; k < l; k++) {
                                var people = fqcData[k];

                                var childElement = $("#" + people.fqcLineName + "_1");
                                if (childElement.length) {

                                    childElement.removeClass("blub-empty blub-target");

                                    childElement.addClass((people.isPass ? (people.productivity == 0 ?
                                            "blub-abnormal" : "blub-alarm") : "blub-normal"))
                                            .html(people.station)
                                            .tooltipster('content',
                                                    "機種:" + people.modelName + " / " +
                                                    "工號:" + people.jobnumber + " / " +
                                                    "總作業時間:" + people.seconds + "秒 / " +
                                                    "目前台數:" + people.records + "台 / " +
                                                    "標工" + people.standardTime + "(秒/台) / " +
                                                    "效率:" + getPercent(people.productivity) + "% / ")
                                            .html(1);
                                }
                            }
                        }
                    }
                }

                var hostname = window.location.host;//Get the host ip address to link to the server.
                //var hostname = "172.20.131.52:8080";
                //--------------websocket functions
                //websocket will reconnect by reconnecting-websocket.min.js when client or server is disconnect

                var ws2, ws4;

                var onopen = function () {
                    console.log("The connection open");
                };

                var onerror = function (event) {
                    console.log("error");
                    console.log(event.data);
                };

                //generate the unnormal close event hint
                var onclose = function (event) {
                    var reason;
                    if (event.code == 1000)
                        reason = "Normal closure, meaning that the purpose for which the connection was established has been fulfilled.";
                    else if (event.code == 1001)
                        reason = "An endpoint is \"going away\", such as a server going down or a browser having navigated away from a page.";
                    else if (event.code == 1002)
                        reason = "An endpoint is terminating the connection due to a protocol error";
                    else if (event.code == 1003)
                        reason = "An endpoint is terminating the connection because it has received a type of data it cannot accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).";
                    else if (event.code == 1004)
                        reason = "Reserved. The specific meaning might be defined in the future.";
                    else if (event.code == 1005)
                        reason = "No status code was actually present.";
                    else if (event.code == 1006)
                        reason = "The connection was closed abnormally, e.g., without sending or receiving a Close control frame";
                    else if (event.code == 1007)
                        reason = "An endpoint is terminating the connection because it has received data within a message that was not consistent with the type of the message (e.g., non-UTF-8 [http://tools.ietf.org/html/rfc3629] data within a text message).";
                    else if (event.code == 1008)
                        reason = "An endpoint is terminating the connection because it has received a message that \"violates its policy\". This reason is given either if there is no other sutible reason, or if there is a need to hide specific details about the policy.";
                    else if (event.code == 1009)
                        reason = "An endpoint is terminating the connection because it has received a message that is too big for it to process.";
                    else if (event.code == 1010) // Note that this status code is not used by the server, because it can fail the WebSocket handshake instead.
                        reason = "An endpoint (client) is terminating the connection because it has expected the server to negotiate one or more extension, but the server didn't return them in the response message of the WebSocket handshake. <br /> Specifically, the extensions that are needed are: " + event.reason;
                    else if (event.code == 1011)
                        reason = "A server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.";
                    else if (event.code == 1015)
                        reason = "The connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate can't be verified).";
                    else
                        reason = "Unknown reason";
                    console.log("The connection was closed for reason: " + reason);
//                    closeConnect();
                };

                if (testGroup.length != 0 || babGroup.length != 0) {
                    ws2 = new ReconnectingWebSocket("ws://" + hostname + "/WarehouseDashBoard/tagHandler");
                    setWebSocketClient(ws2);
                    //Get the server message and transform into table.
                    ws2.onmessage = function (message) {
                        var jsonArray = $.parseJSON(message.data);
                        if (jsonArray.myArrayList.length != 0) {
                            testDataToWiget(jsonArray.myArrayList);
                        }
                    };
                }

                if (fqcGroup.length != 0) {
                    ws4 = new ReconnectingWebSocket("ws://" + hostname + "/WarehouseDashBoard/tagHandler");
                    setWebSocketClient(ws4);
                    //Get the server message and transform into table.
                    ws4.onmessage = function (message) {
                        var jsonArray = $.parseJSON(message.data);
                        if (jsonArray.length != 0) {
                            fqcDataToWiget(jsonArray[0]);
                        }
                    };
                }

                function setWebSocketClient(webSocket) {
                    webSocket.timeoutInterval = 3000;
                    webSocket.reconnectInterval = 30 * 1000;
                    webSocket.onopen = onopen;
                    webSocket.onerror = onerror;
                    webSocket.onclose = onclose;
                }

                function postToServer() {
                }

                function closeConnect() {
                    if (ws2 != null) {
                        ws2.close();
                    }
                    if (ws4 != null) {
                        ws4.close();
                    }
                    console.log("websocket connection is now close");
                }
//-----------------------

                function getPercent(val) {
                    return roundDecimal((val * 100), 0);
                }

                function roundDecimal(val, precision) {
                    var size = Math.pow(10, precision);
                    return Math.round(val * size) / size;
                }

                function appendLog(message) {
                    log.append(message);
                    log.scrollTop(log.prop("scrollHeight"));
                }

                function getDetails(lineName) {
                    log.html("groupTitle').on('click");
//                    $.ajax({
//                        url: "<c:url value="/BabSettingHistoryController/findProcessingByLine" />",
//                        method: 'GET',
//                        dataType: 'json',
//                        data: {
//                            lineName: lineName
//                        },
//                        success: function (d) {
//                            var arr = d.data;
//                            if (arr.length == 0) {
//                                appendLog("<div>該線別無進行中的工單</div>");
//                            } else {
//                                for (var i = 0; i < arr.length; i++) {
//                                    var o = arr[i];
//                                    var stationInfo = o[0];
//                                    var bab = o[1];
//                                    var line = o[2];
//                                    appendLog("<div>" + stationInfo.tagName.name +
//                                            "(" + bab.id + ")" +
//                                            " / po:" + bab.po +
//                                            " / modelName:" + bab.modelName +
//                                            (bab.ispre == 0 ? "" : " / (前置)") +
//                                            "</div>");
//                                }
//                            }
//                        }
//                    });

                }


                function childFunction() {
                    console.log("iframeOUT");
                }
            });

            function childFunction() {
                console.log("iframeOUT");
            }
        </script>
    </head>    
    <body style="cursor: auto;">
        <!--<button id="fullBtn">Full</button>-->
        <div id="wigetCtrl">
            <%--<c:out value="${userLineType == null ? 'N/A' : userLineType}" />--%>
            <%--<c:out value="${userSitefloor}" />--%>
            <div id="mapGroup" class="rotate">
                <div id="wigetInfo">
                    <label for="empty" style="float:left">無資料</label>
                    <div class="draggable blub-empty divCustomBg"></div>

                    <label for="normalSign" style="float:left">空位</label>
                    <div class="draggable blub-normal divCustomBg"></div>

                    <label for="normalSign" style="float:left">滿位</label>
                    <div class="draggable blub-alarm divCustomBg"></div>

                    <!--                    <label for="normalSign" style="float:left">異常</label>
                                        <div class="draggable blub-abnormal divCustomBg"></div>-->

<!--                    <label for="normalSign" style="float:left">目標</label>
                    <div class="draggable blub-target divCustomBg"></div>-->
                </div>

                <div id="mapInfo"></div>
                <!--<div class="clearWiget" /></div>-->

                <div id="titleArea"></div>
                <!--<div class="clearWiget" /></div>-->

                <div id="testArea"></div>
                <!--<div class="clearWiget" /></div>-->

                <div id="babArea"></div>
                <!--<div class="clearWiget"></div>-->

                <div id="fqcArea"></div>
                <!--<div class="clearWiget"></div>-->

                <div id="infoArea" hidden="">
                    <div id="log-toggle">─</div>
                    <div id="log"></div>
                </div>
            </div>
        </div>
            <div class="clearWiget"></div>
    </body>
</html>

