/*
 * Copyright 2022 Pnoker All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.pnoker.api.center.data.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.pnoker.api.center.data.fallback.PointValueClientFallback;
import io.github.pnoker.common.bean.R;
import io.github.pnoker.common.bean.point.PointValue;
import io.github.pnoker.common.constant.ServiceConstant;
import io.github.pnoker.common.dto.PointValueDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 数据 FeignClient
 *
 * @author pnoker
 */
@FeignClient(path = ServiceConstant.Data.VALUE_URL_PREFIX, name = ServiceConstant.Data.SERVICE_NAME, fallbackFactory = PointValueClientFallback.class)
public interface PointValueClient {

    /**
     * 查询最新 PointValue 集合
     *
     * @param deviceId Device Id
     * @return PointValue Array
     */
    @GetMapping("/latest/device_id/{deviceId}")
    R<List<PointValue>> latest(@NotNull @PathVariable(value = "deviceId") String deviceId, @RequestParam(required = false, defaultValue = "false") Boolean history);

    /**
     * 查询最新 PointValue
     *
     * @param deviceId Device Id
     * @param pointId  Point Id
     * @return PointValue
     */
    @GetMapping("/latest/device_id/{deviceId}/point_id/{pointId}")
    R<PointValue> latest(@NotNull @PathVariable(value = "deviceId") String deviceId, @NotNull @PathVariable(value = "pointId") String pointId, @RequestParam(required = false, defaultValue = "false") Boolean history);

    /**
     * 分页查询 PointValue
     *
     * @param pointValueDto PointValueDto
     * @return Page<PointValue>
     */
    @PostMapping("/list")
    R<Page<PointValue>> list(@RequestBody(required = false) PointValueDto pointValueDto);
}