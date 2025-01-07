package fr.bloomyindev.cgj2024;

public class Ut {
    public static float pow(float base, float power) {
        float result = 1;
        for (int i = 0; i < power; i++) {
            result *= base;
        }
        return result;
    }
}
