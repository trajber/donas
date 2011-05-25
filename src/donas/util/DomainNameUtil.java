package donas.util;

import java.nio.ByteBuffer;
import java.util.regex.Pattern;

import org.jboss.netty.buffer.ChannelBuffer;

public class DomainNameUtil {
	private static final Pattern dot = Pattern.compile("\\.");
	private static final int LABEL_LENGTH_MAX = 63;
	private static final int DOMAIN_NAME_LENGTH_MAX = 255;

	public static String toFullName(String name, String zonename) {
		if (name.isEmpty()) {
			return zonename;
		}
		
		StringBuilder fullname = new StringBuilder(trimDot(name));
		fullname.append('.').append(trimDot(zonename));
	
		if ((fullname.length() > 0)
				&& (fullname.charAt(fullname.length() - 1) != '.')) {
			fullname.append('.');
		}
	
		return fullname.toString();
	}
	
	/**
	 * label validation (rfc 1034 sec.3.1)
	 * @param fqdn
	 * @return true or false
	 */
	public static boolean validate(String fqdn) {
		if (fqdn.length() > DOMAIN_NAME_LENGTH_MAX) {
			return false;
		}
		
		String[] labels = dot.split(fqdn);
		for (String label : labels) {
			if (label.isEmpty() || label.length() > LABEL_LENGTH_MAX) {
				return false;
			}
		}
		return true;
	}
	
	public static byte[] toWireFormat(String name) {
		return DomainNameUtil.toWireFormat(name, "");
	}
	
	public static byte[] toWireFormat(String name, String zonename) {
		String fqdn = trimDot(toFullName(name, zonename));
	
		byte[] data = new byte[fqdn.length() + 2];

		String[] labels = dot.split(fqdn);
		int curr = 0;
		for (String label : labels) {
			data[curr] = (byte) label.length();
			byte[] labelBytes = label.getBytes();
			System.arraycopy(labelBytes, 0, data, curr + 1, labelBytes.length);
			curr += labelBytes.length + 1;
		}
		data[data.length - 1] = 0;
	
		return data;
	}
	
	public static String trimDot(String name) {
		if (name.isEmpty() || (name.charAt(0) != '.' && name.charAt(name.length() - 1) != '.')) {
			return name;
		}
	
		StringBuilder own = new StringBuilder(name);
		while ((own.length() > 0) && (own.charAt(0) == '.')) {
			own.deleteCharAt(0);
		}
		while ((own.length() > 0) && (own.charAt(own.length() - 1) == '.')) {
			own.deleteCharAt(own.length() - 1);
		}
		return own.toString();
	}
	
	/**
	 * Read name in wire-format from buffer. </br>
	 * Based RFC 1035 - 4.1.4
	 */
	public static String toPresentationFormat(ChannelBuffer buffer) {
		int position = buffer.readerIndex();
		
		StringBuilder name = new StringBuilder();
		DomainNameUtil.toPresentationFormat(name, buffer, position);
		
		return name.toString();
	}

	/**
	 * Read name in wire-format from buffer
	 * and return in variable 'name'. </br>
	 * 
	 * Based RFC 1035 - 4.1.4
	 */
	private static void toPresentationFormat(StringBuilder name,
			ChannelBuffer buffer,
			int position) {
		buffer.readerIndex(position);
		byte firstByte = buffer.readByte();
		
		if ((firstByte & 0xC0) == 0xC0) {
			byte secondByte = buffer.readByte();
			
			ByteBuffer offset = ByteBuffer.allocate(2);
			offset.put(ByteUtil.getBits(firstByte, 3, 6));
			offset.put(secondByte);
			offset.rewind();

			toPresentationFormat(name, buffer, (int) offset.getShort());
			buffer.readerIndex(position + 2);
			
		} else {
			byte labelLength = ByteUtil.getBits(firstByte, 3, 6);
			
			if (labelLength == 0) {
				return;
			}
			
			byte[] labelBytes = new byte[labelLength];
			buffer.readBytes(labelBytes);
			
			name.append(new String(labelBytes));
			name.append('.');
			toPresentationFormat(name, buffer, buffer.readerIndex());
		}		
	}
}