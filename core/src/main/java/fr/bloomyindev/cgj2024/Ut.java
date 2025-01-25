package fr.bloomyindev.cgj2024;

import java.util.Random;

public class Ut {
    public static float pow(float base, float power) {
        float result = 1;
        for (int i = 0; i < power; i++) {
            result *= base;
        }
        return result;
    }

    public static int randomMinMax(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }
}
