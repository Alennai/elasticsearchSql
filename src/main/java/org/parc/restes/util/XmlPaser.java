package org.parc.restes.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

class XmlPaser {
	private static final Logger logger = LoggerFactory.getLogger(XmlPaser.class);

	public static <T> T paser(File source, Class<T> c) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (T) unmarshaller.unmarshal(source);
		} catch (Exception e) {
			logger.error("{}", e);
		}
		return null;
	}

//	public static Categories categories(File source) {
//		return paser(source, Categories.class);
//	}
}
