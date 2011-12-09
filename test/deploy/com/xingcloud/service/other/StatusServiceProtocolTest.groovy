package com.xingcloud.service.other

import static org.junit.Assert.*

import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream

import org.junit.Before
import org.junit.Test

import com.xingcloud.service.AbstractServiceTest
import com.xingcloud.util.json.JSONUtil

class StatusServiceProtocolTest extends AbstractServiceTest {

	def req
	
	@Before
	public void setUp() throws Exception {
		req = [id:'100', lang:'cn']
	}
	
	@Test
	public void testGetRequest1() {
		//the request is gzipped
		def url = super.uri + '/status'
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
		assertEquals '100', r["id"]
	}

	@Test
	public void testGetRequest2() {
		//the request is not gzipped
		def url = super.uri + '/status'
		def conn = new URL(url).openConnection()
		conn.doOutput = true
		def writer = new OutputStreamWriter(conn.outputStream)
		writer.write(JSONUtil.toJSON(req))
		writer.flush()
		writer.close()
		
		assertNull conn.getHeaderField('Content-Encoding')
		
		def reader = new BufferedReader(new InputStreamReader(conn.inputStream))
		def result = reader.text
		def r = JSONUtil.toMap(result)
		assertEquals '100', r["id"]
	}

}
