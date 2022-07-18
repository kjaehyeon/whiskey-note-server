package com.jhkim.whiskeynote.core.repository;

import com.jhkim.whiskeynote.core.entity.Whiskey;
import com.jhkim.whiskeynote.core.entity.WhiskeyImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WhiskeyImageRepository extends JpaRepository<WhiskeyImage, Long> {
    Optional<WhiskeyImage> findWhiskeyImageById(Long whiskeyImageId);
    List<WhiskeyImage> findAll();
    List<WhiskeyImage> findWhiskeyImageByWhiskey(Whiskey whiskey);
}
