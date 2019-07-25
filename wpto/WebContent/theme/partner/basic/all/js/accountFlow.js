$(document).ready(function () {
    loadTable();
});


//查询明细列表信息
function queryAccountFlow(pageNum) {
    var accountNo = $("#accountNo").val().trim();
    var fundType = $("#fundType option:selected").val().trim();
    var optType = $("#optType option:selected").val().trim();
    var transactionId = $("#transactionId").val().trim();
    var createdDateBegin = $("#createdDateBegin").val().trim();
    var createdDateEnd = $("#createdDateEnd").val().trim();
    //参数拼接
    var param = {
        "starts": pageNum,
        "accountNo": accountNo,
        "fundType": fundType,
        "optType": optType,
        "transactionId": transactionId,
        "createdDateBegin": createdDateBegin,
        "createdDateEnd": createdDateEnd
    };

    var paramStr = JSON.stringify(param);
    $.ajax({
        type: "POST",
        url: "/accountFlow/get.json",
        dataType: "json",
        data: {
            paramStr: paramStr
        },
        success: function (result) {
            loadData(result, pageNum);
        },
        error: function () {

        }
    });
}

//查询流水详细信息
function queryAccountFlowById(flowId) {
    //参数拼接
    var param = {
        "id": flowId
    };

    var paramStr = JSON.stringify(param);
    $.ajax({
        type: "POST",
        url: "/accountFlow/getDetail.json",
        dataType: "json",
        data: {
            paramStr: paramStr
        },
        success: function (result) {
            loadDetailInfo(result);
        },
        error: function () {

        }
    });
}
//详细信息数据处理
function loadDetailInfo(result) {
    if (result.code == "00000") {
        //数据处理
        var data = result.data;
        $("#flow_id").html(data.id);
        $("#account_no").html(data.accountNo);//账户
        //账户类型
        $("#account_type").html(getAccountType(data.accountNo.substring(data.accountNo.length - 4, data.accountNo.length)));
        $("#member_no").html(data.memberNo);//会员号
        $("#org_account_no").html(data.orgAccountNo);//来源账户
        $("#org_member_no").html(data.orgMemberNo);//来源会员
        //$("#trade_type").html(data.tradeType);//交易类型
        //$("#sub_trade_type").html(data.subTradeType);
        $("#fund_type").html(getFundType(data.fundType));
        $("#trade_amount").html(amountFormat(data.tradeAmount) + " 元");
        $("#finish_amount").html(amountFormat(data.finishAmount) + " 元");
        $("#order_amount").html(amountFormat(data.orderAmount) + " 元");
        $("#transaction_id").html(data.transactionId);
        $("#bank_transaction_id").html(data.bankOrderId);
        $("#currency").html(data.currency);//货币
        $("#account_category").html(getAccountCategory(data.accountCategory));
        $("#opt_type").html(getFundOperateType(data.optType));
        $("#trade_status").html(getTradeStatus(data.tradeStatus));
        // $("#clear_type").html(data.clearStatus);
        $("#trade_subject").html(data.tradeSubject);
        $("#clear_status").html(getClearStatus(data.clearStatus));
        $("#created_date").html(formatDate(data.createdDate));
        $("#success_date").html(formatDate(data.successDate));
        $("#modified_date").html(formatDate(data.modifiedDate));
        //展示框
        $("#flowDetailInfo").css("display", "block");
        $(".sweet-overlay").css("display", "block");
    } else {
        //无数据
        alert("无数据信息！");
    }

}

function closeDiv() {
    //隐藏狂
    $("#flowDetailInfo").css("display", "none");
    $(".sweet-overlay").css("display", "none");
}

//查询事件
function queryAgain() {
    var accountNo = $("#accountNo").val().trim();
    var fundType = $("#fundType option:selected").val().trim();
    var optType = $("#optType option:selected").val().trim();
    var transactionId = $("#transactionId").val().trim();
    var createdDateBegin = $("#createdDateBegin").val().trim();
    var createdDateEnd = $("#createdDateEnd").val().trim();
    //参数拼接
    var param = {
        "accountNo": accountNo,
        "fundType": fundType,
        "optType": optType,
        "transactionId": transactionId,
        "createdDateBegin": createdDateBegin,
        "createdDateEnd": createdDateEnd
    };

    $('#grid_table').GM('setQuery', param);

}

function loadTable() {
    $('#grid_table').GM({
        supportRemind: true
        ,
        gridManagerName: 'test'
        ,
        supportAjaxPage: true
        ,
        supportSorting: false
        ,
        ajax_url: '/accountFlow/get.json'
        ,
        ajax_type: 'POST'
        ,
        pageSize: 10
        ,
        query: {
            'createdDateBegin': $("#createdDateBegin").val().trim(),
            'createdDateEnd': $("#createdDateEnd").val().trim()
        }
        ,
        supportDrag: false
        ,
        supportRemind: false
        ,
        supportSorting: false
        ,
        supportAutoOrder: false
        ,
        height: 'auto'
        ,
        supportCheckbox: false
        ,
        supportConfig: false
        ,
        columnData: [
            {key: 'accountNo', remind: '账户', text: '账户'},
            {
                key: 'null3', remind: '账户类型', text: '账户类型',
                template: function (nodeData, rowData) {
                    return getAccountType(rowData.accountNo.substring(rowData.accountNo.length - 4, rowData.accountNo.length));
                }
            },
            {key: 'transactionId', remind: '订单号', text: '订单号'},
            {
                key: 'optType', remind: '操作类型', text: '操作类型',
                template: function (nodeData, rowData) {
                    return getFundOperateType(nodeData);
                }
            },
            {
                key: 'fundType', remind: '资金类型', text: '资金类型',
                template: function (nodeData, rowData) {
                    return getFundType(nodeData);
                }
            },
            {
                key: 'tradeAmount', remind: '交易金额', text: '交易金额',
                template: function (nodeData, rowData) {
                    return amountFormat(nodeData);
                }
            },
            {
                key: 'finishAmount', remind: '完成后余额', text: '完成后余额',
                template: function (nodeData, rowData) {
                    return amountFormat(nodeData);
                }
            },
            {
                key: 'tradeStatus', remind: '交易状态', text: '交易状态',
                template: function (nodeData, rowData) {
                    return getTradeStatus(nodeData);
                }
            },
            {
                key: 'accountCategory', remind: '账目类型', text: '账目类型',
                template: function (nodeData, rowData) {
                    return getAccountCategory(nodeData);
                }
            },
            {
                key: 'clearStatus', remind: '清算状态', text: '清算状态',
                template: function (nodeData, rowData) {
                    return getClearStatus(nodeData);
                }
            },
            {
                key: 'successDate', remind: '完成时间', text: '完成时间',
                template: function (nodeData, rowData) {
                    return formatDate(nodeData);
                }
            },
            {
                key: 'id', remind: '操作', text: '操作',
                template: function (nodeData, rowData) {
                    var str = "<span style=\"cursor:pointer;\" onclick='queryAccountFlowById(" + nodeData + ")'>查看</span>";
                    return str;
                }
            }
        ]
        ,
        responseHandler: function (response) {
            if (response.other != null && response.other != "") {
                $("#data_statistics #flowCount").html(response.other.flowCount);
                $("#data_statistics #totalMoney").html(amountFormat(response.other.totalMoney));
                $("#data_statistics #successMoney").html(amountFormat(response.other.successMoney));
            } else {
                $("#data_statistics #flowCount").html(0);
                $("#data_statistics #totalMoney").html(0);
                $("#data_statistics #successMoney").html(0);
            }
        }
    });
}
