import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

//1 밀가루 양이 가장 작은 위치에 1더하기
	//모든 작은 위치에 다 더하기
//2 말기
	//바닥보다 위 밀가로가 넓으면 중단
//3 눌르기
	//상하좌우 인접 이거 뭔소리야 시발 좆같이 구네 시발년들이
	//왼쪽 아래부터 해서 위로 가면서 하나씩 펼친다.
//4 두번 접기
	//절반씩 쪼개서 접는다.
//5 눌르기 진행
	//상하좌우 뭐시기 진행
	//왼쪽아래부터 해서 펼친다.
//최대와 최소가 k이하가 될때까지 반복
//


public class Main {
	static int N;
	static int max,min;
	public static void main(String[] args) throws Exception{
		

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		
		N = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());
		
		int[][] map = new int[N][N];
		
		
		
		min = 3001;
		max = 0;
		st = new StringTokenizer(br.readLine());
		for(int i=0;i<N;i++) {
			map[N-1][i] = Integer.parseInt(st.nextToken());
			min = Math.min(map[N-1][i], min);
			max = Math.max(map[N-1][i], max);
		}
		
		int answer = 0;
//		System.out.println("시작이용");
		while(true) {
//			System.out.println(max+","+min);
			if(max-min<=K) break;
			
			add(map);
//			print(map);

			roll(map);
//			print(map);
			map = stamp(map);
//			print(map);
			fold(map);
//			print(map);
			min = 3001;
			max = 0;
			
			map = stamp(map);
//			print(map);
			answer++;
		}
		System.out.println(answer);
		
	}
	
	

	private static void add(int[][] map) {
		for(int r=0;r<N;r++) {
			if(map[N-1][r]==min) {
				map[N-1][r]++;
			}
		}
	}



	private static void print(int[][] map) {
		System.out.println("---");
		for(int r=0;r<N;r++) {
			System.out.println(Arrays.toString(map[r]));
		}
		
	}



	private static void roll(int[][] map) {
		
		int[] dst = {N-2,1};
		int[] src = {dst[0]+1,dst[1]-1};
		
		int rollCnt =0 ;
		int step =1;
		while(true) {
			if(rollCnt%2==1) {
				step++;
			}
			if(dst[1]+step>N) break;
			for(int c=0;c<N;c++) {
				//c를 이동할때 다음 접기가 가능한지 계속 확인한다.
				for(int r=0;r<N;r++) {
					if(src[1]-r<0) break;
					if(map[src[0]-c][src[1]-r]==0) break;
					map[dst[0]-r][dst[1]+c] = map[src[0]-c][src[1]-r];
					map[src[0]-c][src[1]-r]=0;
				}
			}
			//dst다음으로 이동
			
			dst[1]+=step;
			//src도 다음으로 이동
			src[1]+=step;
			rollCnt++;
		}
		
		//0 1
		//1 2
		//2 2
		//3 3
		//4 3
		//5 4
	}



	private static void fold(int[][] map) {
		
		minifold(map,new int[] {N-1,N/2-1}, new int[] {N-2,N/2});
		minifold(map,new int[] {N-2,(3*N)/4-1}, new int[] {N-3,(3*N)/4});
	
	}
	
	

	private static void minifold(int[][] map, int[] src, int[] dst) {
		
		//접혀서 올라가는 위치dst
		//접히는 위치 src
		for(int c=0;c<N;c++) {
			if(dst[1]+c>=N) break;
			for(int r=0;r<N;r++) {
				if(src[0]+r>=N) break;
				map[dst[0]-r][dst[1]+c]=map[src[0]+r][src[1]-c];
				map[src[0]+r][src[1]-c] = 0;
			}
		}
		
	}

	private static int[][] stamp(int[][] map) {
		int[][] nMap = new int[N][N];
		
		//복사
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				nMap[i][j] = map[i][j];
			}
		}
		
		
		int[] dr = {1,0};
		int[] dc = {0,1};
		for(int r=0;r<N;r++) {
			for(int c=0;c<N;c++) {
				if(map[r][c]==0) continue;
				for(int d=0;d<2;d++) {
					int zr = r+dr[d];
					int zc = c+dc[d];
					if(zr<0||zc<0||zr>=N||zc>=N) continue;//도우밖 넘기기
					if(map[zr][zc]==0) continue;//빈곳 넘기기
					
					int gap = Math.abs(map[r][c]-map[zr][zc]);
					
					int ds = gap/5;
					
					if(map[r][c]<map[zr][zc]) {
						nMap[zr][zc]-=ds;
						nMap[r][c]+=ds;
					}else {
						nMap[zr][zc]+=ds;
						nMap[r][c]-=ds;
					}
				}
			}
		}
		
//		print(nMap);
		
		int[][] stampMap = new int[N][N];
		int idx = 0;
		for(int c=0;c<N;c++) {
			if(nMap[N-1][c]==0) continue;
			for(int r=N-1;r>=0;r--) {
				if(nMap[r][c]==0) break;
				stampMap[N-1][idx] = nMap[r][c];
				min = Math.min(stampMap[N-1][idx], min);
				max = Math.max(stampMap[N-1][idx], max);
				idx++;
			}
		}
		return stampMap;
	}
}