package donas.dns.dao;

import donas.dns.server.DnsServerConfig;


public abstract class AbstractDao {
	
	public final String BASE_DIRECTORY;
	
	public AbstractDao() {
		DnsServerConfig config = DnsServerConfigDao.getInstance();
		BASE_DIRECTORY = config.getPath();
	}

}
