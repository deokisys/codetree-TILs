import java.util.*;
import java.io.*;

public class Main {
    static class Pos{
        int r,c;
        Pos(int r, int c){
            this.r = r;
            this.c =c ;
        }
    }

    static int noFireMax = 0;
    public static void main(String[] args) {
        //불은 상하좌우
        //방화벽 3개를 설치, 불이 퍼지는 영역을 최소로


        //지도는 8*8
        //불은 10개
        //빈한은 3개이상
        //64*63*62
            //249984
        //전체탐색
            //64
        //=15998976 1천만 = 완탐 할만 함.
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();

        int[][] map = new int[n][m];

        ArrayList<Pos> wallCan = new ArrayList<>();
        ArrayList<Pos> firePos = new ArrayList<>();

        int noFireArea = 0;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                map[i][j] = sc.nextInt();
                if(map[i][j]==0){
                    wallCan.add(new Pos(i,j));
                    noFireArea++;
                }
                if(map[i][j]==2){
                    firePos.add(new Pos(i,j));
                }
            }
        }

        //지도, 벽 설치가능한 위치 ,설치한 벽수
        buildWall(map,wallCan,0,0,noFireArea,firePos);

        System.out.println(noFireMax);
    }

    public static void buildWall(int[][] map,ArrayList<Pos> wallCan, int idx, int wall, int noFireArea, ArrayList<Pos> firePos){

        if(wall==3){
            goFire(map,firePos,noFireArea);
            return;
        }

        for(int i=idx;i<wallCan.size();i++){
            int r = wallCan.get(i).r;
            int c = wallCan.get(i).c;
            map[r][c]=1;
            buildWall(map,wallCan,i+1,wall+1,noFireArea-1,firePos);
            map[r][c] = 0;
        }
    }

    private static void goFire(int[][] map, ArrayList<Pos> firePos,int noFireArea) {
        Queue<Pos> que = new ArrayDeque<>();
        boolean[][] visited = new boolean[map.length][map[0].length];

        for(int i=0;i<firePos.size();i++){
            que.add(new Pos(firePos.get(i).r,firePos.get(i).c));
            visited[firePos.get(i).r][firePos.get(i).c] = true;
        }

        int[] dr = {-1,1,0,0};
        int[] dc = {0,0,-1,1};

        while(!que.isEmpty()){
            Pos cur = que.poll();

            for(int d=0;d<4;d++){

                int zr = cur.r+dr[d];
                int zc = cur.c+dc[d];
                if(zr<0||zc<0||zr>=map.length||zc>=map[0].length) continue;
                if(map[zr][zc]==1) continue;
                if(visited[zr][zc]) continue;
                visited[zr][zc] = true;
                noFireArea--;
                que.add(new Pos(zr,zc));
            }

        }
        noFireMax = Math.max(noFireArea,noFireMax);
        
    }
}