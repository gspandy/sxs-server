namespace java com.sxs.server.thrift.service
namespace php com.sxs.server.thrift.service

enum Direction {
  REQ = 1,
  RESP = 2,
  NOTIFY = 3
}

struct BaseHeader {
  1: string version;
  2: Direction direction;
  3:string logId;
  4:string ip;
  5:i32 port;
}