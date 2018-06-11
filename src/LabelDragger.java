import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
public class LabelDragger {
    public static void main(final String[] args) {
        final int labelRows = 5,    //How many rows of labels.
                  labelColumns = 5, //How many columns of labels.
                  labelWidth = 55,  //Width for each label.
                  labelHeight = 20; //Height for each label.

        //Border colors for labels:
        final Color[] colors = new Color[]{Color.BLUE, Color.GREEN, Color.BLACK, Color.GRAY};
        final Random prng = new Random(); //For selecting border color for each label.

        final JPanel dragP = new JPanel(null); //Nicely set to null! :D Did not know that trick.

        //Creating the listener for the panel:
        final MouseAdapter ma = new MouseAdapter() {
            private JLabel selectedLabel = null; //Clicked label.
            private Point selectedLabelLocation = null; //Location of label in panel when it was clicked.
            private Point panelClickPoint = null; //Panel's click point.

            //Selection of label occurs upon pressing on the panel:
            @Override
            public void mousePressed(final MouseEvent e) {

                //Find which label is at the press point:
                final Component pressedComp = dragP.findComponentAt(e.getX(), e.getY());

                //If a label is pressed, store it as selected:
                if (pressedComp != null && pressedComp instanceof JLabel) {
                    selectedLabel = (JLabel) pressedComp;
                    selectedLabelLocation = selectedLabel.getLocation();
                    panelClickPoint = e.getPoint();
                    //Added the following 2 lines in order to make selectedLabel
                    //paint over all others while it is pressed and dragged:
                    dragP.setComponentZOrder(selectedLabel, 0);
                    selectedLabel.repaint();
                }
                else {
                    selectedLabel = null;
                    selectedLabelLocation = null;
                    panelClickPoint = null;
                }
            }

            //Moving of selected label occurs upon dragging in the panel:
            @Override
            public void mouseDragged(final MouseEvent e) {
                if (selectedLabel != null
                        && selectedLabelLocation != null
                        && panelClickPoint != null) {

                    final Point newPanelClickPoint = e.getPoint();

                    //The new location is the press-location plus the length of the drag for each axis:
                    final int newX = selectedLabelLocation.x + (newPanelClickPoint.x - panelClickPoint.x),
                              newY = selectedLabelLocation.y + (newPanelClickPoint.y - panelClickPoint.y);

                    selectedLabel.setLocation(newX, newY);
                }
            }
        };
        dragP.addMouseMotionListener(ma); //For mouseDragged().
        dragP.addMouseListener(ma); //For mousePressed().

        //Filling the panel with labels:
        for (int row = 0; row < labelRows; ++row)
            for (int col = 0; col < labelColumns; ++col) {

                //Create label for (row, col):
                final JLabel lbl = new JLabel("Label" + (row * labelColumns + col));
                lbl.setHorizontalAlignment(JLabel.CENTER);
                //lbl.setVerticalAlignment(JLabel.CENTER);
                lbl.setBounds(col * labelWidth, row * labelHeight, labelWidth, labelHeight); //Grid-like positioning.
                lbl.setBorder(new LineBorder(colors[prng.nextInt(colors.length)], 2)); //Set a border for clarity.

                //Add label to panel:
                dragP.add(lbl);
            }

        //Creating and showing the main frame:
        final JFrame frame = new JFrame(LabelDragger.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //The size of the content pane adds some extra room for moving the labels:
        final Dimension paneSize = new Dimension((int)(1.5 * labelWidth * labelColumns),
                                                 (int)(1.5 * labelHeight * labelRows));
        frame.getContentPane().setPreferredSize(paneSize);
        frame.getContentPane().add(dragP);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}