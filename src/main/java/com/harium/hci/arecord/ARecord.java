package com.harium.hci.arecord;

import java.io.IOException;
import java.io.InputStream;

/**
 * Routine to read system audio line
 */
public class ARecord {

  private static final String COMMAND_ARECORD = "arecord";

  private int bufferSize = 4096;

  private int channels = 1;
  private int sampleRate = 4000;
  private String device = "plughw:0";
  private String sampleFormat = "U8";
  private String outputFormat = "raw";

  private RecordListener listener = new OutputRecordListener();

  public ARecord() {
    super();
  }

  public ARecord bufferSize(int bufferSize) {
    this.bufferSize = bufferSize;
    return this;
  }

  public ARecord channels(int channels) {
    this.channels = channels;
    return this;
  }

  public ARecord sampleRate(int sampleRate) {
    this.sampleRate = sampleRate;
    return this;
  }

  public ARecord listener(RecordListener listener) {
    this.listener = listener;
    return this;
  }

  public void start() {
    execute(COMMAND_ARECORD,
        "-D", device,
        "-f", sampleFormat,
        "-c", Integer.toString(channels),
        "-r", Integer.toString(sampleRate),
        "-t", outputFormat,
        "-q", "-");
  }

  private void execute(final String... command) {
    String threadName = "arecord";

    new Thread(() -> {
      ProcessBuilder b = new ProcessBuilder(command);
      b.redirectErrorStream(true);
      try {
        Process process = b.start();
        InputStream out = process.getInputStream();

        byte[] buffer = new byte[bufferSize];
        do {
          int no = out.available();
          if (no > 0) {
            int n = out.read(buffer, 0, Math.min(no, buffer.length));
            listener.listen(buffer, 0, n);
          }
        } while ((isAlive(process)));

        process.destroy();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }, threadName).start();
  }

  public static boolean isAlive(Process p) {
    try {
      p.exitValue();
      return false;
    } catch (IllegalThreadStateException e) {
      return true;
    }
  }

}
