import java.util.*;
import java.io.*;

public class Main {
    static int maxMoney = 0;
    public static void main(String[] args) throws Exception{

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        int[][] data = new int[n+1][2];
        for(int time=1;time<=n;time++){
            int t = sc.nextInt();
            int p = sc.nextInt();
            data[time][0] = t;
            data[time][1] = p;
        }
        
        //data, 현재시간, 합친값
        maxMoney = 0;
        bt(data,1,0);
        System.out.println(maxMoney);
    }

    public static void bt(int[][] data, int time, int sum){
        if(data.length-1<time){
            maxMoney = Math.max(sum, maxMoney);
            return;
        }

        for(int t=time;t<data.length;t++){
            bt(data,t+data[t][0],sum+data[t][1]);
        }
    }
}