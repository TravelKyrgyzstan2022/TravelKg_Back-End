package com.example.benomad.entity;


import lombok.*;

import javax.annotation.sql.DataSourceDefinitions;


@Builder
@AllArgsConstructor
@Data
public class Req {

    private String name;
    private String integer;
}
