import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MandelbrotMain extends JApplet{

    public int WIDTH;
    public int HEIGHT;
    private double SCALE;
    private double maxIterations;

    private Color[] pixels;

    private Canvas canvas;
    private Engine engine;
    private Base base;

    private ArrayList<Color> gradient;

    public void init(){

        WIDTH = 800;
        HEIGHT = 800;
        SCALE = 200.0;
        maxIterations = 35.0;

        Color[] gradColors = {Color.WHITE, Color.RED, Color.YELLOW};
        gradient = MakeGradient.makeGradient(gradColors);

        pixels = new Color[WIDTH * HEIGHT];
        for (int i = 0; i < pixels.length; i++){
            pixels[i] = Color.WHITE;
        }

        this.setSize(WIDTH, HEIGHT);

        base = new Base();
        base.setLayout(new OverlayLayout(base));

        canvas = new Canvas(WIDTH, HEIGHT);
        engine = new Engine();

        base.add(new Options());
        base.add(canvas);

        this.add(base);

        engine.start();

    }

    public class Base extends JPanel{}

    public class Options extends JPanel{

        JTextField maxIterField;
        JButton redrawButton;
        JButton autoAdvanceButton;

        Incrementor inc;

        public Options(){
            this.setLayout(new GridLayout(3, 1));
            this.setMaximumSize(new Dimension(150, 75));
            this.setAlignmentX(.95f);
            this.setAlignmentY(.95f);
            this.setBackground(Color.GRAY);
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

            JPanel p1 = new JPanel();
            p1.setLayout(new GridLayout(1, 2));
            p1.add(new JLabel(" Iterations:"));

            maxIterField = new JTextField();
            maxIterField.setText("" + (int) maxIterations);

            p1.add(maxIterField);

            redrawButton = new JButton ("Redraw!");
            redrawButton.addActionListener(new ButtonAction());

            autoAdvanceButton = new JButton("Auto Inc");
            autoAdvanceButton.addActionListener(new ButtonAction());

            this.add(p1);
            this.add(redrawButton);
            this.add(autoAdvanceButton);

            inc = new Incrementor();
        }

        private class Incrementor extends Thread{

            private boolean running;

            public Incrementor(){
                running = false;
            }

            public void stopExec(){
                running = false;
            }

            @Override
            public void run(){
                running = true;
                while (running){
                    int nextIter = Integer.parseInt(maxIterField.getText()) + 1;
                    if (nextIter > 50) nextIter = 1;
                    maxIterField.setText("" + nextIter);
                    maxIterations = nextIter;
                    engine.updateGraph();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private class ButtonAction implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent event) {
                if (event.getActionCommand().equals("Redraw!")) {
                    int iter;
                    try {
                        iter = Integer.parseInt(maxIterField.getText());
                    } catch (NumberFormatException e) {
                        maxIterField.setText("35");
                        iter = 35;
                    }
                    if (iter != (int) maxIterations) {
                        maxIterations = iter;
                        engine.updateGraph();
                    }
                } else if (event.getActionCommand().equals("Auto Inc")){
                    inc.start();
                    autoAdvanceButton.setText("Stop Inc");

                } else if (event.getActionCommand().equals("Stop Inc")){
                    inc.stopExec();
                    autoAdvanceButton.setText("Auto Inc");

                }
            }
        }
    }

    public class Canvas extends JPanel{

        private int WIDTH;
        private int HEIGHT;

        public Canvas(int width, int height){
            this.setBackground(Color.WHITE);
            this.setLayout(new GridLayout(1, 1));

            this.setSize(width, height);

            this.WIDTH = width;
            this.HEIGHT = height;
        }

        @Override
        public void paintComponent(Graphics gfx){

            for (int i = 0; i < pixels.length; i++){
                int x = i % WIDTH;
                int y = i / WIDTH;
                gfx.setColor(pixels[i]);
                gfx.drawLine(x, y, x, y);
            }

            gfx.setColor(Color.GRAY);
            gfx.drawLine(0, HEIGHT / 2, WIDTH, HEIGHT / 2);
            gfx.drawLine(WIDTH/4, HEIGHT/2, WIDTH/4, HEIGHT/2 - 5);
            gfx.drawLine(3*WIDTH/4, HEIGHT/2, 3*WIDTH/4, HEIGHT/2 - 5);

            gfx.drawLine(WIDTH/2, 0, WIDTH/2, HEIGHT);
            gfx.drawLine(WIDTH/2, HEIGHT/4, WIDTH/2 + 5, HEIGHT/4);
            gfx.drawLine(WIDTH/2, 3*HEIGHT/4, WIDTH/2 + 5, 3*HEIGHT/4);
        }
    }

    public class Engine extends Thread{

        @Override
        public void run(){
            updateGraph();
        }

        public void updateGraph(){

            for (double y = -2; y < 2; y += 1/SCALE){
                for (double x = -2; x < 2; x += 1/SCALE){

                    ComplexNums z0 = new ComplexNums(x, y);
                    ComplexNums z = new ComplexNums(0, 0);

                    int iterations = 0;

                    double dist = 0;
                    while (dist <= 2 && iterations < maxIterations){
                        z.square();
                        z.add(z0);
                        dist = z.getDistance();
                        iterations ++;
                    }

                    int i = (int) (((y + 2) * WIDTH + (x + 2))*SCALE);

                    double iter = iterations / (maxIterations);
                    iter = 1 - iter;

                    int index = (int) (iter * (gradient.size() - 1));

                    if (i >= pixels.length) continue;

                    if (iterations == maxIterations){
                        pixels[i] = Color.BLACK;
                    }
                    else{
                        pixels[i] = gradient.get(index);
                    }
                }
            }
            base.repaint();
        }
    }
}
