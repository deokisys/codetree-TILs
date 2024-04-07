import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception{

        //현재 방향기준 왼쪽 안가면 좌회전 전진
        //왼쪽 벽 또는 간곳이면 다시 좌회전 확인
        //4방향 했는데 못가면, 후진
        //후진도 못하면 정지

        //거쳤던 도로의 총 면적은?
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();

        int carX = sc.nextInt();
        int carY = sc.nextInt();
        int d = sc.nextInt();//0북,1동,2남,3서

        

        int[][] map = new int[n][m];
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                map[i][j] = sc.nextInt();
            }
        }

        int answer = start(map,carX,carY,d);

        System.out.println(answer);
    }

    static public int start(int[][] map , int carX,int carY, int d){
        int[] dr = {-1,0,1,0};
        int[] dc = {0,1,0,-1};

        boolean[][] visited = new boolean[map.length][map[0].length];
        visited[carX][carY] = true;
        int result = 1;

        while(true){

            boolean isMove = false;
            //왼쪽 확인
            for(int s=1;s<=4;s++){
                int leftD = d-s;
                if(leftD<0){
                    leftD+=4;
                }
    
                int leftX = carX+dr[leftD];
                int leftY = carY+dc[leftD];
                if(leftX<0||leftY<0||leftX>=map.length||leftY>=map[0].length) continue;
                if(map[leftX][leftY]==1) continue;
                if(visited[leftX][leftY]) continue;
                isMove = true;
                visited[leftX][leftY] = true;
                carX = leftX;
                carY = leftY;
                d = leftD;
                result+=1;
                break;
            }

            if(!isMove){
                //후진

                int backD = (d+2)%4;

                int backX = carX+dr[backD];
                int backY = carY+dc[backD];
                if(backX<0||backY<0||backX>=map.length||backY>=map[0].length) {
                    break;
                }
                if(map[backX][backY]==1){
                    break;
                }

                if(!visited[backX][backY]){
                    result+=1;
                }
                carX = backX;
                carY = backY;
            }
        }
        return result;
    }
}