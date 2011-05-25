package donas.dns.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Zone {
	private String name;
	private Map<RrType, RrSet> records = new HashMap<RrType, RrSet>();

	public Zone(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void addRrSet(RrSet rrSet) {
		records.put(rrSet.getType(), rrSet);
	}
	
	public RrSet getRecords(RrType type) {
		return records.get(type);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(name);
		for (Entry<RrType, RrSet> entry : records.entrySet()) {
			builder.append(entry.getValue().getRecords().toString());
		}
		return builder.toString();
	}
}