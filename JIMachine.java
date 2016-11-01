import java.io.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.*;

class JIMachine extends JFrame implements ActionListener
{
  private JPanel buttonPanel;
  private JButton open, zoomIn, original, zoomOut, quitButton;
  private ImageComponent image;
  private JFileChooser chooser;
  private boolean imageInFrame;

  public JIMachine ()
  {
    buttonPanel = new JPanel();
    quitButton = new JButton("Quit");
    open = new JButton("Open");
    zoomIn = new JButton("Zoom In");
    original = new JButton("100%");
    zoomOut = new JButton("Zoom Out");
    chooser = new JFileChooser();
    imageInFrame = false;

    buttonPanel.add(open);
    buttonPanel.add(zoomIn);
    buttonPanel.add(original);
    buttonPanel.add(zoomOut);
    buttonPanel.add(quitButton);
    add(buttonPanel, BorderLayout.NORTH);
    pack();
    quitButton.addActionListener(this);
    open.addActionListener(this);
    zoomIn.addActionListener(this);
    zoomOut.addActionListener(this);
    original.addActionListener(this);

  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getActionCommand().equals("Quit")){
      System.exit(1);
    }

    else if(e.getActionCommand().equals("Open")){
      chooser.setCurrentDirectory(new File("."));
      int result = chooser.showOpenDialog(JIMachine.this);

      if (result == JFileChooser.APPROVE_OPTION){
        if (imageInFrame){
          this.remove(image);
          imageInFrame = false;
        }
        String name = chooser.getSelectedFile().getPath();
        image = new ImageComponent(1, name);
        imageInFrame = true;
        add(image);
        pack();
      }
    }

    else if(e.getActionCommand().equals("Zoom In")){
      image.SetZoom(0.25, true);
      repaint();
    }

    else if(e.getActionCommand().equals("Zoom Out")){
      image.SetZoom(0.25, false);
      repaint();
    }

    else if(e.getActionCommand().equals("100%")){
      image.SetZoom(1, false);
      repaint();
    }
  }

  public static void main(String[] args)
  {
    JIMachine imageMachine = new JIMachine();
    imageMachine.setSize(600, 600);
    imageMachine.setVisible(true);
  }

  private class ImageComponent extends JComponent
  {
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 600;
    private final int MAX_ZOOM_IN = 2;
    private final double MAX_ZOOM_OUT = 0.25;
    private double zoomFactor, width, height;

    private Image image;

    public ImageComponent(int zoom, String filePath)
    {
      zoomFactor = zoom;
      width = DEFAULT_WIDTH;
      height = DEFAULT_HEIGHT;
      image = new ImageIcon(filePath).getImage();
    }

    public void SetZoom(double zoom, boolean in)
    {
      if (in){
        if (zoomFactor + zoom <= MAX_ZOOM_IN)
            zoomFactor += zoom;
        }

      else if(zoom == 1){
        zoomFactor = zoom;
      }

      else{
        if (zoomFactor - zoom >= MAX_ZOOM_OUT)
          zoomFactor -= zoom;
      }
    }

    public void paintComponent(Graphics g)
    {
      if (image == null) return;

      int imageWidth = image.getWidth(this);
      int imageHeight = image.getHeight(this);

      double convertedWidth = width * zoomFactor;
      double convertedHeight = height * zoomFactor;

      g.drawImage(image, 0, 0, (int)convertedWidth, (int)convertedHeight, null);
    }

    public Dimension getPreferredSize()
    {
      return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

  }
}
