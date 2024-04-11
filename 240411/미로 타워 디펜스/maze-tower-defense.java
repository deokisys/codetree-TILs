import java.io.*;
import java.util.*;

public class Main{

    static class Node{
        int n,cnt;
        Node(int n, int cnt){
            this.n = n;
            this.cnt = cnt;
        }
        @Override
        public String toString() {
            return "["+this.cnt+","+this.n+"]";
        }
    }
    static int answer = 0;
    public static void main(String[] args) throws Exception{
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());


        //상하 좌우, 주어진 공격 칸수 만큼 공격
        //비어진 공간은 몬스터가 앞으로 이동하여 빈칸을 채움

        //몬스터의 종류가 4번 이상 반복하면 몬스터가 동시에 사라짐
            //앞으로 땍기고, 4번 반복 몬스터 없을때까지 반복

        //삭제 완료후, 몬스터를 나열해서 같은 숫자가 붙으면 짝을 지어준다.
            //붙은 갯수, 숫자 형태로 바꿔서 넣는다.

        //새로 생긴 배열이 격자 범위를 넘기면 나머지는 무시한다.

        //이게 한라운드
        //라운드마다 삭제되는 몬스터의 번호를 점수에 합친다.
            //4개 사라지면 *4만큼 지운다

        //총 라운드는 100까지
        //지도 크기는 25*25 = 625
        
        int[][] map = new int[N][N];
        for(int i=0;i<N;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<N;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        answer = 0;
        for(int i=0;i<M;i++){
            st = new StringTokenizer(br.readLine());
            int d = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());

            attack(map,d,p);

            //땡기고, 반복해서 다시 만들기
            map = move(map);
            // print(map);
        }

        System.out.println(answer);
    }
    static void print(int[][] map){
        System.out.println("---");
        for(int i=0;i<map.length;i++){
            System.out.println(Arrays.toString(map[i]));
        }
    }

    static void attack(int[][] map,int d, int p){
        //가운데를 기준으로 d
        int r = (map.length-1)/2;
        int c = r;
         int[] dr = {0,1,0,-1};
         int[] dc = {1,0,-1,0};

        for(int step = 1;step<=p;step++){
            r+=dr[d];
            c+=dc[d];
            answer+=map[r][c];
            map[r][c] = 0;
        }
    }

    static int[][]  move(int[][] map){

        int[][] result = new int[map.length][map.length];
        boolean[][] visited = new boolean[map.length][map.length];
        //1렬로 만든다.
        Stack<Node> stack = new Stack<>();
        int[] line = new int[map.length*map.length];

        int[] dr = {0,1,0,-1};
        int[] dc = {1,0,-1,0};

        int d = 0;
        int r = 0;
        int c = 0;
        int idx = line.length-1;
        visited[r][c] = true;

        while(idx>0){
            int zr = r+dr[d];
            int zc = c+dc[d];
            if(zr<0||zc<0||zr>=map.length||zc>=map.length) {
                d+=1;
                d%=4;
                continue;
            }
            if(visited[zr][zc]) {
                d+=1;
                d%=4;
                continue;
            }
            visited[zr][zc] = true;
            idx--;
            r = zr;
            c = zc;
            
            //중복 제거하기
            if(map[r][c]==0) continue;

            if(stack.isEmpty()){
                stack.add(new Node(map[r][c],1));
            }else{
                if(stack.peek().n==map[r][c]){
                    int tmpCnt = stack.pop().cnt;
                    stack.add(new Node(map[r][c],tmpCnt+1));
                }else{
                    //peek해서 그게 4개 이상인지 확인후 넣는다.
                    if(stack.peek().cnt>=4){

                        //4개이상이 존재했으면 제거
                        Node same = stack.pop();
                        answer += (same.cnt*same.n);

                        //새로 넣을것이 이전것과 중복되는 번호인지 확인
                        if(!stack.isEmpty()&&stack.peek().n==map[r][c]){
                            int tmpCnt = stack.pop().cnt;
                            stack.add(new Node(map[r][c],tmpCnt+1));
                        }else{
                            stack.add(new Node(map[r][c],1));
                        }
                    }else{
                        //그냥 추가
                        stack.add(new Node(map[r][c],1));
                    }
                }
            }

            // System.out.println(stack);
        }
        
        //마지막 스택만 확인한다.
        if(stack.peek().cnt>=4){
            Node same = stack.pop();
            answer+=(same.cnt*same.n);
        }
        

        //중복개수 특징에 맞게 다시 값을 넣는다.
        idx = 1;
        while(!stack.isEmpty()){
            Node node = stack.pop();
            if(idx==line.length)break;
            line[idx]=node.cnt;
            if(idx+1==line.length)break;
            line[idx+1] = node.n;
            idx+=2;
        }
        
        //System.out.println(Arrays.toString(line));
    


        //뒤에서 부터 돌면서 result채우기

        visited = new boolean[map.length][map.length];
        r = 0;
        c = 0;
        d=0;
        visited[r][c] = true;
        result[r][c] = line[line.length-1];
        idx=line.length-1;
        while(idx>0){
            int zr = r+dr[d];
            int zc = c+dc[d];
            if(zr<0||zc<0||zr>=map.length||zc>=map.length) {
                d+=1;
                d%=4;
                continue;
            }
            if(visited[zr][zc]) {
                d+=1;
                d%=4;
                continue;
            }
            visited[zr][zc] = true;
            idx--;
            r = zr;
            c = zc;
            result[r][c] = line[idx];
            //중복 제거하기
        }
        return result;
    }
}