package filetypes.model;

import com.binaryfilereader.BinaryReader;
import java.io.IOException;
import utils.Reader;
import utils.Triangle;
import utils.Vector2f;
import utils.Vector3f;

public class MDL
{
  private byte[] data;
  private Vector3f[] vertex;
  private Vector2f[] texCoords;
  private Triangle[] tri;
  private int triCount;
  private int triIndex;
  public boolean successful;
  
  public MDL(byte[] data) throws Exception
  {
    BinaryReader br = Reader.createReader(data);
    br.skip(16);
    int indexLength = br.readInt();
    if (br.readInt() == Integer.MIN_VALUE) {
      return;
    }
    br.skip(12);
    int vertexCount = br.readInt();
    vertex = new Vector3f[vertexCount];
    int[] in = new int[vertex.length];
    for (int i = 0; i < vertex.length; i++) {
      vertex[i] = new Vector3f(br.readBytes(6), i + 1);
    }
    br.skip(vertex.length * 6 % 4);
    
    for (int i = 0; i < vertex.length; i++) {
      br.skip(3);
      in[i] = br.readByte();
      if (in[i] == 0) {
        triCount += 1;
      }
    }
    tri = new Triangle[triCount];
    
    boolean started = false;
    
    int i = 0; for (int size = 0; i < in.length; i++) {
      if ((started) && (in[i] == 0)) {
        size++;
      } else if ((started) && (in[i] != 0)) {
        started = false;
        
        resolveTriStrip(i - size, size, in[(i - size)]);
        size = 0;
      }
      if ((in[i] != 0) && (!started)) {
        started = true;
        size += 2;
        i++;
      }
      if (i == in.length - 1) {
        resolveTriStrip(i - size + 1, size, in[(i - size + 1)]);
      }
    }
    
    br.skip(vertex.length * 4);
    texCoords = new Vector2f[vertex.length];
    for (i = 0; i < texCoords.length; i++) {
      texCoords[i] = new Vector2f(br.readBytes(4), i + 1);
    }
    successful = true;
  }
  
  public Vector3f[] getVertices() {
    return vertex;
  }
  
  public Triangle[] getTriangles() {
    return tri;
  }
  
  public Vector2f[] getTexCoords() {
    return texCoords;
  }
  
  private void resolveTriStrip(int start, int size, int type) throws IOException
  {
    for (int i = 0; i < size - 2; i++) {
      switch (type) {
      case 1: 
        if (i % 2 == 1) {
          tri[(triIndex++)] = new Triangle(vertex[(start + i + 1)], vertex[(start + i)], vertex[(start + i + 2)]);
        } else {
          tri[(triIndex++)] = new Triangle(vertex[(start + i)], vertex[(start + i + 1)], vertex[(start + i + 2)]);
        }
        break;
      case 2: 
        if (i % 2 == 1) {
          tri[(triIndex++)] = new Triangle(vertex[(start + i)], vertex[(start + i + 1)], vertex[(start + i + 2)]);
        } else {
          tri[(triIndex++)] = new Triangle(vertex[(start + i + 1)], vertex[(start + i)], vertex[(start + i + 2)]);
        }
        break;
      default: 
        System.out.println("unexpected type " + type);
        throw new IOException("tris could not be created");
      }
    }
  }
  
  public String exportOBJ() {
    String file = "# Created with CCS OBJ Extractor application\r\n\r\n";
    file = file + "g Object\r\n\r\n";
    file = file + "# " + vertex.length + " vertices\r\n\r\n";
    for (int i = 0; i < vertex.length; i++) {
      file = file + vertex[i] + "\r\n";
    }
    file = file + "\r\n";
    for (int i = 0; i < vertex.length; i++) {
      file = file + texCoords[i] + "\r\n";
    }
    file = file + "\r\n# " + tri.length + " faces\r\n\r\n";
    for (int i = 0; i < tri.length; i++) {
      file = file + tri[i] + "\r\n";
    }

    return file;
  }
}
