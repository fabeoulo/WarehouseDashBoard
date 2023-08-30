<%-- 
    Document   : whPosition
    Created on : 2023年8月1日, 上午10:45:10
    Author     : Justin.Yeh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="userSitefloor" value="${param.floor_id}" />
<c:if test="${(userSitefloor == null) || (userSitefloor == '' || userSitefloor < 1 || userSitefloor > 7)}">
    <c:redirect url="index.jsp" />
</c:if>
<title>${initParam.pageTitle}</title>

<link rel="shortcut icon" href="<c:url value="/images/favicon.ico" />"/>
<link rel="stylesheet" href="<c:url value="/libs/datatables.net-dt/jquery.dataTables.css" />" />

<script src="<c:url value="/libs/jQuery/jquery.js" />"></script> 
<script src="<c:url value="/libs/bootstrap/bootstrap.js" />"></script>
<script src="<c:url value="/libs/mobile-detect/mobile-detect.min.js" />"></script>
<script src="<c:url value="/libs/block-ui/jquery.blockUI.js" />"></script>
<script src="<c:url value="/libs/datatables.net/jquery.dataTables.js" />"></script>

<style>
    #dashboard >  .po-list{
        border: 1px solid;
    }

    .po{
        border-bottom: 2px solid red;
    }

    span.highlight {
        background: red
    }

    .adjustPosition{
        position: absolute;
    }
    .row>div{
        border: 1px black solid;
    }
    .red{
        color: red
    }

    #items > * {
        transition: fill 0.2s, fill-opacity 0.2s, stroke 0.2s, stroke-opacity 0.2s;
        cursor: pointer;
    }
    body {
        margin:0;
        padding:0;
    }
    /**/
    #items > .polygon{
        fill:#d60404;
        fill-opacity:0.40;
        stroke:none;
        stroke-opacity:0.50
    }
    #items > .polygon:hover{
        fill:#f5d416;
        fill-opacity:0.60;
        stroke:;
        stroke-opacity:0.50
    }
    #items > .polygon.active{
        fill:#f5d416;
        fill-opacity:0.60;
        stroke:;
        stroke-opacity:0.50
    }
</style>

