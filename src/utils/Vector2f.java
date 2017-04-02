package utils;

public class Vector2f implements Comparable<Vector2f> {
	private int id;
	public float x;
	public float y;

	public Vector2f() {
	}

	public Vector2f(Vector2f v) {
		this(v.x, v.y);
		id = v.getID();
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f(byte[] data, int id) {
		x = ((data[0] & 0xFF) / 256.0F + data[1]);
		y = ((data[2] & 0xFF) / 256.0F + data[3]);

		this.id = id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public Vector2f add(Vector2f other) {
		Vector2f result = new Vector2f(this);
		result.x += other.x;
		result.y += other.y;
		return result;
	}

	public Vector2f sub(Vector2f other) {
		Vector2f result = new Vector2f(this);
		result.x -= other.x;
		result.y -= other.y;
		return result;
	}

	public Vector2f mul(float scalar) {
		Vector2f result = new Vector2f(this);
		result.x *= scalar;
		result.y *= scalar;
		return result;
	}

	public Vector2f div(float factor) {
		return mul(1.0F / factor);
	}

	public float dot(Vector2f other) {
		return x * x + y * y;
	}

	public Vector2f normalize() {
		Vector2f result = new Vector2f();
		float length = length();
		x /= length;
		y /= length;
		return result;
	}

	public void negate() {
		x = (-x);
		y = (-y);
	}

	public float length() {
		return (float) Math.abs(Math.sqrt(x * x + y * y));
	}

	public boolean equals(Vector2f other) {
		return (x == x) && (y == y);
	}

	public String toString() {
		return "vt " + x + " " + y;
	}

	public int compareTo(Vector2f v) {
		return (int) (length() - v.length());
	}
}
