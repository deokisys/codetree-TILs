import java.util.*;
import java.io.*;
public class Main {
	
	static int N,M;
	static class Pos{
		int r,c;

		public Pos(int r, int c) {
			super();
			this.r = r;
			this.c = c;
		}
		
	}
	public static void main(String[] args) throws Exception {
		//반지름 1,2,n
		//r번째 반지름
		//m개의 정수가 적혀있다. r번째 원판에 m번째 정수
			//r,m
		//12시 방향부터 m개의 정수가 시계방향으로 매겨진다.
		
		//원판을 회전, 독립적
		//종류,방향,회전칸수
			//x의 배수의 판들을 회전시킨다.
		
		//회전 시키고 인접 한 수를 지운다고 한다(뭔소리야
			//같은 원의 양엽
			//서로 다른 원끼리 인접한것
		//지워지는 수가 없는경우
			//전체원판 평균보다 큰수는 -1, 작은수+1, 같으면 유지
			//소수아래는 버림
		
		
		//q번 회전시키고 남아있는 수의 총합은?

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());//원판개수
		M = Integer.parseInt(st.nextToken());//숫자개수
		int Q = Integer.parseInt(st.nextToken());//회전횟수
		
		//원판은 50개, 숫자도 50개
		//이동도 최대 50개
		
		int[][] pan = new int[N][M];
		
		for(int i=0;i<N;i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0;j<M;j++) {
				pan[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		//print(pan);
		
		for(int q=0;q<Q;q++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken());//x번째
			int d = Integer.parseInt(st.nextToken());//0시계,1반시계
			int k = Integer.parseInt(st.nextToken());//이동갯수
			
			//돌리기
			turn(pan,x,d,k);

			//인접처리
			if(!del(pan)) {
				//만약 인접한게 없으면정규화
				norm(pan);				
			}
			
			//print(pan);
		}
		
		//남아있는 총합
		
		System.out.println(sum(pan));
		
	}

	private static void print(int[][] pan) {
		System.out.println("--");
		for(int i=0;i<N;i++) {
			System.out.println(Arrays.toString(pan[i]));
		}
	}

	private static void turn(int[][] pan, int x, int d, int k) {
		//x가 0이면 그냥 안돌려도 문제없다.
		if(x==1) return;
		for(int t=x-1;t<N;t+=x) {
			//x번째 판들을 돌린다.
			int[] tmpPan = spin(pan[t],d,k);
			
			for(int i=0;i<M;i++) {
				pan[t][i] = tmpPan[i];
			}
		}
		
	}


	
	
	private static int[] spin(int[] pan, int d, int k) {
		int[] tmp = new int[M];
		for(int i=0;i<M;i++) {
			if(d==0) {//시계방향
				tmp[i] = pan[(i-k+M)%M];
			}else {
				tmp[i] = pan[(i+k)%M];		
			}
		}
		return tmp;
	}

	private static boolean del(int[][] pan) {
		boolean[][] visited = new boolean[N][M];
		boolean isDel = false;
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) {
				if(pan[i][j]!=0&&!visited[i][j]) {
					bfs(pan,visited,i,j);
					if(pan[i][j]==0) {
						isDel = true;
					}
				}
			}
		}
		
		return isDel;
	}

	

	private static void bfs(int[][] pan, boolean[][] visited, int i, int j) {
		Queue<Pos> que = new ArrayDeque<>();
		visited[i][j] = true;
		que.add(new Pos(i,j));
		int check = pan[i][j];
		
		int[] dr = {-1,1,0,0};
		int[] dc = {0,0,-1,1};
		
		while(!que.isEmpty()) {
			Pos cur = que.poll();
			
			//좌우는 연결되어있음, 상하는 연결안됨
			for(int d=0;d<4;d++) {
				int zr = cur.r+dr[d];
				int zc = cur.c+dc[d];
				if(zr<0||zr>=N) continue;
				if(zc<0) {
					zc = M-1;
				}
				if(zc>=M) {
					zc = 0;
				}
				
				//방문여부 확인
				if(visited[zr][zc]) continue;
				//같은숫자인지 확인
				if(pan[zr][zc]!=check) continue;
				visited[zr][zc] = true;
				//숫자들 제거 = 0으로 만들기
				pan[cur.r][cur.c]= 0;
				pan[zr][zc] = 0;
				que.add(new Pos(zr,zc));
			}
			
		}
	}

	private static void norm(int[][] pan) {
		int sum = 0;
		int cnt = 0;
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) {
				if(pan[i][j]!=0) {					
					sum+=pan[i][j];
					cnt++;
				}
			}
		}
		
		int avg = Math.floorDiv(sum, cnt);
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) {
				if(pan[i][j]>avg) {					
					pan[i][j]-=1;
				}else if(pan[i][j]<avg) {
					pan[i][j]+=1;
				}
			}
		}
	}

	private static int sum(int[][] pan) {
		int result = 0;
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) {
				result+=pan[i][j];
			}
		}
		return result;
	}
}