import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test {

    public static void main(String args[]) throws Exception {
        JFrame frame = new JFrame("Rotation Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final BufferedImage bi = ImageIO.read(new File("gCarrier.png"));
        frame.add(new JPanel() {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(bi.getWidth(), bi.getHeight());
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.rotate(Math.PI / 2, bi.getWidth() / 2, bi.getHeight() / 2);
                g2.drawImage(bi, 0, 0, null);
            }
        });
        frame.pack();
        frame.setVisible(true);
    }
}