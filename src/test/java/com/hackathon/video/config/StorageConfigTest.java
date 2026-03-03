package com.hackathon.video.config;

import com.fiap.soat.storage.VideoStorageService;
import com.fiap.soat.storage.local.LocalVideoStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class StorageConfigTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withUserConfiguration(StorageConfig.class);

    @Test
    void shouldCreateBeansWithDefaultPaths() {
        contextRunner.run(context -> {

            assertThat(context).hasBean("videoStorage");
            assertThat(context).hasBean("zipStorage");

            VideoStorageService videoStorage =
                    context.getBean("videoStorage", VideoStorageService.class);

            VideoStorageService zipStorage =
                    context.getBean("zipStorage", VideoStorageService.class);

            assertThat(videoStorage).isInstanceOf(LocalVideoStorageService.class);
            assertThat(zipStorage).isInstanceOf(LocalVideoStorageService.class);
        });
    }

    @Test
    void shouldUseCustomPathsWhenProvided() {
        contextRunner
                .withPropertyValues(
                        "app.storage.videos-path=/custom/videos",
                        "app.storage.zips-path=/custom/zips"
                )
                .run(context -> {

                    VideoStorageService videoStorage =
                            context.getBean("videoStorage", VideoStorageService.class);

                    VideoStorageService zipStorage =
                            context.getBean("zipStorage", VideoStorageService.class);

                    assertThat(videoStorage).isInstanceOf(LocalVideoStorageService.class);
                    assertThat(zipStorage).isInstanceOf(LocalVideoStorageService.class);
                });
    }
}