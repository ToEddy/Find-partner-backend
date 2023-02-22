package com.findPartner.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author eddy
 * @createTime 2023/2/21
 */
@Data
public class PageParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 8396266026691236639L;

    private Long pageNum;
    private Long pageSize;
}
