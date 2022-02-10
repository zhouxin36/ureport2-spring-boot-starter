package vip.zhouxin.ureport.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xinxingzhou
 * @since 2022/2/10
 */
@ConfigurationProperties(prefix = "ureport")
public class UreportProperties {
    private String fileStoreDir = "/WEB-INF/ureportfiles";
    private Boolean disableFileProvider = false;
    private Boolean debug = true;

    public String getFileStoreDir() {
        return fileStoreDir;
    }

    public void setFileStoreDir(String fileStoreDir) {
        this.fileStoreDir = fileStoreDir;
    }

    public Boolean getDisableFileProvider() {
        return disableFileProvider;
    }

    public void setDisableFileProvider(Boolean disableFileProvider) {
        this.disableFileProvider = disableFileProvider;
    }

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }
}
