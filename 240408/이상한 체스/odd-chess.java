import java.util.*;
import java.io.*;

public class Main {

    static class Pos{
        int r,c,id;
        Pos(int r, int c){
            this.r = r;
            this.c = c;
        }
        Pos(int r, int c,int id){
            this.r = r;
            this.c = c;
            this.id = id;
        }
    }
    static int minCnt = Integer.MAX_VALUE;
    public static void main(String[] args) throws Exception{
        
        //1번은 한방향
        //상대는 못뛰어남음
        //1~5는 자신
        //6부터는 상대말
        //1은 한방향
        //2는 양방향
        //3은 위,오른
        //4는 양,위
        //5는 전방향
        Scanner sc = new Scanner(System.in);
        int n  = sc.nextInt();
        int m = sc.nextInt();
        int[][] map = new int[n][m];

        ArrayList<Pos> ches = new ArrayList<>();

        boolean[][] visited = new boolean[n][m];
        for(int i=0;i<n;i++){   
            for(int j=0;j<m;j++){
                map[i][j] = sc.nextInt();
                if(map[i][j]>=1&&map[i][j]<=5){
                    ches.add(new Pos(i,j,map[i][j]));
                    visited[i][j] = true;
                }else if(map[i][j]>5){
                    visited[i][j] = true;
                }
            }
        }


        play(map,ches,0,visited);

        System.out.println(minCnt);

    }

    static public void play(int[][] map,ArrayList<Pos> ches, int id,boolean[][] visited){
        // System.out.println(id);
        if(ches.size()==id){

            int cnt = 0;
            // System.out.println("-------");
            for(int i=0;i<map.length;i++){
                // System.out.println(Arrays.toString(visited[i]));
                for(int j=0;j<map[0].length;j++){
                    if(!visited[i][j]){
                        cnt++;
                    }
                }
            }
            // System.out.println(cnt);

            minCnt = Math.min(cnt,minCnt);
            return;
        }


        int[] dr = {-1,0,1,0};
        int[] dc= {0,1,0,-1};

        int[][][] check = {{{}},
            {{0},{1},{2},{3}},
            {{0,2},{1,3}},
            {{0,1},{1,2},{2,3},{3,0}},
            {{3,0,1},{0,1,2},{1,2,3},{2,3,0}},
            {{0,1,2,3}}
        };
        int idx = ches.get(id).id;
        //check[id][d]가 d 가 바라보는 방향에서 진행되는 경로들
        // System.out.println(check[idx].length+"만큼 반복한다.");
        for(int d=0;d<check[idx].length;d++){

            //check[id][d]에서 가는 방향으로 계속직진
            //채우기
            // System.out.println(d);
            ArrayList<Pos> save = new ArrayList<>();
            for(int k=0;k<check[idx][d].length;k++){
                int dir = check[idx][d][k];
                // System.out.println(dir);
                //6이상의 숫자가 나올때까지.
                int step = 0;
                
                while(true){
                    int zr = ches.get(id).r+dr[dir]*step;
                    int zc = ches.get(id).c+dc[dir]*step;

                    if(zr<0||zc<0||zr>=map.length||zc>=map[0].length) break;
                    if(map[zr][zc]>5) break;//상대말 만나면 종료
                    if(!visited[zr][zc]){
                        save.add(new Pos(zr,zc));
                        visited[zr][zc] = true;
                    }
                    step++;
                }
            }

            play(map,ches,id+1,visited);

            //다시 비우기
            for(Pos cur : save){
                visited[cur.r][cur.c] = false;
            }

        }
        
    }
}