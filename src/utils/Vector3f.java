package utils;

public class Vector3f implements Comparable<Vector3f> {
  public static final Vector3f UNIT_Z = new Vector3f(0.0F, 0.0F, 1.0F);
  private int id;
  public float x;
  public float y;
  public float z;
  
  public Vector3f() {}
  
  public Vector3f(Vector3f v) {
    this(v.x, v.y, v.z);
    id = v.getID();
  }
  
  public Vector3f(float x, float y, float z) { this.x = x;
    this.y = y;
    this.z = z;
  }
  

  public Vector3f(byte[] data, int id)
  {
    x = ((data[0] & 0xFF) / 256.0F + data[1]);
    y = ((data[2] & 0xFF) / 256.0F + data[3]);
    z = ((data[4] & 0xFF) / 256.0F + data[5]);
    
    this.id = id;
  }
  

  public void setID(int id)
  {
    this.id = id;
  }
  
  public int getID() { return id; }
  
  public Vector3f add(Vector3f other) {
    Vector3f result = new Vector3f();
    x += x;
    y += y;
    z += z;
    return result;
  }
  
  public Vector3f sub(Vector3f other) { Vector3f result = new Vector3f();
    x -= x;
    y -= y;
    z -= z;
    return result;
  }
  
  public Vector3f mul(float scalar) { Vector3f result = new Vector3f();
    x *= scalar;
    y *= scalar;
    z *= scalar;
    return result;
  }
  
  public Vector3f div(float factor) { return mul(1.0F / factor); }
  
  public Vector3f cross(Vector3f other) {
    Vector3f result = new Vector3f();
    x = (y * z - z * y);
    y = (z * x - x * z);
    z = (x * y - y * x);
    return result;
  }
  
  public float dot(Vector3f other) { return x * x + y * y + z * z; }
  
  public Vector3f normalize() {
    Vector3f result = new Vector3f();
    float length = length();
    x /= length;
    y /= length;
    z /= length;
    return result;
  }
  
  public void negate() { x = (-x);
    y = (-y);
    z = (-z);
  }
  
  public float length() { return (float)Math.abs(Math.sqrt(x * x + y * y + z * z)); }
  
  public float length2D() {
    return (float)Math.abs(Math.sqrt(x * x + z * z));
  }
  
  public boolean equals(Vector3f other) { return (x == x) && (y == y) && (z == z); }
  
  public String toString() {
    return "v " + x + " " + y + " " + z;
  }
  
  public int compareTo(Vector3f v) { return (int)(length() - v.length()); }
}
