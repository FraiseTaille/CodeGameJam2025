package fr.bloomyindev.cgj2024.CoordinateSystems;

import fr.bloomyindev.cgj2024.Ut;

public class AbsoluteCoords3D {
	private float x, y, z;

	public AbsoluteCoords3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

    public AbsoluteCoords3D(AbsoluteCoords3D coords3D) {
        this.x = coords3D.x;
        this.y = coords3D.y;
        this.z = coords3D.z;
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
        if (!(this.x > 200000 && Math.abs(speedX) > 0) && !(this.x < -200000 && Math.abs(speedX) > 0)) {
            this.x += speedX;
        } else if (this.x > 200000) {
            this.x = 200000;
        } else if (this.x < -200000) {
            this.x = -200000;
        }
        if (!(this.y > 200000 && Math.abs(speedY) > 0) && !(this.y < -200000 && Math.abs(speedY) > 0)) {
            this.y += speedY;
        } else if (this.y > 200000) {
            this.y = 200000;
        } else if (this.y < -200000) {
            this.y = -200000;
        }
	}

	public void setToOrigin() {
		this.x = 0.f;
		this.y = 0.f;
		this.z = 0.f;
	}

    public float[] get20kX20kZone() {
        return new float[]{x - 10000, x + 10000, y - 10000, y + 10000};
    }

    public boolean identicalZones(float[] other20kX20kZone) {
        if (other20kX20kZone.length != 4) {
            return false;
        } else {
            boolean identical = true;
            int i = 0;
            while (i < get20kX20kZone().length && identical) {
                if (get20kX20kZone()[i] != other20kX20kZone[i]) {
                    identical = false;
                }
                i++;
            }
            return identical;
        }
    }

    public boolean isInZone(float[] zone) {
        return x >= zone[0] && x <= zone[1] && y >= zone[2] && y <= zone[3];
    }

    public static AbsoluteCoords3D generateNewRandomCoordinates(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        float x = Ut.randomMinMax(minX, maxX);
        float y = Ut.randomMinMax(minY, maxY);
        float z = Ut.randomMinMax(minZ, maxZ);
        return new AbsoluteCoords3D(x, y, z);
    }
}
