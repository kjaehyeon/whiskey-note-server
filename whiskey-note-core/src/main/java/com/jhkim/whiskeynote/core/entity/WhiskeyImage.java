package com.jhkim.whiskeynote.core.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Table(name = "whiskey_image")
@Entity
public class WhiskeyImage extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "whiskey_id")
    private Whiskey whiskey;

    private String url;
}
