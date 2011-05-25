package donas.dns.domain;

import java.util.Comparator;

public class RrTypeComparator implements Comparator<RrType> {

	@Override
	public int compare(RrType o1, RrType o2) {
		
		if ((o1 == RrType.SOA) && (o2 != RrType.SOA)) {
			return -1;
		} else if (o1 != RrType.SOA && o2 == RrType.SOA) {
			return 1;
		} 

		if (o1.getValue() < o2.getValue()) {
			return -1;
		} else if (o1.getValue() > o2.getValue()) {
			return 1;
		} else {
			return 0;
		}
	}
}
