package com.hackathon.video.application.usecase;

import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.enums.StorageType;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import com.hackathon.video.domain.repository.VideoStoragePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteVideoUseCaseTest {

    @Mock private VideoRepositoryPort repository;
    @Mock private VideoStoragePort storage;
    @InjectMocks private DeleteVideoUseCase useCase;

    @Test
    void shouldDeleteVideo() {
        UUID id = UUID.randomUUID();
        Video video = Video.builder().id(id).storagePath("/path/videos").zipPath("/path/zips").build();
        when(repository.findById(id)).thenReturn(Optional.of(video));

        useCase.execute(id);

        verify(storage).delete(StorageType.VIDEO, "/path/videos");
        verify(storage).delete(StorageType.ZIP, "/path/zips");

        verify(repository).delete(id);
    }
}
