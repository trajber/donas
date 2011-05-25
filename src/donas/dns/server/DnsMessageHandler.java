package donas.dns.server;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import donas.dns.domain.DnsHeader;
import donas.dns.domain.DnsMessage;
import donas.dns.domain.DnsQuestion;
import donas.dns.domain.RrSet;
import donas.dns.domain.RrType;
import donas.dns.domain.Zone;
import donas.util.DomainNameUtil;
import donas.util.ZoneStorage;



public class DnsMessageHandler {
	private final static Logger logger = LoggerFactory.getLogger(DnsMessageHandler.class);

	public DnsMessage process(DnsMessage request) throws UnknownHostException {
		DnsMessage response = request;
		
		DnsHeader header = response.getHeader();
		header.resetCounters();
		
		DnsQuestion question = request.getQuestion();

		logger.info("Processing DNS question [" + question.getQname() + " " + question.getQclass() + " " + question.getQtype() + "]");
		RrSet rrset = findAnswer(question);
		
		if (rrset != null) {
			logger.info("Answer found, preparing response");
			response.addAllAnswers(rrset);
			
			header.setQuestion(false);
			header.setAuthoritativeAnswer(true);
		}

		return response;
	}
	
	private RrSet findAnswer(DnsQuestion question) throws UnknownHostException {
		logger.info("Building A RR for response");

		RrType type = RrType.valueOf(question.getQtype().toString());
		ZoneStorage zoneStorage = ZoneStorage.getInstance();
		String zoneName = DomainNameUtil.trimDot(question.getQname());
		Zone zone = zoneStorage.get(zoneName);
		
		if (zone == null) {
			zone = new Zone(zoneName);
		}
		
		RrSet rrSet = zone.getRecords(type);
		if (rrSet != null) {
			logger.info(zone.getRecords(type).getRecords().toString());
		}
		
		return zone.getRecords(type);
	}
}