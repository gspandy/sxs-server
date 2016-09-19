package com.sxs.server.utils;

import com.alibaba.fastjson.JSONObject;
import com.sxs.server.exception.BusinessException;
import com.sxs.server.exception.ExceptionEnum;

import java.util.Objects;

/**
 * 验证工具类
 */
public class Validate {

    public static void require(Object obj, String... fields) throws BusinessException {
        if (obj == null) {
            throw new BusinessException(ExceptionEnum.PARAMETER_NULL_EXCEPTION);
        }
        JSONObject params = (JSONObject) JSONObject.toJSON(obj);
        if (fields.length > 0) {
            for (String field : fields) {
                if (!params.containsKey(field)) {
                    throw new BusinessException(ExceptionEnum.MISSING_PARAMETER_EXCEPTION, "缺少参数:" + field);
                }
            }
        }
    }

    /**
     * 参数不能为空
     *
     * @param obj
     * @param fields
     * @throws BusinessException
     */
    public static void notEmpty(Object obj, String... fields) throws BusinessException {
        if (obj == null) {
            throw new BusinessException(ExceptionEnum.PARAMETER_NULL_EXCEPTION);
        }
        JSONObject params = (JSONObject) JSONObject.toJSON(obj);
        if (fields.length > 0) {
            for (String field : fields) {
                if (Objects.isNull(params.get(field))) {
                    throw new BusinessException(ExceptionEnum.PARAMETER_NULL_EXCEPTION, field + "不能为空");
                }
            }
        }
    }


}
