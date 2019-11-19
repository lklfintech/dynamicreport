/****************************** START 生成参数页面部分 ******************************/
// 生成查询条件HTML
function buildParamHtml(requestParam) {
    var pageId = requestParam['pageId'];
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "../../report/param/build",
        data: {pageId: pageId},
        success: function (data) {
            // 报表名称
            $("#formTitle").html("<h5>" + data.title + "</h5>");
            // 报表说明
            if ('' != data.remark && null != data.remark) {
                $("#rptTooltip").attr("title", data.remark);
                $("#rptTooltip").show();
            } else {
                $("#rptTooltip").hide();
            }
            // 查询条件
            if ('' != data.paramHtml && null != data.paramHtml) {
                $("#formDiv").html(data.paramHtml);
                // 监听form提交事件
                addFormSubmitEventListener();
            } else {
                $("#submitBtn").hide();
                $("#resetBtn").hide();
            }

            // 如果是钻取报表，需要对form中相应参数赋值
            $.each(requestParam, function (key, value) {
                if (value != undefined && value != 'undefined') {
                    $("#" + key).val(value);
                }
            });

            // 生成组件头部HTML（包含数据列表和图形）
            buildComponentHeaderHtml(pageId);
        },
        error: function (e) {
            layer.msg(e.error);
        }
    });
}

// 监听form提交事件
function addFormSubmitEventListener() {
    $("#frm").validate({
        messages: {},
        submitHandler: function () {
            $('#myModal').modal('show');
            // 查询数据列表
            var tableComponentId = $("#tableComponentId").val();
            if (null != tableComponentId && "" != tableComponentId) {
                queryTable();
            }
            // 查询图形
            var chartType = $("#chartTypeDiv").html();
            if (null != chartType && "" != chartType.trim()) {
                queryChartTemplate();
            }
            $('#myModal').modal('hide');
        }
    });
}

/****************************** END 生成参数页面部分 ******************************/

/****************************** START 生成组件头部维度、指标筛选条件部分 ******************************/
// 生成组件头部HTML
function buildComponentHeaderHtml(pageId) {
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "../../report/component/header/build",
        data: {pageId: pageId},
        success: function (data) {
            var charts = [];
            var isChartHeaderShown = false;
            for (var i = 0; i < data.length; i++) {
                var componentBO = data[i];
                var componentParentType = componentBO.component.type.parent.key;
                // 生成列表头部的维度和指标
                if ("TYPE_TABLE" == componentParentType) {
                    buildTableConditionHeaderHtml(componentBO);
                }
                // 生成图形头部的维度和指标
                if ("TYPE_CHART" == componentParentType) {
                    if (!isChartHeaderShown) {
                        if ("CHART_MULTIPLE_DIM" == componentBO.component.type.key) {
                            buildMultipleDimChartHeaderDetailHtml(componentBO);
                        } else {
                            buildChartConditionHeaderHtml(componentBO);
                        }
                        isChartHeaderShown = true;
                    }
                    charts.push(componentBO.component);
                }
            }
            // 监听表格维度、指标点击事件
            addTableHeaderEventListener();
            // 查询表格
            queryTable();
            if (charts.length > 0) {
                // 显示图形类型radio
                buildChartTypeHeaderDetailHtml(charts);
                // 监听维度、指标和图形类型点击事件
                addChartHeaderEventListener();
                // 查询图形模板
                queryChartTemplate();
            } else {
                $("#chartRow").hide();
            }
        },
        error: function (e) {
            layer.msg(e.error);
        }
    });
}

// 生成表格列表头部的维度、指标筛选条件
function buildTableConditionHeaderHtml(componentBO) {
    var componentType = componentBO.component.type.key;
    if ("TABLE_COMMON_TABLE" == componentType) {
        buildNormalTableConditionHeaderHtml(componentBO);
    } else if ("TABLE_AGGR_TABLE" == componentType) {
        buildAggrTableConditionHeaderHtml(componentBO);
    } else if ("TABLE_PIVOT_TABLE" == componentType) {
        buildPivotTableConditionHeaderHtml(componentBO);
    }
}

