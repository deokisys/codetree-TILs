import java.util.*;
import java.io.*;

public class Main {
	
	static int minDepth;
	public static void main(String[] args) throws Exception{
		//System.setIn(new FileInputStream("src/두개의사탕/input2.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		//빨강, 파랑
		//장애물 여러개, 사탕이 나가는 구명 한개
		//위,아래,왼,오로 기울여야함
		//빨강만 빼야함
			//파랑 나오면 안됨
		//10번 이내 안되면  -1
		
		//지도는 10*10 = 100, 10번 이내
		
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		int[][] map = new int[N][M];
		
		int blueR = 0;
		int blueC = 0;
		int redR = 0;
		int redC = 0;
		
		for(int i=0;i<N;i++) {
			String line = br.readLine();
			for(int j=0;j<M;j++) {
				if(line.charAt(j)=='B') {
					blueR = i;
					blueC = j;
				}else if(line.charAt(j)=='R') {
					redR = i;
					redC = j;
				}else if(line.charAt(j)=='O') {
					map[i][j] = 3;//탈출
				}if(line.charAt(j)=='#') {
					map[i][j] = 1;//벽
				}
				
			}
		}
		
		minDepth = 100;
		//지도,파랑,빨강,깊이
		bfs(map,blueR,blueC,redR,redC,0);
		
//		print(map);
//		System.out.println(blueR+","+blueC+","+redR+","+redC);
//		int[] moveD = move(map,blueR,blueC,redR,redC,1);
//		System.out.println(moveD[0]+","+moveD[1]+","+moveD[2]+","+moveD[3]);
		
		
		if(minDepth == 100) {
			System.out.println(-1);
			return;
		}
		
		System.out.println(minDepth);
	}
	
	private static void print(int[][] map) {
		// TODO Auto-generated method stub
		System.out.println("---");
		for(int i=0;i<map.length;i++) {
			System.out.println(Arrays.toString(map[i]));
		}
	}


	
	static public void bfs(int[][] map, int blueR, int blueC, int redR, int redC, int depth) {
		if(depth>10) {
			return;
		}
		if(minDepth<depth) {
			return;
		}
		
		//빨강이 도착했는지 확인
		if(map[redR][redC]==3) {
			minDepth = Math.min(depth, minDepth);
			return;
		}
		
		//4방향으로 이동시키기
		
		for(int d=0;d<4;d++) {
			
			int[] moveD = move(map,blueR,blueC,redR,redC,d);
			
			if(moveD[0]==-1) {
				//파랑이 나가는 구멍에 빠지는 경우
				continue;
			}
			
			bfs(map,moveD[0],moveD[1],moveD[2],moveD[3],depth+1);
		}
	}
	
	static public int[] move(int[][] map, int blueR, int blueC, int redR, int redC, int delta) {
		int[] result = {-1,-1,-1,-1};
		
		int[] dr = {-1,1,0,0};
		int[] dc = {0,0,-1,1};
		
		while(true) {
			boolean moveCheck = false;
			int rzR = dr[delta]+redR;
			int rzC = dc[delta]+redC;
			//빨강을 이동시킨다.
			//이동불가, 대기하기
			//현재 파랑이랑 겹치면 넘긴다.
			//도착하면 넘긴다.
			//벽이면 넘긴다.
			//이동가능
			//그냥 빈칸이면 이동시킨다.
			
			
			//이미 도착한 경우는 넘긴다.
			if(map[redR][redC]!=3) {				
				if((map[rzR][rzC]==0||map[rzR][rzC]==3) && (rzR!=blueR||rzC!=blueC)) {
					//이동위치가 파랑이 아니고, 빈칸or도착일때만 이동한다.
					redR = rzR;
					redC = rzC;
					moveCheck = true;
				}
			}
			
			//--------------------------
			
			int bzR = dr[delta]+blueR;
			int bzC = dc[delta]+blueC;
			
			
			//파랑을 이동시킨다.
			//이동불가, 대기하기
			//현재 빨강이랑 겹치면 넘긴다.
			//벽이면 넘긴다.
			//이동가능
			//그냥 빈칸이면 이동시킨다.
			//도착
			//바로 종료
			//겹쳤는데, 그 위치가 종료 위치(두개 확인해야함)
			
			if((map[bzR][bzC]==0) && (bzR!=redR||bzC!=redC)) {
				//이동위치가 빨강이 아니고, 빈칸일때만 이동한다.
				blueR = bzR;
				blueC = bzC;
				moveCheck = true;
			}
			//이동한 위치가 도착위치면 바로 종료
			if(map[bzR][bzC]==3) {
				return result;
			}
			
			
			
			//둘다 이동을 안했으면 해당 위치를 반환
			if(!moveCheck) {
				result[0] = blueR;
				result[1] = blueC;
				result[2] = redR;
				result[3] = redC;
				
				return result;
			}
			
			//이동 했으면 한번더 반복
			
		}
		
	}
}