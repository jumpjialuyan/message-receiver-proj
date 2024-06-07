/**
 * Created by Administrator on 2023/10/5.
 */

function sendRequest(url,requestData,type,callback){
    //var url = "http://localhost:9123/servlet/faultTrigger"
    var resultBean = {};

    $.ajax(
        {
            url:url,
            data:requestData,
            type:type,
            timeout:60000,
            success:function(result){
                callback(result)
                /*console.log(result)
                 $('#content').html("请求成功!");
                 $('#myModal').modal('show');*/
            },
            error:function(xhr,status,error){
                resultBean.errorCode = 1;
                resultBean.message = error;
                callback(JSON.stringify(resultBean))
                /*$('#content').html("请请求失败!"+error);
                 $('#myModal').modal('show');*/
            }
        }
    );
}
