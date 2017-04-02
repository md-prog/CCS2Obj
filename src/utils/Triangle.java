package utils;

public class Triangle {
  public Vector3f v1;
  public Vector3f v2;
  public Vector3f v3;
  private Vector3f centroid;
  private Vector3f normal;
  
  public Triangle(Triangle other) { this(other.v1, other.v2, other.v3); }
  
  public Triangle(Vector3f v1, Vector3f v2, Vector3f v3) {
    this.v1 = new Vector3f(v1);
    this.v2 = new Vector3f(v2);
    this.v3 = new Vector3f(v3);
    normal = v2.sub(v1).cross(v3.sub(v1)).normalize();
    centroid = v1.add(v2).add(v3).div(3.0F);
  }
  
  public Vector3f getNormal() { return normal; }
  
  public Vector3f getCentroid() {
    return centroid;
  }
  
  public boolean contains(Vector3f v) { return (v.equals(v1)) || (v.equals(v2)) || (v.equals(v3)); }
  
  public String toString() {
    return 
    
      "f " + v1.getID() + "/" + v1.getID() + " " + v2.getID() + "/" + v2.getID() + " " + v3.getID() + "/" + v3.getID();
  }
  
  public boolean equals(Triangle other) { return normal.equals(other.getNormal()); }
}
