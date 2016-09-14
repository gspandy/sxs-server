package com.sxs.server.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 本地ip解析工具
 */
public class LocalIpResolve {

    public static String get() throws SocketException {
        // 一个主机有多个网络接口
        String localIp = null;
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = netInterfaces.nextElement();
            // 每个网络接口,都会有多个"网络地址",比如一定会有lookback地址,会有siteLocal地址等.以及IPV4或者IPV6 .
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (address != null && address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress()) {
                    localIp = address.getHostAddress();
                    break;
                }
            }
        }
        return localIp;
    }
}