// 生成普通表格列表头部的维度、指标筛选条件
function buildNormalTableConditionHeaderHtml(componentBO) {
    var dimensionDivContent, targetDivContent;
    dimensionDivContent = buildComponentHeaderDetailHtml2("维度：", "checkbox", "dimension", componentBO.dimensionList);
    $("#tblDimensionDiv").html(dimensionDivContent);
    $("#dimension").selectpicker('refresh');
    if (componentBO.targetList != null && componentBO.targetList.length > 0) {
        targetDivContent = buildComponentHeaderDetailHtml2("指标：", "checkbox", "target", componentBO.targetList);
        $("#tblTargetDiv").html(targetDivContent);
        $("#target").selectpicker('refresh');
    }
    if ('' != componentBO.component.remark && null != componentBO.component.remark) {
        $("#tableTooltip").attr("title", componentBO.component.remark);
        $("#tableTooltip").show();
    } else {
        $("#tableTooltip").hide();
    }
    // 保存列表组件ID
    $("#tableComponentId").val(componentBO.component.id);
    // 保存列表组件类型
    $("#tableComponentType").val(componentBO.component.type.key);
    // 保存列表组件固定列数
    if (componentBO.component.fixedCol != null) {
        $("#tableComponentFixedCol").val(componentBO.component.fixedCol);
    } else {
        $("#tableComponentFixedCol").val("");
    }
    // 保存列表组件是否分页
    if (componentBO.component.pagination != null) {
        $("#tableComponentPagination").val(componentBO.component.pagination);
    } else {
        $("#tableComponentPagination").val("");
    }

}

// 生成聚合表格列表头部的维度、指标筛选条件
function buildAggrTableConditionHeaderHtml(componentBO) {
    buildNormalTableConditionHeaderHtml(componentBO);
    // 聚合数据列表类型，保存需要合并的列名和需要小计的列名
    var rowMergeCols = '';
    var rowSummary = '';
    if ('TABLE_AGGR_TABLE' == $("#tableComponentType").val()) {
        for (var i = 0; i < componentBO.dimensionList.length; i++) {
            var data = componentBO.dimensionList[i];
            if ('Y' == data.rowMerge) {
                rowMergeCols += data.colName + ",";
            }
            if ('Y' == data.rowSummary) {
                rowSummary = data.colName;
            }
        }
        if (rowMergeCols != '') {
            // 去掉最后的逗号
            rowMergeCols = rowMergeCols.substring(0, rowMergeCols.length - 1);
        }
    }
    $("#tableRowMergeCols").val(rowMergeCols);
    $("#tableRowSummary").val(rowSummary);
}

// 生成透视表格列表头部的维度、指标筛选条件
function buildPivotTableConditionHeaderHtml(componentBO) {
    buildNormalTableConditionHeaderHtml(componentBO);
    var pivotDimensionDivContent, pivotTargetDivContent;
    pivotDimensionDivContent = buildComponentHeaderDetailHtml2("透视维度：", "radio", "pivotDimension", componentBO.pivotDimensionList);
    $("#tblPivotDimDiv").html(pivotDimensionDivContent);
    $("#pivotDimension").selectpicker('refresh');
    pivotTargetDivContent = buildComponentHeaderDetailHtml2("透视指标：", "checkbox", "pivotTarget", componentBO.pivotTargetList);
    $("#tblPivotTargetDiv").html(pivotTargetDivContent);
    $("#pivotTarget").selectpicker('refresh');
}

// 生成图形头部的维度和指标的checkbox和radio
function buildChartConditionHeaderHtml(componentBO) {
    var dimensionDivContent, targetDivContent;
    dimensionDivContent = buildComponentHeaderDetailHtml("维度：", "radio", "dimension", componentBO.dimensionList);
    $("#chartDimensionDiv").html(dimensionDivContent);
    targetDivContent = buildComponentHeaderDetailHtml("指标：", "checkbox", "target", componentBO.targetList);
    $("#chartTargetDiv").html(targetDivContent);
    if ('' != componentBO.component.remark && null != componentBO.component.remark) {
        $("#chartTooltip").attr("title", componentBO.component.remark);
        $("#chartTooltip").show();
    } else {
        $("#chartTooltip").hide();
    }
}

