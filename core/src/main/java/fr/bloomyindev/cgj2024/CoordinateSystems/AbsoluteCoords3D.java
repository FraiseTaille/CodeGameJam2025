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

    public float[] getZone(float radius) {
        return new float[]{x - radius, x + radius, y - radius, y + radius};
    }

    public boolean identicalZones(float[] otherZone) {
        if (otherZone.length != 4) {
            return false;
        } else {
            boolean identical = true;
            int i = 0;
            while (i < getZone(getZoneRadius(otherZone)).length && identical) {
                if (getZone(getZoneRadius(otherZone))[i] != otherZone[i]) {
                    identical = false;
                }
                i++;
            }
            return identical;
        }
    }

    public float getZoneRadius(float[] zone) {
        return (zone[0] - zone[1]) / 2;
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
