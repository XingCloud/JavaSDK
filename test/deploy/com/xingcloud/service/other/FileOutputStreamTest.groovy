package com.xingcloud.service.other

import static org.junit.Assert.*

import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.InflaterInputStream

import org.junit.Before
import org.junit.Test

import com.xingcloud.framework.context.application.XingCloudApplication;
import com.xingcloud.framework.service.annotation.CloudService
import com.xingcloud.framework.service.result.ResourceFileResult;
import com.xingcloud.service.AbstractServiceTest
import com.xingcloud.util.json.JSONUtil

class FileOutputStreamTest extends AbstractServiceTest {

	def req
	@Before
	public void setUp() throws Exception {
		req = [:]
	}

	@Test
	public void testFileOutputStream1() {
		//the request is gzipped
		def url = super.uri + '/file/filezip/test'
		def conn = new URL(url).openConnection()
		conn.doOutput = true
		def writer = new OutputStreamWriter(conn.outputStream)
		writer.write(JSONUtil.toJSON(req))
		writer.flush()
		writer.close()

		assertNotNull conn.getHeaderField('Content-Encoding')
		assertEquals 'gzip', conn.getHeaderField('Content-Encoding')

		def reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.inputStream)))
		def result = reader.text
		assertTrue result.startsWith('<?xml version="1.0" encoding="UTF-8"?>')
	}

	@Test
	public void testFileOutputStream2() {
		//the request is deflated
		def url = super.uri + '/file/filezip/test'
		def conn = new URL(url).openConnection()
		conn.doOutput = true
		conn.setRequestProperty 'Content-Encoding', 'gzip'
		def writer = new OutputStreamWriter(new DeflaterOutputStream(conn.outputStream))
		writer.write(JSONUtil.toJSON(req))
		writer.flush()
		writer.close()

		assertNull conn.getHeaderField('Content-Encoding')

		def reader = new BufferedReader(new InputStreamReader(new InflaterInputStream(conn.inputStream)))
		def result = reader.text
		assertTrue result.startsWith('<?xml version="1.0" encoding="UTF-8"?>')
	}
}

@CloudService(value="testFileOutput", api="filezip")
public class TestFileOutput{
	public Object test() {
		ResourceFileResult result = new ResourceFileResult("/config/global.xml");
		result.setFileName("/config/global.xml");
		result.setContentType("text/xml;charset=utf-8");
		return result;
	}
}
