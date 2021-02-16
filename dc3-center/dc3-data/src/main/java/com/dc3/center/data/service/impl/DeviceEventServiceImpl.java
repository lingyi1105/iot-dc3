/*
 * Copyright 2018-2020 Pnoker. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dc3.center.data.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc3.center.data.service.DeviceEventService;
import com.dc3.common.bean.Pages;
import com.dc3.common.bean.driver.DeviceEvent;
import com.dc3.common.bean.driver.DeviceEventDto;
import com.dc3.common.constant.Common;
import com.dc3.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pnoker
 */
@Slf4j
@Service
public class DeviceEventServiceImpl implements DeviceEventService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public String deviceStatus(Long deviceId) {
        String key = Common.Cache.DEVICE_STATUS_KEY_PREFIX + deviceId;
        String status = redisUtil.getKey(key, String.class);
        return null != status ? status : Common.Device.Status.OFFLINE;
    }

    @Override
    public void addDeviceEvent(DeviceEvent deviceEvent) {
        if (null != deviceEvent) {
            mongoTemplate.insert(deviceEvent);
        }
    }

    @Override
    public void addDeviceEvents(List<DeviceEvent> deviceEvents) {
        if (null != deviceEvents) {
            if (deviceEvents.size() > 0) {
                mongoTemplate.insert(deviceEvents, DeviceEvent.class);
            }
        }
    }

    @Override
    public Page<DeviceEvent> list(DeviceEventDto deviceEventDto) {
        Criteria criteria = new Criteria();
        if (null == deviceEventDto) {
            deviceEventDto = new DeviceEventDto();
        }
        if (null != deviceEventDto.getDeviceId()) {
            criteria.and("deviceId").is(deviceEventDto.getDeviceId());
        }
        if (null != deviceEventDto.getPointId()) {
            criteria.and("pointId").is(deviceEventDto.getPointId());
        }

        Pages pages = null == deviceEventDto.getPage() ? new Pages() : deviceEventDto.getPage();
        if (pages.getStartTime() > 0 && pages.getEndTime() > 0 && pages.getStartTime() <= pages.getEndTime()) {
            criteria.and("originTime").gte(pages.getStartTime()).lte(pages.getEndTime());
        }

        Query query = new Query(criteria);
        long count = mongoTemplate.count(query, DeviceEvent.class);

        query.with(Sort.by(Sort.Direction.DESC, "originTime"));
        int size = (int) pages.getSize();
        long page = pages.getCurrent();
        query.limit(size).skip(size * (page - 1));

        List<DeviceEvent> deviceEvents = mongoTemplate.find(query, DeviceEvent.class);

        return (new Page<DeviceEvent>()).setCurrent(pages.getCurrent()).setSize(pages.getSize()).setTotal(count).setRecords(deviceEvents);
    }

}
