package com.firecode.elktest.biz.domain.dto;

import lombok.Data;

@Data
public final class QiNiuPutRet {
    String key;
    String hash;
    String bucket;
    int width;
    int height;
}
