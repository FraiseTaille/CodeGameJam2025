package fr.bloomyindev.cgj2024;

public class Spaceship {
    private float latitude;
    private float longitude;
    private int distance;

    public Spaceship(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = 0;
    }

    public int getDistance() {
        return distance;
    }

    public void rotateLongitude(float longitude) {
        if (this.longitude < longitude) {
            for (float i = this.longitude; i < longitude; i++) {
                this.longitude += i;
            }
        } else {
            for (float i = this.longitude; i > longitude; i--) {
                this.longitude += i;
            }
        }
    }

    public void rotateLatitude(float latitude) {
        if (this.latitude < latitude) {
            for (float i = this.latitude; i < latitude; i++) {
                this.latitude += i;
            }
        } else {
            for (float i = this.latitude; i > latitude; i--) {
                this.latitude += i;
            }
        }
    }
}

