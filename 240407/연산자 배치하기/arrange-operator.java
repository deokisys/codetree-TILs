import java.util.*;
import java.io.*;

public class Main {

    static int min = Integer.MAX_VALUE;
    static int max = 0;
    public static void main(String[] args) throws Exception{

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        //연산자 +-*해서 최소, 최대를 출력

        int[] numbers = new int[n];

        for(int i=0;i<n;i++){
            numbers[i] = sc.nextInt();
        }
        int plus = sc.nextInt();
        int mins = sc.nextInt();
        int mult = sc.nextInt();


        calc(numbers,plus,mins,mult,1,numbers[0]);

        System.out.println(min+" "+max);
    }

    static public void calc(int[] numbers,int plus,int mins, int mult, int idx, int sum){
        if(idx==numbers.length){
            min = Math.min(min,sum);
            max = Math.max(max,sum);
            return;
        }

        if(plus>0){
            calc(numbers,plus-1,mins,mult,idx+1,sum+numbers[idx]);
        }

        if(mins>0){
            calc(numbers,plus,mins-1,mult,idx+1,sum-numbers[idx]);
        }

        if(mult>0){
            calc(numbers,plus,mins,mult-1,idx+1,sum*numbers[idx]);
        }

    }
}