package model.entity;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class CurrencyEntity {

    private Long Id;
    private String Code;
    private String FullName;
    private String Sign;


}
