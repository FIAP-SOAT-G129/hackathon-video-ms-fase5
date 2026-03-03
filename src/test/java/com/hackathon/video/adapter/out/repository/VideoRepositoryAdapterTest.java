package com.hackathon.video.adapter.out.repository;

import com.hackathon.video.adapter.out.entity.VideoEntity;
import com.hackathon.video.adapter.out.mapper.VideoMapper;
import com.hackathon.video.domain.entity.Video;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoRepositoryAdapterTest {

    @Mock
    private JpaVideoRepository jpaVideoRepository;

    @InjectMocks
    private VideoRepositoryAdapter videoRepositoryAdapter;

    @Test
    void shouldSaveVideo() {
        Video video = Video.builder().id(UUID.randomUUID()).build();
        VideoEntity entity = new VideoEntity();

        when(jpaVideoRepository.save(any(VideoEntity.class))).thenReturn(entity);

        try (MockedStatic<VideoMapper> mocked = mockStatic(VideoMapper.class)) {
            mocked.when(() -> VideoMapper.toEntity(video)).thenReturn(entity);
            mocked.when(() -> VideoMapper.toDomain(entity)).thenReturn(video);

            Video savedVideo = videoRepositoryAdapter.save(video);

            assertNotNull(savedVideo);
            verify(jpaVideoRepository, times(1)).save(entity);
        }
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        VideoEntity entity = new VideoEntity();
        Video video = Video.builder().id(id).build();

        when(jpaVideoRepository.findById(id)).thenReturn(Optional.of(entity));

        try (MockedStatic<VideoMapper> mocked = mockStatic(VideoMapper.class)) {
            mocked.when(() -> VideoMapper.toDomain(entity)).thenReturn(video);

            Optional<Video> result = videoRepositoryAdapter.findById(id);

            assertTrue(result.isPresent());
            assertEquals(id, result.get().getId());
        }
    }

    @Test
    void shouldFindByUserId() {
        String userId = "user123";
        VideoEntity entity = new VideoEntity();
        Video video = Video.builder().userId(userId).build();

        when(jpaVideoRepository.findByUserId(userId)).thenReturn(java.util.List.of(entity));

        try (MockedStatic<VideoMapper> mocked = mockStatic(VideoMapper.class)) {
            mocked.when(() -> VideoMapper.toDomain(entity)).thenReturn(video);

            java.util.List<Video> result = videoRepositoryAdapter.findByUserId(userId);

            assertFalse(result.isEmpty());
            assertEquals(userId, result.get(0).getUserId());
        }
    }

    @Test
    void shouldDelete() {
        UUID id = UUID.randomUUID();
        doNothing().when(jpaVideoRepository).deleteById(id);

        videoRepositoryAdapter.delete(id);

        verify(jpaVideoRepository, times(1)).deleteById(id);
    }
}
