public class ComplexNums {

    private double[] coefs;

    public ComplexNums(){

        coefs = new double[] {0.0, 0.0};

    }

    public ComplexNums(double a, double b){
        coefs = new double[] {a, b};
    }

    public void set(double a, double b){
        coefs[0] = a;
        coefs[1] = b;
    }

    public double[] get(){
        return coefs;
    }

    public ComplexNums copy(){
        ComplexNums cp = new ComplexNums();
        cp.set(coefs[0], coefs[1]);
        return cp;
    }

    public void square(){
        double a = coefs[0]*coefs[0] - coefs[1]*coefs[1];
        double b = 2 * coefs[0] * coefs[1];
        coefs[0] = a;
        coefs[1] = b;
    }

    public void add(ComplexNums other){
        coefs[0] += other.get()[0];
        coefs[1] += other.get()[1];
    }

    public void add(double realNumber){
        coefs[0] += realNumber;
    }

    public double getDistance(){
        return Math.sqrt(coefs[0]*coefs[0] + coefs[1]*coefs[1]);
    }

    @Override
    public String toString(){
        return Math.round(coefs[0]*1000)/1000.0 + "+" + Math.round(coefs[1]*1000)/1000.0 + "i";
    }
}
