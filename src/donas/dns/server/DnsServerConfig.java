package donas.dns.server;

public final class DnsServerConfig {
	private int port;
	private String path;
	private int cacheMaxEntries;
	
	private DnsServerConfig() {}
	
	public int getPort() {
		return port;
	}
	
	public String getPath() {
		return path;
	}
	
	
	public int getCacheMaxEntries() {
		return cacheMaxEntries;
	}


	public static class DnsServerConfigBuilder {
		private DnsServerConfig config = new DnsServerConfig();
		
		public DnsServerConfigBuilder withPort(int port) {
			config.port = port;
			return this;
		}
		
		public DnsServerConfigBuilder withPath(String path) {
			config.path = path;
			return this;
		}
		
		public DnsServerConfigBuilder withCacheMaxEntries(int cacheMaxEntries) {
			config.cacheMaxEntries = cacheMaxEntries;
			return this;
		}
		
		public DnsServerConfig build() {
			return config;
		}
	}
}
