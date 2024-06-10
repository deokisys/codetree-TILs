import java.util.*;
import java.io.*;
public class Main {
	
	static int[][] map;
	static Mal[][] malMap;
	static Mal[] malList;
	static int N;
	static class Mal{
		int idx;//번호
		int r,c;//현위치
		int d;//방향
		
		Mal up;//위의 주소
		Mal down;//아래의 주소
		
		public Mal() {}
		public Mal(int idx,int r, int c, int d) {
			super();
			this.idx = idx;
			this.r = r;
			this.c = c;
			this.d = d;
		}
		

		
		
		@Override
		public String toString() {
			return "Mal [idx=" + idx + ", r=" + r + ", c=" + c + ", d=" + d + "]";
		}
		boolean move() {
			//이동부터 시킨다.
			//1우,2좌,3상,4하
			int[] dr = {0,0,-1,1};
			int[] dc = {1,-1,0,0};
			
			int zr = this.r+dr[this.d];
			int zc = this.c+dc[this.d];
			
			if(zr<0||zc<0||zr>=N||zc>=N) {
				//파랑색 진행
				if(this.d%2==1) {
					this.d--;
				}else {
					this.d++;
				}
				return this.moveBlue();
			}
			
			if(map[zr][zc]==0) {
				return this.moveWhite(zr,zc);
			}else if(map[zr][zc]==1) {
				return this.moveRed(zr,zc);
			}else {
				if(this.d%2==1) {
					this.d--;
				}else {
					this.d++;
				}
				return this.moveBlue();
			}
			
		}
		
		boolean moveWhite(int zr, int zc) {
			
			//내가 이동해야 하는 위치의 높이와, 가장 위의 말을 확인한다.
			
			Mal to = malMap[zr][zc];
			
			int cnt = 0;
			while(to.up!=null) {
				
				to = to.up;
				cnt++;
			}
			
			
			//현재 말의 밑과 연결을 끊는다.
			Mal thisMal = this.down.up;
			this.down.up = null;
			this.down = null;
			
			
			//이동위치와 연결시킨다.
			to.up = thisMal;
			this.down = to;
			
			//위로 올라가면서 갯수를 확인, 위치수정
			//System.out.println(to.idx+","+to.r+","+to.c);
			while(true) {
				to.r = zr;
				to.c = zc;
				if(to.up==null) break;
				to = to.up;
				cnt++;
			}
			
			if(cnt>=4) {
				return false;
			}
			return true;
		}
		
		boolean moveBlue() {
			
			//반대로 된 방향으로 다시 이동시키고 확인한다.
			
			int[] dr = {0,0,-1,1};
			int[] dc = {1,-1,0,0};
			
			int zr = this.r+dr[this.d];
			int zc = this.c+dc[this.d];
			
			//막히거나, 파랑색이면 그냥 종료
			if(zr<0||zc<0||zr>=N||zc>=N) {
				return true;//그냥 종료
			}
			if(map[zr][zc]==2) {
				return true;//그냥 종료
			}else if(map[zr][zc]==0) {
				//흰색처럼 이동시킨다.
				return this.moveWhite(zr,zc);
			}else {
				//빨간색 처럼 이동시킨다.
				return this.moveRed(zr,zc);
			}
		}
		
		boolean moveRed(int zr, int zc) {
			Mal to = malMap[zr][zc];
			
			int cnt = 0;
			while(to.up!=null) {
				
				to = to.up;
				cnt++;
			}
			
			//나의 아래와 연결을 끊는다.
			Mal thisMal = this.down.up;
			this.down.up = null;
			this.down = null;
			
			//위로 올라가면서 진행한다.
			while(true) {
				
				cnt++;
				Mal tmp = thisMal.up;
				thisMal.up = thisMal.down;
				thisMal.down = tmp;
				
				thisMal.r = zr;
				thisMal.c = zc;
				
				if(thisMal.down==null) break;
				thisMal = thisMal.down;
			}
			
			
			to.up = thisMal;
			thisMal.down = to;
			
			
			if(cnt>=4) {
				return false;
			}
			return true;
			
		}
		
	}
	public static void main(String[] args) throws Exception{
		//n,n
		//흰빨파
		//k개 말
		//1~k 번호
		//이동방향 미리 정해짐
		//상하 좌우
		//1~k까지 순서대로 움직
		
		
		//흰색이면 해당 칸으로 이동, 말이 있어도 이동 가능
		//빨간색이면 이동전 순서를 뒤집고 쌓는다.
		//파랑색이면 방향을 반대로 이동. 반대도 파랑이면 방향만 바꾸고 가만히
		
		//격자 벗어나려고 하면 반대로 이동시킨다. 파랑하고 같은 원리
		
		
		
		//말이 4개이상 겹쳐지면 즉시 게임이 종료한다.
		//게임이 종료되는 순간의 턴의 번호는?
		
		
		//n은 4~12개 - 144
		//말은 4~10개
		//System.setIn(new FileInputStream("src/이상한윷놀이/input3.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());
		
		map = new int[N][N];
		malMap = new Mal[N][N];
		for(int i=0;i<N;i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0;j<N;j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				malMap[i][j] = new Mal();
			}
		}
		
		
		malList = new Mal[K];
		
		for(int i=0;i<K;i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken())-1;
			int c = Integer.parseInt(st.nextToken())-1;
			int d = Integer.parseInt(st.nextToken())-1;
			
			malList[i] = new Mal(i,r,c,d);
			
			malMap[r][c].up = malList[i];
			malList[i].down = malMap[r][c];
			
			
		}
		
		
		boolean isEnd = false;
		int endTurn=0;
		for(int turn=1;turn<=1000;turn++) {
			//System.out.println(turn+"?");
			endTurn = turn;
			for(int i=0;i<K;i++) {
				//System.out.println(i+"번 시작");
				if(!malList[i].move()) {
					isEnd = true;
					break;
				}
			}
			if(isEnd) {
				break;
			}
			
		}
		
		if(isEnd) {
			System.out.println(endTurn);
		}else {
			System.out.println(-1);
		}
		
		
	}
}