package com.hackathon.video.adapter.out.repository;

import com.hackathon.video.adapter.out.entity.VideoEntity;
import com.hackathon.video.adapter.out.mapper.VideoMapper;
import com.hackathon.video.domain.entity.Video;
import com.hackathon.video.domain.repository.VideoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VideoRepositoryAdapter implements VideoRepositoryPort {

    private final JpaVideoRepository jpaVideoRepository;

    @Override
    public Video save(Video video) {
        VideoEntity entity = VideoMapper.toEntity(video);
        VideoEntity savedEntity = jpaVideoRepository.save(entity);
        return VideoMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Video> findById(UUID id) {
        return jpaVideoRepository.findById(id).map(VideoMapper::toDomain);
    }

    @Override
    public List<Video> findByUserId(String userId) {
        return jpaVideoRepository.findByUserId(userId).stream()
                .map(VideoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        jpaVideoRepository.deleteById(id);
    }
}
