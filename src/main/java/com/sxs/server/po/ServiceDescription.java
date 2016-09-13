package com.sxs.server.po;

import java.io.Serializable;

/**
 * 服务地址描述
 */
public class ServiceDescription implements Serializable {

    private static final long serialVersionUID = -606707537538335241L;
    /**
     * 服务名称
     */
    private final String name;
    /**
     * 服务地址
     */
    private final String address;
    /**
     * 服务注册时间
     */
    private final long registrationTimeUTC;
    /**
     * 服务版本
     */
    private final String version;

    public ServiceDescription(String name, String address) {
        this.name = name;
        this.address = address;
        this.version = "1.0.0";
        registrationTimeUTC = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public long getRegistrationTimeUTC() {
        return registrationTimeUTC;
    }

    public String getVersion() {
        return version;
    }


    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (int) (registrationTimeUTC ^ (registrationTimeUTC >>> 32));
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceDescription{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", registrationTimeUTC=" + registrationTimeUTC +
                ", version='" + version + '\'' +
                '}';
    }
}
