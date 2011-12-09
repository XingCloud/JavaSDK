package com.xingcloud.framework.integration.http.stream;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.xingcloud.framework.context.result.Result;
import com.xingcloud.framework.context.stream.input.ResultInputStream;
import com.xingcloud.framework.context.stream.output.ResultOutputStream;
import com.xingcloud.framework.security.Authentication;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.ActionContext;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.io.amf.AmfMessageDeserializer;
import flex.messaging.io.amf.AmfMessageSerializer;
import flex.messaging.io.amf.AmfTrace;
import flex.messaging.io.amf.MessageBody;
import flex.messaging.io.amf.MessageHeader;
import flex.messaging.messages.AcknowledgeMessage;
import flex.messaging.messages.Message;
import flex.messaging.messages.RemotingMessage;

/**
 * 对AMF协议的输入输出进行控制
 * 
 * @author hsx
 */
public class AMFStream implements ResultInputStream<Object>,
		ResultOutputStream<Serializable>, Authentication {
	public static final Logger LOGGER = Logger.getLogger(AMFStream.class);
	protected HttpServletRequest httpServletRequest;
	protected HttpServletResponse httpServletResponse;
	protected ActionMessage message;
	protected AmfTrace trace = new AmfTrace();

	public AMFStream(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		this.httpServletRequest = httpServletRequest;
		this.httpServletResponse = httpServletResponse;
		init();
	}

	public ActionMessage getMessage() {
		return message;
	}

	public void setMessage(ActionMessage message) {
		this.message = message;
	}

	// 输入初始化
	protected void init() {
		try {
			AmfMessageDeserializer deserializer = new AmfMessageDeserializer();
			deserializer.initialize(
					SerializationContext.getSerializationContext(),
					new DataInputStream(httpServletRequest.getInputStream()),
					trace);
			ActionContext context = new ActionContext();
			message = new ActionMessage();
			context.setRequestMessage(message);
			// 解析AMF输入
			deserializer.readMessage(message, context);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getParameters() throws IOException {
		if (message == null || message.getBodyCount() > 0) {
			return new HashMap<String, Object>();
		}
		return (Map<String, Object>) message.getBody(0).getData();
	}

	public String getRawData() throws IOException {
		if (message == null) {
			return "";
		}
		return message.toString();
	}

	public void close() {
	}

	public Object getCredentials() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getDetails() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void output(String rawData) throws IOException {
	}

	/**
	 * AMF协议输出
	 * 
	 * @param Result
	 *            <Serializable> 调用了相应的服务后结果保存在result中
	 * @exception Exception
	 * @author hsx
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void output(Result<Serializable> result) throws Exception {

		if (message.getBody(0) == null) {
			return;
		}
		// 返回的message
		ActionMessage resultMessage = new ActionMessage();

		// 设置返回AMF的header
		MessageHeader head = new MessageHeader();
//		resultMessage.addHeader(head);
		head.setMustUnderstand(false);
		head.setName("test");
		head.setData("test");

		// 设置版本
		resultMessage.setVersion(message.getVersion());
		
		MessageBody body = message.getBody(0);
		List<MessageHeader> headers = message.getHeaders();
		// 如果请求中存在header并且header的name是serviceBrowser，则在返回中的主体中要加入另一个body
		if (headers.size() > 0
				&& (((MessageHeader) headers.get(0)).getName()
						.equals("serviceBrowser"))) {
			MessageBody bodyResult = new MessageBody();
			AcknowledgeMessage fristMessage = new AcknowledgeMessage();
			bodyResult.setTargetURI(body.getResponseURI() + "/onDebugEvents");
			bodyResult.setResponseURI(body.getResponseURI());
			Map<String, Serializable> map = new HashMap<String, Serializable>();
			// 模仿php的写法
			map.put("callTime", System.currentTimeMillis());
			map.put("decodeTime", 0);
			map.put("frameworkTime", 0);
			map.put("includeTime", 0);
			map.put("totalTime", System.currentTimeMillis());
			map.put("EventType", "profiling");
			map.put("__className", "ProfilingHeader");
			Object[] fristTemp = new Object[1];
			Object[] secondTemp = new Object[1];
			secondTemp[0] = map;
			fristTemp[0] = secondTemp;
			fristMessage.setBody(fristTemp);
			bodyResult.setData((Object)fristTemp);
			//将附加主体加入到resultMessage中
			resultMessage.addBody(bodyResult);
		}
		// 返回值的主体
		MessageBody resultBody = new MessageBody();
		resultBody.setTargetURI(body.getResponseURI() + "/onResult");
		resultBody.setResponseURI(body.getResponseURI());
		// 返回主体中的body
		AcknowledgeMessage ackownlegdeMessage = new AcknowledgeMessage();
		Object object = body.getData();
		if (object != null) {
			if (Object[].class.isAssignableFrom(object.getClass())) {
				Object[] objects = (Object[]) object;
				if (objects.length > 0) {
					object = objects[0];
				}
			}
			if (Message.class.isAssignableFrom(object.getClass())) {
				Message fromMessage = (Message) object;
				ackownlegdeMessage.setCorrelationId(fromMessage.getMessageId());
				ackownlegdeMessage.setClientId(fromMessage.getClientId());
			}
			if (RemotingMessage.class.isAssignableFrom(object.getClass())
					&& result != null) {
				Map<String, Serializable> map = (Map<String, Serializable>) result
						.asResult();
				// 对于每个服务的介绍
				if (((RemotingMessage) object).getSource().equals(
						"amfphp.DiscoveryService")) {
					ackownlegdeMessage.setBody(((List) map.get("data"))
							.toArray());
				// 调用单个服务的结果
				} else {
					Map<String, Serializable> putMap = new HashMap<String, Serializable>();
					putMap.put("message", map.get("message"));
					putMap.put("code", map.get("code"));
					putMap.put("data", map.get("data"));
					ackownlegdeMessage.setBody(putMap);
				}
//				resultBody.setData(((List) map.get("data"))
//						.toArray());
			}
		}
		//将ackownlegdeMessage加入AMf的主体body中
		resultBody.setData((Object) ackownlegdeMessage);
		
		//将主体加入resultMessage中
		resultMessage.addBody(resultBody);
		ServletOutputStream outputStream = httpServletResponse
				.getOutputStream();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		AmfMessageSerializer serializer = new AmfMessageSerializer();
		serializer.initialize(SerializationContext.getSerializationContext(),
				outStream, trace);
		serializer.writeMessage(resultMessage);
		httpServletResponse.setContentType(httpServletRequest.getContentType());
		//设置http的头部
		httpServletResponse.setHeader("content-type", "application/x-amf");
		httpServletResponse.setIntHeader("content-length", outStream.size());
		httpServletResponse.setHeader("Connection", "Keep-Alive");
		httpServletResponse.setHeader("Keep-Alive", "timeout=15,max=100");
		httpServletResponse.setHeader("Cache-Control", "no-store");
		httpServletResponse.setHeader("Pragma", "no-store");
		httpServletResponse.flushBuffer();
		outStream.writeTo(outputStream);
		outputStream.flush();
		outputStream.close();
	}
}