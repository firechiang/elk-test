package com.firecode.elktest.biz.domain.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前位置房源数量
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseBucketDTO {
    /**
     * 聚合bucket的key
     */
    private String key;

    /**
     * 聚合结果值
     */
    private long count;
}
