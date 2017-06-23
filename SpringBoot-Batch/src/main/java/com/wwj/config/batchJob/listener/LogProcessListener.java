package com.wwj.config.batchJob.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

public class LogProcessListener implements ItemProcessListener<Object, Object> {

	private static final Logger log = LoggerFactory.getLogger(LogProcessListener.class);

	public void afterProcess(Object item, Object result) {
        if(item!=null) log.info("Input to Processor: " + item.toString());
		if(result!=null) log.info("Output of Processor: " + result.toString());
	}

	public void beforeProcess(Object item) {
	}

	public void onProcessError(Object item, Exception e) {
	}

}