package com.harium.hci.arecord;

public class OutputRecordListener implements RecordListener {

  @Override
  public void listen(byte[] buffer, int offset, int size) {
    System.out.println(new String(buffer, offset, size));
  }
}
