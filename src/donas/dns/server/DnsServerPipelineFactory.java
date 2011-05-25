package donas.dns.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

import donas.dns.codec.DnsDecoder;
import donas.dns.codec.DnsEncoder;


import static org.jboss.netty.channel.Channels.*;

public class DnsServerPipelineFactory implements ChannelPipelineFactory{

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		
		// codec
		pipeline.addLast("decoder", new DnsDecoder());
		pipeline.addLast("encoder", new DnsEncoder());
		
		// business logic
		pipeline.addLast("handler", new DnsServerHandler());
		
		return pipeline;
	}
}