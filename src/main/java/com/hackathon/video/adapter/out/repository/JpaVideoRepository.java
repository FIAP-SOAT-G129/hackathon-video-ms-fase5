package com.hackathon.video.adapter.out.repository;

import com.hackathon.video.adapter.out.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaVideoRepository extends JpaRepository<VideoEntity, UUID> {
    List<VideoEntity> findByUserId(String userId);
}
