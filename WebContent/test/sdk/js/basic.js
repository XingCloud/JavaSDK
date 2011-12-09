/**
 * 进入页面时先加载这个js
 */
$(function(){
	init();
	//获得初始的服务列表
	function init(){	
		$("#submitButton").hide();
		$("#inputTable").hide();
		$("#serviceContent").hide();
		$("#resultField").hide();
		//清空
		$("#serviceName").val("");
		$("#methodName").val("");
		//查找serivce的地址
		var urlStr="../../back/rest/discovery/getServices";
		$.ajax({
			type:"POST",
			url:urlStr,
			data:"",
			dataType:"jason",
			success:function(jasonObj){
				var obj =JSON.parse(jasonObj);
				//只需要参数中的data
				var data = obj.data;
				for(var service in data)
				{
					//获得第一个值
					var stringtemp = service.substring(0,service.indexOf("."));
					//如果是action服务
					if(stringtemp=="Action"){
						$("#actionmenuList").append("<br>");
						$("#actionmenuList").append("<div class='serviceNames'>"+service+"</div>");
						for(var serobj in data[service])
						{
							$("#actionmenuList").append("<div class='method' serviceid='"+service+"'>"+serobj+"</div>");
						}
					}
					//如果是admin服务
					else if(stringtemp=="Admin"){
						$("#adminmenuList").append("<br>");
						$("#adminmenuList").append("<div class='serviceNames'>"+service+"</div>");
						for(var serobj in data[service])
						{
							$("#adminmenuList").append("<div class='method' serviceid='"+service+"'>"+serobj+"</div>");
						}
					}
					//如果是Change值
					else if(stringtemp=="Change"){
						$("#auditchangemenuList").append("<br>");
						$("#auditchangemenuList").append("<div class='serviceNames'>"+service+"</div>");
						for(var serobj in data[service])
						{
							$("#auditchangemenuList").append("<div class='method' serviceid='"+service+"'>"+serobj+"</div>");
						}
					}
					//如果是Change值
					else if(stringtemp=="Amf"){
					}
					else{
						$("#servicemenuList").append("<br>");
						$("#servicemenuList").append("<div class='serviceNames'>"+service+"</div>");
						for(var serobj in data[service])
						{
							$("#servicemenuList").append("<div class='method' serviceid='"+service+"'>"+serobj+"</div>");	
						}
					}
				}
				$("#servicemenuList").show();
				$("#auditchangemenuList").hide();
				$("#adminmenuList").hide();
				$("#actionmenuList").hide();
				$("#servicemenu").css({"color":"red"});
				$("#actionmenu").css({"color":"black"});
				$("#adminmenu").css({"color":"black"});
				$("#auditchangemenu").css({"color":"black"});
			}
		});		
	}
	
	//获得单个服务的信息
	$(".method").live("click", function() {
		//获得服务id
		var serviceid = jQuery(this).attr("serviceid");
		//获得方法名
		var opertion = jQuery(this).html();
		$(".method").css({"color":"black"});
		jQuery(this).css({"color":"blue"});
		var urlStr="../../back/rest/discovery/getSingleService";
		var input = {methodName:opertion,serviceID:serviceid};
		$.ajax({
			type:"get",
			url:urlStr,
			data:input,
			dataType:"jason",
			success:function(jasonObj){
				var obj =JSON.parse(jasonObj);					
				var data = obj.data;
				//清除之前的data值
				$(".tempdata").remove();
				//清空方法描述
				$("#methodDescription").empty();
				for(var param in data)
				{
					$("#serviceName").val(serviceid);
					$("#methodName").val(param);
					for(var element in data[param])
					{
						if(element=="Description")
						{
							for(var elementobj in data[param][element])
							{
								var description = decodeURI(data[param][element][elementobj]);
								description = encodeTransform(description);
								$("#methodDescription").append("<br><h3>description</h3>"+description);
							}
						}
						if(element=="Serviceparam")
						{
							for(var paramtemp in data[param][element])
							{
								var remark="";
								var name="";
								var type="";
								for(var single in data[param][element][paramtemp])
								{
									//评论参数
									if(single=="remark")
									{
										remark = decodeURI(data[param][element][paramtemp][single]);
										//还原特殊符号
										remark = encodeTransform(remark);									
										
									}
									//名字
									if(single=="name")
									{
										name = data[param][element][paramtemp][single];
										
									}
									//类型
									if(single=="type")
									{
										type = data[param][element][paramtemp][single];
										
									}
								}
								$("#serviceTable").append("<tr class='tempdata'><td><input value='"+name+"' type='text' class='dataName' readonly>"+
								"</td><td><input value='' type='text' class='dataValue'></td><td>"+
								"<input value='"+type+"' type='text' readonly></td><td><input value='"+remark+"' type='text' readonly></td></tr>");	
							}
						}
					}
				}
			}
		}); 
		$("#submitButton").show();
		$("#inputTable").show();
		$("#serviceContent").show();
	}); 
	
	//调用某个具体的服务
	$("#submitButton").live("click", function() {
		var serviceName = $("#serviceName").val();
		var methodName = $("#methodName").val();
		var getpost = $("input[name='getpost']:checked").val();
		//首字母大写转换为小写
		serviceName =  serviceName.substring(0,1).toLowerCase()+serviceName.substring(1,serviceName.length);
		
		serviceName = serviceName.replace(".","/");
		var idValue = $("#idValue").val();
		var platformAppIdValue =$("#platformAppIdValue").val();
		var platformUserIdValue = $("#platformUserIdValue").val();
		var gameUserIdValue = $("#gameUserIdValue").val();
		var dataIn = "{";
		if(serviceName=="user/user"&&methodName=="register")
		{
			dataIn = dataIn +"\"account\":{";
		}
		$(".tempdata").each(function(){
			var dataNameTemp = $(this).find(".dataName").val();
			var dataValuetemp = $(this).find(".dataValue").val();
			dataIn = "\""+dataIn +"\""+ dataNameTemp + "\":\"" + dataValuetemp+"\",";
		});
		if(serviceName=="user/user"&&methodName=="register")
		{
			dataIn = dataIn +"}";
		}
		dataIn = dataIn + "}";
		
		var serviceAdminTest = serviceName.substring(0,serviceName.indexOf("/"));
		var urlStr;
		var showurl;
		//不同的服务访问的地址不同
		if(serviceAdminTest=="admin"||serviceAdminTest=="amf"||serviceAdminTest=="status"||serviceAdminTest=="discovery"){
			urlStr = "../../back/"+serviceName+"/"+methodName;
			showurl = getRootPath()+"/back/"+serviceName+"/"+methodName;			
		}
		else if(serviceAdminTest=="item"){
			urlStr = "../../back/file/"+serviceName+"/"+methodName;
			showurl = getRootPath()+"/back/file/"+serviceName+"/"+methodName;				
		}
		else{
			urlStr = "../../back/rest/"+serviceName+"/"+methodName;
			showurl = getRootPath()+"/back/rest/"+serviceName+"/"+methodName;
		}
		
		//如果是action的excute方法单独处理
		if(serviceAdminTest=="action"&&methodName=="execute")
		{
			dataIn = $(".tempdata").find(".dataValue").val();
		}
		
		if(getpost=="post"){
			var inputData = {id:idValue,info:{platformAppId:platformAppIdValue,platformUserId:platformUserIdValue,gameUserId:gameUserIdValue},data:dataIn};
			var objx = $.toJSON(inputData);
			//将返回json搞正常
			objx=objx.replace(/\\/g,"");
			objx=objx.replace(/}\"/g,"}");
			objx=objx.replace(/\"{/g,"{");
			objx=objx.replace(/\"{/g,"{");
			objx=objx.replace(/\"{/g,"{");
			objx=objx.replace(/,}/g,"}");
			objx=objx.replace("\"[","[");
			objx=objx.replace("]\"","]");
			objx=objx.replace("\"\"\"","");

			//显示json字符串
			$("#jsonoutput").val(objx);
			//显示访问地址
			$("#urloutput").val(showurl);
			$.ajax({
				type:getpost,
				url:urlStr,
				data:objx,
				success:function(jasonObj){
					//判断返回类型，xml或者是json
					if(jasonObj instanceof XMLDocument){
						if(jasonObj.xml){//不在IE中,会返回undefined
							$("#resultoutput").val(jasonObj.xml);
						}else{
							$("#resultoutput").val(new XMLSerializer().serializeToString(jasonObj));
						}
						$("#returnType").empty();
						$("#returnType").append("返回类型是：xml");
					}
					else{
						var data = $.toJSON(jasonObj);
						data = data.replace(/\\/g,"");
						$("#resultoutput").empty();
						$("#resultoutput").val(data);
						$("#returnType").empty();
						$("#returnType").append("返回类型是：json");
					}
				}
			});			
		}
		else if(getpost=="json"){
			var inputData = $("#jsondatainput").val();
			//显示json字符串
			$("#jsonoutput").val(inputData);
			//显示访问地址
			$("#urloutput").val(showurl);
			$.ajax({
				type:"post",
				url:urlStr,
				data:inputData,
				success:function(jasonObj){
					//判断返回类型，xml或者是json
					if(jasonObj instanceof XMLDocument){
						if(jasonObj.xml){//不在IE中,会返回undefined
							$("#resultoutput").val(jasonObj.xml);
						}else{
							$("#resultoutput").val(new XMLSerializer().serializeToString(jasonObj));
						}
						$("#returnType").empty();
						$("#returnType").append("返回类型是：xml");
					}
					else{
						var data = $.toJSON(jasonObj);
						data = data.replace(/\\/g,"");
						$("#resultoutput").empty();
						$("#resultoutput").val(data);
						$("#returnType").empty();
						$("#returnType").append("返回类型是：json");
					}
				}
			});	
		}
		//get方法暂时有问题
		else{
			var inputData = {id:idValue,info:{platformAppId:platformAppIdValue,platformUserId:platformUserIdValue,gameUserId:gameUserIdValue},data:""};
			var infoObject = {platformAppId:platformAppIdValue,platformUserId:platformUserIdValue,gameUserId:gameUserIdValue};
			var objx = $.toJSON(inputData);
			//var info = $.toJSON(infoObject);
			var info=infoObject;
			var data={};
			var sendData = "format=json&&id="+idValue+"&&info="+info+"&&data="+data;
			//var sendData = "format=json&&id="+idValue+"&&info={platformAppId:"+platformAppIdValue+",platformUserId:"+platformUserIdValue+",gameUserId:"+gameUserIdValue+"}"+"&&data={}";
			//var sendData =  "id="+idValue+"&&info="+info+"&&data=";
			showurl = showurl+"?"+sendData;
			//urlStr = urlStr+"?"+sendData;
			//urlStr = encodeURI(urlStr);
			$("#jsonoutput").val(objx);
			$("#urloutput").val(showurl);
			$.ajax({
				type:getpost,
				url:urlStr,
				data:{id:idValue,data:{},info:{platformAppId:platformAppIdValue,platformUserId:platformUserIdValue,gameUserId:gameUserIdValue}},
				data:"",
				dataType:"jason",
				success:function(jasonObj){				
					var data = $.toJSON(jasonObj);
					data = data.replace(/\\/g,"");
					$("#resultoutput").val(data);
				}
			});				
		}
		$("#resultField").show();
	});
	
	//获取根目录的函数
	function getRootPath(){
		var strFullPath=window.document.location.href;
		var strPath=window.document.location.pathname;
		var pos=strFullPath.indexOf(strPath);
		var prePath=strFullPath.substring(0,pos);
		var postPath=strPath.substring(0,strPath.substr(1).indexOf('/')+1);
		return(prePath+postPath);
	}
	
	function encodeTransform(changeword){
		//还原，号
		changeword = changeword.replace(/%2C/g,",");
		//还原/号
		changeword = changeword.replace(/%2F/g,"/");
		//还原+号
		changeword = changeword.replace(/%2B/g,"\+");
		//还原空格
		changeword = changeword.replace(/\+/g," ");
		//还原&
		changeword = changeword.replace(/%26/g,"&");
		//还原;
		changeword = changeword.replace(/%3B/g,";");
		//还原 ?
		changeword = changeword.replace(/%3F/g,"?");
		//还原 ;
		changeword = changeword.replace(/%3A/g,":");
		//还原 @
		changeword = changeword.replace(/%40/g,"@");
		//还原=
		changeword = changeword.replace(/%3D/g,"=");
		//还原 $
		changeword = changeword.replace(/%24/g,"$");
		//还原 #
		changeword = changeword.replace(/%23/g,"#");
		return(changeword);
	}
	
	$("#servicemenu").click(function(){
		$("#servicemenuList").show();
		$("#auditchangemenuList").hide();
		$("#adminmenuList").hide();
		$("#actionmenuList").hide();
		$("#servicemenu").css({"color":"red"});
		$("#actionmenu").css({"color":"black"});
		$("#adminmenu").css({"color":"black"});
		$("#auditchangemenu").css({"color":"black"});
	});
	$("#actionmenu").click(function(){
		$("#servicemenuList").hide();
		$("#auditchangemenuList").hide();
		$("#adminmenuList").hide();
		$("#actionmenuList").show();
		$("#servicemenu").css({"color":"black"});
		$("#actionmenu").css({"color":"red"});
		$("#adminmenu").css({"color":"black"});
		$("#auditchangemenu").css({"color":"black"});
	});
	$("#adminmenu").click(function(){
		$("#servicemenuList").hide();
		$("#auditchangemenuList").hide();
		$("#adminmenuList").show();
		$("#actionmenuList").hide();
		$("#servicemenu").css({"color":"black"});
		$("#actionmenu").css({"color":"black"});
		$("#adminmenu").css({"color":"red"});
		$("#auditchangemenu").css({"color":"black"});
	});
	$("#auditchangemenu").click(function(){
		$("#servicemenuList").hide();
		$("#auditchangemenuList").show();
		$("#adminmenuList").hide();
		$("#actionmenuList").hide();
		$("#servicemenu").css({"color":"black"});
		$("#actionmenu").css({"color":"black"});
		$("#adminmenu").css({"color":"black"});
		$("#auditchangemenu").css({"color":"red"});
	});
});

jQuery.extend(
		 {
		  /**
		   * @see  将json字符串转换为对象
		   * @param   json字符串
		   * @return 返回object,array,string等对象
		   */
		  evalJSON : function (strJson)
		  {
		   return eval( "(" + strJson + ")");
		  }
		 });
jQuery.extend(
		 {
		  /**
		   * @see  将javascript数据类型转换为json字符串
		   * @param 待转换对象,支持object,array,string,function,number,boolean,regexp
		   * @return 返回json字符串
		   */
		  toJSON : function (object)
		  {
		   var type = typeof object;
		   if ('object' == type)
		   {
		    if (Array == object.constructor)
		     type = 'array';
		    else if (RegExp == object.constructor)
		     type = 'regexp';
		    else
		     type = 'object';
		   }
		      switch(type)
		   {
		         case 'undefined':
		       case 'unknown': 
		     return;
		     break;
		    case 'function':
		       case 'boolean':
		    case 'regexp':
		     return object.toString();
		     break;
		    case 'number':
		     return isFinite(object) ? object.toString() : 'null';
		       break;
		    case 'string':
		     return '"' + object.replace(/(\\|\")/g,"\\$1").replace(/\n|\r|\t/g,
		       function(){   
		                 var a = arguments[0];                   
		        return  (a == '\n') ? '\\n':   
		                       (a == '\r') ? '\\r':   
		                       (a == '\t') ? '\\t': ""  
		             }) + '"';
		     break;
		    case 'object':
		     if (object === null) return 'null';
		        var results = [];
		        for (var property in object) {
		          var value = jQuery.toJSON(object[property]);
		          if (value !== undefined)
		            results.push(jQuery.toJSON(property) + ':' + value);
		        }
		        return '{' + results.join(',') + '}';
		     break;
		    case 'array':
		     var results = [];
		        for(var i = 0; i < object.length; i++)
		     {
		      var value = jQuery.toJSON(object[i]);
		           if (value !== undefined) results.push(value);
		     }
		        return '[' + results.join(',') + ']';
		     break;
		      }
		  }
});