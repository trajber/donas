package donas.dns.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class ZoneIndexDao extends AbstractDao {
	private static final Logger logger = Logger.getLogger(ZoneIndexDao.class);
		
	public Map<String, String> readIndex() {
		Scanner scanner = null;
		HashMap<String, String> index = new HashMap<String, String>();
		
		try {
			logger.info("Reading zones index");
			scanner = new Scanner(new File(BASE_DIRECTORY + "zones.idx"));
						
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(" ");
				if (parts.length != 2) {
					throw new ArrayIndexOutOfBoundsException();
				}
				// zonename + path
				index.put(parts[0], parts[1]);
			}

			logger.info(index.size() + " zones read");
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return index;
	}
	
	public void appendNewIndex(String zonename, String index) {
		try {
			FileWriter writer = new FileWriter("zones.idx", true); // append
			writer.write(zonename + " " + index); // pular linha ? 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}