package com.hackathon.video.adapter.out.repository;

import com.hackathon.video.adapter.out.entity.VideoEntity;
import com.hackathon.video.adapter.out.mapper.VideoMapper;
import com.hackathon.video.domain.entity.Video;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Mock
    private VideoMapper videoMapper;

    @InjectMocks
    private VideoRepositoryAdapter videoRepositoryAdapter;

    @Test
    void shouldSaveVideo() {
        Video video = Video.builder().id(UUID.randomUUID()).build();
        VideoEntity entity = new VideoEntity();
        
        when(videoMapper.toEntity(any(Video.class))).thenReturn(entity);
        when(jpaVideoRepository.save(any(VideoEntity.class))).thenReturn(entity);
        when(videoMapper.toDomain(any(VideoEntity.class))).thenReturn(video);

        Video savedVideo = videoRepositoryAdapter.save(video);

        assertNotNull(savedVideo);
        verify(jpaVideoRepository, times(1)).save(entity);
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        VideoEntity entity = new VideoEntity();
        Video video = Video.builder().id(id).build();

        when(jpaVideoRepository.findById(id)).thenReturn(Optional.of(entity));
        when(videoMapper.toDomain(entity)).thenReturn(video);

        Optional<Video> result = videoRepositoryAdapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }
}
