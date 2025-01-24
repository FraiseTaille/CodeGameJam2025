package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.math.MathUtils;
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
        rotate(false, number);
    }

    public void rotateLatitude(float number) {
        rotate(true, number);
    }

    /*
    * Lat = true, long = false
    */
    private void rotate(boolean dir, float number) {
        for (float i = 0; i < Math.abs(number); i++) {
            if (dir) {
                this.latitude += number;
                this.latitude += (float)Math.PI;
                this.latitude %= 2.f * (float)Math.PI;
                this.latitude -= (float)Math.PI;
            } else {
                this.longitude += number;
                this.longitude += (float)Math.PI;
                this.longitude %= 2.f * (float)Math.PI;
                this.longitude -= (float)Math.PI;
            }
        }
    }

    public AbsoluteCoords3D getSpaceshipCord() {
        return spaceshipCord;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void increaseSpeed(float speed) {
        this.speed = MathUtils.clamp(speed+this.speed, 0, 20);
    }
}

