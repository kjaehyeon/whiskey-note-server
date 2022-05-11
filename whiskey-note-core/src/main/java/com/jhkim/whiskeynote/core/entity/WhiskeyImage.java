package com.jhkim.whiskeynote.core.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "whiskey_image")
@Entity
public class WhiskeyImage extends BaseEntity {
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "whiskey_id")
    private Whiskey whiskey;

    @Column(nullable = false)
    private String url;
}
