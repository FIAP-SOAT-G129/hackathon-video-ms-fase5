package com.hackathon.video.domain.enums;

import lombok.Getter;

@Getter
public enum SupportedVideoFormat  {

    MP4("video/mp4", ".mp4"),
    WEBM("video/webm", ".webm"),
    MOV("video/quicktime", ".mov"),
    OGG("video/ogg", ".ogg");

    private final String mimeType;
    private final String extension;

    SupportedVideoFormat(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }

    public static Boolean isSupported(String mimeType) {
        for (SupportedVideoFormat type : values()) {
            if (type.mimeType.equalsIgnoreCase(mimeType)) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isSupportedExtension(String extension) {
        for (SupportedVideoFormat type : values()) {
            if (type.extension.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    public static SupportedVideoFormat fromMimeType(String mimeType) {
        for (SupportedVideoFormat type : values()) {
            if (type.mimeType.equalsIgnoreCase(mimeType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported mime type: " + mimeType);
    }

    public static SupportedVideoFormat fromExtension(String extension) {
        for (SupportedVideoFormat type : values()) {
            if (type.extension.equalsIgnoreCase(extension)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported file extension: " + extension);
    }
}

