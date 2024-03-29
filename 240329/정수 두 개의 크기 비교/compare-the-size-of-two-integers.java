import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) {
        //정수 두개를 선택하고 작은게 큰것의 90%보다 같거나 큰것?
        //정렬을 한다.
        //큰거부터 시작해서 90%의 값을 만든다.
        //이분법을 이용해 해당 위치를 찾는다.
            //정수갯수-위치가 만족하는 경우의 수가 된다.

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] ns = new int[n];
        for(int i=0;i<n;i++){
            ns[i] = sc.nextInt();
        }
        Arrays.sort(ns);

        int answer = 0;
        int self = 1;
        for(int i=n-1;i>=0;i--){
            int nsCheck = (int)Math.ceil((double)ns[i]*9/10);
            //System.out.println(ns[i]+","+nsCheck);
            answer+=(n-find(ns,nsCheck))-self;
            self+=1;//이전에 계산한 자기자신들은 제외한다.
        }
        System.out.println(answer);
    }
    public static int find(int[] arr, int n){
        int left = 0;
        int right = arr.length-1;

        //1,2,3,3,4,5
        //n보다 크거나 같은 위치
        while(left<=right){
            int mid = (left+right)/2;

            if(n<=arr[mid]){
                right = mid-1;
            }else{
                left = mid+1;
            }
        }

        return left;
    }
}