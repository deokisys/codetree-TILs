import java.util.*;

public class Main {
    static class Pos{
        int r,c;
        Pos(int r, int c){
            this.r = r;
            this.c = c;
        }
    }
    public static void main(String[] args) {
        
        //n병원크기,m필요한 병원
        //두 거리
            //x-x + y-y
        //m병원만 남기고 폐업
        //m병원에서 각 사람의 거리의 합이 최소가 되도록

        //n, m
        //병원 2
        //사람 1

        //병원의 위치를 저장
        //병원의 위치를 m개 지정하고, 거리를 계산

        //병원 크기
            //50*50
        //병원 13개
        //사람 100
        ArrayList<Pos> people = new ArrayList<>();
        ArrayList<Pos> hospital = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();

        int[][]map = new int[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                int tmp = sc.nextInt();
                if(tmp==1){
                    people.add(new Pos(i,j));
                    map[i][j] = tmp;
                }else if(tmp==2){
                    hospital.add(new Pos(i,j));
                }
                
            }   
        }


        select(map,people,hospital,0,m);
        System.out.println(minDist);
    }
    static int minDist = Integer.MAX_VALUE;
    static public void select(int[][] map, ArrayList<Pos> people,ArrayList<Pos> hospital,int idx, int m){
        if(idx==hospital.size()) return;
        if(m==0){

            //갯수 세기

            int cnt = count(map,people);

            minDist = Math.min(minDist, cnt);

            return;
        }

        for(int i=idx;i<hospital.size();i++){
            map[hospital.get(i).r][hospital.get(i).c] = 2;

            select(map,people,hospital,idx+1,m-1);

            map[hospital.get(i).r][hospital.get(i).c] = 0;
        }
    }


    public static int count(int[][] map, ArrayList<Pos> people){
        int result = 0;

        for(int i=0;i<people.size();i++){
            result+=bfs(map,people.get(i));
        }

        return result;
    }


    private static int bfs(int[][] map, Pos pos) {
        boolean[][] visited = new boolean[map.length][map[0].length];
        Queue<Pos> que = new ArrayDeque<>();
        visited[pos.r][pos.c] = true;
        que.add(new Pos(pos.r,pos.c));

        int[] dr = {-1,1,0,0};
        int[] dc = {0,0,-1,1};
        int step = 0;
        while(!que.isEmpty()){
            for(int s=0,size=que.size();s<size;s++){

                Pos cur = que.poll();

                if(map[cur.r][cur.c]==2){
                    return step;
                }

                for(int d=0;d<4;d++){
                    int zr = cur.r+dr[d];
                    int zc = cur.c+dc[d];
                    if(zr<0||zc<0||zr>=map.length||zc>=map[0].length) continue;
                    if(visited[zr][zc]) continue;
                    visited[zr][zc] = true;
                    que.add(new Pos(zr,zc));
                }
            }
            step++;
        }

        return 0;
    }
}