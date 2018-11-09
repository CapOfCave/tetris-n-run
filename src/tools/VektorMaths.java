package tools;

public class VektorMaths {

	public static double getLength(Vector v) {
		return Math.sqrt(v.x * v.x + v.y * v.y);
	}
	
	public static double dotP(Vector v1, Vector v2) {
		return (v1.x * v2.x + v1.y * v2.y);
	}
	
	public static double getAngleDeg(Vector v1, Vector v2) {
		return dotP(v1, v2) / (getLength(v1) * getLength(v2));
	}

}