// 生成多维度图形头部的维度和指标的checkbox和radio
function buildMultipleDimChartHeaderDetailHtml(componentBO) {
    var dimensionDivContent, targetDivContent;
    dimensionDivContent = buildComponentHeaderDetailHtml("维度：", "checkbox", "dimension", componentBO.dimensionList);
    $("#chartDimensionDiv").html(dimensionDivContent);
    targetDivContent = buildComponentHeaderDetailHtml("指标：", "radio", "target", componentBO.targetList);
    $("#chartTargetDiv").html(targetDivContent);
    if ('' != componentBO.component.remark && null != componentBO.component.remark) {
        $("#chartTooltip").attr("title", componentBO.component.remark);
        $("#chartTooltip").show();
    } else {
        $("#chartTooltip").hide();
    }
}

// 生成图形头部的类型radio
function buildChartTypeHeaderDetailHtml(charts) {
    $('#chartTypeDiv').html(buildComponentHeaderDetailHtml("图形：", "radio", "ChartType", charts));
    $("#ChartType" + charts[0].id).prop("checked", true);
}

// 生成维度和指标的checkbox或radio
function buildComponentHeaderDetailHtml(labelText, type, name, dataList) {
    var divContent = '<label class="font-middle" style="width:40px">' + labelText + '</label>';
    for (var i = 0; i < dataList.length; i++) {
        var data = dataList[i];
        if ("checkbox" == type) {
            divContent += '<div class="checkbox checkbox-info checkbox-inline">';
        } else {
            divContent += '<div class="radio radio-info radio-inline">';
        }
        divContent += '<input type="' + type + '" name="option' + name + '" id="' + name + data.id + '" value="' + data.id + '"';
        if ('Y' == data.selected) {
            divContent += ' checked'
        }
        divContent += ' style="margin-left:-20px;width: 16px;height: 20px;">';
        divContent += '<label for="' + data.id + '">' + data.name + '</label>';
        divContent += '</div>';
    }
    return divContent;
}

//生成维度和指标的checkbox或radio
function buildComponentHeaderDetailHtml2(labelText, type, name, dataList) {
    var divContent = '<label class="font-middle" style="width:70px">' + labelText + '</label>';
    if ("checkbox" == type) {
        divContent += '<div class="radio radio-info radio-inline"><select class="selectpicker" id="'+ name +'" title="请选择"   style="width:200px;height:35px" multiple="multiple">';
    } else {
        divContent += '<div class="radio radio-info radio-inline"><select class="selectpicker" id="'+ name +'" title="请选择"   style="width:200px;height:35px">';
    }
    for (var i = 0; i < dataList.length; i++) {
        var data = dataList[i];
        divContent += '<option value=' + data.id;
        if ('Y' == data.selected) {
            divContent += '  selected="selected"'
        }
        divContent += ' >' + data.name + '</option>';
    }
    divContent += '</select></div>';
    return divContent;
}

// 监听数据列表维度、指标、透视维度和指标的checkbox点击事件
function addTableHeaderEventListener() {
    $("#tblDimensionDiv").on('change', function (e) {
        $('#myModal').modal('show');
        queryTable();
        $('#myModal').modal('hide');
    });
    $("#tblTargetDiv").on('change', function (e) {
        $('#myModal').modal('show');
        queryTable();
        $('#myModal').modal('hide');
    });
    $("#tblPivotDimDiv").on('change', function (e) {
        $('#myModal').modal('show');
        queryTable();
        $('#myModal').modal('hide');
    });
    $("#tblPivotTargetDiv").on('change', function (e) {
        $('#myModal').modal('show');
        queryTable();
        $('#myModal').modal('hide');
    });
}

