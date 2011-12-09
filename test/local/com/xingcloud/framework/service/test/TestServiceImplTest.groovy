package com.xingcloud.framework.service.test;

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import com.xingcloud.test.AbstractTest
import com.xingcloud.util.annotation.CloudTest

class TestServiceImplTest extends AbstractTest{

	@Before
	public void setUp() {
		super.setUp()
	}
	@Test
	public void testDoTest() {
		TestServiceImpl tsi = new TestServiceImpl();
		def result = tsi.test();
		println result
		assertNotNull result
		assertNotNull result.timestamp
		assertNotNull result.cost
		assertEquals 'success', result.'test1'.'result'
		assertEquals 'failure', result.'test2'.'result'
		assertEquals 'success', result.'test3'.'result'
		assertEquals 'failure', result.'test4'.'result'
		assertNotNull result.'test1'.'cost'
		assertNotNull result.'test2'.'cost'
		assertNotNull result.'test3'.'cost'
		assertNotNull result.'test4'.'cost'
	}
}

@CloudTest(value="test1")
public class Test1 {
	public Boolean test(){
		return true
	}
}

@CloudTest(value="test2")
public class Test2 {
	public boolean test() {
		return false
	}
}

@CloudTest(value="test3")
public class Test3 {
	public Map<String, Serializable> test() {
		return [result:"success"]
	}
}

@CloudTest(value="test4")
public class Test4 {
	public boolean test() throws Exception {
		throw new Exception("this is test")
	}
}