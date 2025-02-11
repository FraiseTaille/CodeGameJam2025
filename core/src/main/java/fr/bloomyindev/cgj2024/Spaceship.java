package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.math.MathUtils;
import fr.bloomyindev.cgj2024.CoordinateSystems.*;

public class Spaceship {
    private AbsoluteCoords3D spaceshipCoord;
    private float pitch;
    private float yaw;
    private float speed;
    private float speedX;
    private float speedY;
    private float speedZ;

    public Spaceship(AbsoluteCoords3D spaceshipCoord, float pitch, float yaw) {
        this.spaceshipCoord = spaceshipCoord;
        this.pitch = pitch;
        this.yaw = yaw;
        this.speed = 0;
        this.speedX = 0;
        this.speedY = 0;
        this.speedZ = 0;
    }

    public Spaceship(Spaceship spaceship) {
        this.spaceshipCoord = new AbsoluteCoords3D(spaceship.getSpaceshipCoord());
        this.pitch = spaceship.getPitch();
        this.yaw = spaceship.getYaw();
        this.speed = spaceship.getSpeed();
    }

    public float getSpeed() {
        return speed;
    }

    public void rotateYaw(float number) {
        rotate(false, number);
    }

    public void rotatePitch(float number) {
        rotate(true, number);
    }

    /*
     * Lat = true, long = false
     */
    private void rotate(boolean dir, float number) {
        for (float i = 0; i < Math.abs(number); i++) {
            if (dir) {
                this.pitch = number;

                if (this.pitch < -(float)Math.PI+.02f) {
                    this.pitch = (float)Math.PI-.01f;
                } else if (this.pitch > Math.PI+.02f) {
                    this.pitch = -(float)Math.PI+.01f;
                }
            } else {
                this.yaw += number;

                if (this.yaw < -Math.PI+.02f) {
                    this.yaw = (float)Math.PI+.005f;
                }else if (this.yaw > Math.PI+.02f) {
                    this.yaw = -(float)Math.PI+.0625f;
                }
                //this.yaw -= (float) Math.PI;
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

    public void changeSpeed(float delta) {
        this.speed = MathUtils.clamp(this.speed + delta, -20, 20);
    }

    public void updateSpeedCoords() {
        speedZ += (float) Math.sin(this.pitch) * this.speed;
        speedY += (float) Math.sin(this.yaw) * (float) Math.cos(this.pitch) * this.speed;
        speedX += (float) Math.cos(this.yaw) * (float) Math.cos(this.pitch) * this.speed;
    }

    public void move() {
        this.spaceshipCoord.move(speedX, speedY, speedZ);

        speedZ = 0;
        speedY = 0;
        speedX = 0;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void adjustMovement(float adjustmentYaw, float adjustmentSpeed) {
        speedX += (float) Math.cos(adjustmentYaw) * (float) Math.cos(this.pitch) * adjustmentSpeed;
        if (speedX > 20) speedX = 20;
        if (speedX < -20) speedX = -20;
        speedY += (float) Math.sin(adjustmentYaw) * (float) Math.cos(this.pitch) * adjustmentSpeed;
        if (speedY > 20) speedY = 20;
        if (speedY < -20) speedY = -20;
    }
}
