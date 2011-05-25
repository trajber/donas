package donas.dns.records;

import org.jboss.netty.buffer.ChannelBuffer;

import donas.dns.domain.DnsClass;
import donas.dns.domain.RrType;
import donas.util.DomainNameUtil;



public class Cname extends ResourceRecord {
	private final String canonicalName;

	public Cname(String ownername, DnsClass dnsClass, int ttl, String cname)  {
		super(ownername, RrType.CNAME, dnsClass, ttl);
		this.canonicalName = cname.toLowerCase();
		if (!DomainNameUtil.validate(canonicalName)) {
			// TODO tratar
		}
		this.rdata.setData(DomainNameUtil.toWireFormat(this.canonicalName));
	}
	
	public String getCanonicalName() {
		return canonicalName;
	}
	
	public String rdataPresentation() {
		return canonicalName;
	}

	public static Cname fromWireFormat(String ownername, DnsClass dnsClass, int ttl, ChannelBuffer buffer) {
		buffer.readUnsignedShort(); // rdlength
		String canonicalName = DomainNameUtil.toPresentationFormat(buffer);
		return new Cname(ownername, dnsClass, ttl, canonicalName);
	}
	
	public static Cname fromPresentation(String presentation) {
		String[] parts = presentation.split(" ");
		String ownername = "";
		
		int counter = 0;
		if (parts.length == 5) {
			ownername = parts[0];
			counter++;
		}
		
		DnsClass clazz = DnsClass.valueOf(parts[counter++]);
		int ttl = Integer.parseInt(parts[counter++]);
		counter++; // despreza o type...
		String name = parts[counter];	
		return new Cname(ownername, clazz, ttl, name);
	}
}
