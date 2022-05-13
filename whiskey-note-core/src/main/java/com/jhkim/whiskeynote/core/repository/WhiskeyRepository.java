package com.jhkim.whiskeynote.core.repository;

import com.jhkim.whiskeynote.core.entity.Whiskey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WhiskeyRepository extends JpaRepository<Whiskey, Long> {
    Optional<Whiskey> findWhiskeyById(Long whiskeyId);
}
