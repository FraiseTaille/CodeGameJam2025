package fr.bloomyindev.cgj2024;

import com.badlogic.gdx.math.MathUtils;
import fr.bloomyindev.cgj2024.CoordinateSystems.*;

public class Spaceship {
    private AbsoluteCoords3D spaceshipCoord;
    private float pitch;
    private float yaw;
    private float speed;

    public Spaceship(AbsoluteCoords3D spaceshipCoord, float pitch, float yaw) {
        this.spaceshipCoord = spaceshipCoord;
        this.pitch = pitch;
        this.yaw = yaw;
        this.speed = 0;
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
                this.pitch += number;

                this.pitch = (this.pitch + (float) Math.PI/2.f) % (float)Math.PI - (float)Math.PI/2.f;
                System.out.println(this.pitch);
            } else {
                this.yaw += number;

                this.yaw = (this.yaw + (float) Math.PI) % (float)Math.TAU - (float)Math.PI;
                System.out.println("," + this.yaw);
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

    public void move() {
        if (speed == 0) {
            return;
        }

        float speedZ = (float) Math.sin(this.pitch) * this.speed;
        float speedY = (float) Math.sin(this.yaw) * (float) Math.cos(this.pitch) * this.speed;
        float speedX = (float) Math.cos(this.yaw) * (float) Math.cos(this.pitch) * this.speed;

        this.spaceshipCoord.move(speedX, speedY, speedZ);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
