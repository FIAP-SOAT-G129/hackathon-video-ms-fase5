package com.hackathon.video.domain.repository;

import java.util.Optional;

public interface UserIdentityPort {
    Optional<String> getEmailByUserId(String userId);
}
