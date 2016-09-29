package com.sxs.server.utils;

import com.sxs.server.thrift.gen.OperationResult;
import com.sxs.server.thrift.gen.SqlCallParameter;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by g.h on 2016/9/21.
 */
public class LogHandleUtils {

    public static String handleLog(SqlCallParameter sqlCallParameter) {
        StringBuilder builder = new StringBuilder();
        builder.append("{ ")
                .append("\"logId\":").append(sqlCallParameter.getHeader().getLogId()).append(",")
                .append("\"ip\":").append(sqlCallParameter.getHeader().getIp()).append(",")
                .append("\"port\":").append(sqlCallParameter.getHeader().getPort()).append(",")
                .append("\"sql\":").append(sqlCallParameter.getSql()).append(",")
                .append("\"parameters\":").append(sqlCallParameter.getParameters())
                .append(" }");
        return builder.toString();
    }

    public static String handleLog(OperationResult operationResult) {
        StringBuilder builder = new StringBuilder();
        builder.append("{ ")
                .append("\"success\":").append(operationResult.isSuccess()).append(",")
                .append("\"code\":").append(operationResult.getCode()).append(",")
                .append("\"result\":").append(operationResult.getResult())
                .append(" }");
        return builder.toString();
    }

    public static String handleLog(List<SqlCallParameter> parameterList) {
        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        if (CollectionUtils.isNotEmpty(parameterList)) {
            for (SqlCallParameter sqlCallParameter : parameterList) {
                builder.append(handleLog(sqlCallParameter)).append(",");
            }
            builder.subSequence(0, builder.lastIndexOf(","));
        }
        builder.append(" ]");
        return builder.toString();
    }
}
