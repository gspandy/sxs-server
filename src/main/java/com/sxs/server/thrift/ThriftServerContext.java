package com.sxs.server.thrift;

import org.apache.thrift.server.ServerContext;
import org.apache.thrift.transport.TTransport;

/**
 * thrift连接上下文
 */
class ThriftServerContext implements ServerContext {

    private int connectionId;

    private TTransport transport;

    public ThriftServerContext(int connectionId, TTransport transport) {
        this.connectionId = connectionId;
        this.transport = transport;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public TTransport getTransport() {
        return transport;
    }

    public void setTransport(TTransport transport) {
        this.transport = transport;
    }
}