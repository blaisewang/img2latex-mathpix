package io;


/**
 * IO.APICredentialConfig.java
 * used to store appId and appKey.
 */
public class APICredentialConfig {

    private final String appId;
    private final String appKey;

    public APICredentialConfig(String appId, String appKey) {
        this.appId = appId;
        this.appKey = appKey;
    }

    /**
     * @return appId
     */
    public String getAppId() {
        return appId;
    }

    /**
     * @return appKey.
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
