import java.util.*;
import java.io.*;
public class Main {
	
	static class Robot {
		int r,c,level;
		int killCnt = 0;
		Robot(int r, int c, int level){
			this.r = r;
			this.c = c;
			this.level = level;
		}
		
		void kill() {
			this.killCnt++;
			if(this.killCnt==this.level) {
				this.level++;
				this.killCnt=0;
			}
		}
		
	}
	
	static class Pos{
		int r, c;
		Pos(int r, int c){
			this.r = r;
			this.c =c ;
		}
	}
	public static void main(String[] args) throws Exception {
		
		//n,n m몬스터, 전투로봇
		//몬스터는 최대 하나만 존재
		
		//레벨 존재
		//전투로봇 2, 1초에 상하좌우 한칸
			//자기보다 레벨 높은 몬스터 못감
			//낮은 레벨만 잡음
			//같은 몬스터는 못없애고 지나기만 함
		
		
		//잡을 몬스터가 있으면 잡는다.
			//가장 가까운 몬스터에게 간다.
				//가장 위
					//가장 왼쪽
		//몬스터가 없으면 종료
		
		//본인 레벨과 같은 수의 몬스터를 잡으면 레벨 상승
			//3레벨은 3명 잡으면 상승
		
		
		//죵로까지 걸리는 시간
		//System.setIn(new FileInputStream("src/전투로봇/input.txt"));
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		
		
		int[][] map = new int[n][n];
		
		Robot robot = new Robot(0,0,0);
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				map[i][j] = sc.nextInt();
				if(map[i][j]==9) {
					map[i][j] = 0;
					robot.r = i;
					robot.c = j;
					robot.level = 2;
				}
			}
		}
		
		int time = 0;
		while(true) {
			
			int killTime = 0;
			//탐색 시작
			
			
			killTime = bfs(robot,map);
			//System.out.println(killTime);
			//print(map);
			
			
			
			//로봇이 처리를 못함
			if(killTime==0) break;

			time+=killTime;
		}
		
		System.out.println(time);
	}
	
	static void print(int[][] map) {
		System.out.println("---");
		for(int i=0;i<map.length;i++) {
			System.out.println(Arrays.toString(map[i]));
		}
	}
	
	private static int bfs(Robot robot, int[][] map) {
		Queue<Pos> que = new ArrayDeque<>();
		boolean[][] visited = new boolean[map.length][map.length];
		que.add(new Pos(robot.r,robot.c));
		visited[robot.r][robot.c] = true;
		
		int[] dr = {-1,1,0,0};
		int[] dc = {0,0,-1,1};
		
		
		int killTime = 0;
		
		int r = -1;
		int c = -1;
		
		int time = 0;
		while(!que.isEmpty()) {
			
			for(int s=0,size=que.size();s<size;s++) {				
				Pos cur = que.poll();
				
				if(map[cur.r][cur.c]!=0 && map[cur.r][cur.c]<robot.level) {
					killTime = time;
					if(r==-1) {
						r = cur.r;
						c = cur.c;
					}else {
						//r은 낮을수록
						
						if(cur.r<r) {
							r=cur.r;
							c = cur.c;
						}else if(r==cur.r) {
							//c는 낮을 수록
							if(cur.c<c) {
								r = cur.r;
								c = cur.c;
							}
						}
					}
				}
				
				for(int d=0;d<4;d++) {
					int zr = dr[d]+cur.r;
					int zc = cur.c+dc[d];
					if(zr<0||zc<0||zr>=map.length||zc>=map.length) continue;
					if(visited[zr][zc]) continue;
					if(map[zr][zc]>robot.level) continue; // 레벨 높으면 못감
					
					visited[zr][zc] = true;
					que.add(new Pos(zr,zc));
				}
			}
			
			if(killTime>0) {
				break;
			}
			time++;
		}
		
		if(killTime>0) {
			//죽임
			map[r][c] = 0;
			robot.r = r;
			robot.c = c;
			robot.kill();
		}
		
		return killTime;
	}
}