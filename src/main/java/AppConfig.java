/**
 * AppConfig.java
 * used to store app_id and id_key.
 */
class AppConfig {

    private String app_id;
    private String app_key;

    AppConfig(String app_id, String app_key) {
        this.app_id = app_id;
        this.app_key = app_key;
    }

    /**
     * @return app_id
     */
    String getApp_id() {
        return app_id;
    }

    /**
     * @return app_key
     */
    String getApp_key() {
        return app_key;
    }

}