// 监听图形维度、指标及类型点击事件
function addChartHeaderEventListener() {
    // 解绑后在绑定click事件，避免图形类型切换后，radio和checkbox的click事件重复监听
    $("#chartDimensionDiv input[type='radio']").unbind('click');
    $("#chartTargetDiv input[type='checkbox']").unbind('click');
    $("#chartTypeDiv input[type='radio']").unbind('click');
    //　监听图形checkbox和radio点击事件
    $("#chartDimensionDiv input[type='radio']").on('click', function (e) {
        $('#myModal').modal('show');
        queryChartTemplate();
        $('#myModal').modal('hide');
    });
    $("#chartDimensionDiv input[type='checkbox']").on('click', function (e) {
        $('#myModal').modal('show');
        queryChartTemplate();
        $('#myModal').modal('hide');
    });
    $("#chartTargetDiv input[type='checkbox']").on('click', function (e) {
        $('#myModal').modal('show');
        queryChartTemplate();
        $('#myModal').modal('hide');
    });
    $("#chartTargetDiv input[type='radio']").on('click', function (e) {
        $('#myModal').modal('show');
        queryChartTemplate();
        $('#myModal').modal('hide');
    });
    $("#chartTypeDiv input[type='radio']").on('click', function (e) {
        $('#myModal').modal('show');
        refreshChartHeaderDetailHtml();
        $('#myModal').modal('hide');
    });
}

/****************************** END 生成组件头部维度、指标筛选条件部分 ******************************/

/****************************** START 数据列表部分 ******************************/
// 查询列表
function queryTable() {
    var tblDimensionIds = "";
    $.each($("#tblDimensionDiv option:selected"), function () {
        tblDimensionIds += $(this).val() + ",";
    });

    if (tblDimensionIds == '') {
        layer.alert("数据列表维度不能为空！");
        return;
    }
    var tblTargetIds = "";
    $.each($("#tblTargetDiv option:selected"), function () {
        tblTargetIds += $(this).val() + ",";
    });
    var tblComponentId = $('#tableComponentId').val();
    var tableComponentType = $("#tableComponentType").val();
    if ("TABLE_COMMON_TABLE" == tableComponentType) {
        queryNormalTable(tblComponentId, tblDimensionIds, tblTargetIds);
    } else if ("TABLE_AGGR_TABLE" == tableComponentType) {
        queryAggrTable(tblComponentId, tblDimensionIds, tblTargetIds);
    } else if ("TABLE_PIVOT_TABLE" == tableComponentType) {
        var pivotDimensionId = $("#tblPivotDimDiv option:selected").val();
        var pivotTargetIds = "";
        $.each($("#tblPivotTargetDiv option:selected"), function () {
            pivotTargetIds += $(this).val() + ",";
        });
        queryPivotTable(tblComponentId, tblDimensionIds, tblTargetIds, pivotDimensionId, pivotTargetIds);
    }
}

// 普通数据表格查询
function queryNormalTable(tblComponentId, dimensionIds, targetIds) {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "../../report/component/table/header",
        data: {
            dimensionIds: dimensionIds,
            targetIds: targetIds,
            componentId: tblComponentId
        },
        success: function (data) {
            var normalTable = $("#table_list");
            var fixedCol = $("#tableComponentFixedCol").val();
            var tablePaginationFlag = $("#tableComponentPagination").val();

            // 设置查询url及是否分页变量
            var url = '';
            var isPaging = true;
            if (tablePaginationFlag == "Y") {
                url = "../../report/component/table/data?";
            } else {
                url = "../../report/component/table/data-no-paging?";
                isPaging = false;
            }
            url += $("#frm").serialize() + "&componentId=" + $('#tableComponentId').val() + "&dimensionIds=" + dimensionIds + "&targetIds=" + targetIds;

            normalTable.bootstrapTable('destroy');
            if (fixedCol != null && fixedCol != "") {
                normalTable.bootstrapTable({
                    method: "GET",
                    contentType: "application/json",
                    url: url,
                    striped: true,
                    pagination: isPaging,
                    pageSize: 10,
                    pageNumber: 1,
                    pageList: [10, 30, 50, 100, 500, 1000, 2000, 3000],
                    search: false,
                    detailView: false,
                    detailFormatter: null,
                    sidePagination: "server",
                    queryParamsType: "undefined",
                    fixedColumns: true,
                    fixedNumber: fixedCol,
                    responseHandler: function (res) {
                        return {"rows": res.data, "total": res.rowsCount};
                    },
                    columns: eval("(" + data.KEY_TABLE_COLUMNS + ")")
                });
            } else {
                normalTable.bootstrapTable({
                    method: "GET",
                    contentType: "application/json",
                    url: url,
                    striped: true,
                    pagination: isPaging,
                    pageSize: 10,
                    pageNumber: 1,
                    pageList: [10, 30, 50, 100, 500, 1000, 2000, 3000],
                    search: false,
                    detailView: false,
                    detailFormatter: null,
                    sidePagination: "server",
                    queryParamsType: "undefined",
                    responseHandler: function (res) {
                        return {"rows": res.data, "total": res.rowsCount};
                    },
                    columns: eval("(" + data.KEY_TABLE_COLUMNS + ")")
                });
            }
        },
        error: function (e) {
            layer.msg(e.error);
        }
    });
}

