package com.firecode.elktest.biz.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.firecode.elktest.biz.domain.HouseDetail;


public interface HouseDetailRepository extends CrudRepository<HouseDetail, Long>{
    HouseDetail findByHouseId(Long houseId);

    List<HouseDetail> findAllByHouseIdIn(List<Long> houseIds);
}
