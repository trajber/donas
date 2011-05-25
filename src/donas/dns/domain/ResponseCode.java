package donas.dns.domain;

/**
 * RFC 1035 - 4.1.1. Header section format,
 * RFC 2136 - 2.2. Message Header,
 * RFC 2845 - 1.7. New Assigned Numbers
 * 
 * RCODE (Response code) - this four bit field is undefined in requests and 
 * set in responses.
 * 
 */
public enum ResponseCode {
	NOERROR(0), 
	FORMERR(1), 
	SERVFAIL(2), 
	NXDOMAIN(3), 
	NOTIMP(4), 
	REFUSED(5), 
	YXDOMAIN(6), 
	YXRRSET(7), 
	NXRRSET(8), 
	NOTAUTH(9), 
	NOTZONE(10), 
	BADSIG(16), 
	BADKEY(17), 
	BADTIME(18);

	private byte value; // only necessary 4 bits
	
	private ResponseCode(int value) {
		this.value = (byte) value;
	}

	public byte getValue() {
		return value;
	}

	public static ResponseCode fromValue(byte value) throws IllegalArgumentException {
		ResponseCode[] values = ResponseCode.values();
		for (ResponseCode type : values) {
			if (type.getValue() == value) {
				return type;
			}
		}

		throw new IllegalArgumentException();
	}
}
