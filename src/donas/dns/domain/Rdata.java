package donas.dns.domain;

import java.util.Arrays;

public class Rdata implements Comparable<Rdata> {
	private byte[] data;

	public Rdata() {}
	
	public Rdata(byte[] data) {
		this.data = data;
	}
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public short getRdlen() {
		return (short) this.data.length;
	}

	@Override
	public int compareTo(Rdata o) {
		int minSize = (this.data.length < o.data.length) ? this.data.length : o.data.length;

		for (int i = 0; i < minSize; i++) {
			if (this.data[i] != o.data[i]) {
				if (this.data[i] < o.data[i]) {
					return -1;
				} else {
					return 1;
				}
			}
		}

		if (this.data.length < o.data.length) {
			return -1;
		} else if (this.data.length > o.data.length) {
			return 1;
		}
	
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Rdata) {
			Rdata other = (Rdata) obj;
			if (Arrays.equals(this.data, other.data)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(data);
	}
	
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(System.getProperty("line.separator"));
		for (int i = 0; i < data.length; i++) {
			stringBuffer.append(data[i] + ", ");
		}
		stringBuffer.append(System.getProperty("line.separator"));
		return stringBuffer.toString();
	}
}
