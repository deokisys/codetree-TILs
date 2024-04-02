import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) {
        // 여기에 코드를 작성해주세요.

        Scanner sc = new Scanner(System.in);

        //k개의 연속된 정수 선택
            //c 정수 값을 추가로 하나 더 선택 가능
        //서로 다른 정수 개수의 최대값


        //투포인터인듯

        int n = sc.nextInt();
        int d = sc.nextInt();//정수 최대값
        int k = sc.nextInt();
        int c = sc.nextInt();//추가로 선택


        int[] data = new int[n];
        for(int i=0;i<n;i++){
            data[i] = sc.nextInt();
        }

        int[] check = new int[d+1];
        
        int curCount = 1;
        check[c]+=1;

        int start = 0;
        //초기 시작
        for(int i=0;i<k;i++){
            if(check[data[i]]==0){
                curCount+=1;
            }
            check[data[i]]+=1;
        }

        int answer = curCount;

        for(int jump=0;jump<n;jump++){
            //jump의 위치를 뺀다.
            check[data[jump]]-=1;
            if(check[data[jump]]==0){
                curCount-=1;
            }
            //jump+k의 위치를 추가한다.
            if(check[data[(jump+k)%n]]==0){
                curCount+=1;
            }
            check[data[(jump+k)%n]]+=1;
        }



        System.out.println(answer);
    }
}