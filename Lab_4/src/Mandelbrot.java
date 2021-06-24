import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;
    public void getInitialRange(Rectangle2D.Double range){
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    public int  numIterations(double x, double y){
        ComplexNumbers complexNumbers = new ComplexNumbers();

        int counter = 0;
        while(complexNumbers.square() < 4 && counter < MAX_ITERATIONS){
            complexNumbers.countMandelbrot(x, y);
            counter++;
        }
        if(counter >= MAX_ITERATIONS)
            return -1;
        else
            return counter;
    }
    public String toString(){
        return "Mandelbrot";
    }
}
