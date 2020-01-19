package io;

/**
 * IO.AppConfig.java
 * used to store app_id and id_key.
 */
public class AppConfig {

    private String appId;
    private String appKey;

    public AppConfig(String appId, String appKey) {
        this.appId = appId;
        this.appKey = appKey;
    }

    /**
     * @return app_id.
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @return app_key.
     */
    public String getAppKey() {
        return appKey;
    }

}
