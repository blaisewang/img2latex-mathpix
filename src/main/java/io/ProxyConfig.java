package io;


/**
 * IO.ProxyConfig.java
 * used to store proxy config.
 */
public class ProxyConfig {

    private String hostname;
    private int port;

    ProxyConfig(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * @return hostname.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @return port.
     */
    public int getPort() {
        return port;
    }

    /**
     * @return port as a String.
     */
    public String getPortAsString() {
        return port >= 0 ? Integer.toString(port) : "";
    }

    /**
     * @return whether the config is valid.
     */
    public boolean isValid() {
        return !"".equals(hostname) & port >= 0 && port <= 65535;
    }

}
