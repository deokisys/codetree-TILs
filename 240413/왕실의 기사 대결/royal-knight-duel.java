import java.util.*;
import java.io.*;

//L,L
//1,1로 시작
//체스밖은 벽이다.
//자신의 마력으로 상대를 밀친다.

//기사
//r,c로 주어짐
    //r,c를 좌측 상단, h,w크기의 직사각형, 
    //체력은 k

//기사이동
    //상하좌우 하나
    //다른 기사 유무, 다른 기사가 존재, 연쇄적으로 밀린다.
        //벽이 존재하면 모든기사 못움직인다.
    //사라진 기사에게 명령은 반응 x

//대결 대미지
    //밀려난 기사는 피해
        //해당 기사가 이동한 곳 함정수만큼 피해를 입는다.
    //채력 이상의 대미지는 사라진다.
    //명령 받은 기사는 피해x
    //모두 밀린이후 대미지 입는다.

//q번의 왕 명령
    //생존한 기사들이 받은 총 대미지의 합은?



//기사들은 전부 bfs로 완탐을 진행한다.
    //d방향 한칸씩만 확인한다.
        //벽이면 이동x
    //이동 가능하면
        //bfs한 결과를 돌리면서 이동시킨다.
        //이동한 위치에 함정 있으면 해당 블럭 번호에서 체력을 뺀다
//N개의 기사마다 정보 저장
    //현재 채력
        //체력이 0이 되면 해당 블럭은 사라지는처리
public class Main {

    static class Knight{
        int r,c;
        int hp;
        int damage;
        Knight(int r,int c, int hp){
            this.r = r;
            this.c = c;
            this.hp = hp;
        }
    }
    static int[] dr ={-1,0,1,0};
    static int[] dc = {0,1,0,-1};

    static Knight[] knights;
    static int L,N;
    public static void main(String[] args) throws Exception {
        // System.setIn(new FileInputStream("왕실의기사대결/input3.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st  = new StringTokenizer(br.readLine());
        
        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken());

        knights = new Knight[N+1];
        //0 빈칸, 1 함정, 2 벽

        int[][] map = new int[L][L];//가시, 벽 유무 가시 1, 벽2
        int[][] knightMap = new int[L][L];//기사 지도

        for(int i=0;i<L;i++){
            st  = new StringTokenizer(br.readLine());
            for(int j=0;j<L;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for(int n=1;n<=N;n++){
            st  = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken())-1;
            int c = Integer.parseInt(st.nextToken())-1;
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            knights[n] = new Knight(r, c, k);
            for(int i=r;i<r+h;i++){
                for(int j=c;j<c+w;j++){
                    knightMap[i][j] = n;
                }
            }
        }
        // print(map);

        for(int q=0;q<Q;q++){
            st  = new StringTokenizer(br.readLine());
            int id = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            // System.out.println(id+":"+d);
            // print(knightMap);
            knightMap = move(map,knightMap,id,d);
            // print(knightMap);
            // printHP();
            // System.out.println("----");
        }


        //마무리 출력
        int answer = 0;
        for(int i=1;i<=N;i++){
            if(knights[i].hp>0){
                answer+=knights[i].damage;
            }
        }
        System.out.println(answer);
    }

    private static void print(int[][] knightMap) {
        System.out.println("---");
        for(int i=0;i<knightMap.length;i++){
            System.out.println(Arrays.toString(knightMap[i]));
        }
    }

    static class Path{
        int r,c;
        int id;//기사 번호
        Path(int r, int c, int id){
            this.r = r;
            this.c = c;
            this.id = id;
        }
    }
    static class Pos{
        int r,c;
        Pos(int r, int c){
            this.r = r;
            this.c = c;
        }
    }
    private static int[][] move(int[][] map, int[][] knightMap, int id, int moveD) {
        
        
        //bfs를 진행
        //4방향으로 진행한다.
        //벽이면 진행안한다.
        //moveD에 대해서만 특별히 진행  
        //따로 que에 들어가지 않는 체크용이다.
        //해당 위치가 2이면 불가능, 해당위차가 기사 존재&체력이0보다 크면 불가능
        
        Queue<Pos> que = new ArrayDeque<>();
        boolean[][] visited = new boolean[L][L];

        visited[knights[id].r][knights[id].c] = true;
        que.add(new Pos(knights[id].r,knights[id].c));

        boolean[] moveCheck = new boolean[N+1];
        moveCheck[id] = true;

        while(!que.isEmpty()){
            Pos cur = que.poll();

            for(int d=0;d<4;d++){
                int zr = cur.r+dr[d];
                int zc = cur.c+dc[d];
                if(zr<0||zc<0||zr>=L||zc>=L){
                    if(d==moveD) return knightMap;
                    continue;
                };
                if(visited[zr][zc]) continue;

                //같은 번호면 그냥 진행
                if(knightMap[zr][zc]==knightMap[cur.r][cur.c]){
                    visited[zr][zc] = true;
                    moveCheck[knightMap[zr][zc]] = true;;
                    que.add(new Pos(zr,zc));
                    continue;
                }

                //같은 번호는 아님, 근데 이동방향임
                if(moveD==d){
                    //더 나아가는 위치
                    if(map[zr][zc]==2) return knightMap;//해당 위치에 벽이 존재한다.
                    if(knightMap[zr][zc]>0){

                        //기사가 존재하는데 체력이 있음
                        if(knights[knightMap[zr][zc]].hp>0){
                            //다른 기사를 밀어야 함
                            visited[zr][zc] = true;
                            moveCheck[knightMap[zr][zc]] = true;
                            que.add(new Pos(zr,zc));
                        }


                        //체력이 없으면 넘긴다.
                        //if(knights[knightMap[zr][zc]].hp<=0) continue;

                        
                    }
                }
            }
        }


        //실제 이동 진행
        // System.out.println("이동한다");
        return moveMap(map,knightMap,moveCheck,moveD,id);

    }

    
    private static void printHP() {
        System.out.println("체력확인");
        for(int i=1;i<=N;i++){
            System.out.println(i+"-"+knights[i].hp);
        }
    }

    private static int[][] moveMap(int[][] map, int[][] knightMap, boolean[] moveCheck, int moveD,int moveId) {
        int[][] result = new int[L][L];

        
        boolean[] pivotCheck = new boolean[N+1];
        
        for(int i=0;i<L;i++){
            for(int j=0;j<L;j++){
                int targetId = knightMap[i][j];
                if(targetId>0 && moveCheck[targetId]){
                    if(!pivotCheck[targetId]){
                        pivotCheck[targetId] = true;

                        //기준점 이동
                        knights[targetId].r+=dr[moveD];
                        knights[targetId].c+=dc[moveD];
                    }

                    int zr = i+dr[moveD];
                    int zc = j+dc[moveD];
                    result[zr][zc] = targetId;
                    if(map[zr][zc]==1){
                        //이동 위치에 가시가 존재
                        //체력 -1진행
                        if(targetId!=moveId){
                            if(knights[targetId].hp>0){
                                knights[targetId].hp--;
                                knights[targetId].damage++;
                            }
                            // System.out.println(targetId+"번 다침"+knights[targetId].hp);
                        }
                    }   
                }else if(targetId>0 && knights[targetId].hp>0){
                    result[i][j] = targetId;
                }
            }
        }

        return result;

    }

    
}