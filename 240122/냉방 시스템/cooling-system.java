import java.io.*;
import java.util.*;

public class Main {
	static class Aircon{
		int r,c;
		int d;
		public Aircon(int r, int c, int d) {
			super();
			this.r = r;
			this.c = c;
			this.d = d;
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
	
	
	static int minAir = Integer.MAX_VALUE;
	static int N;
	public static void main(String[] args) throws IOException {
		    	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		//n*n격자정보, 0빈,1삽무실 

		//2석이기
			//시원함이 높은곳->낮은곳으로 전파
			//시원함차이/4만큼
			//동시
			//벽있으면X
			//빈칸도 이동한다.
		//3외벽 시원함 감소
			//밖은 1씩 감소
			//0까지만 감소
		
		//모든 사무실의 시원함이 k이상이 될때까지 반복
		
		
		
		//사무실의 위치를 저장
		ArrayList<Node> offices = new ArrayList<>();
		ArrayList<Aircon> aircons = new ArrayList<>();
		
		//맵
		N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());
		
		int[][] map = new int[N][N];//벽여부 확인
		int[][] air = new int[N][N];//현재공기상태
		
		for(int i=0;i<N;i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0;j<N;j++) {
				int pos = Integer.parseInt(st.nextToken());
				if(pos==1) {
					offices.add(new Node(i,j));
				}
				if(pos>1) {
					aircons.add(new Aircon(i, j,pos-2));
				}
			}
		}
		
		for(int i=0;i<M;i++) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken())-1;
			int c = Integer.parseInt(st.nextToken())-1;
			int s = Integer.parseInt(st.nextToken());
			//0은 위, 1은왼쪽, 2아래, 3오른쪽 - 입력되는것
			
			//0는 왼쪽방향, 1은 위,2 오른쪽, 3아래쪽 - 실제
			if(s==0) {				
				map[r][c] |= 1<<1;
				
				//r-1,c는 아래가 막힘
				if(r-1>=0) {
					map[r-1][c] |= 1<<(3);
				}
			}
			
			if(s==1) {				
				map[r][c] |= 1<<0;
				
				//r,c-1는 오른쪽이 막힘
				if(c-1>=0) {
					map[r][c-1] |= 1<<(2);
				}
			}
		}
		
		
		int time = 1;
