package com.phunghv.base.interceptor;

import com.phunghv.base.config.DBConfigDataFactory;
import com.phunghv.base.constant.RequestHeaderConstants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

@Component
@RequiredArgsConstructor
public class DataSourceInterceptor implements AsyncHandlerInterceptor {

    private final DBConfigDataFactory dbConfigDataFactory;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var db = request.getHeader(RequestHeaderConstants.DATABASE_HEADER);
        var dbKey = dbConfigDataFactory.validateDBKey(db);
        if (StringUtils.isBlank(dbKey)) {
            response.setStatus(403);
            return false;
        }
        request.setAttribute(RequestHeaderConstants.DATABASE_KEY, dbKey);
        return true;
    }
}
