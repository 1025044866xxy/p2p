//注意：若将代码写到$(function(){})中，则里面的所有方法都不是全局的，故此a标签中的onclick调用不到里面的方法！！！。
//总记录数
var totalNum = 0;

//总页数
var totalPage = 0;

//默认每页显示数
var avageNum = 20;

//默认显示第一页
var currentPage = 1;


getUserList(currentPage,avageNum);
initPagination(totalPage,totalNum);

//与后台交互获取数据，异步加载到页面上
function getUserList(pageNum,pageSize){
    currentPage = pageNum;
    $(".panel-body table tbody").html(" ");
    $.ajax({
        url:"/borrow/borrow-money/list",
        method:"get",
        data:{"pageNo":pageNum,"pageSize":pageSize},
        async:false,
        beforeSend: function (request) {
                                request.setRequestHeader("token", $.cookie('token'));
                            },
        success:function(data){
            if (data.result != null){
                var length = data.result.resultList.length;
                totalNum = data.result.resultCount;
                totalPage = Div(totalNum,avageNum)+1;
                for(var  i = 0;i<length;i++){
                    var startDate = data.result.resultList[i].startDate;
                    var endDate = data.result.resultList[i].endDate;
                    var id = data.result.resultList[i].id;
                    var money = data.result.resultList[i].money;
                    var repayment = data.result.resultList[i].repayment;
                    var overMoney = data.result.resultList[i].totalMoney-repayment;
                    var interestMoney = data.result.resultList[i].interestMoney;
                    var state = data.result.resultList[i].state;
                    var item = $('<tr></tr>');
                    item.append('<td hidden>'+id+'</td>'+
                                                        '<td>'+startDate+'</td>'+
                                                        '<td>'+endDate+'</td>'+
                                                        '<td>'+interestMoney+'</td>'+
                                                        '<td>'+money+'</td>'+
                                                        '<td>'+repayment+'</td>'+
                                                        '<td>'+overMoney+'</td>');
                    if(state == 1){
                                        console.log(state);
                                           item.append(
                                                            '<td>'+"已还清"+'&nbsp;'+'&nbsp;'+'<a href="repayment-detail.html?id='+id+'">'+'还款明细'+'</a></td>')
                                        }else{
                                        console.log(id);
                                        item.append('<td><a href="repayment.html?id='+id+'">'+'立即还款'+'</a>'+'&nbsp;'+'&nbsp;'+'<a href="repayment-detail.html?id='+id+'">'+'还款明细'+'</a></td>')
//                                        item.append(
//                                        '<td><a href="repayment.html?id="' + id + '"" >' +'立即还款'+ '</a> </td>')
//                                        '<td><a href = "repayment?id='+id'">'+'立即还款'+'</a></td>')
                                        }
                    $("#userListTable").append(item);

                }
            }
            else{
            alert(data.errorDesc);
            window.location.href = 'login.html';
            }
        }
    });
}

//初始化分页栏
function initPagination(totalPage,totalNum) {
    $('#pagination').html(" ");
    $('#pagination').append(
        '<ul class="pagination" style="display:inline;">' +
        '<span style="float: right;">每页显示' +
        '<select id="dataNum">' +
        '<option value="1">20</option>' +
        '</select>条记录,总共有' + totalPage + '页，总共' + totalNum + '条记录</span>' +
        '</ul>'
    )
    // οnclick="getUserList('+ (currentPage - 1) + ','+ avageNum + ')"
    $("#pagination ul").append(
        '<li><a href="javascript:void(0);" id="prev">上一页</a>'
    )
    for (var i = 1; i <= totalPage; i++) {
        $("#pagination ul").append(
            '<li id="page'+i+'"><a href="javascript:void(0);"  οnclick="getUserList(' + i + ',' + avageNum + ')">' + i + '</a>'
        )
    }
    $("#pagination ul").append(
        '<li><a href="javascript:void(0);"  id="next">下一页</a>'
    )
    $("select option[value=" + avageNum + "]").attr('selected', true)
    $("#page1").addClass("active");
    checkA();
}

//很关键，因为执行initPagination方法时，将select删除再重新添加，所以需要先将select上的结点移除off
//然后再绑定结点on，如果不这么做，会出现change事件只被触发一次。
$(document).off('change','#dataNum').on('change','#dataNum',function(){
    avageNum = $(this).children('option:selected').val();
    currentPage = 1;
    getUserList(currentPage,avageNum);
    initPagination(totalPage,totalNum);
});

//设置分页栏点击时候的高亮
$("#pagination").on("click","li",function(){
    var aText = $(this).find('a').html();
    checkA();
    if (aText == "上一页"){
    if(currentPage == 1){return ;}
        $(".pagination > li").removeClass("active");
        currentPage = currentPage -1;
        $("#page"+(currentPage)).addClass("active");
        getUserList(currentPage,avageNum);
        checkA();
    }else if(aText == "下一页"){
    if(currentPage == totalPage){return ;}
    currentPage = currentPage+1;
        $(".pagination > li").removeClass("active");
        $("#page"+(currentPage)).addClass("active");
        getUserList(currentPage,avageNum);
        checkA();
    }else{
        $(".pagination > li").removeClass("active");
        $("#page"+(aText)).addClass("active");
        currentPage = aText;
        getUserList(currentPage,avageNum);
        checkA();
    }
})

//因为其他地方都需要用到这两句，所以封装出来
//主要是用于检测当前页如果为首页，或者尾页时，上一页和下一页设置为不可选中状态
function checkA() {
    currentPage == 1 ? $("#prev").addClass("btn btn-default disabled") : $("#prev").removeClass("btn btn-default disabled");
    currentPage == totalPage ? $("#next").addClass("btn btn-default disabled") : $("#next").removeClass("btn btn-default disabled");
}
function Div(exp1, exp2)
{
    var n1 = Math.round(exp1); //四舍五入
    var n2 = Math.round(exp2); //四舍五入
   
    var rslt = n1 / n2; //除
   
    if (rslt >= 0)
    {
        rslt = Math.floor(rslt); //返回值为小于等于其数值参数的最大整数值。
    }
    else
    {
        rslt = Math.ceil(rslt); //返回值为大于等于其数字参数的最小整数。
    }
   
    return rslt;
}
