package donas.dns.domain;

public enum DnsClass {
	IN(1), 
	CHAOS(3), 
	NONE(254), 
	ANY(255);

	private int value;

	private DnsClass(int value) {
		this.value = value;
	}

	public short getValue() {
		return (short) value;
	}

	public static DnsClass fromValue(short value) throws IllegalArgumentException {
		DnsClass[] values = DnsClass.values();
		for (DnsClass type : values) {
			if (type.getValue() == value) {
				return type;
			}
		}

		throw new IllegalArgumentException("No such value:"+value);
	}
}