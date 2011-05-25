package donas.dns.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import donas.dns.domain.DnsHeader;
import donas.dns.domain.DnsMessage;
import donas.dns.domain.DnsQuestion;
import donas.dns.records.ResourceRecord;
import donas.util.ByteUtil;
import donas.util.DomainNameUtil;



public class DnsEncoder extends OneToOneEncoder {
	
	private final static Logger logger = LoggerFactory.getLogger(DnsEncoder.class);

	@Override
	protected Object encode(ChannelHandlerContext ctx, 
							Channel channel, 
							Object msg) throws Exception {
		
		if (!(msg instanceof DnsMessage)) {
			return msg;
		}
		
		logger.info("Starting encoder");
		ChannelBuffer response = ChannelBuffers.dynamicBuffer();

		
		DnsMessage packet = (DnsMessage) msg;
		DnsHeader header = packet.getHeader();
		DnsQuestion question = packet.getQuestion();
		
		ChannelBuffer headerBuffer = encodeHeader(header);
		response.writeBytes(headerBuffer);

		if (header.getQuestionCount() > 0 && question != null) {
			ChannelBuffer questionBuffer = encodeQuestion(question);
			response.writeBytes(questionBuffer);
		}

		// Answer
		for (ResourceRecord record : packet.getAnswerList()) {
			ChannelBuffer rrBuffer = encodeResourceRecord(record);
			response.writeBytes(rrBuffer);
		}

		// Authority
		for (ResourceRecord record : packet.getAuthorityList()) {
			ChannelBuffer rrBuffer = encodeResourceRecord(record);
			response.writeBytes(rrBuffer);
		}
		
		// Additional
		for (ResourceRecord record : packet.getAdditionalList()) {
			ChannelBuffer rrBuffer = encodeResourceRecord(record);
			response.writeBytes(rrBuffer);
		}		
		
//		response.writeShort(headerBuffer.capacity()); // tcp must have packet size...

		logger.info("Encoder done");

		return response;
	}
	
	private ChannelBuffer encodeHeader(DnsHeader header) {
		logger.info("Encoding header");

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeShort(header.getId());

		short headerShort = 0;
		headerShort = ByteUtil.setBit(headerShort, 1, header.isQuestion());
		headerShort = ByteUtil.setBits(headerShort, header.getOpcode().getValue(), 2, 4);
		headerShort = ByteUtil.setBit(headerShort, 6, header.isAuthoritativeAnswer());
		headerShort = ByteUtil.setBit(headerShort, 7, header.isTruncatedResponse());
		headerShort = ByteUtil.setBit(headerShort, 8, header.isRecursionDesired());
		headerShort = ByteUtil.setBit(headerShort, 9, header.isRecursionAvailable());
		headerShort = ByteUtil.setBit(headerShort, 10, header.isReserved());
		headerShort = ByteUtil.setBit(headerShort, 11, header.isAuthenticData());
		headerShort = ByteUtil.setBit(headerShort, 12, header.isCheckingDisabled());
		headerShort = ByteUtil.setBits(headerShort, header.getResponseCode().getValue(), 13, 4);

		buffer.writeShort(headerShort);
		
		buffer.writeShort(header.getQuestionCount());
		buffer.writeShort(header.getAnswerCount());
		buffer.writeShort(header.getAuthorityCount());
		buffer.writeShort(header.getAdditionalCount());
		return buffer;
	}
	
	private ChannelBuffer encodeQuestion(DnsQuestion question) {
		logger.info("Encoding question");
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

		buffer.writeBytes(DomainNameUtil.toWireFormat(question.getQname()));
		buffer.writeShort(question.getQtype().getValue());
		buffer.writeShort(question.getQclass().getValue());
		return buffer;
	}
	
	private ChannelBuffer encodeResourceRecord(ResourceRecord record) {
		logger.info("Encoding resource record");

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes(DomainNameUtil.toWireFormat(record.getOwnername()));
		buffer.writeShort(record.getType().getValue());
		buffer.writeShort(record.getDnsClass().getValue());
		buffer.writeInt(record.getTtl());
		buffer.writeShort(record.getRdata().getRdlen());
		buffer.writeBytes(record.getRdata().getData());
		
		return buffer;
	}
}