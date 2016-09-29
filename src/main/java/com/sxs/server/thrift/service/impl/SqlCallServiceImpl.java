package com.sxs.server.thrift.service.impl;

import com.alibaba.fastjson.JSON;
import com.sxs.server.annotation.ThriftService;
import com.sxs.server.exception.BusinessException;
import com.sxs.server.exception.ExceptionEnum;
import com.sxs.server.thrift.gen.OperationResult;
import com.sxs.server.thrift.gen.SqlCallParameter;
import com.sxs.server.thrift.service.SqlCallService;
import com.sxs.server.utils.LogHandleUtils;
import com.sxs.server.utils.SpringUtils;
import com.sxs.server.utils.Validate;
import org.apache.commons.lang3.StringUtils;
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
import java.sql.Statement;
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
        String parameterLogString = LogHandleUtils.handleLog(sqlCallParameter);
        OperationResult result = new OperationResult();
        result.setSuccess(true);
        try {
            logger.info("insertSql ## {}", parameterLogString);
            Validate.notEmpty(sqlCallParameter, "sql", "parameters");
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate = fetchJdbcTemplate();
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection conn)
                        throws SQLException {
                    String[] params = sqlCallParameter.getParameters().toArray(new String[]{});
                    PreparedStatement ps = conn.prepareStatement(sqlCallParameter.getSql(), Statement.RETURN_GENERATED_KEYS);
                    if (params != null && params.length > 0) {
                        for (int i = 0; i < params.length; i++) {
                            if (StringUtils.isNotBlank(params[i])) {
                                ps.setObject(i + 1, params[i]);
                            }
                        }
                    }
                    return ps;
                }
            }, keyHolder);
            result.setResult(keyHolder.getKey().toString());
            logger.info("插入成功:{}", LogHandleUtils.handleLog(result));
        } catch (Exception ex) {
            logger.error("insertSql:{}", parameterLogString, ex);
            if (ex instanceof BusinessException) {
                result.setCode(((BusinessException) ex).getCode());
            }
            result.setSuccess(false);
            result.setMessage("插入失败:" + parameterLogString);

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
        String parameterLogString = LogHandleUtils.handleLog(sqlCallParameter);
        OperationResult result = new OperationResult();
        result.setSuccess(true);
        try {
            logger.info("selectSql ## {}", parameterLogString);
            Validate.notEmpty(sqlCallParameter, "sql", "parameters");
            jdbcTemplate = fetchJdbcTemplate();
            List<Map<String, Object>> mapList = jdbcTemplate.query(sqlCallParameter.getSql(), sqlCallParameter.getParameters().toArray(), new ColumnMapRowMapper());
            result.setResult(JSON.toJSONString(mapList));
            logger.info("查询成功:{}", LogHandleUtils.handleLog(result));
        } catch (Exception ex) {
            logger.error("selectSql:{}", parameterLogString, ex);
            if (ex instanceof BusinessException) {
                result.setCode(((BusinessException) ex).getCode());
            }
            result.setSuccess(false);
            result.setMessage("查询失败:" + parameterLogString);

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
        String parameterLogString = LogHandleUtils.handleLog(sqlCallParameter);
        OperationResult result = new OperationResult();
        result.setSuccess(true);
        try {
            logger.info("updateSql ## {}", parameterLogString);
            Validate.notEmpty(sqlCallParameter, "sql", "parameters");
            jdbcTemplate = fetchJdbcTemplate();
            int r = jdbcTemplate.update(sqlCallParameter.getSql(), sqlCallParameter.getParameters().toArray());
            result.setResult(String.valueOf(r));
            logger.info("更新成功:{}", LogHandleUtils.handleLog(result));
        } catch (Exception ex) {
            logger.error("updateSql:{}", parameterLogString, ex);
            if (ex instanceof BusinessException) {
                result.setCode(((BusinessException) ex).getCode());
            }
            result.setSuccess(false);
            result.setMessage("更新失败:" + parameterLogString);

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
        String parameterLogString = LogHandleUtils.handleLog(sqlCallParameter);
        OperationResult result = new OperationResult();
        result.setSuccess(true);
        try {
            logger.info("deleteSql ## {}", parameterLogString);
            Validate.notEmpty(sqlCallParameter, "sql", "parameters");
            jdbcTemplate = fetchJdbcTemplate();
            int r = jdbcTemplate.update(sqlCallParameter.getSql(), sqlCallParameter.getParameters().toArray());
            result.setResult(String.valueOf(r));
            logger.info("删除成功:{}", LogHandleUtils.handleLog(result));
        } catch (Exception ex) {
            logger.error("deleteSql:{}", parameterLogString, ex);
            if (ex instanceof BusinessException) {
                result.setCode(((BusinessException) ex).getCode());
            }
            result.setSuccess(false);
            result.setMessage("删除失败:" + parameterLogString);
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
        String parameterLogString = LogHandleUtils.handleLog(parameterList);
        OperationResult result = new OperationResult();
        result.setSuccess(true);
        try {
            logger.info("batchOperationSql ## {}", parameterLogString);
            if (parameterList == null) {
                throw new BusinessException(ExceptionEnum.PARAMETER_NULL_EXCEPTION);
            }
            jdbcTemplate = fetchJdbcTemplate();
            for (SqlCallParameter sqlCallParameter : parameterList) {
                jdbcTemplate.update(sqlCallParameter.getSql(), sqlCallParameter.getParameters());
            }
            result.setResult("");
            logger.info("批执行成功:{}", parameterLogString);
        } catch (Exception ex) {
            logger.error("batchOperationSql:{}", parameterLogString, ex);
            if (ex instanceof BusinessException) {
                result.setCode(((BusinessException) ex).getCode());
            }
            result.setSuccess(false);
            result.setMessage("批执行失败:" + parameterLogString);

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
