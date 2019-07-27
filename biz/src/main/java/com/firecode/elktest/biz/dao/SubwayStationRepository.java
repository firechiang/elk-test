package com.firecode.elktest.biz.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.firecode.elktest.biz.domain.SubwayStation;


public interface SubwayStationRepository extends CrudRepository<SubwayStation, Long> {
    List<SubwayStation> findAllBySubwayId(Long subwayId);
}
