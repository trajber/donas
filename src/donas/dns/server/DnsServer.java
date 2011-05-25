package donas.dns.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import donas.dns.dao.DnsServerConfigDao;
import donas.util.ZoneIndex;


public class DnsServer {
    private static final Logger logger = LoggerFactory.getLogger(DnsServer.class);

	public static void main(String[] args) throws IOException {
		InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
		printAbout();
		
		// loading defaults
		DnsServerConfig config = DnsServerConfigDao.getInstance();
		
		// loading zone index
		ZoneIndex.load();
		
		// starting server
		startTcpServer(config.getPort());
		startUdpServer(config.getPort());
	}
	
	private static void startTcpServer(int port) {
		ServerBootstrap tcpBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		tcpBootstrap.setPipelineFactory(new DnsServerPipelineFactory());

		tcpBootstrap.bind(new InetSocketAddress(port));	
	}
	
	private static void startUdpServer(int port) {
		ConnectionlessBootstrap udpBootstrap = new ConnectionlessBootstrap(
				new NioDatagramChannelFactory(Executors.newCachedThreadPool())); 

		udpBootstrap.setPipelineFactory(new DnsServerPipelineFactory());
		udpBootstrap.bind(new InetSocketAddress(port));	
	}
	
	
	private static void printAbout() throws IOException {
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("donas/resources/version.properties");
		Properties properties = new Properties();
		properties.load(stream);
		String version = properties.get("dns.server").toString();
		stream.close();
		logger.info("Starting DNS Server version " + version);
	}
}