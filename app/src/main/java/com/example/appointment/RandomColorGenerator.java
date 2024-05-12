package com.example.appointment;

import java.util.Random;

public class RandomColorGenerator {

    // Method to generate a random color code
    public static String getRandomColorCode() {
        // Create a Random object
        Random random = new Random();

        // Generate light color components with higher values (e.g., 200-255 for each RGB component)
        int r = random.nextInt(56) + 200; // Red component: 200-255
        int g = random.nextInt(56) + 200; // Green component: 200-255
        int b = random.nextInt(56) + 200; // Blue component: 200-255

        // Combine the RGB values into a color code in hexadecimal format (e.g., #RRGGBB)
        String colorCode = String.format("#%02X%02X%02X", r, g, b);

        return colorCode;
    }
}
