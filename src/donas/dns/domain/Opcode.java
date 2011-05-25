package donas.dns.domain;

public enum Opcode {

	QUERY(0), 
	IQUERY(1), 
	STATUS(2), 
	NOTIFY(4);

	private int value; // only necessary 4 bits

	@Override
	public String toString() {
		return name();
	}

	private Opcode(int value) {
		this.value = value;
	}

	public byte getValue() {
		return (byte) value;
	}

	public static Opcode fromValue(byte value)
			throws IllegalArgumentException {
		Opcode[] values = Opcode.values();
		for (Opcode type : values) {
			if (type.getValue() == value) {
				return type;
			}
		}

		throw new IllegalArgumentException();
	}

}
