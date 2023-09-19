package ru.vkokourov.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Currency {
    private int id;
    private String code;
    private String fullName;
    private String sign;
}
