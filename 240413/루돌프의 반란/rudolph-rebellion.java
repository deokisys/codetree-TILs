import java.util.*;
import java.io.*;

//1~P 산타
// n,n크기
    //r,c
    //1,1시작 - ㅗ
//m개의 턴
    //루돌프 -> 산타 이동(1~p)
    //기절, 이탈한 산타 이동 못ㅎ람
    //거리 = 뺀거 제곱
//루돌프
    //가장 가까운 산타로 1칸 돌진
        //탈락하지 않은 산타중 가까운 산타
    //가까운 산타 2명이상
        //r큰산타, c큰산타
    //상하좌우대각선 8방향
        //대각선도 1칸전진
    //우선순위가 높은 산타를 향해 가까워지는 방향으로 한칸 돌진

//산타
    //1~p
    //기절, 탈락 산타는 못감
    //루돌프와 가까워지는 방향으로 1칸 이동
    //산타 중복, 밖으로는 못감
    //못움직이면 움직x
        //안가까워지는곳 x
    //산타는 상하좌우만
        //상,우,하,좌 우선순위에 맞춰 움직

//충돌
    //산타, 루돌프 같은위치
    //루돌프 충돌
        //해당 산타 c점수
        //산타는 루돌프 방향으로 c이동
    //산타 충돌
        //산타 d점수
        //산타 이동 반대 방향으로 d이동
    //밀리는것은 중간에 충돌x
    //게임밖으로 나가면 탈락
    //밀려난칸에 산타면 상호작용
//상호작용
    //해당 산타는 1칸 밀림, 연쇄적이다.

//기절
    //산타는 루돌프와 충돌하면 기절
    //k+1턴까지 기절, k+2부터 정상
    //기절해도, 충돌, 상호작용 가능
//종료
    //p 산타 모두 탈락하면 즉시 게임 종료
    //탈락 하지 않은 산타는 1점 추가
//출력
    //각 산타가 얻은 최종 점수 


//자료구조
    //지도 = 산타의 번호를 저장
    //루돌프
        //현재위치
    //산타는 30명뿐,
        //30개 전부 돌면서 해도 충분한 시간임
        //산타 객체로 관리
    //산타
        //얻은 점수
        //현재위치
        //기절한 턴
        //탈락여부
public class Main {

    static class Rudolf{
        int r,c;
        Rudolf(int r,int c){
            this.r = r;
            this.c = c;
        }
    }

    static class Santa{
        int r,c;
        int point;
        boolean isStun;
        int stunTurn;
        boolean isOut;
        Santa(int r,int c){
            this.r = r;
            this.c = c;
        }

    }

    static Santa[] santas;//산타 관리
    static int[][] map;//산타의 번호가 저장되서, 위치바로 파악용

