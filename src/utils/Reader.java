package utils;

import com.binaryfilereader.BinaryReader;

public class Reader {
  private static BinaryReader br;
  
  private Reader() {}
  
  public static BinaryReader createReader(byte[] data) { if (br == null) {
      br = new BinaryReader(data);
    }
    br.setContents(data);
    return br;
  }
}
