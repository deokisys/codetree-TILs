import java.util.*;

public class Main {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        double[] data = new double[3];
        data[0] = sc.nextDouble();
        data[1] = sc.nextDouble();
        data[2] = sc.nextDouble();

        Arrays.sort(data);

        int min = (int)Math.floor(data[0]);
        int max = (int)Math.ceil(data[2]);
        int mid = (int)Math.round(data[1]);
        

        System.out.println(max+" "+min+" "+mid+"    ");
    }
}