    static int N,M,P,C,D;
    public static void main(String[] args) throws Exception{
        // System.setIn(new FileInputStream("루돌프의반란/input3.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());


        N  = Integer.parseInt(st.nextToken());
        M  = Integer.parseInt(st.nextToken());
        P  = Integer.parseInt(st.nextToken());
        C  = Integer.parseInt(st.nextToken());
        D  = Integer.parseInt(st.nextToken());

        map = new int[N][N];

        st = new StringTokenizer(br.readLine());
        int rudolfR = Integer.parseInt(st.nextToken());
        int rudolfC = Integer.parseInt(st.nextToken());
        Rudolf rudolf = new Rudolf(rudolfR-1, rudolfC-1);

        santas = new Santa[P+1];
        for(int p=1;p<=P;p++){
            st = new StringTokenizer(br.readLine());
            int id = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            santas[id] = new Santa(r-1, c-1);
            map[r-1][c-1] = id;
        }

        for(int m=1;m<=M;m++){
            //루돌프 이동
            if(!rudolfMove(rudolf,m)){
                //System.out.println("살아남은 산타 없는디?");
                break;
            }
            //산타이동
            for(int p=1;p<=P;p++){
                if(santas[p].isOut) continue;
                if(santas[p].isStun){
                    //기절에서 일어나는지 확인한다.
                    if(santas[p].stunTurn+2==m){
                        //이제 기절에서 일어난다.
                        //System.out.println(p+"번 기절에서 깸");
                        santas[p].isStun = false;
                    }else{
                        //아직 기절중
                        continue;
                    }
                }
                santaMove(rudolf,santas[p],m);
            }
            //점수 1점 추가
            for(int p=1;p<=P;p++){
                if(santas[p].isOut) continue;
                santas[p].point++;
            }

            //점수 확인용
            // System.out.println(m+"-----");
            // for(int p=1;p<=P;p++){
            //     System.out.print(santas[p].point+",");
            // }
            // System.out.println("");
            // System.out.println("루돌프"+rudolf.r+","+rudolf.c);
            // print();
        }
        
        StringBuilder sb = new StringBuilder();
        //출력하기
        for(int p=1;p<=P;p++){
            sb.append(santas[p].point).append(" ");
        }
        System.out.println(sb.toString());
    }
    private static void print() {
        System.out.println("지도");
        for(int i=0;i<map.length;i++){
            System.out.println(Arrays.toString(map[i]));
        }
    }
    private static boolean rudolfMove(Rudolf rudolf, int turn) {
        //가까운 산타 찾기
        int santaId = 0;

        int santaR = -1;
        int santaC = -1;
        int santaDist = Integer.MAX_VALUE;
        for(int p=1;p<=P;p++){
            //탈락 산타 넘기기
            if(santas[p].isOut) continue;
            int dist = getDist(rudolf.r,rudolf.c,santas[p].r,santas[p].c);
            if(dist<santaDist){
                santaId = p;
                santaR = santas[p].r;
                santaC = santas[p].c;
                santaDist = dist;
            }else if(dist==santaDist){
                //r좌표 큰것으로
                if(santaR<santas[p].r){
                    santaId = p;
                    santaR = santas[p].r;
                    santaC = santas[p].c;
                }else if(santaR==santas[p].r){
                    //c가 큰것으로
                    if(santaC<santas[p].c){
                        santaId = p;
                        santaR = santas[p].r;
                        santaC = santas[p].c;
                    }
                }
            }
        }

        //이동 가능한 산타가 없음 = 종료
        if(santaId==0) return false;

        //8방향에서 santaDist보다 낮아지는 위치로 돌진한다.
        int[] dr = {-1,-1,-1,0,1,1,1,0};
        int[] dc = {-1,0,1,1,1,0,-1,-1};

        int moveD = -1;
        int moveR = -1;
        int moveC = -1;
        for(int d =0 ;d<8;d++){
            int zr = rudolf.r+dr[d];
            int zc = rudolf.c+dc[d];
            if(zr<0||zc<0||zr>=N||zc>=N) continue;
            int dist =  getDist(zr,zc,santas[santaId].r,santas[santaId].c);
            if(dist<santaDist){
                santaDist = dist;
                moveD = d;
                moveR = zr;
                moveC = zc;
            }
        }

        //moveD를 향해 돌진
        rudolf.r = moveR;
        rudolf.c = moveC;

        //충돌 확인
        if(map[rudolf.r][rudolf.c]>0){
            // System.out.println("루돌프 이동 충돌"+map[rudolf.r][rudolf.c]);
            //충돌됬다!
            int santaP = map[rudolf.r][rudolf.c];
            //해당 산타 점수 획득
            santas[santaP].point+=C;

            //산타를 루돌프 이동 방향으로 점프시킨다.
                //C만큼 점프
            int zr = santas[santaP].r+dr[moveD]*C;
            int zc = santas[santaP].c+dc[moveD]*C;

            if(zr<0||zc<0||zr>=N||zc>=N){
                //탈락
                map[rudolf.r][rudolf.c] = 0;
                santas[santaP].isOut = true;
            }else{
                //기절 처리
                // System.out.println(santaP+"번 기절함"+turn);
                santas[santaP].isStun = true;
                santas[santaP].stunTurn = turn;

                map[santas[santaP].r][santas[santaP].c] = 0;
                //이동처리
                santas[santaP].r = zr;
                santas[santaP].c = zc;

                //비어있으면 그냥 이동하고 끝
                if(map[zr][zc]==0){
                    map[zr][zc] = santaP;
                }else{
                    //해당 산타번호가 zr,zc로 이동한다는 뜻
                    collision(santaP,zr,zc,moveD);
                }
            }
        }

        return true;
    }
    private static void santaMove(Rudolf rudolf, Santa santa, int turn) {
        //산타 이동
        int[] dr = {-1,-1,-1,0,1,1,1,0};
        int[] dc = {-1,0,1,1,1,0,-1,-1};

        int dist = getDist(rudolf.r, rudolf.c, santa.r ,santa.c);

        int moveR = -1;
        int moveC = -1;
        int moveD = -1;
        
        //상,우,하,좌순서
        for(int d=1;d<8;d+=2){
            int zr = santa.r+dr[d];
            int zc = santa.c+dc[d];
            if(zr<0||zc<0||zr>=N||zc>=N) continue;
            if(map[zr][zc]>0) continue;
            int moveDist = getDist(rudolf.r, rudolf.c, zr, zc);
            if(moveDist<dist){
                dist= moveDist;
                moveR = zr;
                moveC = zc;
                moveD = d;
            }
        }

        if(moveD==-1) return;//움직이지 않으면 가만히 있는다.

        //이동 처리
        int santaP = map[santa.r][santa.c];
        map[santa.r][santa.c] = 0;

        map[moveR][moveC] = santaP;
        santa.r = moveR;
        santa.c = moveC;

        //충돌 확인
        if(rudolf.r==moveR&&rudolf.c==moveC){
            //충돌남
            //moveD+2만큼 이동한다.
            moveD = (moveD+4)%8;

            //점수 획득
            santa.point+=D;

            map[santa.r][santa.c] = 0;
            // System.out.println(santaP+"번 기절함");
            santa.isStun = true;
            santa.stunTurn = turn;
            
            //해당 산타 moveD로 D만큼 점프 시킨다.
            int zr = santa.r+dr[moveD]*D;
            int zc = santa.c+dc[moveD]*D;
            if(zr<0||zc<0||zr>=N||zc>=N){
                //탈락함
                santa.isOut = true;
            }else{
                //충돌 진행
                collision(santaP, zr, zc, moveD);
            }
        }

    }
    

    
    private static void collision(int santaP, int zr, int zc,int moveD) {
        int[] dr = {-1,-1,-1,0,1,1,1,0};
        int[] dc = {-1,0,1,1,1,0,-1,-1};

        santas[santaP].r = zr;
        santas[santaP].c = zc;
        //snataP가 zr,zc로 이동한다는 뜻
        if(map[zr][zc]==0){
            map[zr][zc] = santaP;
            
        }else{
            //다른 산타가 존재함
            int moveSanta = map[zr][zc];//미리 저장
            map[zr][zc] = santaP;
            //다른 산타가 이동하는 위치 확인
            int zr2 = santas[moveSanta].r+dr[moveD];
            int zc2 = santas[moveSanta].c+dc[moveD];
            
            if(zr2<0||zc2<0||zr2>=N||zc2>=N){
                //다른 산타가 탈락함
                santas[moveSanta].isOut = true;
            }else{
                //연속으로 발생하는지 확인
                collision(moveSanta, zr2, zc2, moveD);
            }

        }
    }
    private static int getDist(int r1, int c1, int r2, int c2) {
        return (int)Math.pow(r1-r2, 2)+(int)Math.pow(c1-c2, 2);
    }
}