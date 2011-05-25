package donas.dns.server;


import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import donas.dns.domain.DnsMessage;



public class DnsServerHandler extends SimpleChannelUpstreamHandler {
	private final static Logger logger = Logger.getLogger(DnsServerHandler.class);

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent event)
			throws Exception {
		if (event instanceof ChannelStateEvent) {
			logger.info(event.toString());
		}
		super.handleUpstream(ctx, event);
	}
	 
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Object response = e.getMessage();
		
		if (e.getMessage() instanceof DnsMessage) {
			DnsMessageHandler messageHandler = new DnsMessageHandler();
			logger.info("Message parsed, calling domain logic.");
			response = messageHandler.process((DnsMessage)e.getMessage());	
		}
		e.getChannel().write(response, e.getRemoteAddress());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
		logger.error(e);

		if (ctx.getChannel().isConnected()) {
			e.getChannel().close();
		}
	}	
}