import java.io.FileInputStream;
import java.util.*;


public class Main {
    static int[][] map = new int[101][101];
    public static void main(String[] args) throws Exception{
        //x는 오른쪽, y는 아래
        //시작점, 시작방향, 세대
        //0,0에서 시작하고, 시작방향은 오른쪽 0세대
        //1세대는 0세대를 시계방향 90도회전, 끝에 이은다.
        //2세대도 1세대를 90도 회전, 1세대 끝에 잇기

        // System.setIn(new FileInputStream("드래곤커브/input.txt"));
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        //x,y,d,g( d는 시작방향, g는 세대)
            //0은 우,1은 상, 2는 좌, 3은 하
        //커브는 겹쳐진다.

        for (int i = 0; i < N; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int d = sc.nextInt();
            int g = sc.nextInt();

            goDragon(x, y, d, g);
        }

        int answer=0;
        //4각형 확인 - dfs로 모든 경로를 가면서 
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
            	
                if(map[i][j]==1&&map[i][j+1]==1&&map[i+1][j]==1&&map[i+1][j+1]==1) {
                	answer+=1;
                }
            }
        }
        
        System.out.println(answer);
    }

    static void goDragon(int x,int y, int d, int g){
        ArrayList<int[]> dragon = new ArrayList<>();
        dragon.add(new int[]{x,y});
        if(d==0){
            dragon.add(new int[]{x,y+1});
        }else if(d==1){
            dragon.add(new int[]{x-1,y});
        }else if(d==2){
            dragon.add(new int[]{x,y-1});
        }else{
            dragon.add(new int[]{x+1,y});
        }
        int curG = 0;
        Stack<int[]> tmp = new Stack<>();
        while(curG<g){
            //드래곤의 값을 스택으로
            for (int i = 0; i < dragon.size(); i++) {
                tmp.push(dragon.get(i));
            }

            //스택에서 하나 꺼낸다
            int[] center = tmp.pop();//마지막 들어간것은 중심점
            //스택에서 전부 꺼내면서 중심 기준 90도 회전
            while(tmp.size()>0){
                int[] check = tmp.pop();
                //계산
                int[] checkTurn = turn(center,check);
                dragon.add(checkTurn);
            }
        
            
            curG+=1;
        }
        
        for (int i = 0; i < dragon.size(); i++) {
            int []to = dragon.get(i);
            if(to[0]>=0&&to[0]<=100&&to[1]>=0&&to[1]<=100){
            	map[to[0]][to[1]] = 1;
            }
        }
        
//        System.out.println("지도 확인");
//        for (int i = 0; i < 8; i++) {
//        	System.out.println(Arrays.toString(map[i]));
//        }
        

    }

    static int[] turn(int[] center, int[] target){
        //90도 회전
        

        int x = target[0];
        int y = target[1];

        //먼저 0,0으로 이동 계산이 편하도록
        x-=center[0];
        y-=center[1];

        //90도 회전
        //x<0, y<0 -> x만 -1
        //x<0, y>0 -> x만 -1
        //x>0 y>0 
        int tmp = x*(-1);
        x = y;
        y = tmp;
        //다시 원상복귀
        x+=center[0];
        y+=center[1];
        return new int[]{x,y};
    }
}