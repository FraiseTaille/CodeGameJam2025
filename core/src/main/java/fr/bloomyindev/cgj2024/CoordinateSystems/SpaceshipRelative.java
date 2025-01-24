package fr.bloomyindev.cgj2024.CoordinateSystems;

import fr.bloomyindev.cgj2024.Ut;

public class SpaceshipRelative {
	private float lat, lng;
	private float xRel, yRel, zRel;
	private long distance;

	public SpaceshipRelative(AbsoluteCoords3D coords3d, AbsoluteCoords3D spaceshipCoords3d) {
		float[] Delta3D = spaceshipCoords3d.getDelta(coords3d);

		this.xRel = Delta3D[0];
		this.yRel = Delta3D[1];
		this.zRel = Delta3D[2];

		this.distance = (int)Math.sqrt(Ut.pow(xRel, 2.0f) + Ut.pow(yRel, 2.0f) + Ut.pow(zRel, 2.0f)); // Distance de l'étoile au vaisseau

		this.lat = (long)Math.asin(zRel / (float)distance);
		this.lng = (long)Math.atan2(xRel / (float)distance, yRel / (float)distance);
	}
}
