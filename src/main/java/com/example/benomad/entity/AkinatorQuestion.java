package com.example.benomad.entity;

import lombok.*;

//@Entity
//fixme
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AkinatorQuestion {
    private Long id;
    private Integer stage; // RUS: этап fixme
    private String question;
    private String firstOptionText;
    private String secondOptionText;
    private String neutralOptionText;
    private String firstOptionQuery; // query
    private String secondOptionQuery; // like < p.place_category != 'Nature' >
    private String neutralOptionQuery;
}
