package donas.dns.domain;

import java.util.Comparator;
import java.util.regex.Pattern;

public class OwnernameComparator implements Comparator<String> {
	private static final Pattern pattern = Pattern.compile("\\.");
	
	@Override
	public int compare(String first, String second) {
		String[] firstParts = pattern.split(first);
		String[] secondParts = pattern.split(second);

		int min = firstParts.length < secondParts.length ? firstParts.length : secondParts.length;

		for (int i = 1; i <= min; i++) {
			if (firstParts[firstParts.length - i].equals(secondParts[secondParts.length - i])) {
				continue;
			}
			return firstParts[firstParts.length - i].compareTo(secondParts[secondParts.length - i]) > 0 ? 1	: -1;

		}
		if (firstParts.length == secondParts.length) {
			return 0;
		} else if (firstParts.length > secondParts.length) {
			return 1;
		} else {
			return -1;
		}

	}
}