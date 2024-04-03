import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        // 여기에 코드를 작성해주세요.
        //오름차순 최소 이동 횟수
            //제일 낮은게 맨 왼쪽이어야 함.
        //한번에 하나, 어디든 이동 가능

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        int[] data = new int[n];
        for(int i=0;i<n;i++){
            data[i] = sc.nextInt();
        }


        //오름차순으로 정렬된게 많은것을 기준으로 교체해주면 된다.

        int maxLis = 0;
        int[] dp = new int[n];//
        for(int i=0;i<n;i++){
            //lis를 어떻게 구해?

            //3 7 5 2 6 1 4
            //이라고 하자?
                //3부터 시작 3,5,6이 나와야하거든?
                //3,7로 끝나버림...
                    //어떤 예외가 필요하지?
                //3을 기준으로 
                    //큰거를 찾아라.
                    //무조건 큰거면 안되다. 순서대로 커야 한다.
                    //그러면?
                        //해당 숫자 뒤에 그 숫자보다 큰것들의 갯수를 센다면?
                //뒤에서부터 하면 어뗘?
                    //몰러?
            //현재 값을 기준으로 왼쪽 녀석들을 확인하는 방식
                //현재값 기준 작은 값이 존재하면 +1이 된다.
            dp[i]=1;

            for(int j=0;j<i;j++){
                if(data[j]<data[i]){
                    //작은 값이 존재함.
                    if(dp[i]<dp[j]+1){
                        //갱신하기전에 이미 높은지 확인
                        dp[i] = dp[j]+1;//해당 값의 +1한 값으로 갱신한다.
                    }
                }
            }
            
            maxLis = Math.max(maxLis,dp[i]);
        }
        System.out.println(n-maxLis);
    }
}