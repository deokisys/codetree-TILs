import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int N = sc.nextInt();
        int M = sc.nextInt();

        int[] map = new int[M];
        int answer = 0;
        int[] leftMax = new int[M];
        for(int i=0;i<M;i++){
            map[i] = sc.nextInt();
            if(i==0){
                leftMax[i] = map[i];
            }else{
                leftMax[i] = Math.max(map[i],leftMax[i-1]);
            }
        }
        int rightMax = 0;
        for(int i=M-1;i>=0;i--){
            if(i==M-1){
                rightMax = map[i];
            }else{
                //map[i] < min(leftMax[i-1],rightMax) => min(left,right)-map[i]
                if(i!=0 && map[i]<=Math.min(leftMax[i-1],rightMax)){
                    answer+=Math.min(leftMax[i-1],rightMax)-map[i];
                }
                rightMax = Math.max(rightMax,map[i]);
            }
        }

        System.out.println(answer);

    }
}