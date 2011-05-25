package donas.dns.domain;

public enum RrType {

	A			(1),
	NS			(2),
	CNAME		(5),
	SOA			(6),
	PTR			(12),
	HINFO		(13),
	MINFO		(14),
	MX			(15),
	TXT			(16),
	AAAA		(28),
	LOC			(29),
	SRV			(33),
	NAPTR		(35),
	CERT		(37),
	DNAME		(39),
	OPT			(41),
	DS			(43),
	SSHFP		(44),
	IPSECKEY	(45),
	RRSIG		(46),
	NSEC		(47),
	DNSKEY		(48),
	NSEC3		(50),
	NSEC3PARAM	(51),
	TSIG		(250);

	@Override
	public String toString() {
		return name();
	}

	private RrType(int value) {
		this.value = (short) value;
	}

	private short value;

	public short getValue() {
		return value;
	}

	public boolean isDnssec() {
		if (this == RrType.DNSKEY ||
				this == RrType.NSEC ||
				this == RrType.RRSIG ||
				this == RrType.NSEC3 ||
				this == RrType.NSEC3PARAM) {
			return true;
		}
		
		return false;
	}
	
	public static RrType fromValue(short value)
		throws IllegalArgumentException {
		RrType[] values = RrType.values();
		for (RrType type : values) {
			if (type.getValue() == value) {
				return type;
			}
		}

		throw new IllegalArgumentException("Invalid RRTYPE: " + value);
	}

}
