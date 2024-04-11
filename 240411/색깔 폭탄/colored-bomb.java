import java.util.*;
import java.io.*;

public class Main {

    static class Pos{
        int r,c;
        Pos(int r, int c){
            this.r = r;
            this.c = c;
        }
        @Override
        public String toString() {
            return "["+r+","+c+"]";
        }
    }
    static class Bomb{
        int redCnt=0;//빨간갯수
        ArrayList<Pos> bombList = new ArrayList<>();//폭탄위치들
        Pos pivot = null;//기준점

        Bomb(){}

        void addBomb(Pos newBomb,boolean isRed){
            if(isRed){
                this.redCnt+=1;
            }else{
                
                if(this.pivot==null){
                    this.pivot = newBomb;
                }else{
                    //pivot비교
                    if(this.pivot.r<newBomb.r){
                        this.pivot = newBomb;
                    }else if(this.pivot.r==newBomb.r){
                        if(newBomb.c<this.pivot.c){
                            this.pivot = newBomb;
                        }
                    }
                }
            }

            this.bombList.add(newBomb);
        }
    }
    static int N,M;
    static int answer = 0;
    public static void main(String[] args) throws Exception{
        // System.setIn(new FileInputStream("색깔폭탄/input2.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        //-1,0, 1이상 m 이하 숫자로 이루어진 n,n
        //-1 검, 0 빨폭, 1~m은 다른색 폭
        

        //폭탄 묶음이 없을때까지 반복

        //큰 폭탄 묶음 찾기
            //2개이상 모인거
            //같은색or빨간색 포함, 2개의 색만
            //빨간만있는것은 묶음x
            //상하좌우 인접

            //큰 폭탄 묶음은, 많은 수의 폭탄으로 이루어진것
            //여러개
                //1 빨간개 가장 적게 포함된것
                //2 빨간게 아니면서 행이 가장 큰 묶음
                    //열이 가장 작은칸
        //해당 폭탄을 제거
            //폭탄은 아래로 이동
            //검은돌은 안이동
        //반시계 방향 회전
        //중력이 다시 작용

        //라운드마다 터지는 폭탄수제곱 만큼 획득
            //총점수는?


        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());//폭탄의 종류 

        int[][] map = new int[N][N];
        for(int i=0;i<N;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<N;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        while(true){
            // System.out.println("----~~~~~~~~");
            // print(map);
            if(!group(map)) break;
            
            // print(map);

            gravity(map);
            map = turn(map);
            gravity(map);

            // print(map);
        }
        System.out.println(answer);
    }

    static boolean group(int[][] map){

        PriorityQueue<Bomb> pq = new PriorityQueue<>((a,b)->{
            
            if(b.bombList.size()==a.bombList.size()){

                if(a.redCnt==b.redCnt){
                    if(a.pivot.r==b.pivot.r){
                        //c가 작은것
                        return a.pivot.c-b.pivot.c;
                    }
                    //r이 큰것
                    return b.pivot.r-a.pivot.r;
                }
                //빨간게 적은것
                return a.redCnt-b.redCnt;
            }
            //전체 갯수 많은것
            return b.bombList.size()-a.bombList.size();
        });
        boolean[][] allVisited = new boolean[map.length][map.length];

        //bfs로 돌면서 그룹을 한다.

        for(int i=0;i<map.length;i++){
            for(int j=0;j<map.length;j++){
                if(!allVisited[i][j]&&map[i][j]>0&&map[i][j]<=M){
                    pq.add(bfs(map,i,j,allVisited));
                }
            }
        }

        if(pq.isEmpty()) return false;

        Bomb delBomb = pq.poll();
        if(delBomb.bombList.size()==1) return false;

        //해당 폭탄 전부 제거
        // System.out.println(delBomb.bombList);
        // System.out.println(delBomb.redCnt);
        answer += delBomb.bombList.size()*delBomb.bombList.size();
        for(Pos pos : delBomb.bombList){
            map[pos.r][pos.c] = M+1;
        }


        return true;
    }

    static Bomb bfs(int[][] map, int startR, int startC, boolean[][] allVisited){
        Bomb groupBomb = new Bomb();
        Queue<Pos> que = new ArrayDeque<>();
        boolean[][] visited = new boolean[map.length][map.length];
        visited[startR][startC] = true;
        allVisited[startR][startC] = true;

        que.add(new Pos(startR,startC));
        groupBomb.addBomb(new Pos(startR,startC), false);

        int[] dr = {-1,1,0,0};
        int[] dc = {0,0,-1,1};

        int bombId = map[startR][startC];

        while(!que.isEmpty()){
            Pos cur = que.poll();

            for(int d=0;d<4;d++){
                int zr = cur.r+dr[d];
                int zc = cur.c+dc[d];
                if(zr<0||zc<0||zr>=map.length||zc>=map.length) continue;
                if(visited[zr][zc]) continue;
                if(map[zr][zc]==-1) continue;
                if(map[zr][zc]!=0&&map[zr][zc]!= bombId) continue;
                if(map[zr][zc]>M) continue;//빈공간
                
                if(map[zr][zc]>0){
                    //빨강은 다른 구역에서도 이동이 가능해야 하므로 별도로 표시
                    allVisited[zr][zc] = true;
                }
                visited[zr][zc] = true;//현재 구역은 중복체크해야 함
                que.add(new Pos(zr,zc));
                
                groupBomb.addBomb(new Pos(zr,zc), map[zr][zc]==0);
            }
        }

        return groupBomb;

    }
    static void print(int[][] map){
        System.out.println("----");
        for(int i=0;i<map.length;i++){
            System.out.println(Arrays.toString(map[i]));
        }
    }

    static void gravity(int[][] map){
        for(int j=0;j<map.length;j++){
            int to = map.length-1;//이동시키는 위치
            for(int i=map.length-1;i>=0;i--){
                if(map[i][j]>=0&&map[i][j]<=M){
                    //폭탄이 존재함
                    if(to!=i){
                        map[to][j] = map[i][j];
                        map[i][j] = M+1;
                    }
                    to--;
                }
                if(map[i][j]==-1){
                    to=i-1;
                }
            }
        }
    }

    static int[][] turn(int[][] map){
        int[][] result = new int[map.length][map.length];


        for(int i=0;i<map.length;i++){
            for(int j=0;j<map.length;j++){
                result[map.length-1-j][i] = map[i][j];
            }
        }

        return result;
    }
}