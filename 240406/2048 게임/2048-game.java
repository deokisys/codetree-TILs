import java.util.*;
import java.io.*;

public class Main{

    static int maxBlock = 0;
    public static void main(String[] args) throws Exception{
        //4,4
        //연속으로 안합쳐진다.
            //2,2,2,2
                //4,4
            //2,2,4
                //4,4
        
        //5번 움직인 후 최대값 구하기.
        // System.out.println("???E?FE?");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());

        int[][] map = new int[n][n];
        for(int i=0;i<n;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<n;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        maxBlock = 0;
        // System.out.println("???");
        // print(map);
        // print(moveLeft(map));
        move(map,0);

        System.out.println(maxBlock);
    }
    public static void move(int[][] map, int idx) {
        // System.out.println("???");
        if(idx==5){
            maxBlock = Math.max(maxBlock,getMax(map));
            //print(map);
            return;
        }

        move(moveLeft(map),idx+1);
        move(moveRight(map),idx+1);
        move(moveUp(map),idx+1);
        move(moveDown(map),idx+1);
        
    }
    public static void print(int[][] map){
        System.out.println("-----");
        for(int i=0;i<map.length;i++){
            System.out.println(Arrays.toString(map[i]));
        }
    }

    public static int[][] moveLeft(int[][] map){
        int[][] result = new int[map.length][map[0].length];
        //큐에 넣는다.
        //큐에서 하나씩 뽑는다.
        //체크하는 index가 존재한다.
        //index의 값이 0이다.
            //큐에서 뽑은것을 넣는다.
            //index는 그대로
        //index의 값이 존재
            //큐에서 뽑은것과 갑이 같음
                //합친다.
                //index+1
            //큐에서 뽑은것과 다르다.
                //index+1
                //값을 넣는다.
        for(int i=0;i<map.length;i++){
            Queue<Integer> que = new ArrayDeque<>();

            //큐에 넣는다.
            for(int j=0;j<map[0].length;j++){
                if(map[i][j]!=0){
                    que.add(map[i][j]);
                }
            }
            int idx = 0;
            
            while(!que.isEmpty()){
                int cur = que.poll();

                if(result[i][idx]==0){
                    result[i][idx] = cur;
                }else{
                    if(result[i][idx]==cur){
                        result[i][idx] = cur*2;
                        idx++;
                    }else{
                        idx++;
                        result[i][idx] = cur;
                    }
                }
            }
        }

        return result;
    }

    public static int[][] moveRight(int[][] map){
        int[][] result = new int[map.length][map[0].length];

        for(int i=0;i<map.length;i++){
            Queue<Integer> que = new ArrayDeque<>();

            //큐에 넣는다.
            for(int j=map[0].length-1;j>=0;j--){
                if(map[i][j]!=0){
                    que.add(map[i][j]);
                }
            }
            int idx = map[0].length-1;
            
            while(!que.isEmpty()){
                int cur = que.poll();

                if(result[i][idx]==0){
                    result[i][idx] = cur;
                }else{
                    if(result[i][idx]==cur){
                        result[i][idx] = cur*2;
                        idx--;
                    }else{
                        idx--;
                        result[i][idx] = cur;
                    }
                }
            }
        }

        return result;
    }

    public static int[][] moveUp(int[][] map){
        int[][] result = new int[map.length][map[0].length];
        for(int j=0;j<map[0].length;j++){
            Queue<Integer> que = new ArrayDeque<>();

            //큐에 넣는다.
            for(int i=0;i<map.length;i++){
                if(map[i][j]!=0){
                    que.add(map[i][j]);
                }
            }

            int idx = 0;
            
            while(!que.isEmpty()){
                int cur = que.poll();

                if(result[idx][j]==0){
                    result[idx][j] = cur;
                }else{
                    if(result[idx][j]==cur){
                        result[idx][j] = cur*2;
                        idx++;
                    }else{
                        idx++;
                        result[idx][j] = cur;
                    }
                }
            }
        }

        return result;
    }

    public static int[][] moveDown(int[][] map){
        int[][] result = new int[map.length][map[0].length];

        for(int j=0;j<map[0].length;j++){
            Queue<Integer> que = new ArrayDeque<>();

            //큐에 넣는다.
            for(int i=map.length-1;i>=0;i--){
                if(map[i][j]!=0){
                    que.add(map[i][j]);
                }
            }
            int idx = map.length-1;
            
            while(!que.isEmpty()){
                int cur = que.poll();

                if(result[idx][j]==0){
                    result[idx][j] = cur;
                }else{
                    if(result[idx][j]==cur){
                        result[idx][j] = cur*2;
                        idx--;
                    }else{
                        idx--;
                        result[idx][j] = cur;
                    }
                }
            }
        }

        return result;
    }

    private static int getMax(int[][] map) {
        int result = 0;
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[i].length;j++){
                if(result<map[i][j]){
                    result = map[i][j];
                }
            }
        }    
        return result;
    }
}