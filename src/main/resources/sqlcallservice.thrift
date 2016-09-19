namespace java com.sxs.thrift.service
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
    1:required string sql;
    2:required list<string> parameters;
    3:optional bool isNamed;
    4:optional map<string,string>parametersMap;
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