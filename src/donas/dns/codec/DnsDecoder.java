package donas.dns.codec;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import donas.dns.domain.DnsClass;
import donas.dns.domain.DnsHeader;
import donas.dns.domain.DnsMessage;
import donas.dns.domain.DnsQuestion;
import donas.dns.domain.Opcode;
import donas.dns.domain.QueryType;
import donas.dns.domain.ResponseCode;
import donas.dns.domain.RrType;
import donas.util.ByteUtil;
import donas.util.DomainNameUtil;



public class DnsDecoder extends OneToOneDecoder {
	private final static Logger logger = LoggerFactory.getLogger(DnsDecoder.class);

	@Override
	protected Object decode(
			ChannelHandlerContext ctx, 
			Channel channel,
			Object msg) throws Exception {
		
		if (!(msg instanceof ChannelBuffer)) {
			return msg;
		}
		
		ChannelBuffer channelBuffer = (ChannelBuffer) msg;
		// in TCP size comes first
		if (channel.isConnected()) {
			channelBuffer.readShort();
		}
		
		DnsHeader header;
		DnsQuestion question;
		DnsMessage packet;
		
		header = decodeHeader(channelBuffer);
		question = decodeQuestion(channelBuffer);
		
		/* Answer Section */
		for (int count = 0; count < header.getAnswerCount(); count++) {
			decodeResourceRecord(channelBuffer);
		}
		
		/* Authority Section */
		for (int count = 0; count < header.getAuthorityCount(); count++) {
			decodeResourceRecord(channelBuffer);
		}

		packet = new DnsMessage();
		packet.setHeader(header);
		packet.setQuestion(question);
		return packet;
	}

	private DnsQuestion decodeQuestion(ChannelBuffer channelBuffer) {
		String qname = DomainNameUtil.toPresentationFormat(channelBuffer);
	
		QueryType qt = QueryType.fromValue(channelBuffer.readShort());
		DnsClass dc = DnsClass.fromValue(channelBuffer.readShort());
			
		DnsQuestion question = new DnsQuestion();
		question.setQname(qname);
		question.setQtype(qt);
		question.setQclass(dc);
		return question;
	}
	
	private DnsHeader decodeHeader(ChannelBuffer buffer) {
		// Header
		DnsHeader header = new DnsHeader();
		header.setId(buffer.readShort());
		
		byte byte1 = buffer.readByte();
		header.setQuestion(ByteUtil.getBit(byte1, 1)); // 10000000

		byte opcodeByte = ByteUtil.getBits(byte1, 2, 4);
		Opcode opcode = Opcode.fromValue(opcodeByte); // 01111000
		header.setOpcode(opcode);

		header.setAuthoritativeAnswer(ByteUtil.getBit(byte1, 6)); // 00000100
		header.setTruncatedResponse(ByteUtil.getBit(byte1, 7)); // 00000010
		header.setRecursionDesired(ByteUtil.getBit(byte1, 8)); // 00000001

		byte byte2 = buffer.readByte();
		header.setRecursionAvailable(ByteUtil.getBit(byte2, 1)); // 10000000
		header.setReserved(ByteUtil.getBit(byte2, 2));  // 01000000
		header.setAuthenticData(ByteUtil.getBit(byte2, 3)); // 00100000
		header.setCheckingDisabled(ByteUtil.getBit(byte2, 4)); // 00010000
		header.setResponseCode(ResponseCode.fromValue(ByteUtil.getBits(byte2, 5, 4))); // 00001111
		
		header.setQuestionCount(buffer.readShort());
		header.setAnswerCount(buffer.readShort());
		header.setAuthorityCount(buffer.readShort());
		
		header.setAdditionalCount(buffer.readShort());

		return header;
	}
	
	private void decodeResourceRecord(ChannelBuffer buffer) {
		String ownername = DomainNameUtil.toPresentationFormat(buffer);
		ownername = ownername.toLowerCase();
		short typeShort = buffer.readShort();
		RrType type = RrType.fromValue(typeShort);
		DnsClass dnsClass = DnsClass.fromValue(buffer.readShort());
		
		logger.info(ownername);
		logger.info(type.toString());
		logger.info(dnsClass.toString());
	}
}