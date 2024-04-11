import java.util.*;
import java.io.*;

public class Main {
    static class Pos{
        int r, c;
        Pos(int r, int c){
            this.r  = r;
            this.c = c;
        }
        @Override
        public String toString() {
            return "["+r+","+c+"]";
        }
    }

    static boolean[][] check;
    public static void main(String[] args) throws Exception{
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st= new StringTokenizer(br.readLine());


        //n,n격자
        //높이 1증가, 씨앗만 있으면 1로 키움
        //좌하단 4개칸만 주어짐
        //이동규칙 = 방향, 이동칸수
            //1~8 우,우상,상,좌상,좌,좌하,하,우하
            //이동칸수만큼 이동
        //끝과 끝은 연결, 밖으로 나가면 반대쪽에서 나옴

        //특수를 이동 규칙에 따라 이동
        //이동시킨후 , 해당 땅에 영양제 투입
        //투입후 대각선으로 인접한 방향 1나무만큼 높이가 더 성장
            //격자밖은 안센다.
        //투입한 리브로수를 제외, 높이가 2이상인 나무는 높이 2제거
            //해당 위치에 영영자를 올린다.


        //각 년도 이동 규칙,
        //모든 이동을 하고 나서, 남은 리브로수 높이의 총합

        Queue<Pos> powers = new ArrayDeque<>();
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int[][] map = new int[N][N];

        for(int i=0;i<N;i++){
            st= new StringTokenizer(br.readLine());
            for(int j=0;j<N;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        
        //초기 영양제위치 추가
        powers.add(new Pos(N-2,0));
        powers.add(new Pos(N-2,1));
        powers.add(new Pos(N-1,0));
        powers.add(new Pos(N-1,1));

        for(int m=0;m<M;m++){
            st= new StringTokenizer(br.readLine());
            int d = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());

            // System.out.println("------"+m);
            // print(map);
            //이동
            check = new boolean[N][N];
            move(map,powers,d-1,p);
            //새로운 영양제 찾기
            powers = newPower(map);
        }

        //나무 갯수 세기
        int answer =0;
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                answer+=map[i][j];
            }
        }
        // print(map);
        System.out.println(answer);
    }

    static void print(int[][] map){
        System.out.println("---");
        for(int i=0;i<map.length;i++){
            System.out.println(Arrays.toString(map[i]));
        }
    }

    static void move(int[][] map, Queue<Pos> powers, int d, int p){
       //우,우상,상,좌상,좌,좌하,하,우하
        int[] dr = {0,-1,-1,-1,0,1,1,1};
        int[] dc = {1,1,0,-1,-1,-1,0,1};
        
        //이동하고 1더하기
        for(int s = 0,size = powers.size();s<size;s++){
            Pos cur = powers.poll();
            int r = cur.r;
            int c = cur.c;
            for(int step=0;step<p;step++){
                r+=dr[d];
                c+=dc[d];
                if(r<0){
                    r=map.length-1;
                }
                if(r>=map.length){
                    r = 0;
                }
                if(c<0){
                    c = map.length-1;
                }
                if(c>=map.length){
                    c = 0;
                }
            }

            //r,c로 이동함
            map[r][c]++;//1증가
            powers.add(new Pos(r,c));
            check[r][c] = true;
        }

        //print(map);

        //해당위치 대각선만큼 확인


        while(!powers.isEmpty()){
            Pos cur = powers.poll();
            
            //대각선만 확인
            int cnt = 0;
            for(int cd=1;cd<8;cd+=2){
                int zr = cur.r+dr[cd];
                int zc = cur.c+dc[cd];
                if(zr<0||zc<0||zr>=map.length||zc>=map.length) continue;
                if(map[zr][zc]>0){
                    cnt+=1;
                }
            }

            //r,c로 이동함
            map[cur.r][cur.c]+=cnt;//대각선 1이상인 갯수만큼 증가
        }

        //print(map);
    }


    static Queue<Pos> newPower(int[][] map){
        Queue<Pos> powers = new ArrayDeque<>();


        for(int i=0;i<map.length;i++){
            for(int j=0;j<map.length;j++){
                if(!check[i][j]&&map[i][j]>=2){
                    map[i][j]-=2;
                    powers.add(new Pos(i,j));
                }
            }
        }
        return powers;
    }
}