//		print2(map);
		while(true) {
//			System.out.println("----------------------");
			if(time>100) {
				time = -1;
				break;
			}
			minAir = Integer.MAX_VALUE;
			
			blow(air,map,aircons);
			
			
			air = mix(air,map);
			
//			print(air);
			down(air);
//			print(air);
			check(air,offices);
			if(minAir>=K) break;
			time++;
		}
		
		
		System.out.println(time);
		
	
	}
	private static void check(int[][] air, ArrayList<Node> offices) {
		for(int i=0;i<offices.size();i++) {
			minAir = Math.min(minAir, air[offices.get(i).r][offices.get(i).c]);
		}
		
	}
	private static void down(int[][] air) {
		
		for(int r=0;r<N;r++) {
			for(int c=0;c<N;c++) {
				if(r==0||c==0||r==N-1||c==N-1) {
					if(air[r][c]>0) {
						
						air[r][c]-=1;
					}
				}
			}
		}
	}
	private static int[][] mix(int[][] air, int[][] map) {
		int[][] nAir = new int[N][N];
		
		//복사
		for(int r=0;r<N;r++) {
			for(int c=0;c<N;c++) {
				nAir[r][c] = air[r][c];
			}
		}
		
		for(int r=0;r<N;r++) {
			for(int c=0;c<N;c++) {
				//오른쪽
				if(c+1<N) {
					if((map[r][c]&(1<<2))==0) {
						//벽이 없다는 의미
						//둘의 차이
						int gap = Math.abs(air[r][c]-air[r][c+1])/4;
						
						if(air[r][c]>air[r][c+1]) {
							nAir[r][c]-=gap;
							nAir[r][c+1]+=gap;
						}else {
							nAir[r][c]+=gap;
							nAir[r][c+1]-=gap;
						}
					}
				}
				
				//아래
				if(r+1<N) {
					if((map[r][c]&(1<<3))==0) {
						//벽이 없다는 의미
						//둘의 차이
						int gap = Math.abs(air[r][c]-air[r+1][c])/4;
						
						if(air[r][c]>air[r+1][c]) {
							nAir[r][c]-=gap;
							nAir[r+1][c]+=gap;
						}else {
							nAir[r][c]+=gap;
							nAir[r+1][c]-=gap;
						}
					}
				}
			}
		}
		
		return nAir;
	}
	private static void blow(int[][] air, int[][] map, ArrayList<Aircon> aircons) {
	
		for(int i=0;i<aircons.size();i++) {
			bfs(air,map,aircons.get(i));
//			print(air);
		}
	}
	
	
	
	
	
	private static void bfs(int[][] air, int[][] map, Aircon aircon) {
		//2는 왼쪽방향, 3은 위,4 오른쪽, 4아래쪽
		
		//1 퍼지기
			//45,직진,45로 퍼짐
				//벽이 있으면 퍼지지 않음
				//45도는 90도 두번 이동하듯이 직각으로 이동
			//두 에어컨의 시원함은 합친다.
		//방향의 그 다음칸 부터 시작
		int[] dr = {0,-1,0,1};//좌,상,우,하
		int[] dc = {-1,0,1,0};
		
		int airconDir = aircon.d;
		
		//시작
		Queue<Node> que = new ArrayDeque<>();
		boolean[][] visited = new boolean[N][N];
		
		
		que.add(new Node(aircon.r+dr[airconDir],aircon.c+dc[airconDir]));
		visited[aircon.r+dr[airconDir]][aircon.c+dc[airconDir]] = true;
		
		int power = 5;
		
		while(!que.isEmpty()) {
			
			int size = que.size();
			if(power==0) break;
			
			for(int s=0;s<size;s++) {
				Node cur = que.poll();
				
				air[cur.r][cur.c]+=power;
				
				//직진
				if(checkDirection(cur.r,cur.c,airconDir,dr,dc,visited,map)) {
					int zr = cur.r+dr[airconDir];
					int zc = cur.c+dc[airconDir];
					visited[zr][zc]  = true;
					que.add(new Node(zr,zc));
//					air[zr][zc]+=power;
				}
				
				int upD = (airconDir+1)%4;
				//+1위치 확인
				if(checkDirection(cur.r,cur.c,upD,dr,dc,visited,map)) {
					int upr = cur.r+dr[upD];
					int upc = cur.c+dc[upD];
					//+1위치에서 직진 확인
					if(checkDirection(upr,upc,airconDir,dr,dc,visited,map)) {
						int zr = upr+dr[airconDir];
						int zc = upc+dc[airconDir];
						visited[zr][zc]  = true;
						que.add(new Node(zr,zc));
//						air[zr][zc]+=power;
					}
				}
				
				int downD = (airconDir-1);
				if(downD<0) {
					downD = 3;
				}
				//-1위치 확인
				if(checkDirection(cur.r,cur.c,downD,dr,dc,visited,map)) {
					int downr = cur.r+dr[downD];
					int downc = cur.c+dc[downD];
					//+1위치에서 직진 확인
					if(checkDirection(downr,downc,airconDir,dr,dc,visited,map)) {
						int zr = downr+dr[airconDir];
						int zc = downc+dc[airconDir];
						visited[zr][zc]  = true;
						que.add(new Node(zr,zc));
//						air[zr][zc]+=power;
					}
				}
			}
			power-=1;
		}
		
		
		
	}
	private static boolean checkDirection(int r, int c, int airconDir,int[] dr, int[] dc,boolean[][] visited,int[][] map) {

		int zr = r+dr[airconDir];
		int zc = c+dc[airconDir];
		if(zr<0||zc<0||zr>=N||zc>=N) return false;
		//직진 가능
		if(visited[zr][zc]) return false;

		if((map[r][c]&(1<<airconDir))>0) {
			//벽이 있다!
			return false;
		}

		return true;//가도됨
	}
	
	private static void print(int[][] map) {
		System.out.println("---");
		for(int i=0;i<map.length;i++) {
			System.out.println(Arrays.toString(map[i]));
		}
	}
	
	private static void print2(int[][] map) {
		System.out.println("---");
		for(int i=0;i<map.length;i++) {
			for(int j=0;j<map.length;j++) {
				System.out.print(Integer.toBinaryString(map[i][j])+",");				
			}
			System.out.println();
		}
	}
}