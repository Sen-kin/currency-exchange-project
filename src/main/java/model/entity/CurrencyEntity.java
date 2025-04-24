package model.entity;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class CurrencyEntity {

    private Long id;
    private String code;
    private String fullName;
    private String sign;


}
