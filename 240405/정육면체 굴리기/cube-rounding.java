import java.util.*;
import java.io.*;

//9:10
public class Main {

    static public class Dice{
        int[][] map;
        Dice(){
            this.map = new int[4][3];
        }
        public String toString(){

            StringBuilder sb = new StringBuilder();
            for(int i=0;i<map.length;i++){
                sb.append(Arrays.toString(map[i]));
                sb.append("\n");
            }
            return sb.toString();
        }

        public int getBottom(){
            return this.map[1][1];
        }

        public void setBottom(int n){
            this.map[1][1] = n;
        }

        public int getTop(){
            return this.map[3][1];
        }

        public void move(int d){
            if(d==1){
                this.moveEast();
            }else if(d==2){
                this.moveWest();
            }else if(d==3){
                this.moveNorth();
            }else{
                this.moveSouth();
            }
        }

        public void moveEast() {
            //왼쪽값 저장
            int tmp = map[1][0];

            for(int i=0;i<2;i++){
                map[1][i] = map[1][i+1];
            }
            map[1][2] = map[3][1];

            map[3][1] = tmp;
        }

        public void moveWest() {
            //왼쪽값 저장
            int tmp = map[1][2];

            for(int i=2;i>0;i--){
                map[1][i] = map[1][i-1];
            }
            map[1][0] = map[3][1];

            map[3][1] = tmp;
        }

        public void moveSouth() {
            //
            int tmp = map[0][1];

            for(int i=0;i<3;i++){
                map[i][1] = map[i+1][1];
            }

            map[3][1] = tmp;
        }

        public void moveNorth() {
            //왼쪽값 저장
            int tmp = map[3][1];

            for(int i=3;i>0;i--){
                map[i][1] = map[i-1][1];
            }

            map[0][1] = tmp;
        }
    }
    public static void main(String[] args) throws Exception{
       
        //n,m 격자판,
        //주사위 각 면은 0

        //밖으로 이동x
            //해당 시도는 무시
        
        //주사위 0
            //바닥면 -> 주사위 복제
            //해당 칸은 0
        //0이 아니면
            //주사위바닥 -> 바닥
            //주사위 그대로
        

        //이동할때마다 정육면체 상단 숫자 출력
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int H = Integer.parseInt(st.nextToken());
        int W = Integer.parseInt(st.nextToken());
        int startR = Integer.parseInt(st.nextToken());
        int startC = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        int[][] map = new int[H][W];
        for(int i=0;i<H;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<W;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }        
        }

        st = new StringTokenizer(br.readLine());

        //1동,2서,3북,4남
        int[] dr = {0,0,0,-1,1};
        int[] dc = {0,1,-1,0,0};

        Dice dice = new Dice();

        //
        // dice.map[0][1] = 1;
        // dice.map[1][0] = 2;
        // dice.map[1][1] = 3;
        // dice.map[1][2] = 4;
        // dice.map[2][1] = 5;
        // dice.map[3][1] = 6;

        // System.out.println(dice);

        // dice.move(4);

        // System.out.println(dice);


        for(int i=0;i<k;i++){
            //이동
            int move = Integer.parseInt(st.nextToken());
            
            int zr = startR+dr[move];
            int zc = startC+dc[move];

            if(zr>=0&&zc>=0&&zr<H&&zc<W){
                //이동을 진행
                // System.out.println("-------------------go"+move);
                // System.out.println(startR+","+startC);
                // System.out.println("여기로 이동"+zr+","+zc);
                

                startR = zr;
                startC = zc;
                
                //주사위 아래가 0
                //바닥면을 주사위 아래로 복사
                //바닥면은 0으로 처리
                //주사위아래가 0 아님
                //주사위아래 -> 바닥면으로 복사
                // System.out.println("------dice");
                // System.out.println(dice);
                dice.move(move);
                // System.out.println("------dice 이동");
                // System.out.println(dice);
                if(map[startR][startC]==0){
                    //칸의 수가 0
                    //주사위 바닥 수를 칸으로 이동
                    map[startR][startC] = dice.getBottom();
                }else{
                    dice.setBottom(map[startR][startC]);
                    map[startR][startC] = 0;
                }
                // System.out.println("------dice 변화");
                // System.out.println(dice);
                // print(map);

                //상단 출력
                System.out.println(dice.getTop());
            }
        }
    }
    private static void print(int[][] map) {
        System.out.println("------map");
        for(int i=0;i<map.length;i++){
            System.out.println(Arrays.toString(map[i]));
        }    
    }
}