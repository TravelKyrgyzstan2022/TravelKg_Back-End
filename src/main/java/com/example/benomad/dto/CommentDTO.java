package com.example.benomad.dto;



import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private PlaceDTO placeDTO;
    private UserDTO userDTO;
    private String body;
}
