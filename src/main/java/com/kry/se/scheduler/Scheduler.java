package com.kry.se.scheduler;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kry.se.entity.PollerData;
import com.kry.se.repository.PollerRepository;
import com.kry.se.service.PollerService;
import com.mysql.cj.log.LogFactory;

@Component
public class Scheduler {
	public static final int TIME_TO_TEST = 40000;
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
   
    @Autowired
    PollerService pollerService;
	//should we use repo here or from service class
	@Autowired
	PollerRepository pollerRepository;

	@Scheduled(fixedRate = TIME_TO_TEST)
   public void schedule() {
	   
	   List<PollerData> pollerData = (List<PollerData>) pollerService.findAllServices();
   	for(PollerData poller:pollerData) {
   		//periodically does a HTTP GET ?
   		poller.setStatus(pollerService.testService(poller.getUrl()));
   		poller.setDateUpdated(LocalDateTime.now());
   		logger.info("Updating status for URL: "+poller.getUrl()+" at: "+poller.getDateUpdated());
           pollerRepository.save(poller);
   	}
   }
}
