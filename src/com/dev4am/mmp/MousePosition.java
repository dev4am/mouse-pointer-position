package com.dev4am.mmp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by zfk on 2017/4/17.
 */
public class MousePosition extends Thread implements ActionListener {

    private JFrame frame;
    private JLabel currentPos;
    private JLabel cursor;
    private Point p;
    private Timer fireMouseLocation;
    private MyMouseListener mouseListener;

    private int windowWidth = 700;
    private int windowHeight = 500;

    int screenWidth = 0;
    int screenHeight = 0;

    int startX = 0;
    int startY = 0;

    int cursorX = 0;
    int cursorY = 0;

    ArrayList<Rectangle> screens;

    public MousePosition() {
        fireMouseLocation = new Timer(20, this);
        mouseListener = new MyMouseListener();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gds = ge.getScreenDevices();
        screens = new ArrayList();

        for(GraphicsDevice gd : gds){
            Rectangle r = gd.getDefaultConfiguration().getBounds(); //x, y, width, height
            screens.add(r);

            screenWidth += r.getWidth();
            screenHeight = 1920;//TODO

            if(r.getX()<startX){
                startX = ((Double)r.getX()).intValue();
            }

            if(r.getY()<startY){
                startY = ((Double)r.getY()).intValue();
            }
        }

        windowWidth = screenWidth/8;
        windowHeight = screenHeight/8;
    }

    @Override
    public void run() {
        fireMouseLocation.start();

        frame = new JFrame("Mouse Locator / Space to add, Esc to remove");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setResizable(false);
        frame.setUndecorated(true);
//        frame.setOpacity(0.1f);
        frame.setBackground(new Color(0, 0, 0, 0.1f));
        frame.addMouseListener(mouseListener);
        frame.addMouseMotionListener(mouseListener);
        frame.setAlwaysOnTop(true);
        frame.setSize(windowWidth+20, windowHeight+20);


//        frame.setLayout(new GridLayout(0, 1));

        p = MouseInfo.getPointerInfo().getLocation();

        Panel2 panel = new Panel2();
        panel.setOpaque(false);
        panel.setBackground(Color.orange);
        panel.setLayout(null);

        //image
        cursor = new JLabel(new ImageIcon("E:\\temp\\cursor40.png"));
        cursor.setBounds(new Rectangle(new Point(40, 45), cursor.getPreferredSize()));
        panel.add(cursor);

        frame.getContentPane().add(panel);
//        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

//        AWTUtilities.setWindowOpacity(frame, 0.1f);
    }

    class Panel2 extends JPanel {

        Panel2() {
            // set a preferred size for the custom panel.
//            setPreferredSize(new Dimension(420,420));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

//            g.drawString("BLAH", 20, 20);
//            g.drawRect(200, 200, 200, 200);

            for(Rectangle screen : screens){
                int x = ((Double)screen.getX()).intValue() - startX;
                int y = ((Double)screen.getY()).intValue() - startY;

                int rX = x*windowWidth/screenWidth;
                int rY = y*windowHeight/screenHeight;
                int rWidth = screen.width*windowWidth/screenWidth;
                int rHeight = screen.height*windowHeight/screenHeight;

                g.drawRect(rX+10, rY+10, rWidth, rHeight);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        p = MouseInfo.getPointerInfo().getLocation();

        cursorX = ((Double)(((p.getX()-startX)*windowWidth)/screenWidth)).intValue();
        cursorY = ((Double)(((p.getY()-startY)*windowHeight)/screenHeight)).intValue();

        cursor.setLocation(cursorX-10, cursorY-10);
    }

    class MyMouseListener extends MouseAdapter{

        private int offsetX = 0;
        private int offsetY = 0;

        public void mousePressed(MouseEvent event) {
            offsetX = event.getXOnScreen() - frame.getX();
            offsetY = event.getYOnScreen() - frame.getY();
        }

        public void mouseReleased(MouseEvent event) {
            offsetX = 0;
            offsetY = 0;
        }

        public void mouseDragged(MouseEvent event) {
            int x = event.getXOnScreen();
            int y = event.getYOnScreen();

            frame.setLocation(x-offsetX, y-offsetY);
        }

    }

}
