package donas.dns.records;

import org.jboss.netty.buffer.ChannelBuffer;

import donas.dns.domain.DnsClass;
import donas.dns.domain.Rdata;
import donas.dns.domain.RrType;
import donas.util.ByteUtil;
import donas.util.DomainNameUtil;



public class Soa extends ResourceRecord {

	final private String mname;
	final private String rname;
	final private int serial;
	final private int refresh;
	final private int retry;
	final private int expire;
	final private int minimum;
	
	public Soa(String ownername, DnsClass dnsClass, int ttl,
			String mname, String rname,
			int serial, int refresh, int retry, int expire, int minimum) {
		super(ownername, RrType.SOA, dnsClass, ttl);
		this.mname = mname.toLowerCase();
		this.rname = rname.toLowerCase();
		this.serial = serial;
		this.refresh = refresh;
		this.retry = retry;
		this.expire = expire;
		this.minimum = minimum;
		this.rdata = get(this.mname, this.rname,
				serial, refresh, retry, expire, minimum);
	}
	
	public String getMname() {
		return mname;
	}

	public String getRname() {
		return rname;
	}

	public int getSerial() {
		return serial;
	}

	public int getRefresh() {
		return refresh;
	}
	
	public int getRetry() {
		return retry;
	}

	public int getExpire() {
		return expire;
	}

	public int getMinimum() {
		return minimum;
	}
	
	@Override
	public String rdataPresentation() {
		StringBuilder rdata = new StringBuilder();
		rdata.append(mname).append(SEPARATOR);
		rdata.append(rname).append(SEPARATOR);
		rdata.append(serial).append(SEPARATOR);
		rdata.append(refresh).append(SEPARATOR);
		rdata.append(retry).append(SEPARATOR);
		rdata.append(expire).append(SEPARATOR);
		rdata.append(minimum);
	
		return rdata.toString();
	}

	public static Soa fromRdata(String ownername, DnsClass dnsClass, int ttl,
			ChannelBuffer buffer) {
		buffer.readUnsignedShort(); // despreza rdlen
		
		String mname = DomainNameUtil.toPresentationFormat(buffer);
		String rname = DomainNameUtil.toPresentationFormat(buffer);
		int serial = buffer.readInt();
		int refresh = buffer.readInt();
		int retry = buffer.readInt();
		int expire = buffer.readInt();
		int minimum = buffer.readInt();
		
		return new Soa(ownername, dnsClass, ttl,
				mname, rname, serial, refresh, retry, expire, minimum);
	}
	
	public static Soa fromPresentation(String presentation) {
		String[] parts = presentation.split(" ");
		int counter = 0;
		String ownername = "";
		if (parts.length == 11) {
			ownername = parts[0];
			counter++;
		}
		DnsClass clazz = DnsClass.valueOf(parts[counter++]);
		int ttl = Integer.parseInt(parts[counter++]);
		counter++; // despreza o type...
		
		String mname = parts[counter++];
		String rname = parts[counter++];
		int serial = Integer.parseInt(parts[counter++]);
		int refresh = Integer.parseInt(parts[counter++]);
		int retry = Integer.parseInt(parts[counter++]);
		int expire = Integer.parseInt(parts[counter++]);
		int minimum = Integer.parseInt(parts[counter++]);
		
		return new Soa(ownername, clazz, ttl,
				mname, rname, serial, refresh, retry, expire, minimum);
		
	}
	
	public static Rdata get(String soaMname, String soaRname, int serial,
			int refresh, int retry, int expire, int minimum) {

		byte[] mname = DomainNameUtil.toWireFormat(soaMname);
		byte[] rname = DomainNameUtil.toWireFormat(soaRname);

		int len = mname.length + rname.length + (5 * 4);
		byte data[] = new byte[len];

		int pos = 0;

		// mname
		System.arraycopy(mname, 0, data, pos, mname.length);
		pos += mname.length;

		// rname
		System.arraycopy(rname, 0, data, pos, rname.length);
		pos += rname.length;

		// serial
		byte[] serialArray = ByteUtil.toByteArray(serial);
		System.arraycopy(serialArray, 0, data, pos, serialArray.length);
		pos += serialArray.length;

		// refresh
		byte[] refreshArray = ByteUtil.toByteArray(refresh);
		System.arraycopy(refreshArray, 0, data, pos, refreshArray.length);
		pos += refreshArray.length;

		// retry
		byte[] retryArray = ByteUtil.toByteArray(retry);
		System.arraycopy(retryArray, 0, data, pos, retryArray.length);
		pos += retryArray.length;

		// expire
		byte[] expireArray = ByteUtil.toByteArray(expire);
		System.arraycopy(expireArray, 0, data, pos, expireArray.length);
		pos += expireArray.length;

		// minimum
		byte[] minimumArray = ByteUtil.toByteArray(minimum);
		System.arraycopy(minimumArray, 0, data, pos, minimumArray.length);
		pos += minimumArray.length;

		return new Rdata(data);
	}
}
