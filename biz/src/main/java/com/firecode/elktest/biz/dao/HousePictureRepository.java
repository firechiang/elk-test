package com.firecode.elktest.biz.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.firecode.elktest.biz.domain.HousePicture;


public interface HousePictureRepository extends CrudRepository<HousePicture, Long> {
    List<HousePicture> findAllByHouseId(Long id);
}
