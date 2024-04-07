import java.io.FileInputStream;
import java.util.*;

public class Main {
    static int minGap = Integer.MAX_VALUE;
    public static void main(String[] args) throws Exception{
        //아침, 저녁으로 n/2씩 처리
        //아침, 저녁 업무 강도 차이의 최솟값

        //n은 20
        //10개뽑기 - 오래걸리지 않나
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        int[][] map = new int[n][n];

        boolean[] check = new boolean[n];
        
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                map[i][j] = sc.nextInt();
            }
        }
        
        minGap = Integer.MAX_VALUE;

        mdCheck(map,check,0,0);
        
        System.out.println(minGap);
    }

    static public void mdCheck(int[][] map, boolean[] check,int idx,int morning){
        if(morning==check.length/2){
            
            calMap(map,check);
            
            return;
        }

        for(int i=idx;i<check.length;i++){
            check[i] = true;
            mdCheck(map,check,i+1,morning+1);
            check[i] = false;
        }

    }

    static public void calMap(int[][] map,boolean[] check){
        int morning = 0;
        int dinner = 0;

        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                if(check[i]==check[j]){
                    if(check[i]){
                        morning+=map[i][j];
                    }else{
                        dinner+=map[i][j];
                    }
                }
            }
        }

        minGap = Math.min(minGap,Math.abs(morning-dinner));
        
    }
}