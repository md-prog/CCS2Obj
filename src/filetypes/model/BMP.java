package filetypes.model;

import com.binaryfilereader.BinaryReader;

public class BMP
{
  private java.awt.Color[][] palettes;
  private java.awt.image.BufferedImage[] images;
  private int[] imageData;
  private int width;
  private int height;
  
  public BMP(byte[] data, java.util.ArrayList<CLT> clt)
  {
    BinaryReader br = utils.Reader.createReader(data);
    palettes = new java.awt.Color[clt.size()][];
    for (int i = 0; i < clt.size(); i++) {
      palettes[i] = ((CLT)clt.get(i)).getPalette();
    }
    br.skip(16);
    width = ((int)Math.pow(2.0D, br.readByte()));
    height = ((int)Math.pow(2.0D, br.readByte()));
    imageData = new int[width * height];
    br.skip(10);
    if (imageData.length == (data.length - br.getOffset()) * 2) {
      int index = -1;
      for (int i = 0; i < imageData.length; i++) {
        index = br.readByte();
        imageData[i] = (index % 16);
        imageData[(++i)] = (index / 16);
      }
    } else if (imageData.length == data.length - br.getOffset()) {
      for (int i = 0; i < imageData.length; i++) {
        imageData[i] = br.readByte();
      }
    } else {
      System.out.println("Unhandled palette type");
    }
    images = new java.awt.image.BufferedImage[palettes.length];
    for (int i = 0; i < images.length; i++) {
      images[i] = new java.awt.image.BufferedImage(width, height, 2);
      int j = height - 1; for (int l = 0; j > -1; j--) {
        for (int k = 0; k < width; k++) {
          int rgb = palettes[i][imageData[(l++)]].getRGB();
          images[i].setRGB(k, j, rgb);
        }
      }
    }
  }
  
  public java.awt.image.BufferedImage[] getImages() {
    return images;
  }
}
