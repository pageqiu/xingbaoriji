<#include "./common/layout.ftl">
<@html page_title="登录" page_tab="login">
<div class="row">
  <div class="col-md-9">
    <div class="panel panel-default">
      <div class="panel-heading">
        <a href="/">主页</a> / 登录
      </div>
      <div class="panel-body">
        <#if SPRING_SECURITY_LAST_EXCEPTION??>
         <div class="alert alert-danger">用户名或密码错误</div>
         
        </#if>
        <#if s?? && s == "reg">
          <div class="alert alert-success">注册成功，快快登录吧！</div>
        </#if>
        <form role="form" action="/login" method="post">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <div class="form-group">
            <label for="username">用户名</label>
            <input type="text" class="form-control" id="username" name="username" placeholder="用户名">
          </div>
          <div class="form-group">
            <label for="password">密码</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="密码">
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
          <button type="submit" class="btn btn-default">登录</button>
        </form>
      </div>
    </div>
  </div>
</div>

<script>

	function changeImg() {
		var imgSrc = $("#createCheckCode");
		var src = imgSrc.attr("src");
		imgSrc.attr("src", chgUrl(src));
	}
	//时间戳
	//为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳
	function chgUrl(url) {
		var timestamp = (new Date()).valueOf();
		url = url.substring(0, 17);
		if ((url.indexOf("&amp;") >= 0)) {
			url = url + "×tamp=" + timestamp;
		} else {
			url = url + "?timestamp=" + timestamp;
		}
		return url;
	}

	function isRightCode() {
	
	    var base = $("base").href;
	    alert("------"+base);
		var code = $("#checkCode").attr("value");
		code = "c=" + code;
		$.ajax({
			type : "POST",
			url : base+"getCode",
			data : code,
			success : callback
		});
	}
	function callback(data) {
		$("#info").html(data);
	}

</script>

</@html>