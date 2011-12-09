package com.xingcloud.framework.service.test;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.xingcloud.framework.bean.BeanFactory;
import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.service.annotation.Auth;
import com.xingcloud.framework.service.annotation.CloudService;
import com.xingcloud.framework.service.annotation.Description;
import com.xingcloud.framework.service.annotation.Method;
import com.xingcloud.framework.service.annotation.Protocol;
import com.xingcloud.framework.service.annotation.ServiceParam;

/**
 * 服务测试类，用于检测选中的服务是否可用
 * 
 */
@CloudService(value = "testService", api = "test")
public class TestServiceImpl implements TestService {

	// 保存当前时间，防止恶意攻击
	private static long servicecurrenttime = 0;
	private static Map<String, Serializable> servicelastresult = new HashMap<String, Serializable>();

	/**
	 * 测试服务
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Protocol(allow = "ANY", deny = "none")
	@Method(allow = "ANY", deny = "none")
	@Auth(type = "oauth")
	@Description(content = "对添加了CloudTest注解的测试类进行测试")
	@ServiceParam(name = "", type = "", remark = "")
	public Object test() throws Exception {
		// 获得两次测试之间的间隔时间
		long current = System.currentTimeMillis()
				- TestServiceImpl.servicecurrenttime;
		// 如果小于一分钟
		if (current < 1000 * 60) {
			// 返回保存的值，防止恶意攻击数据库，缓存最近一次测试的值
			return servicelastresult;
		} else {
			// 如果是1分钟后新的测试则刷新当前时间重新调用数据库测试
			TestServiceImpl.servicecurrenttime = System.currentTimeMillis();

			Map<String, Serializable> result = new HashMap<String, Serializable>();
			Map<String, String> test = (Map<String, String>) XingCloudApplication
					.getInstance().getParameter("Test");
			double starttime = System.nanoTime();
			DecimalFormat df = new DecimalFormat("#.##");
			for (String testName : test.keySet()) {
				Object testclass = BeanFactory.getInstance().getBean(testName);
				java.lang.reflect.Method testMethod = Class.forName(
						test.get(testName)).getMethod("test");
				Object testResult = null;
				double singlestart = System.nanoTime();
				try {
					
					testResult = testMethod.invoke(testclass);
					double singleent = System.nanoTime();
					double singletime = (singleent-singlestart)/1000000;
					String singlecost = df.format(singletime)+"ms";					
					if (Boolean.class.isAssignableFrom(testResult.getClass())) {
						Map<String, String> mapresult = new HashMap<String, String>();
						mapresult
								.put("result", ((Boolean) testResult)
										.booleanValue() == true ? "success"
										: "failure");
						mapresult.put("cost", singlecost);
						result.put(testName, (Serializable) mapresult);
					} else if(Map.class.isAssignableFrom((testResult.getClass()))){
						Map<String,String> temp = (Map<String,String>)testResult;
						temp.put("cost", singlecost);
						result.put(testName, (Serializable) temp);
					} else {
						result.put(testName, (Serializable) testResult);
					}
				} catch (Exception e) {
					double singleent = System.nanoTime();
					double singletime = (singleent-singlestart)/1000000;
					String singlecost = df.format(singletime)+"ms";	
					Map<String, String> mapresult = new HashMap<String, String>();
					mapresult.put("result", "failure");
					mapresult.put(e.getCause().getClass().toString(), e.getCause().getMessage());
					mapresult.put("cost", singlecost);
					result.put(testName, (Serializable) mapresult);
				}

			}
			double endtime = System.nanoTime();
			double time = (endtime-starttime)/1000000;
			String costtime = df.format(time)+"ms";
			Date date = new Date();
			DateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timestamp = d.format(date);
			// 增加时间戳
			result.put("timestamp", timestamp);
			// 增加消耗时间
			result.put("cost", costtime);
			// 缓存结果
			servicelastresult = result;
			return result;
		}
	}

}
