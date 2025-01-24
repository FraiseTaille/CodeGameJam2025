package fr.bloomyindev.cgj2024.CoordinateSystems;

public class AbsoluteCoords3D {
	private float x, y, z;

	public AbsoluteCoords3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float[] getCoords() {
		return new float[]{this.x, this.y, this.z};
	}
}
