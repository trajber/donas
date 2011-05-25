package donas.dns.domain;

import java.util.LinkedList;
import java.util.List;

import donas.dns.records.ResourceRecord;



/**
 * RFC 1035 - 4. MESSAGES
 * 
 * <pre>
 * All communications inside of the domain protocol are carried in a single
 * format called a message.  The top level format of message is divided
 * into 5 sections (some of which are empty in certain cases) shown below:
 * 
 *  +---------------------+
 *  |        Header       |
 *  +---------------------+
 *  |       Question      | the question for the name server
 *  +---------------------+
 *  |        Answer       | RRs answering the question
 *  +---------------------+
 *  |      Authority      | RRs pointing toward an authority
 *  +---------------------+
 *  |      Additional     | RRs holding additional information
 *  +---------------------+
 * 
 * </pre>
 * 
 * 
 */
public class DnsMessage {
	private DnsHeader header = new DnsHeader();
	private DnsQuestion question = new DnsQuestion();

	private List<ResourceRecord> answer = new LinkedList<ResourceRecord>();
	private List<ResourceRecord> authority = new LinkedList<ResourceRecord>();
	private List<ResourceRecord> additional = new LinkedList<ResourceRecord>();

	public DnsHeader getHeader() {
		return header;
	}

	public void setHeader(DnsHeader header) {
		this.header = header;
	}

	public DnsQuestion getQuestion() {
		return question;
	}

	public void setQuestion(DnsQuestion question) {
		this.question = question;
	}

	public List<ResourceRecord> getAnswerList() {
		return answer;
	}

	public void addAnswer(ResourceRecord record) {
		this.answer.add(record);
		this.header.setAnswerCount((short)this.answer.size());
	}
	
	public void addAllAnswers(RrSet items) {
		this.answer.addAll(items.getRecords());
		this.header.setAnswerCount((short)this.answer.size());
	}
	
	public List<ResourceRecord> getAuthorityList() {
		return authority;
	}

	public void addAuthority(ResourceRecord record) {
		authority.add(record);
		this.header.setAuthorityCount((short)this.authority.size());
	}
	
	public List<ResourceRecord> getAdditionalList() {
		return additional;
	}
	
	public void addAdditional(ResourceRecord record) {
		additional.add(record);
		this.header.setAdditionalCount((short)this.additional.size());
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append(header.toString()).append(System.getProperty("line.separator"));
		result.append(question.toString()).append(System.getProperty("line.separator"));

		result.append(System.getProperty("line.separator"));
		result.append("ANSWER: ");
		result.append(System.getProperty("line.separator"));
		
		for (ResourceRecord record : answer) {
			result.append(record.toString()).append(System.getProperty("line.separator"));
		}

		result.append(System.getProperty("line.separator"));
		result.append("AUTHORITY: ");
		result.append(System.getProperty("line.separator"));
		for (ResourceRecord record : authority) {
			result.append(record.toString()).append(System.getProperty("line.separator"));
		}

		result.append(System.getProperty("line.separator"));
		result.append("ADDITIONAL: ");
		result.append(System.getProperty("line.separator"));
		for (ResourceRecord record : additional) {
			result.append(record.toString()).append(System.getProperty("line.separator"));
		}

				
		return result.toString();
	}
}
