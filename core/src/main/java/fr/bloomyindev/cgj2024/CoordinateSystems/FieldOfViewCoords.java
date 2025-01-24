package fr.bloomyindev.cgj2024.CoordinateSystems;

public class FieldOfViewCoords {
	private float X, Y;
	private float normalisedX, normalisedY;

	private FieldOfView FOV;
	private boolean isVisible;

	public FieldOfViewCoords(FieldOfView FOV, SpaceshipRelative objRelativeCoords) {
		this.FOV = FOV;
        reComputeFOVCoords(objRelativeCoords);
	}

	public void reComputeFOVCoords(SpaceshipRelative objRelativeCoords) {
		float DeltaLat, DeltaLng;

		float[] objCoords = objRelativeCoords.getLatLong();
		float[] fovCoords = this.FOV.getCenterCoords();
		float[] fovAngles = this.FOV.getFovAngles();

		float fovX, fovY;

		fovX = fovAngles[0];
		fovY = fovAngles[1];

		DeltaLat = objCoords[0] - fovCoords[0];
		DeltaLng = objCoords[1] - fovCoords[1];

		this.isVisible = Math.sqrt((double)(DeltaLat * DeltaLat)) < (fovY / 2.0f);
		this.isVisible &= Math.sqrt((double)(DeltaLng * DeltaLng)) < (fovX / 2.0f);

		if (this.isVisible) {
			DeltaLat += (float)Math.PI;
			DeltaLng += (float)Math.PI;

			DeltaLat %= 2.f * (float)Math.PI;
			DeltaLng %= 2.f * (float)Math.PI;

			DeltaLat -= (float)Math.PI;
			DeltaLng -= (float)Math.PI;

			this.X = DeltaLng;
			this.Y = DeltaLat;

			this.normalisedX = X / (0.5f * fovX);
			this.normalisedY = Y / (0.5f * fovY);
		}
	}

	public boolean getVisibility() {
		return isVisible;
	}

	public float[] getNormalisedCoords() {
		return new float[]{normalisedX, normalisedY};
	}
}
