package donas.dns.domain;

public enum QueryType {

	A			(1),
	NS			(2),
	CNAME		(5),
	SOA			(6),
	MX			(15),
	PTR			(12),
	HINFO		(13),
	MINFO		(14),
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
	TSIG		(250),

	IXFR		(251),
	AXFR		(252),
	ANY			(255);

	private short value;

	@Override
	public String toString() {
		return name();
	}

	private QueryType(int value) {
		this.value = (short) value;
	}

	public short getValue() {
		return value;
	}

	public static QueryType fromValue(short value) throws IllegalArgumentException {
		QueryType[] values = QueryType.values();
		for (QueryType type : values) {
			if (type.getValue() == value) {
				return type;
			}
		}

		throw new IllegalArgumentException();
	}

}
