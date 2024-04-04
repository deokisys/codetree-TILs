import java.util.*;
import java.io.*;

public class Main {

    static class Pos{
        int r,c,red;
        Pos(int r, int c, int red){
            this.r = r;
            this.c = c;
            this.red = red;
        }
    }
    public static void main(String[] args) throws Exception{
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int X = Integer.parseInt(st.nextToken());
        int Y = Integer.parseInt(st.nextToken());

        int[][] map = new int[1001][1001];

        for(int i=0;i<N;i++){
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());

            map[x][y] = 1;//빨간점
        }

        PriorityQueue<Pos> pq = new PriorityQueue<>((a,b)->a.red-b.red);
        int[][] visited = new int[1001][1001];
        for(int i=0;i<=1000;i++){
            for(int j=0;j<=1000;j++){
                visited[i][j] = Integer.MAX_VALUE;
            }
        }

        visited[X][Y] = 0;
        pq.add(new Pos(X,Y,0));
        int[] dr = {-1,1,0,0};
        int[] dc = {0,0,-1,1};


        while(!pq.isEmpty()){
            Pos cur = pq.poll();

            if(cur.r==0&&cur.c==0){
                System.out.println(cur.red);
                return;
            }

            for(int d=0;d<4;d++){
                int zr = dr[d]+cur.r;
                int zc = dc[d]+cur.c;

                if(zr<0||zc<0||zr>1000||zc>1000) continue;
                
                if(map[zr][zc]==1){
                    if(visited[zr][zc] <= cur.red+1) continue;

                    visited[zr][zc] = cur.red+1;
                    pq.add(new Pos(zr,zc,cur.red+1));
                }else{
                    if(visited[zr][zc] <= cur.red) continue;
                    visited[zr][zc] = cur.red;
                    pq.add(new Pos(zr,zc,cur.red));
                }
            }
        }

    }
}