package com.gucardev.tapucase.model;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "ShortUrl Api model documentation", description = "Model")
public class ShortUrl implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String url;

    @NotNull
    @Column(unique = true)
    @Size(min = 1, max = 15)
    private String code;

    @ManyToOne(cascade =CascadeType.MERGE)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortUrl shortUrl = (ShortUrl) o;
        return Objects.equals(id, shortUrl.id) && Objects.equals(url, shortUrl.url) && Objects.equals(code, shortUrl.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, code);
    }
}
