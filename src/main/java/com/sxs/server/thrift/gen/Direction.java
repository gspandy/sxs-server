/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.sxs.server.thrift.gen;


public enum Direction implements org.apache.thrift.TEnum {
  REQ(1),
  RESP(2),
  NOTIFY(3);

  private final int value;

  private Direction(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static Direction findByValue(int value) { 
    switch (value) {
      case 1:
        return REQ;
      case 2:
        return RESP;
      case 3:
        return NOTIFY;
      default:
        return null;
    }
  }
}