<script>
    $(document).ajaxSend(function () {
        block();
    });
    $(document).ajaxComplete(function () {
        $.unblockUI();
    });
    function block() {
        $.blockUI({
            css: {
                border: 'none',
                padding: '15px',
                backgroundColor: '#000',
                '-webkit-border-radius': '10px',
                '-moz-border-radius': '10px',
                opacity: .5,
                color: '#fff'
            },
            fadeIn: 0,
            overlayCSS: {
                backgroundColor: '#FFFFFF',
                opacity: .3
            }
        });
    }

    $(function () {

        var ssButton = $(".storageButton").detach();
        var po = $(".po").detach();
        var area_select = $("#area-select");
        var groupSel = $("#group-select");
        var dashboard = $("#dashboard>div");
        var floor_id = '${param.floor_id}';
        var group_id = '${param.group_id}';
        var warehouseData = [];
        var poModelMap = new Map();
        var ss_id = [];
        var ss_name = [];
        const allFloorIds = [1, 2, 7];


        $("#testFrame").click(function () {
            console.log("tetF");
        });

        var floorDtConfig = {
            dom: 'rt',
            ordering: false,
            "ajax": {
                type: "GET",
                url: "<c:url value="/StorageSpaceController/findEmptyByFloors" />",
                "data": {ids: allFloorIds.join(',')},
                dataType: "json",
                "dataSrc": function (json) {
                    var ss_empty_map = new Map(Object.entries(json));
                    var obj2 = [];
                    ss_empty_map.forEach((value, key) => {
                        var areas = {floorName: key, emptyCount: value.length};
                        obj2.push(areas);
                    });
                    return obj2;
                }
            },
            "columns": [
                {data: "floorName", title: "樓層"},
                {data: "emptyCount", title: "空儲位"}
            ]
        };
        var floorTable = $('#floorAllSs').DataTable(floorDtConfig);

        function setPoModelMap() {
            $.ajax({
                type: "GET",
                url: "<c:url value="/LineScheduleController/findMap" />",
                data: {
                    floor_id: 3                                   //floor_id: in [1,2,3]
                },
                dataType: "json",
                success: function (response) {
                    poModelMap = new Map(Object.entries(response));
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert(xhr.responseText);
                }
            });
        }

        function setStorageSpaceEmptyOptions() {
            $.ajax({
                type: "GET",
                url: "<c:url value="/StorageSpaceController/findEmptyByFloors" />",
                data: {ids: floor_id},
                dataType: "json",
                success: function (response) {
                    var ss_empty_map = new Map(Object.entries(response));
                    var sel = $("#area-empty");
                    ss_empty_map.forEach((value, key) => {
                        var areas = value;
                        for (var i = 0; i < areas.length; i++) {
                            var str = areas[i];
                            sel.append("<option value='" + str.id + "'>" + str.name + "</option>");
                        }
                    });
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert(xhr.responseText);
                }
            });
        }

        function setStorageSpaceModOptions() {
            $.ajax({
                type: "GET",
                url: "<c:url value="/StorageSpaceController/findByFloor" />",
                data: {
                    id: floor_id
                },
                dataType: "json",
                success: function (response) {
                    var areas = response;
                    var sel = ssButton.find(".storageSpace").hide();

                    sel.append("<option value='-1'>請選擇線別</option>");
                    for (var i = 0; i < areas.length; i++) {
                        var str = areas[i];
                        sel.append("<option value='" + str.id + "'>" + str.name + "</option>");
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert(xhr.responseText);
                }
            });
        }

        function setStorageSpaceGroup() {
            var groupAreas = $("#nav-links");

            $.ajax({
                type: "GET",
                url: "<c:url value="/StorageSpaceGroupController/findAll" />",
                data: {
                    floor_id: floor_id
                },
                dataType: "json",
                success: function (response) {
                    var groups = response;
                    for (var i = 0; i < groups.length; i++) {
                        var str = groups[i];
//                        var content = "";
//                        content += "<li class='nav-item'>";
//                        content += "<a class='nav-link" + (group_id != null && group_id == str.id ? " active" : "") +
//                                "' href='layout.jsp?content=whPosition&map=_map_storagespace&group_id=" + str.id + "&floor_id=" + floor_id + "#'>AREA " + str.name + "</a>";
//                        content += "</li>";
//                        groupAreas.append(content);

                        groupSel.append("<option value='" + str.id + "'>" + str.name + "</option>");
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert(xhr.responseText);
                }
            });
        }

        function setStorageSpace() {
            $.ajax({
                type: "GET",
                url: "<c:url value="/StorageSpaceController/findByStorageSpaceGroup" />",
                data: {
                    id: group_id
                },
                dataType: "json",
                success: function (response) {
                    resetDashboard();
                    var areas = response;
                    for (var i = 0; i < areas.length; i++) {
                        var str = areas[i];
                        area_select.append("<option value='" + str.id + "'>" + str.name + "</option>");
                        dashboard.append("<div id='STORAGE_" + str.id + "' class='col-6 po-list'><label for='" + str.name + "' data-toggle='" + str.name + "'>" + str.name +
                                "</label><a class='storage-faq' data-toggle='" + str.name + "'><span class='fa fa-map-marker-alt red' title='Location'></span></a><div id='po_content_" +
                                str.id + "' class='po_content form-inline'></div></div>");
                    }
                    refreshTable();

                    //regist faq button event
                    $('body').on('click', '.storage-faq, #dashboard label', function () {
                        var labelName = $(this).attr("data-toggle");
                        var target = $("#imagemodal #polygon-" + labelName);
                        highlightSelectArea(target);
                        $('#imagemodal').modal('show');
                    });

                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert(xhr.responseText);
                }
            });
        }

        function highlightSelectArea(target) {
            if (target.length == 0) {
                return;
            }
            var interval;
            var area = target;
            area.trigger("hover");
            interval = setInterval(function () {
                area.toggleClass("active");
            }, 750);

            $('#imagemodal').on('hidden.bs.modal', function () {
                clearInterval(interval);
            });

        }

        function storageToDashboard(response, isSetMap) {
            resetDashboard();
            var areas = response;
            for (var i = 0; i < areas.length; i++) {
                var str = areas[i];
                dashboard.append("<div id='STORAGE_" + str.id + "' class='col-6 po-list form-inline'><label for='" + str.name + "' data-toggle='" + str.name + "'>" + str.name +
                        "</label><a class='storage-faq' data-toggle='" + str.name + "'><span class='fa fa-map-marker-alt red' title='Location'></span></a></div>");

                var target = $("#STORAGE_" + str.id);
                var clone_ss = ssButton.clone();
                clone_ss.find(".ss-id").val(str.id);
                clone_ss.find(".ss-name").val(str.name);
                target.append(clone_ss);
                target.append("<div id='po_content_" + str.id + "' class='po_content form-inline col-12'></div>");

                ss_id.push(str.id);
                if (isSetMap) {
                    ss_name.push(str.name);
                }
            }
//                    ws.send("ADD");
            refreshTable();
            setMapTarget(ss_name);

            //regist faq button event
            $('body').on('click', '.storage-faq, #dashboard label', function () {
//                var labelName = $(this).attr("data-toggle");
//                var target = $("#imagemodal #polygon-" + labelName);
//                highlightSelectArea(target);
//                $('#imagemodal').modal('show');
            });

        }

        function getWarehouse() {
            var reqData, url;
            if (ss_id.length > 0) {
                url = "<c:url value="/WarehouseController/findBySsid" />";
                reqData = {ssIds: ss_id.join(',')};
            } else if (group_id) {
                url = "<c:url value="/WarehouseController/findAll" />";
                reqData = {storageSpaceGroupId: group_id};
            } else {
                return;
            }

            warehouseData = [];
            $.ajax({
                type: "GET",
                url: url,
                data: reqData,
                dataType: "json",
                success: function (response) {
                    var data = JSOG.decode(response);

                    for (var i = 0; i < data.length; i++) {
                        var d = data[i];
                        warehouseData["id" + d.id] = d;
                        var clone_po = po.clone();
                        clone_po.find(".name").html(d.po);
                        clone_po.find(".data-id").val(d.id);
                        var sche = d.lineSchedule;
                        var target = $("#po_content_" + d.storageSpace.id);
                        if (sche != null) {
                            var nameField = clone_po.find(".name");
                            clone_po.addClass("text-success");

                            nameField.append(' / <small>' + sche.modelName + '</small> ');
                            if (sche.line != null) {
                                nameField.append(" (" + sche.line.name + ") ");
                            }
                            if (sche.remark != null && sche.remark.trim() != '') {
                                nameField.append("※");
                            }
                        }
                        target.append(clone_po);
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert(xhr.responseText);
                }
            });
        }

        function findStorageSpaceByIds(isSetTarget) {
            $.ajax({
                type: "GET",
                url: "<c:url value="/StorageSpaceController/findByIds" />",
                data: {ids: ss_id.join(',')},
                dataType: "json",
                success: function (response) {
                    storageToDashboard(response, isSetTarget);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert(xhr.responseText);
                }
            });
        }

        $("input, select").addClass("form-control");
        function refreshTable() {
            $(".po_content").html("");
            getWarehouse();
            floorTable.ajax.reload();
            $("input, select").addClass("form-control");
        }

        function resetDashboard() {
            dashboard.children().remove();
            ss_id = [];
            ss_name = [];
        }

//        setStorageSpaceEmptyOptions();
        setStorageSpaceModOptions();
        setPoModelMap();

        $("#ssg-detail").click(function () {
            $.ajax({
                type: "GET",
                url: "<c:url value="/StorageSpaceController/findByFloor" />",
                data: {
                    id: floor_id
                },
                dataType: "json",
                success: function (response) {
                    storageToDashboard(response, false);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert(xhr.responseText);
                }
            });
        });

        $("#add-po").click(function () {
            $("#po_input").focus();
            var dataList = $("#poInsert-table tbody tr:not(:hidden)");
            var len = dataList.length;
            if (len < 1) {
                alert("請掃描工單.");
                return;
            }

            if (confirm(len + " po(s), OK ?")) {

                resetDashboard();
                var pos = dataList.map(function () {
                    const o = $(this).find("#inputPo").html();
                    $(this).detach();
                    return o;
                }).get();//.get()方法將這個陣列從 jQuery 物件轉換為一般的 JavaScript 陣列
                const myData = {
                    pos: pos,
                    floorId: floor_id,
                    ssgId: groupSel.val()
                };

                $.ajax({
                    type: "POST",
                    url: "<c:url value="/WarehouseController/batchCreate" />",
                    contentType: "application/json",
                    data: JSON.stringify(myData),
                    dataType: "json",
                    success: function (response) {
                        var ssMap = new Map(Object.entries(response));
                        if (ssMap.has("ssId")) {
                            ss_id.push(ssMap.get("ssId"));
                            findStorageSpaceByIds(true);
                        } else {
                            alert("Fail. No space.");
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(xhr.responseText);
                    }
                });
            }
        });

        $(document).on("click", ".ss-pull-out", function () {
            var id = $(this).parents(".storageButton").find(".ss-id").val();
            var name = $(this).parents(".storageButton").find(".ss-name").val();
            if (confirm("Confirm pull out " + name + " ?")) {
                $.ajax({
                    type: "POST",
                    url: "<c:url value="/WarehouseController/deleteFromStorageSpace" />",
                    data: {
                        ssId: id
                    },
                    dataType: "json",
                    success: function (response) {
                        alert(response);
//                        ws.send("REMOVE");
                        refreshTable();
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(xhr.responseText);
                    }
                });
            }
        });

        $(document).on("click", ".change-area", function () {
            $(this).hide();
            $(this).parent().find(".storageSpace").show().focus();
        });

        $(document).on("focusout", ".storageSpace", function () {
            $(this).hide();
            $(".change-area").show();
        });

        $(document).on("change", ".storageSpace", function () {
            var ssid = $(this).parents(".storageButton").find(".ss-id").val();
            var ssName = $(this).parents(".storageButton").find(".ss-name").val();
            var tarSsid = $(this).val();
            var selText = $(this).children("option:selected").text();

            if (tarSsid === -1 || ssid === tarSsid) {
                return false;
            }

            if (confirm("Change area " + ssName + " to " + selText + "?")) {

                $.ajax({
                    type: "POST",
                    url: "<c:url value="/WarehouseController/batchChangeStorageSpace" />",
                    data: {
                        srcSsid: ssid,
                        tarSsid: tarSsid
                    },
                    dataType: "json",
                    success: function (response) {
                        alert("success");
//                        ws.send("ADD");
                        ss_id.splice(ss_id.indexOf(Number(ssid)), 1, Number(tarSsid));
                        findStorageSpaceByIds(false);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(xhr.responseText);
                    }
                });
            } 
            else {
                $(this).val(-1);
            }
        });

        $("#po_input").on("keydown", function (e) {
            const poVal = $(this).val().trim();
            /* ENTER PRESSED*/
            if (e.keyCode === 13 && poVal !== "") {
                $(this).val("");
                if (checkPoExist(poVal))
                    return;

                const lastRow = $("#poInsert-table tbody tr:last");
                lastRow.show().removeAttr("hidden");
                lastRow.find("#inputPo").html(poVal);
                if (poModelMap.has(poVal)) {
                    lastRow.find("#inputModel").html(poModelMap.get(poVal));
                }
                addDetailRow();
            }
        });

        function checkPoExist(poVal) {
            var isExist = false;
            $("#poInsert-table #inputPo").each(function (index) {
                if ($(this).html().includes(poVal)) {
//                    alert('此棧板已有工單' + poVal);
                    isExist = true;
                    return;
                }
            });
            return isExist;
        }
        function addDetailRow() {
            var lastRow = $("#poInsert-table").find("tbody>tr:last");
            var clone = lastRow.clone(true);
            clone.find("label").html("");
            lastRow.after(clone.hide());
        }
        $(".remove-detail").click(function () {
            var length = $("#poInsert-table").find("tbody>tr").length;
            if (length > 1) {
                $(this).closest("tr").remove();
            }
        });

        $("#po_input, #po_search").on("keyup change", function () {
            $(this).val($(this).val().toUpperCase());
        });

        $("#po_search").keyup(function (e) {
            const poSearch = $(this).val().trim();
            if (e.keyCode === 13 && poSearch !== "") {
                $(this).select();

                $.ajax({
                    type: "GET",
                    url: "<c:url value="/StorageSpaceController/findByPo" />",
                    data: {
                        po: poSearch,
                        floorId: floor_id
                    },
                    dataType: "json",
                    success: function (response) {
                        storageToDashboard(response, false);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(xhr.responseText);
                    }
                });
            }

            var mysearchword = this.value.trim();
            $('.po > .name').each(function () {
                $(this).find('span.highlight').contents().unwrap();
                if (mysearchword) {
                    var re = new RegExp('(' + mysearchword.trim().split(/\s+/).join('|') + ')', "gi");
                    $(this).html(function (i, html) {
                        return html.replace(re, '<span class="highlight">$1</span>');
                    });
                }
            });
        });

        $("#clear_search").click(function () {
            var search = $("#po_search");
            search.val("").trigger("keyup").select();
        });

        $("#po_input").keypress(function (e) {
            if (e.which == 13) {
//                $("#add-po").trigger("click");
            }
        });

        $("input").attr("form-control");

        $("input[type='text']").on("click", function () {
            $(this).select();
        });

        $("#po_input").focus();
        area_select.change(function () {
            $("#po_input").focus();
        });

        var ws;
        var hostname = window.location.host;//Get the host ipaddress to link to the server.
        function connectToServer() {

            try {
                ws = new WebSocket("ws://" + hostname + "/WarehouseDashBoard/myHandler");

                ws.onopen = function () {
                    $("#connectionStatus").html("Connected");
                };
                ws.onmessage = function (event) {
                    var d = event.data;
                    d = d.replace(/\"/g, "");
                    console.log(d);
                    if ("ADD" == d || "REMOVE" == d) {
                        refreshTable();
                    }
                };
                ws.onclose = function () {
                    $("#connectionStatus").html("Disconnected");
                };
            } catch (e) {
                console.log(e);
            }


        }
        function disconnectToServer() {
            ws.close();
        }

        setStorageSpaceGroup();
//        if (group_id) {
//            connectToServer();
//            if (ws != null) {
//                setStorageSpace();
//            }
//        }
//        if (ws === undefined) {//|| ws.readyState !== WebSocket.OPEN
//            connectToServer();
//        }
    });
</script>


<c:import url="${param.map}.jsp?sitefloor=${userSitefloor}" /> 
<!--<span class="col-md-12 ">
    <iframe id="map-iframe" style='width:100%; height:550px' frameborder="0" scrolling="no" src="${param.map}.jsp?sitefloor=${userSitefloor}" webkitAllowFullScreen mozAllowFullScreen allowFullScreen>您的瀏覽器不支援內嵌網頁!?</iframe>
</span>-->

<div class="modal fade" id="imagemodal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">              
            <div class="modal-body">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <c:import url="/images/svg_areaMap_${param.floor_id}.jsp" />
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            </div>
        </div>
    </div>
</div>

<div class="col-md-8">
    <div class="row">
        <div class="col-12">
            <ul class="nav nav-pills" id="nav-links">

            </ul>
        </div>

        <span class="storageButton">
            <input type="hidden" value="" class="ss-id">
            <input type="hidden" value="" class="ss-name">
            <input type="button" class="ss-pull-out" value="Pull out" />
            <input type="button" class="change-area" value="Change area" />
            <select class="storageSpace"></select>
        </span>

        <div class="po col-12">
            <div class="name"></div>
            <input type="hidden" value="" class="data-id">
            <input type="hidden" value="" class="data-po">
            <!--            <div class="widget">
                            <input type="button" class="pull-out" value="Pull out" />
                            <input type="button" class="change-area" value="Change area" />
                            <select class="storageSpace"></select>
                        </div>-->
        </div>

        <div class="input-area col-12">
            <table class="table table-striped">
                <tr>
                    <td>
                        <label>掃描工單</label>
                    </td>
                    <td>
                        <div class="form-inline">
                            <input type="text" id="po_input" placeholder="please insert your po" />
                            <input type="button" value="綁定" id="add-po" />
                        </div>
                    </td>
                </tr>               
                <tr>
                    <td>                    
                        <label>儲位詳細</label>
                    </td>
                    <td>
                        <select id="area-empty" hidden=""></select>
                        <table id="poInsert-table" class="table table-striped">
                            <thead>
                                <tr>
                                    <th>工單</th>
                                    <th>機種</th>
                                    <th>#</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr hidden>
                                    <td>
                                        <label id="inputPo"/>
                                    </td>
                                    <td>
                                        <label id="inputModel" />
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-default btn-sm remove-detail btn-outline-dark" aria-label="Left Align">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>優先儲區</label>
                    </td>
                    <td>
                        <select id="area-select" hidden></select>
                        <select id="group-select" class="form-inline"></select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>Po search</label>
                    </td>
                    <td>
                        <div class="form-inline">
                            <input type="text" id="po_search" placeholder="please insert your search" />
                            <input type="button" id="clear_search" value="Clear search" />
                        </div>
                    </td>
                </tr>
            </table>
        </div>


        <div class="col-12 form-inline">
            <!--<input type="button" value="testFrame" id="testFrame" />-->
            <input type="button" value="Display all" id="ssg-detail" />
            <div id="connectionStatus" hidden="">Disconnected</div>
        </div>

        <div id="dashboard" class="col-12">
            <div class="row"></div>
        </div>

    </div>
</div>
<span class="col-md-3 offset-md-1 ">
    <table id="floorAllSs"  cellspacing="10" class="table table-bordered"></table>
</span>
