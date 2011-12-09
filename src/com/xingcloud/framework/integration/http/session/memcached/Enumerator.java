package com.xingcloud.framework.integration.http.session.memcached;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;

@SuppressWarnings("rawtypes")
public class Enumerator implements Enumeration {

	public Enumerator(Collection collection) {
		this(collection.iterator());
	}

	public Enumerator(Collection collection, boolean clone) {
		this(collection.iterator(), clone);
	}

	public Enumerator(Iterator iterator) {
		super();
		this.iterator = iterator;
	}

	@SuppressWarnings("unchecked")
	public Enumerator(Iterator iterator, boolean clone) {
		super();
		if (!clone) {
			this.iterator = iterator;
		} else {
			List list = new ArrayList();
			while (iterator.hasNext()) {
				list.add(iterator.next());
			}
			this.iterator = list.iterator();
		}
	}

	public Enumerator(Map map) {
		this(map.values().iterator());
	}

	public Enumerator(Map map, boolean clone) {
		this(map.values().iterator(), clone);
	}

	private Iterator iterator = null;

	public boolean hasMoreElements() {
		return (iterator.hasNext());
	}

	public Object nextElement() throws NoSuchElementException {
		return (iterator.next());
	}

}