// 聚合数据表格查询
function queryAggrTable(tblComponentId, dimensionIds, targetIds) {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "../../report/component/table/header",
        data: {
            dimensionIds: dimensionIds,
            targetIds: targetIds,
            componentId: tblComponentId
        },
        success: function (data) {
            var aggrTable = $("#table_list");
            var fixedCol = $("#tableComponentFixedCol").val();

            var tablePaginationFlag = $("#tableComponentPagination").val();

            // 设置查询url及是否分页变量
            var url = '';
            var isPaging = true;
            if (tablePaginationFlag == "Y") {
                url = "../../report/component/table/data?";
            } else {
                url = "../../report/component/table/data-no-paging?";
                isPaging = false;
            }
            url += $("#frm").serialize() + "&componentId=" + $('#tableComponentId').val() + "&dimensionIds=" + dimensionIds + "&targetIds=" + targetIds;

            aggrTable.bootstrapTable('destroy');
            if (fixedCol != null && fixedCol != "") {
                aggrTable.bootstrapTable({
                    method: "GET",
                    contentType: "application/json",
                    url: url,
                    striped: true,
                    pagination: isPaging,
                    pageSize: 10,
                    pageNumber: 1,
                    pageList: [10, 30, 50, 100, 500, 1000, 2000, 3000],
                    search: false,
                    detailView: false,
                    detailFormatter: null,
                    sidePagination: "server",
                    queryParamsType: "undefined",
                    fixedColumns:true,
                    fixedNumber:fixedCol,
                    responseHandler: function (res) {
                        return {"rows": res.data, "total": res.rowsCount};
                    },
                    onLoadSuccess: function (res) {
                        addMergeAndSummaryRow(res);
                    },
                    columns: eval("(" + data.KEY_TABLE_COLUMNS + ")")
                });
            } else {
                aggrTable.bootstrapTable({
                    method: "GET",
                    contentType: "application/json",
                    url: url,
                    striped: true,
                    pagination: isPaging,
                    pageSize: 10,
                    pageNumber: 1,
                    pageList: [10, 30, 50, 100, 500, 1000, 2000, 3000],
                    search: false,
                    detailView: false,
                    detailFormatter: null,
                    sidePagination: "server",
                    queryParamsType: "undefined",
                    responseHandler: function (res) {
                        return {"rows": res.data, "total": res.rowsCount};
                    },
                    onLoadSuccess: function (res) {
                        addMergeAndSummaryRow(res);
                    },
                    columns: eval("(" + data.KEY_TABLE_COLUMNS + ")")
                });
            }
        },
        error: function (e) {
            layer.msg(e.error);
        }
    });
}

