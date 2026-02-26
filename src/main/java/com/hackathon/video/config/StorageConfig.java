package com.hackathon.video.config;

import br.com.fiap.storage.VideoStorageService;
import br.com.fiap.storage.local.LocalVideoStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean("videoStorage")
    public VideoStorageService videoStorage(
            @Value("${app.storage.videos-path:/tmp/videos}") String path) {
        return new LocalVideoStorageService(path);
    }

    @Bean("zipStorage")
    public VideoStorageService zipStorage(
            @Value("${app.storage.zips-path:/tmp/zips}") String path) {
        return new LocalVideoStorageService(path);
    }
}
