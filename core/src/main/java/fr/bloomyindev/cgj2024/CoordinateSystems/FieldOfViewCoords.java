package fr.bloomyindev.cgj2024.CoordinateSystems;

public class FieldOfViewCoords {
	private float normalisedX, normalisedY;

	private FieldOfView FOV;

	public FieldOfViewCoords(FieldOfView FOV, SpaceshipRelative objRelativeCoords) {
		this.FOV = FOV;
	}

	public void reComputeFOVCoords(SpaceshipRelative objRelativeCoords) {
		float DeltaLat, DeltaLng;

		float[] objCoords = objRelativeCoords.getLatLong();
		DeltaLat = objCoords[0] - FOV.;
		DeltaLng = objCoords[1];
	}
}
