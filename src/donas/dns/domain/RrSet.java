package donas.dns.domain;

import java.util.Set;
import java.util.TreeSet;

import donas.dns.records.ResourceRecord;



public class RrSet {
	private String ownername;
	private RrType type;
	private DnsClass clazz;
	private Set<ResourceRecord> records = new TreeSet<ResourceRecord>();
	
	public RrSet(String ownername, RrType type, DnsClass clazz) {
		this.ownername = ownername;
		this.type = type;
		this.clazz = clazz;
	}
	
	public String getOwnername() {
		return ownername;
	}
	
	public RrType getType() {
		return type;
	}
	
	public DnsClass getClazz() {
		return clazz;
	}
	
	public Set<ResourceRecord> getRecords() {
		return records;
	}
	
	public boolean addRecord(ResourceRecord record) {
		if (record.getOwnername().equals(ownername) && 
				record.getDnsClass() == clazz &&
				record.getType() == type) {
			return records.add(record);
		}
		return false;
	}
}
