package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.math.MathUtils;
import fr.bloomyindev.cgj2024.CoordinateSystems.*;

public class Spaceship {
    private AbsoluteCoords3D spaceshipCoord;
    private float pitch;
    private float yaw;
    private float speed;

    public Spaceship(AbsoluteCoords3D spaceshipCoord,float pitch, float yaw) {
        this.spaceshipCoord = spaceshipCoord;
        this.pitch = pitch;
        this.yaw = yaw;
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
                this.pitch += number;

                this.pitch += (float)Math.PI;
                this.pitch %= 2.f * (float)Math.PI;
                this.pitch -= (float)Math.PI;
            } else {
                this.yaw += number;

                this.yaw += (float)Math.PI;
                this.yaw %= 2.f * (float)Math.PI;
                this.yaw -= (float)Math.PI;
            }
        }
    }

    public AbsoluteCoords3D getSpaceshipCoord() {
        return spaceshipCoord;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void increaseSpeed(float delta) {
        this.speed = MathUtils.clamp(delta+this.speed, -20, 20);
    }

    public void move() {
        this.spaceshipCoord.move(speed, this.pitch, this.yaw);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}

