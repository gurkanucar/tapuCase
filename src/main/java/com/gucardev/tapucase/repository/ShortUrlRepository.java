package com.gucardev.tapucase.repository;

import com.gucardev.tapucase.model.ShortUrl;
import com.gucardev.tapucase.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByCode(String code);
    Optional<ShortUrl> findById(Long id);
    List<ShortUrl> findAllByUser(User user);

}