function addMergeAndSummaryRow(res) {
    // 合并单元格
    var rowMergeCols = $('#tableRowMergeCols').val().split(",");
    if (null != rowMergeCols && "" != rowMergeCols && rowMergeCols.length > 0 && null != res.rows) {
        for (var i = 0; i < rowMergeCols.length; i++) {
            var colName = rowMergeCols[i];
            var colVal = '';
            var rowStart = 0;
            var rowCount = 0;
            for (var j = 0; j < res.rows.length; j++) {
                var row = res.rows[j];
                if (colVal == '') {
                    colVal = row[colName];
                    rowCount++;
                } else {
                    if (colVal == row[colName]) {// 行的值相同
                        // 计数加1
                        rowCount++;
                        // 最后一行
                        if (j == res.rows.length - 1) {
                            $('#table_list').bootstrapTable('mergeCells', {
                                index: rowStart,
                                field: colName,
                                rowspan: rowCount
                            });
                        }
                    } else {// 行值不同，将前面相同行值的所有行合并
                        $('#table_list').bootstrapTable('mergeCells', {
                            index: rowStart,
                            field: colName,
                            rowspan: rowCount
                        });
                        colVal = row[colName];
                        rowCount = 1;
                        rowStart = j;
                    }
                }
            }
        }
    }

    // 小计
    var rowSummary = $("#tableRowSummary").val();
    var rowColSummary = {};
    var rowInsertArray = [];
    if ('' != rowSummary && null != res.rows && res.rows.length > 0) {
        // 获取列名
        var colNames = Object.keys(res.rows[0]);
        // 循环计算小计
        var currentRowSummaryColVal = undefined;
        for (var i = 0; i < res.rows.length; i++) {
            var row = res.rows[i];
            if (typeof (currentRowSummaryColVal) != "undefined" && currentRowSummaryColVal != row[rowSummary]) {
                rowColSummary[rowSummary] = '小计';
                rowInsertArray.push({
                    "rowNum": i,
                    'rowColSummary': rowColSummary
                });
                currentRowSummaryColVal = row[rowSummary];
                rowColSummary = {};
            } else {
                currentRowSummaryColVal = row[rowSummary];
            }
            for (var j = 0; j < colNames.length; j++) {
                var colName = colNames[j];
                var colVal = row[colName];
                if (colName != rowSummary) {
                    if (null == rowColSummary[colName]) {
                        if (null != colVal && !isNaN(colVal)) {
                            rowColSummary[colName] = colVal;
                        }
                    } else {
                        if (null != colVal && !isNaN(colVal)) {
                            rowColSummary[colName] += colVal;
                        }
                    }
                }
            }
            if (i == res.rows.length - 1) {
                rowColSummary[rowSummary] = '小计';
                rowInsertArray.push({
                    "rowNum": i + 1,
                    'rowColSummary': rowColSummary
                });
                break;
            }
        }
        // 插入小计行
        for (var i = rowInsertArray.length - 1; i > -1; i--) {
            var rowNum = rowInsertArray[i]['rowNum'];
            var rowColSummary = rowInsertArray[i]['rowColSummary'];
            $('#table_list').bootstrapTable('insertRow', {
                index: rowNum,
                row: rowColSummary
            });
        }
    }
}

// 透视数据表格查询
function queryPivotTable(componentId, dimensionIds, targetIds, pivotDimensionId, pivotTargetIds) {
    var requestData = {
        dimensionIds: dimensionIds,
        targetIds: targetIds,
        componentId: componentId,
        pivotDimensionId: pivotDimensionId,
        pivotTargetIds: pivotTargetIds
    };

    var params = $("#frm").serialize().split("&");
    for (var i = 0; i < params.length; i++) {
        var paramArray = params[i].split("=");
        var val = paramArray[1];
        if (null != val && "" != val) {
            requestData[paramArray[0]] = val;
        }
    }

    $.ajax({
        type: "GET",
        dataType: "json",
        url: "../../report/component/table/pivot",
        data: requestData,
        success: function (data) {
            var pivotTable = $("#table_list");
            var fixedCol = $("#tableComponentFixedCol").val();
            pivotTable.bootstrapTable('destroy');
            if (fixedCol != null && fixedCol != "") {
                pivotTable.bootstrapTable({
                    fixedColumns:true,
                    fixedNumber:fixedCol,
                    columns: eval("(" + data.KEY_TABLE_COLUMNS + ")"),
                    data: data.KEY_TABLE_DATA
                });
            } else {
                pivotTable.bootstrapTable({
                    columns: eval("(" + data.KEY_TABLE_COLUMNS + ")"),
                    data: data.KEY_TABLE_DATA
                });
            }
        },
        error: function (e) {
            layer.msg(e.error);
        }
    });
}
/****************************** END 数据列表部分 ******************************/


