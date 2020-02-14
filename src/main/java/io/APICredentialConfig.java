package io;


/**
 * IO.APICredentialConfig.java
 * used to store app_id and id_key.
 */
public class APICredentialConfig {

    private String appId;
    private String appKey;

    public APICredentialConfig(String appId, String appKey) {
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

    /**
     * @return whether the config is valid.
     */
    public boolean isValid() {
        return !"".equals(appId) && !"".equals(appKey);
    }

}
