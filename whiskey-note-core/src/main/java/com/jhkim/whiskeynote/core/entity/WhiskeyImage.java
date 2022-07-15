package com.jhkim.whiskeynote.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "whiskey_image")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Entity
public class WhiskeyImage extends BaseEntity {
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "whiskey_id")
    private Whiskey whiskey;

    @Column(nullable = false)
    private String url;
}
