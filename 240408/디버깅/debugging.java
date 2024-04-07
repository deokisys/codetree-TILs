import java.util.*;
import java.io.*;
public class Main {
    static class Pos{
        int r,c;
        Pos(int r, int c){
            this.r = r;
            this.c = c;
        }
        public String toString(){
            return "["+r+","+c+"]";
        }
    }
    static int minCnt = Integer.MAX_VALUE;
    public static void main(String[] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();//가로
        int m = sc.nextInt();//세로위치
        int h = sc.nextInt();//세로

        int map[][] = new int[h+1][n+1];

        for(int i=0;i<m;i++){
            int a = sc.nextInt();
            int b = sc.nextInt();
            //a번째 행의 b번째 열에서 가로선 생김
            map[a][b] = 1;
        }

        ArrayList<Pos> checkList = new ArrayList<>();
        for(int i=1;i<=h;i++){
            for(int j=1;j<n;j++){
                //왼쪽 가로줄이 없는경우
                if(map[i][j]==0&&map[i][j-1]==0){
                    if(map[i][j+1]==0){
                        //오른쪽에 선이 없음
                        checkList.add(new Pos(i,j));
                    }
                }

                //오른쪽 가로줄이 없는경우
            }
        }

        //10*30
        dfs(map,checkList,0,0);

        if(minCnt==Integer.MAX_VALUE){
            System.out.println(-1);
        }else{
            System.out.println(minCnt);
        }
    }

    static public void dfs(int[][] map, ArrayList<Pos> checkList,int idx,int cnt){
        if(checkList.size()==idx) return;
        if(cnt>3){
            return;
        }
        //이미 최대를 넘음
        if(minCnt<cnt){
            return;
        }

        //확인
        if(check(map)){
            minCnt = cnt;
            return;
        }

        for(int i=idx;i<checkList.size();i++){

            //넣을 수 있는지 확인

            int r = checkList.get(i).r;
            int c = checkList.get(i).c;
            if(map[r][c]==0&&map[r][c-1]==0){
                if(map[r][c+1]==0){
                    //넣기
                    map[r][c] = 1;
                    dfs(map,checkList,idx+1,cnt+1);
                    //넣은거 빼기
                    map[r][c] = 0;
                }
            }
        }
        
    }

    static public boolean check(int[][] map){
        //1열의 1번부터 시작한다.
        for(int n=1;n<map[0].length;n++){
            int cur = n;
            for(int h=1;h<map.length;h++){
                if(map[h][cur]==0){
                    //왼쪽을 확인
                    if(map[h][cur-1]==1){
                        cur--;
                    }
                }else{
                    //오른쪽 이동
                    cur++;
                }
            }

            if(cur!=n){
                return false;
            }
        }
        return true;
    }
}