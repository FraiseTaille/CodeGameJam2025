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

	public float[] getDelta(AbsoluteCoords3D other) {
		float DeltaX, DeltaY, DeltaZ;
		float[] otherCoords = other.getCoords();

		DeltaX = otherCoords[0] - this.x;
		DeltaY = otherCoords[1] - this.y;
		DeltaZ = otherCoords[0] - this.z;

		return new float[]{DeltaX, DeltaY, DeltaZ};
	}
}
