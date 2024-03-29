import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        int[] ns = new int[n+1];
        for(int i=1;i<=n;i++){
            ns[i] = sc.nextInt();
        }

        //1개살때 ns[1]비용이 든다.
        //1개,1개,3개,

        //dp[i]는 i개 살때 든 최고 비용.
        //주어진게 n이므로 dp[i] = max(dp[i-1]+ns[1],dp[i-2]+ns[2],...dp[i-n][n])
        int[] dp = new int[n+1];

        for(int i=1;i<=n;i++){
            for(int k=1;k<=n;k++){
                if(i-k<0) break;
                dp[i] = Math.max(dp[i-k]+ns[k],dp[i]);
            }
        }
        System.out.println(dp[n]);
    }
}