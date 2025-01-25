package fr.bloomyindev.cgj2024.CoordinateSystems;

import fr.bloomyindev.cgj2024.Ut;

import java.util.ArrayList;

public class SpaceshipRelative {
	private AbsoluteCoords3D coords3d;
	private float lat, lng;
	private float xRel, yRel, zRel;
	private long distance;

	public SpaceshipRelative(AbsoluteCoords3D coords3d, AbsoluteCoords3D spaceshipCoords3d) {
		this.coords3d = coords3d;
		this.reComputeRelativeCoords(spaceshipCoords3d);
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

		this.distance = (long)Math.sqrt(Ut.pow(xRel, 2.0f) + Ut.pow(yRel, 2.0f) + Ut.pow(zRel, 2.0f)); // Distance de l'étoile au vaisseau

		if (distance != 0) {
			this.lat = (float)Math.asin(zRel / (float)distance);
			this.lng = (float)Math.atan2(yRel / (float)distance, xRel / (float)distance);
		} else {
			this.lat = 0;
			this.lng = 0;
		}
	}

    public static int smallestDistanceId(ArrayList<SpaceshipRelative> spaceshipRelativeToStars) {
        int i = 0;
        for (int j = 0; j < spaceshipRelativeToStars.size(); j++) {
            if (spaceshipRelativeToStars.get(j).getDistance() < spaceshipRelativeToStars.get(i).getDistance()) {
                i = j;
            }
        }
        return i;
    }
}
