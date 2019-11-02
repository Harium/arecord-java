package examples;

import com.harium.hci.arecord.ARecord;

public class NumbersExample {

  public static void main(String[] args) {
    ARecord record = new ARecord();
    record.listener((buffer, offset, size) -> {
      for (int i = offset; i < size; i++) {
        System.out.print((int) buffer[i] + " ");
      }
      System.out.println("");
    });
    record.start();
  }

}
