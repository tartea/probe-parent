package org.tartea.plugin.infrastructure;

public class DataState {

    private String agentPath = null;
    private boolean mysqlSetting = false;

    public String getAgentPath() {
        return agentPath;
    }

    public void setAgentPath(String agentPath) {
        this.agentPath = agentPath;
    }

    public boolean getMysqlSetting() {
        return mysqlSetting;
    }

    public void setMysqlSetting(boolean mysqlSetting) {
        this.mysqlSetting = mysqlSetting;
    }
}