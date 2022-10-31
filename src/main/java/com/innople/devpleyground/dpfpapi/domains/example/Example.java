package com.innople.devpleyground.dpfpapi.domains.example;

import com.innople.devpleyground.dpfpapi.domains.base.BaseClass;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Example extends BaseClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uid;

    @Column(nullable = false)
    private String name;

    @NotNull
    @ColumnDefault("0")
    private Integer number;

    @Column
    private String extraColumn01;

    @Column
    private String extraColumn02;

    @Column
    private String extraColumn03;

    @PrePersist
    void createdAt() {
        this.uid = UUID.randomUUID().toString();
    }

    @Builder
    public Example(String name, Integer number) {
        Assert.notNull(number, "number object is not null.");

        this.name = name;
        this.number = number;
    }
}
