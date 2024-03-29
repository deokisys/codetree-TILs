import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int N = sc.nextInt();
        int M = sc.nextInt();


        //1,2,3을 이용해 서 n을 만들어라? 

        // 5 = 1,1,3 1,2,2, 

        //dp[n][m] n를 만드는데 쓰인 갯수m
        int[][] dp = new int[N+1][M+1];
        dp[1][1] = 1;
        dp[2][1] = 1;
        dp[3][1] = 1;

        for(int n=1;n<=N;n++){
            for(int m=1;m<M;m++){
                
                if(n+1<=N){
                    dp[n+1][m+1]=(dp[n+1][m+1]+dp[n][m])%1000000007;
                }

                if(n+2<=N){
                    dp[n+2][m+1]=(dp[n+2][m+1]+dp[n][m])%1000000007;
                }

                if(n+3<=N){
                    dp[n+3][m+1]=(dp[n+3][m+1]+dp[n][m])%1000000007;
                }
            }
            //System.out.println(Arrays.toString(dp[n]));
        }

        System.out.println(dp[N][M]);


    }
}