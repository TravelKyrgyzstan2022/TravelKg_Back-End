package com.example.benomad.security.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Claims {
    private List<String> get;
    private List<String> post;
    private List<String> put;
    private List<String> delete;

    public Claims(){
        this.get = new ArrayList<>();
        this.post = new ArrayList<>();
        this.put = new ArrayList<>();
        this.delete = new ArrayList<>();
    }
}
