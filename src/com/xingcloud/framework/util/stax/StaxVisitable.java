package com.xingcloud.framework.util.stax;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMInputCursor;

import com.ctc.wstx.stax.WstxInputFactory;
import com.xingcloud.util.string.Charset;

import flex.messaging.util.URLDecoder;

/**
 * 使用stax扫描文件的类
 * <p>通过传入特定的扫描方式对特定的文件进行扫描访问</p>
 * 
 */
public class StaxVisitable{
	private static final XMLInputFactory2 inputFactory = new WstxInputFactory();
	private static final ReentrantLock lock = new ReentrantLock(false);
	protected XMLStreamReader2 reader;
	protected boolean accepted;
	
	static{
		inputFactory.configureForLowMemUsage();
	}
	
	public StaxVisitable(String filePath) {
		try {
			reader = SMInputFactory.getGlobalSMInputFactory().createStax2Reader(new File(URLDecoder.decode(filePath, Charset.UTF8)));
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}
	}
	
	public StaxVisitable(File file) {
		try {
			reader = SMInputFactory.getGlobalSMInputFactory().createStax2Reader(file);
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}
	}
	
	public StaxVisitable(InputStream inputStream){
		try {
			reader = SMInputFactory.getGlobalSMInputFactory().createStax2Reader(new InputStreamReader(inputStream));
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
		}		
	}
	
	public StaxVisitable(XMLStreamReader2 reader){
		this.reader = reader;
	}
	
	/**
	 * 返回读取方法
	 * @return
	 */
	public XMLStreamReader2 getReader(){
		return reader;
	}

	/**
	 * 确定是否扫描过
	 * @return
	 */
	public boolean isAccepted(){
		return accepted;
	}
	
	/**
	 * 传入一个游标方式的扫描类对文件进行扫描
	 * @param visitor 游标方式的扫描类
	 * @throws Exception
	 */
	public void accept(StaxCursorVisitor visitor) throws Exception{
		lock.lock();
		try{
			SMInputCursor rootCursor = SMInputFactory.rootElementCursor(getReader());
			rootCursor.advance();
			SMInputCursor cursor = rootCursor.childElementCursor().advance();
			while(cursor.asEvent() != null){
				visitor.visit(cursor);
				cursor = cursor.advance();
			}
			accepted = true;
			rootCursor.getStreamReader().closeCompletely();
			visitor.destroy();
		}finally{
			lock.unlock();
		}		
	}
	
	/**
	 * 传入一个事件方式的扫描类对文件进行扫描
	 * @param visitor 事件方式的扫描类
	 * @throws Exception
	 */
	public void accept(StaxEventVisitor visitor) throws Exception{
		lock.lock();
		try{
			XMLEventReader reader = inputFactory.createXMLEventReader(getReader());
			while(reader.hasNext()){
			     visitor.visit(reader.nextEvent());
			}
			accepted = true;
			reader.close();
		}finally{
			lock.unlock();
		}	
	}
	
	/**
	 * 传入一个元素方式的扫描类对文件进行扫描
	 * @param visitor 元素方式的扫描类
	 * @throws Exception
	 */
	public void accept(StaxElementVisitor visitor) throws Exception{
		lock.lock();
		try{
			XMLEventReader reader = inputFactory.createXMLEventReader(getReader());
			while(reader.hasNext()){
			     XMLEvent event = reader.nextEvent();
			     if(event.isStartElement()){
			    	 walk(event.asStartElement(), visitor);
			     }
			}
			accepted = true;
			reader.close();
		}finally{
			lock.unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void walk(StartElement element, StaxElementVisitor visitor) throws Exception{
		visitor.visit(element.asStartElement());
		Iterator<Attribute> iterator = (Iterator<Attribute>) element.getAttributes();
		Attribute attribute;
		while(iterator.hasNext()){
			attribute = iterator.next();
			if(attribute.isStartElement()){
				walk(attribute.asStartElement(), visitor);
			}
		}
	}
	
	/**
	 * 传入一个扫描类对文件进行扫描
	 * @param visitor 扫描类
	 * @throws Exception
	 */
	public void accept(StaxAttributeMapVisitor visitor) throws Exception{
		lock.lock();
		try{
			XMLEventReader reader = inputFactory.createXMLEventReader(getReader());
			while(reader.hasNext()){
			     XMLEvent event = reader.nextEvent();
			     if(event.isStartElement()){
			    	 walk(event.asStartElement(), visitor);
			     }
			}
			accepted = true;
			reader.close();
		}finally{
			lock.unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void walk(StartElement element, StaxAttributeMapVisitor visitor) throws Exception{
		Attribute attribute;
		Map<String, String> map = new HashMap<String, String>();
		Iterator<Attribute> iterator = (Iterator<Attribute>) element.getAttributes();
		while(iterator.hasNext()){
			attribute = iterator.next();
			if(attribute.isAttribute()){
				map.put(attribute.getName().toString(), attribute.getValue());
			}else if(attribute.isStartElement()){
				walk(attribute.asStartElement(), visitor);
			}
		}
		visitor.visit(element, map);
	}
}