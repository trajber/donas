package donas.dns.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import donas.dns.domain.DnsClass;
import donas.dns.domain.RrSet;
import donas.dns.domain.RrType;
import donas.dns.domain.Zone;
import donas.dns.records.A;
import donas.dns.records.Aaaa;
import donas.dns.records.Cname;
import donas.dns.records.ResourceRecord;
import donas.dns.records.Soa;
import donas.util.DomainNameUtil;
import donas.util.ZoneIndex;



public class ZoneDao extends AbstractDao {
	private final static Logger logger = LoggerFactory.getLogger(ZoneDao.class);
	
	public Zone findByZonename(String name) {
		name = DomainNameUtil.trimDot(name);
		Zone zone = null;
		
		Scanner scanner = null;
		
		try {
			ZoneIndex zoneIndex = ZoneIndex.getInstance();
			String path = zoneIndex.get(name);
			if (path == null) {
				logger.warn("Cannot find zone:"+ name);
				return null;
			}
			
			StringBuilder builder = new StringBuilder(BASE_DIRECTORY);
			builder.append('/').append(path).append('/').append(name).append(".zone");
			String fileName = builder.toString();
			
			File file = new File(builder.toString());
			
			logger.info("Reading zone file:" + fileName);
			scanner = new Scanner(file);
			
			zone = new Zone(name);
			zone.setName(name);
			
			String line;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				line = line.trim();
				if (line.isEmpty()) continue;
				
				String[] parts = line.split(" ");
				
				if (parts.length < 3) {
					break;
				}
				
				
				// has ownername ?
				RrType type = null;
				try {
					DnsClass.valueOf(parts[0]);
					type = RrType.valueOf(parts[2]);
				} catch (IllegalArgumentException e) {
					type = RrType.valueOf(parts[3]);
				}
			
				ResourceRecord record = parseResourceRecord(type, line);
				
				RrSet rrSet = null;
				if ((rrSet = zone.getRecords(type)) == null) {
					rrSet = new RrSet(record.getOwnername(), record.getType(), record.getDnsClass());
					zone.addRrSet(rrSet);	
				}

				if (record != null) {
					logger.info("Resource record parsed");
					rrSet.addRecord(record);
				}
			}
			
		} catch (FileNotFoundException e) {
			logger.info("File not found:" + name + ".zone");
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		
		return zone;
	}
	
	private ResourceRecord parseResourceRecord(RrType type, String presentation) {
		ResourceRecord record = null;
		
		try {
			switch(type) {
			case A:
				record = A.fromPresentation(presentation);
				break;
			case SOA:
				record = Soa.fromPresentation(presentation);
				break;
			case AAAA:
				record = Aaaa.fromPresentation(presentation);
				break;
			case CNAME:
				record = Cname.fromPresentation(presentation);
				break;
			}
			
			if (record != null) {
				logger.info("RECORD:"+record.toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return record;
	}
}