<%-- 
    Document   : index
    Created on : 2019/1/2, 下午 03:47:44
    Author     : Wei.Cheng
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<title>${initParam.pageTitle}</title>

<script src="<c:url value="/libs/jQuery/jquery.js" />"></script> 
<script src="<c:url value="/libs/bootstrap/bootstrap.js" />"></script>

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
        border:1px black solid;
    }

    #items > * {transition: fill 0.2s, fill-opacity 0.2s, stroke 0.2s, stroke-opacity 0.2s;cursor: pointer;}body {margin:0;padding:0;}
    /**/
    #items > .polygon{fill:#d60404;fill-opacity:0.40;stroke:none;stroke-opacity:0.50}
    #items > .polygon:hover{fill:#f5d416;fill-opacity:0.60;stroke:;stroke-opacity:0.50}
    #items > .polygon.active{fill:#f5d416;fill-opacity:0.60;stroke:;stroke-opacity:0.50}

</style>

<script>
    $(function () {
        var po = $(".po").detach();
        var area_select = $("#area-select");
        var dashboard = $("#dashboard>div");
        var floor_id = '${param.floor_id}';
        var group_id = '${param.group_id}';

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
                        var content = "";
                        content += "<li class='nav-item'>";
                        content += "<a class='nav-link" + (group_id != null && group_id == str.id ? " active" : "") +
                                "' href='layout.jsp?content=warehouse&group_id=" + str.id + "&floor_id=" + floor_id + "#'>AREA " + str.name + "</a>";
                        content += "</li>";
                        groupAreas.append(content);
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
                url: "<c:url value="/StorageSpaceController/findAll" />",
                data: {
                    id: group_id
                },
                dataType: "json",
                success: function (response) {
                    var areas = response;
                    for (var i = 0; i < areas.length; i++) {
                        var str = areas[i];
                        area_select.append("<option value='" + str.id + "'>" + str.name + "</option>");
                        dashboard.append("<div id='STORAGE_" + str.id + "' class='col-6 po-list'><label for='" + str.name + "' data-toggle='" + str.name + "'>" + str.name +
                                "</label><a class='storage-faq' data-toggle='" + str.name + "'><span class='fa fa-question-sign' title='Location'></span></a><div id='po_content_" +
                                str.id + "' class='po_content form-inline'></div></div>");
                    }
                    getWarehouse();

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

        function getWarehouse() {
            $.ajax({
                type: "GET",
                url: "<c:url value="/WarehouseController/findAll" />",
                data: {
                },
                dataType: "json",
                success: function (response) {
                    var data = JSOG.decode(response);

                    for (var i = 0; i < data.length; i++) {
                        var d = data[i];
                        var clone_po = po.clone();
                        clone_po.find(".name").html(d.po);
                        clone_po.find(".data-id").val(d.id);
                        var sche = d.lineSchedule;
                        var target = $("#po_content_" + d.storageSpace.id);
                        if (sche != null) {
                            clone_po.addClass("text-success");
                            clone_po.find(".name").append(" (" + sche.line.name + ") ");
                        }
                        target.append(clone_po);
                    }
                    $("input, select").addClass("form-control");
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    alert(xhr.responseText);
                }
            });
        }

        function refreshTable() {
            $(".po_content").html("");
            getWarehouse();
        }

        $("#add-po").click(function () {
            if (text == "") {
                alert("Please insert po number.");
                return;
            }
            var text = $("#po").val();
            if (confirm("Confirm add " + text + " ?")) {

                var area_selected = $("#area-select :selected").val();
                $.ajax({
                    type: "POST",
                    url: "<c:url value="/WarehouseController/create" />",
                    data: {
                        po: text,
                        "storageSpace.id": area_selected
                    },
                    dataType: "html",
                    success: function (response) {
                        $("#po").val("");
                        ws.send("ADD");
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(xhr.responseText);
                    }
                });
            }
        });

        $(document).on("click", ".pull-out", function () {
            var po = $(this).parents(".po").find(".name").html();
            if (confirm("Confirm pull out " + po + " ?")) {
                var data_id = $(this).parents(".po").find(".data-id").val();
                $.ajax({
                    type: "POST",
                    url: "<c:url value="/WarehouseController/delete" />",
                    data: {
                        id: data_id
                    },
                    dataType: "html",
                    success: function (response) {
                        ws.send("REMOVE");
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alert(xhr.responseText);
                    }
                });
            }
        });

        $("#po, #po_search").on("keyup change", function () {
            $(this).val($(this).val().toUpperCase());
        });

        $("#po_search").keyup(function () {
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
            search.val("");
            search.trigger("keyup");
        });

        $('input').keypress(function (e) {
            if (e.which == 13) {
                $("#add-po").trigger("click");
            }
        });

        $("input").attr("form-control");

        $("input[type='text']").on("click", function () {
            $(this).select();
        });

        $("#area-select").change(function () {
            $("#po").focus();
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
        if (group_id) {
            connectToServer();
            if (ws != null) {
                setStorageSpace();
            }
        }
    });
</script>

<div class="modal fade" id="imagemodal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">              
            <div class="modal-body">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <c:import url="/images/svg_areaMap_${param.floor_id}f.jsp" />
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

        <div class="col-12">
            <div id="connectionStatus">Disconnected</div>
        </div>

        <div class="po col-12">
            <div class="name"></div>
            <input type="hidden" value="" class="data-id">
            <div class="widget">
                <input type="button" class="pull-out" value="Pull out" />
            </div>
        </div>

        <div class="input-area col-12">
            <table class="table table-striped">
                <tr>
                    <td>
                        <label>Area select</label>
                    </td>
                    <td>
                        <select id="area-select"></select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <label>Po insert</label>
                    </td>
                    <td>
                        <div class="form-inline">
                            <input type="text" id="po" placeholder="please insert your po" />
                            <input type="button" value="submit" id="add-po" />
                        </div>
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

        <div id="dashboard" class="col-12">
            <div class="row"></div>
        </div>

    </div>
</div>
<%--
    <div class="col-md-4">
        <div class="row">
            <iframe src="<c:url value="/pages/poDashboard_1.jsp" />" frameborder="0" width="100%" height="700"></iframe>
        </div>
    </div>
--%>

