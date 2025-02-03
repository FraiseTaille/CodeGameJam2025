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

		DeltaX = - otherCoords[0] + this.x;
		DeltaY = - otherCoords[1] + this.y;
		DeltaZ = - otherCoords[2] + this.z;

		return new float[]{DeltaX, DeltaY, DeltaZ};
	}

	public void move(float speedX, float speedY, float speedZ) {
		this.z += speedZ;
        if (!(this.x > 50000 && Math.abs(speedX) > 0) && !(this.x < -50000 && Math.abs(speedX) > 0)) {
            this.x += speedX;
        } else if (this.x > 50000) {
            this.x = 50000;
        } else if (this.x < -50000) {
            this.x = -50000;
        }
        if (!(this.y > 50000 && Math.abs(speedY) > 0) && !(this.y < -50000 && Math.abs(speedY) > 0)) {
            this.y += speedY;
        } else if (this.y > 50000) {
            this.y = 50000;
        } else if (this.y < -50000) {
            this.y = -50000;
        }
	}

	public void setToOrigin() {
		this.x = 0.f;
		this.y = 0.f;
		this.z = 0.f;
	}
}
