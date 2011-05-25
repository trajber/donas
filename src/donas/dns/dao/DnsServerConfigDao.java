package donas.dns.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import donas.dns.server.DnsServerConfig;
import donas.dns.server.DnsServerConfig.DnsServerConfigBuilder;

public class DnsServerConfigDao {
	
	private final static Logger logger = LoggerFactory.getLogger(DnsServerConfigDao.class);

	private static DnsServerConfig instance;
	
	private static DnsServerConfig loadDefaults() {
		DnsServerConfig config = null;
		try {
			logger.info("Loading default parameters");
			InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("donas/resources/config-defaults.properties");
			Properties properties = new Properties();
			properties.load(stream);
			String dnsServerPort = properties.getProperty("dns.server.port");
			String dnsServerPath = properties.getProperty("dns.server.path");
			String dnsCacheMaxEntries = properties.getProperty("dns.server.cache.max_entries");
			stream.close();
			
			logger.info("Using dns server port=" + dnsServerPort);
			logger.info("Using dns server path=" + dnsServerPath);
			logger.info("Using dns cache max_entries=" + dnsCacheMaxEntries);
			
			int port = Integer.parseInt(dnsServerPort);
			int cacheMaxEntries = Integer.parseInt(dnsCacheMaxEntries);
			
			DnsServerConfigBuilder configBuilder = new DnsServerConfig.DnsServerConfigBuilder();
			
			config = configBuilder.withPort(port).withPath(dnsServerPath).withCacheMaxEntries(cacheMaxEntries).build();

		} catch (IOException e ) {
			logger.error("Cannot load default configuration", e);
		}
		
		return config;
	}
	
	public static DnsServerConfig getInstance() {
		if (instance == null) {
			instance = loadDefaults();
		}
		return instance;
	}
}
