package com.sakura.enums;

public enum DockerStatus {

    MAVEN_START("mvn clean package","开始扫描项目"),
    MAVEN_END("Finished at","maven 构建完成"),
    MAVEN_SUCCESS("BUILD SUCCESS", "maven 构建成功"),
    MAVEN_FAILURE("BUILD FAILURE", "maven 构建失败"),
    MAVEN_PROGRESS("Progress", "maven下载进度"),
    MAVEN_DOWNLOADING("Downloading", "maven下载中"),
    MAVEN_DOWNLOADED("Downloaded", "maven已下载"),
    MAVEN_INFO("INFO","maven信息"),
    PULLING_FS_LAYER("Pulling fs layer", "拉取代码"),
    WAITING("Waiting", "等待"),
    DOWNLOADING("Downloading", "下载"),
    EXTRACTING("Extracting", "提取"),
    PULL_COMPLETE("Pull complete", "拉取完成"),
    VERIFYING_CHECKSUM("Verifying Checksum", "效验"),
    DOWNLOAD_COMPLETE("Download complete", "下载完成"),
    DIGEST("Digest", "摘要"),
    SHA256("sha256", "镜像Id"),
    STATUS("Status", "状态");

    private String code;
    private String message;

    DockerStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
