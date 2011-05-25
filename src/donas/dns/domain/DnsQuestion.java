package donas.dns.domain;


/**
 * Based from RFC 1035
 * 
 * <pre>
 *                                 1  1  1  1  1  1
 *   0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 * |                                               |
 * /                     QNAME                     /
 * /                                               /
 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 * |                     QTYPE                     |
 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 * |                     QCLASS                    |
 * +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
 * </pre>
 * 
 */
public class DnsQuestion {

	private String qname;
	private QueryType qtype;
	private DnsClass qclass;

	public DnsQuestion() {
		qname = "";
		qtype = QueryType.SOA;
		qclass = DnsClass.IN;
	}
	
	public DnsQuestion(DnsQuestion question) {
		qname = question.qname;
		qtype = question.qtype;
		qclass = question.qclass;
	}

	public String getQname() {
		return qname;
	}

	public void setQname(String qname) {
		this.qname = qname;
	}

	public QueryType getQtype() {
		return qtype;
	}

	public void setQtype(QueryType qtype) {
		this.qtype = qtype;
	}

	public DnsClass getQclass() {
		return qclass;
	}

	public void setQclass(DnsClass qclass) {
		this.qclass = qclass;
	}
	

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(qname).append(" ");
		result.append(qtype).append(" ");
		result.append(qclass);
		
		return result.toString();
	}
}
