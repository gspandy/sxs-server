package com.sxs.server.zookeeper;

import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 * (测试,未使用)
 */
@JsonRootName("details")
public class InstanceDetails {
    private String description;

    public InstanceDetails() {
        this("");
    }

    public InstanceDetails(String description) {
        this.description = description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
