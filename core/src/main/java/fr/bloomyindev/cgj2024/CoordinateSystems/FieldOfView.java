package fr.bloomyindev.cgj2024.CoordinateSystems;

public class FieldOfView {
	private float fovAngleX, fovAngleY;
	private float latCenter, lngCenter;

	private float[] TopLeftCorner, TopRightCorner, BottomLeftCorner, BottomRightCorner;

	/*
	 * Les angles sont à fournir en radians
	 */
	public FieldOfView(float centerLat, float centerLng, float fovAngleX) {
		this.fovAngleX = fovAngleX;
		this.fovAngleY = fovAngleX / (9.f / 9.f);

		this.latCenter = centerLat;
		this.lngCenter = centerLng;

		this.TopLeftCorner = new float[]{centerLng - this.fovAngleX, centerLat + this.fovAngleY};
		this.TopRightCorner = new float[]{centerLng + this.fovAngleX, centerLat + this.fovAngleY};
		this.BottomLeftCorner = new float[]{centerLng - this.fovAngleX, centerLat - this.fovAngleY};
		this.BottomRightCorner = new float[]{centerLng + this.fovAngleX, centerLat - this.fovAngleY};
	}

	public float[] getCenterCoords() {
		return new float[]{latCenter, lngCenter};
	}

	public float[] getFovAngles() {
		return new float[]{fovAngleX, fovAngleY};
	}

    public void setCenter(float latCenter, float lngCenter) {
        this.latCenter = latCenter;
        this.lngCenter = lngCenter;

		this.latCenter += (float)Math.PI;
		this.lngCenter += (float)Math.PI;

		this.latCenter %= 2.f * (float)Math.PI;
		this.lngCenter %= 2.f * (float)Math.PI;

		this.latCenter -= (float)Math.PI;
		this.lngCenter -= (float)Math.PI;
    }
}
