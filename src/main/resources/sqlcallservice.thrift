include "header.thrift"
namespace java com.sxs.server.thrift.service
namespace php com.sxs.server.thrift.service

/**
* 操作结果定义
**/
struct OperationResult {
    1: bool success;
    2:string message;
    3:i32 code;
    4:string result;
}
/**
* sql调用参数定义
**/
struct SqlCallParameter{
    1: header.BaseHeader header;
    2:required string sql;
    3:required list<string> parameters;
    4:optional bool isNamed;
    5:optional map<string,string>parametersMap;
}

 /**
 * sql调用服务定义
**/
service SqlCallService {

     OperationResult insertSql(1:SqlCallParameter sqlCallParameter);

     OperationResult selectSql(1:SqlCallParameter sqlCallParameter);

     OperationResult updateSql(1:SqlCallParameter sqlCallParameter);

     OperationResult deleteSql(1:SqlCallParameter sqlCallParameter);

     OperationResult batchOperationSql(1:list<SqlCallParameter> parameterList);
}