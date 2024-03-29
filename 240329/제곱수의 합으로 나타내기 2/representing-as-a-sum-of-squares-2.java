import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        //최대 4까지의 제곱수의 합으로 표현한데
        //최소개수의 제곱수 합으로 ㅍ현하레
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        //dp[n]은 n을 만드는데 쓰이는 최소 제곱수갯수

        //dp[n] = dp[n-1]+1, dp[n-4]+1로 확인

        int[] dp = new int[n+1];
        for(int i=0;i<=n;i++){
            dp[i] = Integer.MAX_VALUE;
        }
        dp[0] = 0;
        for(int i=1;i<=n;i++){
            int s = 1;
            while(s*s<=i){
                dp[i] = Math.min(dp[i-s*s]+1,dp[i]);
                s++;
            }
        }
        System.out.println(dp[n]);
    }
}