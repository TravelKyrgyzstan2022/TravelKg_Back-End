package com.example.travel.dto;


import com.example.travel.entity.Place;
import com.example.travel.entity.User;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Place place;
    private User user;
    private String body;

}
