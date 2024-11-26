package com.fpoly.be_wanren_buffet.dto;

import com.fpoly.be_wanren_buffet.enums.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableDTO {
    private Long tableId;
    private Integer tableNumber;
    private TableStatus tableStatus;
    private String location;
}