/****************************** START 图形部分 ******************************/
// 查询图形
function queryChartTemplate() {
    var targetIds = "";
    $.each($("#chartTargetDiv input[type='checkbox']:checked"), function () {
        targetIds += $(this).val() + ",";
    });

    if (targetIds == '') {
        $.each($("#chartTargetDiv input[type='radio']:checked"), function () {
            targetIds += $(this).val() + ",";
        });
    }

    if (targetIds == '') {
        layer.alert("图形指标不能为空！");
        return;
    }
    var componentId = $("#chartTypeDiv input[type='radio']:checked").val();
    $.ajax({
        type: "GET",
        dataType: "html",
        url: "../../report/component/chart/template/query",
        data: {
            componentId: componentId
        },
        success: function (data) {
            $("#chartScriptDiv").html(data);
        },
        error: function (e) {
            layer.msg(e.error);
        }
    });
}

// 刷新图形头部维度和指标
function refreshChartHeaderDetailHtml() {
    var componentId = $("#chartTypeDiv input[type='radio']:checked").val();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "../../report/component/chart/header/build",
        data: {
            componentId: componentId
        },
        success: function (data) {
            buildChartConditionHeaderHtml(data);
            queryChartTemplate();
            // 监听维度、指标和图形类型点击事件
            addChartHeaderEventListener();
        },
        error: function (e) {
            layer.msg(e.error);
        }
    });
}

// 下钻页面方法
function drillReport(paramStr, pageTitle) {
    var url = 'index.html?' + paramStr;
    var drillLayer = layer.open({
        type: 2,
        title: pageTitle,
        shadeClose: true,
        maxmin: true,
        anim: 3,
        shade: [0.8, '#393D49'],
        area: ['893px', '500px'],
        content: url
    });
    layer.full(drillLayer);
}
/****************************** END 图形部分 ******************************/

/****************************** START 生成CVS ******************************/
function exportCvs() {
    var tblDimensionIds = "";
    $.each($("#tblDimensionDiv option:selected"), function () {
        tblDimensionIds += $(this).val() + ",";
    });

    if (tblDimensionIds == '') {
        layer.alert("数据列表维度不能为空！");
        return;
    }
    var tblTargetIds = "";
    $.each($("#tblTargetDiv option:selected"), function () {
        tblTargetIds += $(this).val() + ",";
    });
    var tblComponentId = $('#tableComponentId').val();
    var tableComponentType = $("#tableComponentType").val();

    if ("TABLE_COMMON_TABLE" == tableComponentType) {
        exportTable(tblComponentId, tblDimensionIds, tblTargetIds);
    } else if ("TABLE_AGGR_TABLE" == tableComponentType) {
        exportTable(tblComponentId, tblDimensionIds, tblTargetIds);
    } else if ("TABLE_PIVOT_TABLE" == tableComponentType) {
        var pivotDimensionId = $("#tblPivotDimDiv option:selected").val();
        var pivotTargetIds = "";
        $.each($("#tblPivotTargetDiv option:selected"), function () {
            pivotTargetIds += $(this).val() + ",";
        });
        exportPivotTable(tblComponentId, tblDimensionIds, tblTargetIds, pivotDimensionId, pivotTargetIds);
    }

}

// 普通数据表格查询
function exportTable(tblComponentId, dimensionIds, targetIds) {
    location.href="../../report/component/table/export?dimensionIds="+dimensionIds+"&targetIds="+targetIds+"&componentId="+tblComponentId+"&"+$("#frm").serialize();
}
// 透视表格查询
function exportPivotTable(tblComponentId, dimensionIds, targetIds, pivotDimensionId, pivotTargetIds) {
    location.href="../../report/component/table/pivot/export?dimensionIds="+dimensionIds+"&targetIds="+targetIds+"&componentId="+tblComponentId+"&pivotDimensionId="+pivotDimensionId+"&pivotTargetIds="+pivotTargetIds+"&"+$("#frm").serialize();
}
/****************************** END 生成CVS ******************************/