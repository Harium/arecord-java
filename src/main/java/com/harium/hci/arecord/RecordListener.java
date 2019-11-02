package com.harium.hci.arecord;

public interface RecordListener {

  void listen(byte[] buffer, int offset, int size);

}
