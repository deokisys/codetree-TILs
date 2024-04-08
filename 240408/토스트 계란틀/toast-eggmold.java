import java.util.*;
import java.io.*;

public class Main {
    static class Node{
        int sum, cnt;
        Node(){}
    }
    static class Pos {
        int r,c;
        Pos(int r, int c){
            this.r = r;
            this.c =c;
        }
    }
    public static void main(String[] args) throws Exception{
        

        //n,n 1,1크기의 계란틀
        //두계란 차이가 l이상, r이하면 선을 분리
        //합치고 다시 분리
        //합치고분리하면, 더하기/더한 개수 가된다.
            //소수점은 버리기.
        //bfs로 탐색 시작
            //l이상 r이하이면 합친다.
            //동시에 진행
                //bfs진행 visited를 번호로 표시
                //해당 번호에 맞게 합한값, 틀갯수를 저장
            //bfs종료후
                //n,n을 전부 돌면서 계산한 값 넣기

        // System.setIn(new FileInputStream("토스트계란틀/input.txt"));
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int L = sc.nextInt();
        int R = sc.nextInt();

        int[][] map = new int[n][n];

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                map[i][j] = sc.nextInt();
            }
        }


        int step = 0;
        while(true){

            int[][] visited = new int[n][n];
            ArrayList<Node> save = new ArrayList<>();
            int idx = 0;
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    if(visited[i][j]==0){
                        idx++;
                        bfs(map,visited,idx,save,i,j,L,R);
                    }
                }
            }
            if(idx==n*n) break;//분리 안됨

            //visited된 것에 맞게 다시 넣어준다.

            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    int sum = save.get(visited[i][j]-1).sum;
                    int cnt = save.get(visited[i][j]-1).cnt;
                    if(cnt!=1){
                        map[i][j] = sum/cnt;
                    }
                }
            }

            step++;
        }
        
        System.out.println(step);
    }
    private static void bfs(int[][] map, int[][] visited, int idx, ArrayList<Node> save, int r, int c, int L, int R) {
        Node saveTmp = new Node();
        visited[r][c] = idx;
        Queue<Pos> que =new ArrayDeque<>();
        que.add(new Pos(r,c));

        int[] dr = {-1,1,0,0};
        int[] dc = {0,0,-1,1};
        while(!que.isEmpty()){
            Pos cur= que.poll();

            saveTmp.sum+=map[cur.r][cur.c];
            saveTmp.cnt++;

            for(int d=0;d<4;d++){
                int zr = dr[d]+cur.r;
                int zc = dc[d]+cur.c;
                if(zr<0||zc<0||zr>=map.length||zc>=map.length) continue;
                if(visited[zr][zc]>0) continue;

                int gap = Math.abs(map[cur.r][cur.c]-map[zr][zc]);
                if(gap>=L&&gap<=R){
                    visited[zr][zc] = idx;
                    que.add(new Pos(zr,zc));
                }
            }
        }

        save.add(saveTmp);
    }
}