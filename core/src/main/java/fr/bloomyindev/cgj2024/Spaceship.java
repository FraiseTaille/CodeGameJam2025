package fr.bloomyindev.cgj2024;

import fr.bloomyindev.cgj2024.CoordinateSystems.*;

public class Spaceship {
    private AbsoluteCoords3D spaceshipCord;
    private float latitude;
    private float longitude;
    private float speed;

    public Spaceship(AbsoluteCoords3D spaceshipCord,float latitude, float longitude) {
        this.spaceshipCord = spaceshipCord;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = 0;
    }

    public float getSpeed() {
        return speed;
    }

    public void rotateLongitude(float number) {
        if (number > 0) {
            for (float i = 0; i < number; i++) {
                this.longitude += i;
            }
        } else {
            for (float i = number ; i > 0; i--) {
                this.longitude += i;
            }
        }
    }

    public void rotateLatitude(float number) {
        if (number > 0) {
            for (float i = 0; i < number; i++) {
                this.latitude += i;
            }
        } else {
            for (float i = number; i > 0; i--) {
                this.latitude += i;
            }
        }
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void increaseSpeed(float speed) {
        this.speed += speed;
    }
}

