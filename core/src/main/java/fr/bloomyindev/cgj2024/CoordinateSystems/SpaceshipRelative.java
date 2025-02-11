package fr.bloomyindev.cgj2024.CoordinateSystems;

import fr.bloomyindev.cgj2024.Stars.Star;
import fr.bloomyindev.cgj2024.Ut;

import java.util.ArrayList;

public class SpaceshipRelative {
	private AbsoluteCoords3D coords3d;
	private float lat, lng;
	private float xRel, yRel, zRel;
	private long distance;
    private long previousDistance;
    private long smallestDistance;

	public SpaceshipRelative(AbsoluteCoords3D coords3d, AbsoluteCoords3D spaceshipCoords3d) {
		this.coords3d = coords3d;
		this.reComputeRelativeCoords(spaceshipCoords3d);
        smallestDistance = 1000000;
        previousDistance = 1000000;
	}

    public SpaceshipRelative(SpaceshipRelative spaceshipRelative) {
        this.coords3d = new AbsoluteCoords3D(spaceshipRelative.coords3d);
        this.lat = spaceshipRelative.lat;
        this.lng = spaceshipRelative.lng;
        this.xRel = spaceshipRelative.xRel;
        this.yRel = spaceshipRelative.yRel;
        this.zRel = spaceshipRelative.zRel;
        this.distance = spaceshipRelative.distance;
        this.smallestDistance = spaceshipRelative.smallestDistance;
    }

	public long getDistance() {
		return this.distance;
	}

	public float[] getRelativeCartesian() {
		return new float[]{this.xRel, this.yRel, this.zRel};
	}

	public float getRelX() {
		return this.xRel;
	}

	public float getRelY() {
		return this.yRel;
	}

	public float getRelZ() {
		return this.zRel;
	}

	public float[] getLatLong() {
		return new float[]{this.lat, this.lng};
	}

	public void reComputeRelativeCoords(AbsoluteCoords3D spaceshipCoords3d) {
		float[] Delta3D = spaceshipCoords3d.getDelta(this.coords3d);

		this.xRel = Delta3D[0];
		this.yRel = Delta3D[1];
		this.zRel = Delta3D[2];

        previousDistance = distance;

		this.distance = (long)Math.sqrt(Ut.pow(xRel, 2.0f) + Ut.pow(yRel, 2.0f) + Ut.pow(zRel, 2.0f)); // Distance de l'étoile au vaisseau

		if (distance != 0) {
			this.lat = (float)Math.asin(zRel / (float)distance);
			this.lng = (float)Math.atan2(yRel / (float)distance, xRel / (float)distance);
		} else {
			this.lat = 0;
			this.lng = 0;
		}
	}

    public float getLng() {
        return this.lng;
    }

    public void updateSmallestDistance() {
        if (distance < smallestDistance) {
            smallestDistance = distance;
        }
    }

    public long getPreviousDistance() {
        return previousDistance;
    }

    public long getSmallestDistance() {
        return smallestDistance;
    }

    public static long getDeltaDistance(long distance1, long distance2) {
        return distance1 - distance2;
    }
}
