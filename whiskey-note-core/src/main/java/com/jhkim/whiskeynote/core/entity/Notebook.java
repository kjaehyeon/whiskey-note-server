package com.jhkim.whiskeynote.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notebook")
@Entity
public class Notebook extends BaseEntity{
    private String title;

    //표지 색상 RGB
    private Integer red;
    private Integer green;
    private Integer blue;
}
