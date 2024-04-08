import java.io.FileInputStream;
import java.util.*;

public class Main {
    static class Pos{
        int r,c;
        Pos(int r, int c){
            this.r = r;
            this.c = c;
        }
    }
    public static void main(String[] args) throws Exception{
        
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

        // System.setIn(new FileInputStream("병원거리/input.txt"));
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


        //select(map,people,hospital,0,m);
        select2(map,people,hospital,0,m,new ArrayList<>());
        System.out.println(minDist);
    }
    

    static public void select2(int[][] map, ArrayList<Pos> people,ArrayList<Pos> hospital,int idx, int m, ArrayList<Pos> save){
        
        if(m==0){

            int totalCnt = 0;
            for(int p=0;p<people.size();p++){
                int cnt = 1000;
                for(int h=0;h<save.size();h++){
                    int tmp = Math.abs(people.get(p).r-save.get(h).r)+Math.abs(people.get(p).c-save.get(h).c);
                    cnt = Math.min(tmp,cnt);
                }
                totalCnt+=cnt;
            }

            minDist = Math.min(minDist, totalCnt);

            return;
        }
        if(idx==hospital.size()) return;

        for(int i=idx;i<hospital.size();i++){
            save.add(new Pos(hospital.get(i).r, hospital.get(i).c));

            select2(map,people,hospital,i+1,m-1,save);

            save.remove(save.size()-1);
        }
    }

    static int minDist = Integer.MAX_VALUE;
    static public void select(int[][] map, ArrayList<Pos> people,ArrayList<Pos> hospital,int idx, int m){
        
        if(m==0){

            //갯수 세기
            // System.out.println("-----");
            // for(int i=0;i<map.length;i++){
            //     System.out.println(Arrays.toString(map[i]));
            // }
            int cnt = count(map,people);

            minDist = Math.min(minDist, cnt);

            return;
        }
        if(idx==hospital.size()) return;

        for(int i=idx;i<hospital.size();i++){
            map[hospital.get(i).r][hospital.get(i).c] = 2;

            select(map,people,hospital,i+1,m-1);

            map[hospital.get(i).r][hospital.get(i).c] = 0;
        }
    }


    public static int count(int[][] map, ArrayList<Pos> people){
        int result = 0;

        for(int i=0;i<people.size();i++){
            result+=bfs(map,people.get(i));
            if(minDist< result) return minDist;
        }

        return result;
    }


    private static int bfs(int[][] map, Pos start) {
        boolean[][] visited = new boolean[map.length][map[0].length];
        Queue<Pos> que = new ArrayDeque<>();
        visited[start.r][start.c] = true;
        que.add(new Pos(start.r,start.c));

        int[] dr = {-1,1,0,0};
        int[] dc = {0,0,-1,1};
        while(!que.isEmpty()){
            Pos cur = que.poll();

            if(map[cur.r][cur.c]==2){
                return Math.abs(cur.r-start.r)+Math.abs(cur.c-start.c);
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

        return 0;
    }
}