import java.awt.BorderLayout;
import java.awt.geom.Rectangle2D;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Color;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;
import javax.swing.SwingUtilities;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.imageio.ImageIO;



public class FractalExplorer {
    private int winSize;
    private JImageDisplay display;
    private FractalGenerator generator;
    private Rectangle2D.Double rectangle;
    private JLabel coordsLabel;
    private JComboBox<FractalGenerator> fractBox;
    private  JFileChooser fileChooser;
    private JButton savingingButton;

    public FractalExplorer(int winSize){
        this.winSize = winSize;
        this.rectangle = new Rectangle2D.Double();
        this.generator = new Mandelbrot();
        this.generator.getInitialRange(this.rectangle);
        this.display = new JImageDisplay(winSize, winSize);
    }

    public void createAndShowGUI (){
        MouseAdapter mListener = new MListener();
        ActionListener actionListener = new AListener(); // объект для отслеживания всех нажатий

        JFrame frame = new JFrame("Фрактал");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        JPanel panel1 = new JPanel();

        contentPane.setLayout(new BorderLayout());

        coordsLabel = new JLabel("Фрактал: ");
        coordsLabel.setPreferredSize(new Dimension(200,30));

        panel1.add(coordsLabel);

        fractBox = new JComboBox<FractalGenerator>();   //выпадающий список
        fractBox.addItem(new Mandelbrot());
        fractBox.addItem(new Tricorn());
        fractBox.addItem(new BurningShip());

        fractBox.addActionListener(actionListener);
        fractBox.setActionCommand("Boxing");

        fractBox.setPreferredSize(new Dimension(200,30));

        panel1.add(fractBox);

        contentPane.add(panel1, BorderLayout.NORTH);
        display.setLayout(new BorderLayout()); //точка вывода
        display.addMouseListener(mListener); // добавление отслеживания нажатий клавиш мыши
        frame.add(display, BorderLayout.CENTER); // добавили изображение
        JPanel panel2 = new JPanel();
        // фрактала в окно и установили место вывода изображения фрактала
        JButton button = new JButton("Сброс");
        button.setActionCommand("Drop");
        contentPane.add(button, BorderLayout.SOUTH);
        panel2.add(button);

        savingingButton = new JButton("Сохранить");
        savingingButton.addActionListener(actionListener);
        savingingButton.setActionCommand("Save");

//        contentPane.add(savingingButton, BorderLayout.SOUTH);
        panel2.add(savingingButton);
//        frame.add(savingingButton, BorderLayout.SOUTH); //???

        contentPane.add(panel2, BorderLayout.SOUTH);
        button.addActionListener(actionListener);
//        frame.add(button, BorderLayout.SOUTH); // добавили в окно

        frame.pack();
       frame.setVisible(true);
        frame.setResizable(false);
    }
    private void drawFractal(){
        int rgbColor;
        for (int x=0; x<winSize; x++){
            for (int y = 0; y<winSize; y++){
                double xCoord = generator.getCoord(rectangle.x, rectangle.x + rectangle.width,
                        winSize, x);
                double yCoord = generator.getCoord(rectangle.y, rectangle.y + rectangle.width,
                        winSize, y);
                int iters = generator.numIterations(xCoord, yCoord);
                if (iters == -1) // задаем черный цвет, если вышли за границы
                    rgbColor = 0;
                else { // иначе задем цвет на основании количества интераций
                    float hue = 0.7f + (float) iters / 200f;
                    rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                }

                display.drawPixel(x, y, rgbColor);
            }
        }
        display.repaint();
    }

    public class AListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            String command = event.getActionCommand();
            System.out.println(command);

            if (command == "Drop" || command == "Boxing"){
                generator = (FractalGenerator)fractBox.getSelectedItem(); //выбор элемента в списке

                display.clearImage();
                generator.getInitialRange(rectangle); // запускаем генератор, который задает диапозон построения фрактала
                drawFractal(); // рисуем фрактал
            } else {
                System.out.println("Saving");
                fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Выбор директории");
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png"); //настройка работы только с jpg файлами
                fileChooser.setFileFilter(filter);
                fileChooser.setAcceptAllFileFilterUsed(false);

                int result = fileChooser.showSaveDialog(display);   //showSaveDialog JFileChooser chooser открывает диалоговое окно «Save file
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        ImageIO.write(display.getIm(), "png", fileChooser.getSelectedFile()); //операция загрузки и сохранения изображения
                    } catch (Exception err) {
                        JOptionPane.showMessageDialog(display, err, "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    public class MListener extends MouseAdapter{
        public void mouseClicked(MouseEvent event) {
            double x = event.getPoint().getX(); // получаю координаты нажатия
            double y = event.getPoint().getY();

            x = generator.getCoord(rectangle.x, rectangle.x + rectangle.width, winSize, (int) x);
            y = generator.getCoord(rectangle.y, rectangle.y + rectangle.width, winSize, (int) y);

            generator.recenterAndZoomRange(rectangle, x, y, 0.5);
            drawFractal();
            display.repaint();
        }
    }
    public static void main (String agrs []){
        FractalExplorer fractalExplorer = new FractalExplorer(700);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }
}

