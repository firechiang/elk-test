package com.firecode.elktest.biz.service;

import java.util.List;
import java.util.Map;

import com.firecode.elktest.biz.domain.SupportAddress;
import com.firecode.elktest.biz.domain.dto.SubwayDTO;
import com.firecode.elktest.biz.domain.dto.SubwayStationDTO;
import com.firecode.elktest.biz.domain.dto.SupportAddressDTO;
import com.firecode.elktest.biz.domain.search.BaiduMapLocation;

/**
 * 地址服务接口
 */
@SuppressWarnings("rawtypes")
public interface IAddressService {
    /**
     * 获取所有支持的城市列表
     */
    ServiceMultiResult<SupportAddressDTO> findAllCities();

    /**
     * 根据英文简写获取具体区域的信息
     */
    Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName);

    /**
     * 根据城市英文简写获取该城市所有支持的区域信息
     */
    ServiceMultiResult findAllRegionsByCityName(String cityName);

    /**
     * 获取该城市所有的地铁线路
     */
    List<SubwayDTO> findAllSubwayByCity(String cityEnName);

    /**
     * 获取地铁线路所有的站点
     */
    List<SubwayStationDTO> findAllStationBySubway(Long subwayId);

    /**
     * 获取地铁线信息
     */
    ServiceResult<SubwayDTO> findSubway(Long subwayId);

    /**
     * 获取地铁站点信息
     */
    ServiceResult<SubwayStationDTO> findSubwayStation(Long stationId);

    /**
     * 根据城市英文简写获取城市详细信息
     */
    ServiceResult<SupportAddressDTO> findCity(String cityEnName);

    /**
     * 根据城市以及具体地位获取百度地图的经纬度
     */
    ServiceResult<BaiduMapLocation> getBaiduMapLocation(String city, String address);

    /**
     * 上传百度LBS数据
     */
    ServiceResult lbsUpload(BaiduMapLocation location, String title, String address, long houseId, int price, int area);

    /**
     * 移除百度LBS数据
     */
    ServiceResult removeLbs(Long houseId);
}

