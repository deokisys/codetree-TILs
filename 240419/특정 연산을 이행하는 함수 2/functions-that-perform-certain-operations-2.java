import java.util.*;

public class Main {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        double a = sc.nextDouble();
        double b = sc.nextDouble();
        double c = sc.nextDouble();

        int aInt = check(a,b,c);
        int bInt = check(b,a,c);
        int cInt = check(c,a,b);

        System.out.println(aInt+" "+bInt+" "+cInt+"    ");
    }

    public static int check(double a, double b, double c){
        if(a>b&&a>c){
            return (int)Math.ceil(a);
        }

        if(a<b&&a<c){
            return (int)Math.floor(a);
        }


        return (int)Math.round(a);
    }
}