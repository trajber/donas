package donas.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import donas.dns.dao.ZoneIndexDao;

public class ZoneIndex {
	private static final ConcurrentHashMap<String, String> zoneIndex = new ConcurrentHashMap<String, String>();
	private static final ZoneIndexDao dao = new ZoneIndexDao();
	
	private static ZoneIndex instance;
	
	private ZoneIndex() {
		Map<String, String> index = dao.readIndex();
		zoneIndex.putAll(index);
	}
	
	public String get(String zonename) {
		return zoneIndex.get(zonename);
	}
	
	public String put(String zonename, String path) {
		dao.appendNewIndex(zonename, path);
		return zoneIndex.put(zonename, path);
	}
		
	public int size() {
		return zoneIndex.size();
	}
	
	public static void load() {
		getInstance();
	}
	
	public static ZoneIndex getInstance() {
		if (instance == null) {
			instance = new ZoneIndex();
		}
		return instance;
	}
}
