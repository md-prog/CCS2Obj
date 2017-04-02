package filetypes.model;

import com.binaryfilereader.BinaryReader;

public class CLT
{
  private byte[] data;
  private java.awt.Color[] palette;
  
  public CLT(byte[] data) {
    BinaryReader br = utils.Reader.createReader(data);
    

    br.skip(20);
    palette = new java.awt.Color[(data.length - br.getOffset()) / 4];
    
    for (int i = 0; i < palette.length; i++) {
      int[] c = { br.readByte(), br.readByte(), br.readByte(), br.readByte() };
      
      if (c[3] <= 128) {
        c[3] = (c[3] * 255 / 128);
      }
      palette[i] = new java.awt.Color(c[0], c[1], c[2], c[3]);
    }
  }
  
  public java.awt.Color[] getPalette() {
    return palette;
  }
}
