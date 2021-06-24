public class ComplexNumbers {
    double mnim; // создали мнимую часть числа
    double real; //создали реальную часть числа

    public ComplexNumbers(){
        this.mnim = 0;
        this.real = 0;
    }
    public double square (){
        return this.real*this.real + this.mnim*this.mnim;
    }

    public void countMandelbrot(double x, double y){
        //формула для построения множества мандельброта
        double newReal = real*real - mnim*mnim + x;
        double newMnim = 2*real*mnim +y;

        this.mnim = newMnim;
        this.real = newReal;
    }
    public void triUpdate(double x, double y){
        //формула построения фрактала tricorn
        double newReal = real * real - mnim * mnim + x;
        double newMnim = -2 * real * mnim + y;

        this.real = newReal;
        this.mnim = newMnim;
    }

    public void shiUpdate(double x, double y){
        //фомула построения фрактала Burning Ship
        double newReal = real * real - mnim * mnim + x;
        double newMnim = 2 * Math.abs(real) * Math.abs(mnim) + y;

        this.real = newReal;
        this.mnim = newMnim;
    }

}
