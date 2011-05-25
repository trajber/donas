package donas.dns.records;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jboss.netty.buffer.ChannelBuffer;

import donas.dns.domain.DnsClass;
import donas.dns.domain.RrType;




public class Aaaa extends ResourceRecord {
	final private Inet6Address addr;

	public Aaaa(String ownername, DnsClass dnsClass, int ttl, String ipv6) throws UnknownHostException {
		super(ownername, RrType.AAAA, dnsClass, ttl);
		this.addr = (Inet6Address) InetAddress.getByName(ipv6);
		this.rdata.setData(this.addr.getAddress());
	}

	public String getIpv6() {
		return addr.getHostAddress();
	}
	
	public String rdataPresentation() {
		return addr.getHostAddress();
	}

	public static Aaaa fromWireFormat(String ownername, DnsClass dnsClass, int ttl, ChannelBuffer buffer) throws UnknownHostException {
		int rdLength = buffer.readUnsignedShort();
		
		byte[] rdata = new byte[rdLength];
		buffer.readBytes(rdata);
		
		Inet6Address addr = (Inet6Address) InetAddress.getByAddress(rdata);
		String ipv6 = addr.getHostAddress();
		return new Aaaa(ownername, dnsClass, ttl, ipv6);
	}
	
	public static Aaaa fromPresentation(String presentation) throws UnknownHostException {
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
		String ipv6 = parts[counter];	
		return new Aaaa(ownername, clazz, ttl, ipv6);
	}
}