package com.binaryfilereader;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class BinaryReader
{
  private byte[] contents;
  private int index = 0;
  
  public BinaryReader(String path) throws java.io.FileNotFoundException {
    this(new FileInputStream(path));
  }
  
  public BinaryReader(File f) throws java.io.FileNotFoundException {
    this(new FileInputStream(f.getAbsolutePath()));
  }
  
  public BinaryReader(java.io.InputStream is) {
    try {
      DataInputStream dis = new DataInputStream(is);
      contents = new byte[dis.available()];
      dis.readFully(contents);
      dis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public BinaryReader(GZIPInputStream gzipFile) {
    try { ByteArrayOutputStream byteArray = new ByteArrayOutputStream(32768);
      byte[] buf = new byte[1024];
      int len;
      while ((len = gzipFile.read(buf)) > 0) {
        byteArray.write(buf, 0, len);
      }
      contents = byteArray.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public BinaryReader(byte[] contents) { this.contents = contents; }
  


  public void skip(int count) { index += count; }
  
  public int readByte() {
    int b1 = contents[index] & 0xFF;
    index += 1;
    return b1;
  }
  
  public byte[] readBytes(int count) { byte[] bytes = new byte[count];
    for (int i = 0; i < count; i++) {
      bytes[i] = contents[(index++)];
    }
    return bytes;
  }
  
  public int readShort() { int s1 = contents[index] & 0xFF;
    int s2 = (contents[(index + 1)] & 0xFF) << 8;
    index += 2;
    return s1 | s2;
  }
  
  public int readInt() { int i1 = contents[index] & 0xFF;
    int i2 = (contents[(index + 1)] & 0xFF) << 8;
    int i3 = (contents[(index + 2)] & 0xFF) << 16;
    int i4 = (contents[(index + 3)] & 0xFF) << 24;
    index += 4;
    return i1 | i2 | i3 | i4;
  }
  
  public long readUInt() { long i1 = contents[index] & 0xFF;
    long i2 = (contents[(index + 1)] & 0xFF) << 8;
    long i3 = (contents[(index + 2)] & 0xFF) << 16;
    long i4 = (contents[(index + 3)] & 0xFF) << 24;
    index += 4;
    return i1 | i2 | i3 | i4;
  }
  
  public long readLong() { long i1 = contents[index] & 0xFF;
    long i2 = (contents[(index + 1)] & 0xFF) << 8;
    long i3 = (contents[(index + 2)] & 0xFF) << 16;
    long i4 = (contents[(index + 3)] & 0xFF) << 24;
    long i5 = (contents[(index + 4)] & 0xFF) << 32;
    long i6 = (contents[(index + 5)] & 0xFF) << 40;
    long i7 = (contents[(index + 6)] & 0xFF) << 48;
    long i8 = (contents[(index + 7)] & 0xFF) << 56;
    index += 8;
    return i1 | i2 | i3 | i4 | i5 | i6 | i7 | i8;
  }
  
  public float readFloat() { return Float.intBitsToFloat(readInt()); }
  
  public double readDouble() {
    return Double.longBitsToDouble(readLong());
  }
  
  public String readUnicodeString() {
    String s = "";
    char c;
    while ((c = (char)readShort()) != 0) {
      s = s + c;
    }
    return s;
  }
  
  public String readString(int size) { for (int i = index; i < index + size; i++) {
      if (contents[i] == 0) {
        for (int j = index; j < i; j++) {
          if (contents[j] < 0) contents[j] = 95;
        }
        String s = new String(contents, index, i - index);
        index += size;
        return s;
      }
    }
    String s = new String(contents, index, size);
    index += size;
    return s;
  }
  
  public void setContents(byte[] contents) { this.contents = contents;
    index = 0;
  }
  
  public void setOffset(int offset) { index = offset; }
  
  public int getOffset() {
    return index;
  }
  
  public int getSize() { return contents.length; }
  
  public boolean isEOF() {
    return index >= contents.length;
  }
}
