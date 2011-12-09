package com.xingcloud.service.other

import static org.junit.Assert.*

import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream

import org.junit.Before
import org.junit.Test

import com.xingcloud.framework.service.annotation.CloudService
import com.xingcloud.service.AbstractServiceTest
import com.xingcloud.util.json.JSONUtil


class HTTPInputStreamTest extends AbstractServiceTest{
	def req, user
	
	@Before
	public void setUp() throws Exception {
		super.setUp()
		req=[:]
	}
	
	@Test
	public void testIsUnGZipRequired1() {
		//the request is gzipped
		def url = uri + '/rest/zip/test'
		def conn = new URL(url).openConnection()
		conn.doOutput = true
		conn.setRequestProperty 'Content-Encoding', 'gzip'
		def writer = new OutputStreamWriter(new DeflaterOutputStream(conn.outputStream))
		writer.write(JSONUtil.toJSON(req))
		writer.flush()
		writer.close()
		
		def reader = new BufferedReader(new InputStreamReader(new InflaterInputStream(conn.inputStream)))
		def result = reader.text
		def r = JSONUtil.toMap(result)
		println r
		assertEquals 'world', r.'data'.'hello'
		assertEquals 'my god', r.'data'.'oh'
	}

	@Test
	public void testIsUnGZipRequired2() {
		//the request is not gzipped
		def url = uri + '/rest/zip/test'
		def conn = new URL(url).openConnection()
		conn.doOutput = true
		def writer = new OutputStreamWriter(conn.outputStream)
		writer.write(JSONUtil.toJSON(req))
		writer.flush()
		writer.close()
		
		assertNull conn.getHeaderField('Content-Encoding')
		
		def reader = new BufferedReader(new InputStreamReader(conn.inputStream))
		def result = reader.text
	//	println result
		def r = JSONUtil.toMap(result)
		print r
		assertEquals 'world', r.'data'.'hello'
		assertEquals 'my god', r.'data'.'oh'
	}

}

@CloudService(value="testZip", api="zip")
public class TestZip {
	public Object test() throws Exception{
		return ["hello":"world","oh":"my god"]
	}
}
