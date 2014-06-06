package org.tsaikd.java;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class SampleTest {

	static Log log = LogFactory.getLog(SampleTest.class);

	@Test
	public void hello() {
		log.info("Hello test");
	}
}
