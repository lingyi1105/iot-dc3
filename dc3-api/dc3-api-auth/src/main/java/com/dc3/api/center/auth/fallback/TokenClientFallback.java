/*
 * Copyright (c) 2022. Pnoker. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dc3.api.center.auth.fallback;

import com.dc3.api.center.auth.feign.TokenClient;
import com.dc3.common.bean.Login;
import com.dc3.common.bean.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * TokenClientFallback
 *
 * @author pnoker
 */
@Slf4j
@Component
public class TokenClientFallback implements FallbackFactory<TokenClient> {

    @Override
    public TokenClient create(Throwable throwable) {
        String message = throwable.getMessage() == null ? "No available server for client: DC3-CENTER-AUTH" : throwable.getMessage();
        log.error("Fallback:{}", message);

        return new TokenClient() {

            @Override
            public R<String> generateSalt(Login login) {
                return R.fail(message);
            }

            @Override
            public R<String> generateToken(Login login) {
                return R.fail(message);
            }

            @Override
            public R<String> checkTokenValid(Login login) {
                return R.fail(message);
            }

            @Override
            public R<Boolean> cancelToken(Login login) {
                return R.fail(message);
            }

        };
    }
}