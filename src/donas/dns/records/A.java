package donas.dns.records;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jboss.netty.buffer.ChannelBuffer;

import donas.dns.domain.DnsClass;
import donas.dns.domain.RrType;



public class A extends ResourceRecord {
	final private Inet4Address addr;

	/**
	 * Using IN as default DNSCLASS
	 * @param ownername
	 * @param ttl
	 * @param ip
	 * @throws UnknownHostException
	 */
	public A(String ownername, int ttl, String ip) throws UnknownHostException {
		super(ownername, RrType.A, DnsClass.IN, ttl);
		this.addr = (Inet4Address) InetAddress.getByName(ip);
		this.rdata.setData(this.addr.getAddress());
	}
	
	public A(String ownername, DnsClass dnsClass, int ttl, String ip) throws UnknownHostException {
		super(ownername, RrType.A, dnsClass, ttl);
		this.addr = (Inet4Address) InetAddress.getByName(ip);
		this.rdata.setData(this.addr.getAddress());
	}
	
	public String getIpv4() {
		return addr.getHostAddress();
	}
	
	public String rdataPresentation() {
		return addr.getHostAddress();
	}

	public static A fromWireFormat(String ownername, DnsClass dnsClass, int ttl, ChannelBuffer buffer) throws UnknownHostException {
		int rdLength = buffer.readUnsignedShort();
	
		byte[] rdata = new byte[rdLength];
		buffer.readBytes(rdata);
		
		Inet4Address addr = (Inet4Address) InetAddress.getByAddress(rdata);
		String ip = addr.getHostAddress();
		return new A(ownername, dnsClass, ttl, ip);
	}
	
	public static A fromPresentation(String presentation) throws UnknownHostException {
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
		String ip = parts[counter];	
		return new A(ownername, clazz, ttl, ip);
	}
}