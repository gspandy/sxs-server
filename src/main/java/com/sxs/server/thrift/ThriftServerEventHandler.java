package com.sxs.server.thrift;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.ServerContext;
import org.apache.thrift.server.TServerEventHandler;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;

class ThriftServerEventHandler implements TServerEventHandler {
    public static final Logger logger = LoggerFactory.getLogger(ThriftServerEventHandler.class);

    private int nextConnectionId = 1;

    public void preServe() {
    }

    public ServerContext createContext(TTransport transport, TProtocol input, TProtocol output) {
        ThriftServerContext ctx = new ThriftServerContext(nextConnectionId++, transport);
        if (transport != null && transport instanceof TNonblockingSocket) {
            String localAddress = null;
            String remoteAddress = null;
            try {
                SocketChannel channel = ((TNonblockingSocket) transport).getSocketChannel();
                localAddress = String.valueOf(channel.getLocalAddress());
                remoteAddress = String.valueOf(channel.getRemoteAddress());
            } catch (IOException e) {
                localAddress = "null";
                remoteAddress = "null";
                logger.error("获取客户端ip地址失败, connection #" + ctx.getConnectionId(), e);
            }
            logger.info("ThriftServer client Socket Info - connection #" + ctx.getConnectionId()
                    + " , server地址: " + remoteAddress + " , client地址: " + localAddress);


        }
        return ctx;
    }

    public void deleteContext(ServerContext serverContext, TProtocol input, TProtocol output) {
        ThriftServerContext ctx = (ThriftServerContext) serverContext;
        logger.info("TServerEventHandler.deleteContext - connection #" + ctx.getConnectionId() + " terminated");
    }

    public void processContext(ServerContext serverContext, TTransport inputTransport, TTransport outputTransport) {
        ThriftServerContext ctx = (ThriftServerContext) serverContext;
        if (logger.isDebugEnabled()) {
            logger.debug("TServerEventHandler.processContext - connection #" + ctx.getConnectionId() + " is ready to process next request");
        }
    }

}