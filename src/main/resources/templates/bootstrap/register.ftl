<#include "./common/layout.ftl">
<@html page_title="注册" page_tab="register">
<div class="row">
  <div class="col-md-9">
    <div class="panel panel-default">
      <div class="panel-heading">
        <a href="/">主页</a> / 注册
      </div>
      <div class="panel-body" style="margin:0 auto">
        <#if errors??>
        <div class="alert alert-danger">${errors!}</div>
        </#if>
        <form role="form" action="/register" method="post" id="form">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    
		  <input name ="baseurl" id="baseurl" type="hidden" value="${baseUrl}"/>
		  
          <div class="form-group">
            <label for="username">用户名</label>
            <input type="text" class="form-control" id="username" name="username" placeholder="用户名" value="${username!''}">
          </div>
          <div class="form-group">
            <label for="password">密码</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="密码" value="${password!''}">
          </div>
          <div class="form-group">
            <label for="password">确认密码</label>
            <input type="password" class="form-control" id="password2" name="password2" placeholder="再次输入密码" value="${password2!''}">
          </div>
          <div class="form-group">


					<table>
								<tr>
									<td>验证码</td>
									<td><input name="checkCode" type="text" id="checkCode"
										title="验证码区分大小写" maxlength="4" /></td>
									<td><div id="info"></div><img id="createCheckCode" src="getCode" /> <a href="#"
										onClick="changeImg()">看不清，换一个</a></td>
								</tr>
							
					</table>

			</div>
			
			<input name="confirm" id="confirm" type="checkbox" checked='checked'>我认真阅读并接受注册协议</input>
			
			<a href="../agree.ftl"><<注册协议>></a>
          <button type="submit" class="btn btn-default" id="register">注册</button>
        </form>
      </div>
      <!--
      <div class="col-md-3 hidden-sm hidden-xs">
	    <#if user??>
	      <#include "components/user_info.ftl">
	      <@info/>
	      <#include "components/create_topic.ftl">
	      <@create_topic/>
	    <#else>
	      <#include "./components/welcome.ftl">
	      <@welcome/>
	    </#if>

    </div>-->
      
    </div>
  </div>
</div>
<script>

  $(function () {
  
  	var register = $("#register");
    $("#confirm").change(function(){
        var that = $(this);
        that.prop("checked",that.prop("checked"));
        if(that.prop("checked")){
            register.prop("disabled",false)
        }else{
            register.prop("disabled",true)
        }
    });
   
  
    $("#register").click(function () {
     if (checkField()) {
      
      ("#form").submit();
      }
      
    })
    
  <#--    $("#checkCode").blur(function() {
			myReload();
		})-->
		
  })
  
  function checkField(){
  
     var username = $("#username").val();
      
      var password = $("#password").val();
      
      var password2 = $("#password2").val();

      if(username.length < 3) {
        alert("用户名不能小于3个字符");
        return false;
      }

      if(password.length < 6) {

        alert("密码不能小于6位数");
        return false;
      }
      
      if(password!=password2) {
      
         alert("确认密码不一致");
        return false;
         
      }
      
      return true;
  
  }
  
  function myReload() {
		$.ajax({
			url : "http://localhost:8080/checkCode",
			type : 'GET',
			data : {
				code : $.trim($("input[name=checkCode]").val())
			},
			dataType : 'json',
			async : false,
			success : function(result) {
			
				if (result.flg == 1) {
					$("#info").html('验证通过！'); //验证码正确提交表单
				} else {
					changeImg();
					$("#info").html('验证码错误！');
					setTimeout(function() {
						$("#info").empty();
					}, 3000);
				}
			},
			error : function(msg) {
				$("#info").html('Error:' + msg.toSource());
			}

		})

	}

	function changeImg() {
	 
	    var baseUrl=$("#baseurl").val();
		var imgSrc = $("#createCheckCode");
		var src = imgSrc.attr("src");
		imgSrc.attr("src", chgUrl(baseUrl+src));
	}
	//时间戳
	//为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳
	function chgUrl(url) {
		var timestamp = (new Date()).valueOf();
		// url = url.substring(0, 17);
		// alert("===url="+url);
		if ((url.indexOf("&amp;") >= 0)) {
			url = url + "×tamp=" + timestamp;
		} else {
			url = url + "?timestamp=" + timestamp;
		}
		return url;
	}

	function isRightCode() {
		var code = $("#checkCode").attr("value");
		code = "c=" + code;
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/getCode",
			data : code,
			success : callback
		});
	}
	function callback(data) {
		$("#info").html(data);
	}
  
</script>
</@html>