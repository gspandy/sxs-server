package com.sxs.server.thrift.service.impl;

import com.alibaba.fastjson.JSON;
import com.sxs.server.annotation.ThriftService;
import com.sxs.server.exception.BusinessException;
import com.sxs.server.exception.ExceptionEnum;
import com.sxs.server.thrift.common.OperationResult;
import com.sxs.server.thrift.common.SqlCallParameter;
import com.sxs.server.thrift.service.SqlCallService;
import com.sxs.server.utils.SpringUtils;
import com.sxs.server.utils.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * sql调用服务实现
 */
@ThriftService
public class SqlCallServiceImpl implements SqlCallService.Iface {
    public static final Logger logger = LoggerFactory.getLogger(SqlCallServiceImpl.class);

    private JdbcTemplate jdbcTemplate;

    /**
     * 执行插入sql
     */
    @Override
    public OperationResult insertSql(SqlCallParameter sqlCallParameter) {
        OperationResult result = new OperationResult();
        result.setSuccess(true);
        try {
            logger.debug("insertSql[{}]", JSON.toJSONString(sqlCallParameter));
            Validate.notEmpty(sqlCallParameter, "sql", "parameters");
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection conn)
                        throws SQLException {
                    String[] params = (String[]) sqlCallParameter.getParameters().toArray();
                    PreparedStatement ps = conn.prepareStatement(sqlCallParameter.getSql(), params);
                    for (int i = 0; i <= params.length; i++) {
                        ps.setObject(i + 1, params[i]);
                    }
                    return ps;
                }
            }, keyHolder);
            result.setResult(keyHolder.getKey().toString());

        } catch (Exception ex) {
            if (ex instanceof BusinessException) {
                result.setCode(((BusinessException) ex).getCode());
            }
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    /**
     * 执行查询sql
     *
     * @param sqlCallParameter
     * @return
     */
    @Override
    public OperationResult selectSql(SqlCallParameter sqlCallParameter) {
        OperationResult result = new OperationResult();
        result.setSuccess(true);
        try {
            logger.debug("selectSql[{}]", JSON.toJSONString(sqlCallParameter));
            Validate.notEmpty(sqlCallParameter, "sql", "parameters");
            jdbcTemplate = fetchJdbcTemplate();
            List<Map<String, Object>> mapList = jdbcTemplate.query(sqlCallParameter.getSql(), sqlCallParameter.getParameters().toArray(), new ColumnMapRowMapper());
            result.setResult(JSON.toJSONString(mapList));

        } catch (Exception ex) {
            logger.error("selectSql:", ex);
            if (ex instanceof BusinessException) {
                result.setCode(((BusinessException) ex).getCode());
            }
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    /**
     * 执行更新sql
     *
     * @param sqlCallParameter
     * @return
     */
    @Override
    public OperationResult updateSql(SqlCallParameter sqlCallParameter) {
        OperationResult result = new OperationResult();
        result.setSuccess(true);
        try {
            logger.debug("updateSql[{}]", JSON.toJSONString(sqlCallParameter));
            Validate.notEmpty(sqlCallParameter, "sql", "parameters");
            jdbcTemplate = fetchJdbcTemplate();
            int r = jdbcTemplate.update(sqlCallParameter.getSql(), sqlCallParameter.getParameters());
            result.setResult(String.valueOf(r));
        } catch (Exception ex) {
            logger.error("updateSql:", ex);
            if (ex instanceof BusinessException) {
                result.setCode(((BusinessException) ex).getCode());
            }
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    /**
     * 执行删除sql
     *
     * @param sqlCallParameter
     * @return
     */
    @Override
    public OperationResult deleteSql(SqlCallParameter sqlCallParameter) {
        OperationResult result = new OperationResult();
        try {
            logger.debug("deleteSql[{}]", JSON.toJSONString(sqlCallParameter));
            Validate.notEmpty(sqlCallParameter, "sql", "parameters");
            jdbcTemplate = fetchJdbcTemplate();
            int r = jdbcTemplate.update(sqlCallParameter.getSql(), sqlCallParameter.getParameters());
            result.setResult(String.valueOf(r));
        } catch (Exception ex) {
            logger.error("deleteSql:", ex);
            if (ex instanceof BusinessException) {
                result.setCode(((BusinessException) ex).getCode());
            }
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    /**
     * 批量执行更新sql
     *
     * @param parameterList
     * @return
     */
    @Override
    public OperationResult batchOperationSql(List<SqlCallParameter> parameterList) {
        OperationResult result = new OperationResult();
        result.setSuccess(true);
        try {
            logger.debug("batchOperationSql[{}]", JSON.toJSONString(parameterList));
            if (parameterList == null) {
                throw new BusinessException(ExceptionEnum.PARAMETER_NULL_EXCEPTION);
            }
            jdbcTemplate = fetchJdbcTemplate();
            for (SqlCallParameter sqlCallParameter : parameterList) {
                jdbcTemplate.update(sqlCallParameter.getSql(), sqlCallParameter.getParameters());
            }
            result.setResult("");
        } catch (Exception ex) {
            logger.error("batchOperationSql:", ex);
            if (ex instanceof BusinessException) {
                result.setCode(((BusinessException) ex).getCode());
            }
            result.setSuccess(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    /**
     * 获取jdbcTemplate
     *
     * @return
     */
    public JdbcTemplate fetchJdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = SpringUtils.getBean(JdbcTemplate.class);
        }
        return jdbcTemplate;
    }

}
