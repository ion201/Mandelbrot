import javax.swing.*;
import java.awt.*;

public class JarMain {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Demonstration of Mandelbrot series");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JApplet app = new MandelbrotMain();
        app.init();

        frame.add(app);

        frame.setSize(new Dimension(app.getWidth(), app.getHeight()));

        frame.setVisible(true);
    }
}
