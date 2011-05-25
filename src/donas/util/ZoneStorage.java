package donas.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.log4j.Logger;

import donas.dns.dao.DnsServerConfigDao;
import donas.dns.dao.ZoneDao;
import donas.dns.domain.Zone;
import donas.dns.server.DnsServerConfig;



public class ZoneStorage {
	private static final Logger logger = Logger.getLogger(ZoneStorage.class);
	private static final DnsServerConfig config = DnsServerConfigDao.getInstance();
	private static final int MAX_CACHE_SIZE = config.getCacheMaxEntries();

	// Create a CacheManager using defaults
	private static Cache cache;
	private static CacheManager manager = new CacheManager();
	private static ZoneStorage zoneCache;
	private static final ZoneDao dao = new ZoneDao();

	private ZoneStorage() {
		logger.info("Creating zone cache with size:" + MAX_CACHE_SIZE);

		// Create a Cache specifying its configuration.
		cache = new Cache(new CacheConfiguration("zone_cache", MAX_CACHE_SIZE)
				.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU)
				.overflowToDisk(false)
				.eternal(true)
				.diskPersistent(false));
		manager.addCache(cache);
	}

	public Zone get(String name) {
		Element element;
		if ((element = cache.get(name)) != null) {
			logger.info("Zone [" + name + "] found in cache.");
			return (Zone) element.getObjectValue();
		}

		logger.info("Zone [" + name	+ "] NOT found in cache. Looking at the disk");
		Zone zone = dao.findByZonename(name);
		if (zone != null) {
			logger.info("Zone [" + name	+ "] was found in disk. Putting it on cache.");
			element = new Element(zone.getName(), zone);
			cache.put(element);
		}

		return zone;
	}

	public void put(Zone zone) {
		Element element = new Element(zone.getName(), zone);
		cache.put(element);
	}

	public static ZoneStorage getInstance() {
		if (zoneCache == null) {
			zoneCache = new ZoneStorage();
		}
		return zoneCache;
	}
}