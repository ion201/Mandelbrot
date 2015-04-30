import java.awt.*;
import java.util.ArrayList;

public class MakeGradient {

    public static ArrayList<Color> makeGradient(Color color1, Color color2){
        ArrayList<Color> gradient = new ArrayList<Color>();
        gradient.add(color1);

        double stepR = (color2.getRed() - color1.getRed()) / 50.0;
        double stepG = (color2.getGreen() - color1.getGreen()) / 50.0;
        double stepB = (color2.getBlue() - color1.getBlue()) / 50.0;

        double red = color1.getRed();
        double green = color1.getGreen();
        double blue = color1.getBlue();

        for (int i = 1; i < 50; i++){

            red += stepR;
            green += stepG;
            blue += stepB;

            gradient.add(new Color((int)red, (int)green, (int)blue));
        }

        return gradient;
    }

    public static ArrayList<Color> makeGradient(Color[] colors){
        ArrayList<Color> gradient = new ArrayList<Color>();

        for (int i = 1; i < colors.length; i++){
            gradient.addAll(makeGradient(colors[i-1], colors[i]));
        }

        return gradient;
    }
}
