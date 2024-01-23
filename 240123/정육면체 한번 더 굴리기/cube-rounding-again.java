import java.util.*;
import java.io.*;

public class Main {

	
	//주사위
	//m번에 걸쳐 주사위를 1칸씩 굴림
		// 처음은 오른쪽
	//격자위숫자와  상하좌우 인접한 모든 칸의 합만큼 점수 얻음
	
	

	
	//벽이면 반대방향으로 이동
	
	//점수 총합은?
		
	static class Dice{
		int d;//방향
		int r,c;//현위치
		
		int[][] dice = {{0,5,0},{4,6,3},{0,2,0},{0,1,0}};
		
		public Dice(int d, int r, int c) {
			super();
			this.d = d;
			this.r = r;
			this.c = c;
		}
		
		public void moveRight() {
			//값들이 왼쪽으로 이동한다.
			
			int save = this.dice[1][0];
			
			for(int i=0;i<2;i++) {
				this.dice[1][i] = this.dice[1][i+1]; 
			}
			
			this.dice[1][2] = this.dice[3][1];
			this.dice[3][1] = save;
		}

		public void moveLeft() {
			//값들이 오른쪽으로 이동한다.
			
			int save = this.dice[1][2];
			
			for(int i=2;i>0;i--) {
				this.dice[1][i] = this.dice[1][i-1]; 
			}
			
			this.dice[1][0] = this.dice[3][1];
			this.dice[3][1] = save;
		}
		
		public void moveDown() {
			//아래 값이 위로 올라간다.
			
			int save = this.dice[0][1];
			
			for(int i=0;i<3;i++) {
				this.dice[i][1] = this.dice[i+1][1];
			}
			
			this.dice[3][1] = save;
		}
		
		public void moveUp() {
			//값들이 아래로 이동한다.
			
			int save = this.dice[3][1];
			
			for(int i=3;i>0;i--) {
				this.dice[i][1] = this.dice[i-1][1];
			}
			
			this.dice[0][1] = save;
		}

		@Override
		public String toString() {
			return "Dice [d=" + d + ", r=" + r + ", c=" + c + ", dice=" + Arrays.toString(dice) + "]";
		}
		
		
	}
	static class Node{
		int r,c;

		public Node(int r, int c) {
			super();
			this.r = r;
			this.c = c;
		}

	}
	static int[] dr = {0,1,0,-1};
	static int[] dc = {1,0,-1,0};
	static int N;
	public static void main(String[] args) throws Exception{
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		int[][] map = new int[N][N];
		
		
		for(int i=0;i<N;i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0;j<N;j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		
		//0오른쪽, 1 아래, 2왼쪽, 3위
		
		Dice dice = new Dice(0,0,0);
		int answer = 0;
		for(int m=0;m<M;m++) {
			move(map,dice);
			answer+=cal(map,dice);
			turn(map,dice);
		}
		System.out.println(answer);
	}
	private static void turn(int[][] map, Dice dice) {
		
		int bottom = dice.dice[1][1];
		int tile = map[dice.r][dice.c];
		
		//회전
		//아랫면 > 칸숫자
			//시계로 회전
		if(bottom>tile) {
			//시계방향은 +1
			dice.d+=1;
			dice.d%=4;
		}else if(bottom<tile) {
			//아랫면 < 칸숫자
			//반시계 회전
			dice.d-=1;
			if(dice.d<0) {
				dice.d=3;
			}
		}
		//동일 그대로 진행
		
		
	}
	private static int cal(int[][] map, Dice dice) {
		Queue<Node> que = new ArrayDeque<>();
		boolean[][] visited = new boolean[N][N];
		que.add(new Node(dice.r,dice.c));
		visited[dice.r][dice.c] = true;

		
		int check = map[dice.r][dice.c];
		int count = 0;
		
		while(!que.isEmpty()) {
			Node cur = que.poll();
			if(map[cur.r][cur.c]==check) {
				count+=1;
			}
			
			for(int d=0;d<4;d++) {
				int zr = cur.r+dr[d];
				int zc = cur.c+dc[d];
				if(zr<0||zc<0||zc>=N||zr>=N) continue;
				if(map[zr][zc]!=check) continue;
				if(visited[zr][zc]) continue;
				
				visited[zr][zc] = true;
				que.add(new Node(zr,zc));
			}
		}
		
		return count*check;
		
	}
	private static void move(int[][] map,Dice dice) {
		
		//이동 가능한지 확인
		if(!check(map,dice)) {
			//방향 전환
			dice.d = (dice.d+2)%4;
			
		}
		
		//이동
		dice.r += dr[dice.d];
		dice.c += dc[dice.d];
		
		
		if(dice.d==0) {
			dice.moveRight();
		}else if(dice.d==1) {
			dice.moveDown();
		}else if(dice.d==2) {
			dice.moveLeft();
		}else {
			dice.moveUp();
		}
		
	}
	private static boolean check(int[][] map, Dice dice) {
		
		int zr = dice.r+dr[dice.d];
		int zc = dice.c+dc[dice.d];
		
		if(zr<0||zc<0||zc>=N||zr>=N) 
			return false;//나갔는데용
		
		
		return true;
	}
}