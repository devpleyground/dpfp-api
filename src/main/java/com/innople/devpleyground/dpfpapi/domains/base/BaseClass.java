package com.innople.devpleyground.dpfpapi.domains.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public abstract class BaseClass extends BaseTimeEntity {
    @Column(columnDefinition = "boolean default true")
    private boolean isUsed = true;

    @Column
    private String creator;

    @Column
    private String modifier;
}
