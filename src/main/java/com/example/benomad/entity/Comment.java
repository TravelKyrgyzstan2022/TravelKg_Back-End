package com.example.benomad.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false,referencedColumnName = "id")
    private User user;

    @Column(nullable = false,length = 2000)
    private String body;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    private boolean isDeleted;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            columnDefinition = "deleted_content_info_id",
            referencedColumnName = "id"
    )
    private DeletionInfo deletionInfo;

    @ManyToMany
    @JoinTable(name = "comment_likes",
            joinColumns = @JoinColumn(columnDefinition = "comment_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(columnDefinition = "user_id",
                    referencedColumnName = "id")
    )
    private Set<User> likedUsers;

    //fixme
//    public void addLikedUser(User user){
//        likedUsers.add(user);
//    }
}
