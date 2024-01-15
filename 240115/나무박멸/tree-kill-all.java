import java.io.*;
import java.util.*;



public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        int N = Integer.parseInt(st.nextToken());//크기
        int M = Integer.parseInt(st.nextToken());//연
        int K = Integer.parseInt(st.nextToken());//퍼지는 정도
        int C = Integer.parseInt(st.nextToken());
        
        int[][] map = new int[N][N];
    	int[][] safeMap = new int[N][N];//제초제 종료 시간이 표시
        for(int i=0;i<N;i++) {
    		st = new StringTokenizer(br.readLine());
    		for(int j=0;j<N;j++) {
    			map[i][j] = Integer.parseInt(st.nextToken());
    			//나무는 1~100, 빈칸 0, 벽 -1
    		}
    	}
    	int answer = 0;
//    	print(map);
    	for(int y=0;y<M;y++) {
//    		System.out.println(y);
    		map = grow(N,y,map,safeMap);
//    		print(map);
    		answer+=kill(N,y,K,C,map,safeMap);
//    		print(map);
    	}
    	
    	System.out.println(answer);
    }

	private static void print(int[][] map) {
		System.out.println("---");
		for(int i=0;i<map.length;i++) {
			System.out.println(Arrays.toString(map[i]));
		}
	}

	private static int kill(int N,int y,int K,int C,int[][] map,int[][] safeMap) {
		//제초제
			//가장 많이 박멸되는 칸에 제초제 뿌리기
			//제초제는 k범위만큼 대각선으로
			//나무가 없거나 벽이 있으면 전파종료
			//c년만큼 유지
		    //뿌리고 나서 c년
			//새로 제초제가 뿌려지면 c년동안 유지
			//박멸되는곳이 동일하면
			    //행이 작은순
			    //열이작은순
		
		int[] dr = {-1,-1,1,1};
		int[] dc = {-1,1,-1,1};
		
		int maxKill = 0;
		int maxR = 0;
		int maxC = 0;
		
		//찾기
		for(int r=0;r<N;r++) {
			for(int c=0;c<N;c++) {
				
				if(map[r][c]<=0) continue;//벽이거나 빈칸 넘겨
				
				int killCnt = map[r][c];
				for(int d=0;d<4;d++) {
					for(int step=1;step<=K;step++) {
						int zr = r+dr[d]*step;
						int zc = c+dc[d]*step;
						if(zr<0||zc<0||zr>=N||zc>=N) break;//외곽은 종료
						//벽, 빈칸 종료
						if(map[zr][zc]<=0) break;
						killCnt+=map[zr][zc];
					}
				}
				
				if(maxKill<killCnt) {
					maxKill = killCnt;
					maxR = r;
					maxC = c;
				}else if(maxKill==killCnt) {
					if(r<maxR) {
						//행이 작은순
						maxR = r;
						maxC = c;
					}else if(r==maxR) {
						if(c<maxC) {
							//열이작은순
							maxR = r;
							maxC = c;
						}
					}
				    
				}
			}
		}
		
		//해당위치 제거
		
		if(maxKill>0) {
			
			map[maxR][maxC] = 0;
			safeMap[maxR][maxC] = y+C+1;//제초제 갱신
			for(int d=0;d<4;d++) {
				for(int step=1;step<=K;step++) {
					int zr = maxR+dr[d]*step;
					int zc = maxC+dc[d]*step;
					if(zr<0||zc<0||zr>=N||zc>=N) break;//외곽은 종료
					//벽이면 종료
					if(map[zr][zc]<=0) break;
					map[zr][zc] = 0;
					safeMap[zr][zc] = y+C+1;//제초제 갱신
				}
			}
			
		}
		
		
		//m년후 박멸한 나무의 그루수
		
		return maxKill;
	}

	private static int[][] grow(int N,int y,int[][] map, int[][] safeMap) {
		int[][] nMap = new int[N][N];
		//성장
			//인접칸의 나무수만큼 나무가 성장
			//동시
		//번식
			//벽,나무,제초제가 없는칸으로 번식
			//나무그루수/번식카능한 칸개수 
			//동시
		
		int[] dr = {-1,1,0,0};
		int[] dc = {0,0,-1,1};
		
		for(int r=0;r<N;r++) {
			for(int c=0;c<N;c++) {

				if(map[r][c]==-1) {
					//벽이면 갱신해
					nMap[r][c] = map[r][c];
					continue;
				}
				if(map[r][c]==0) {
					//비면 넘겨
					continue;
				}
				
				int treeCnt = 0;//나무수
				int childCnt = 0;
				//성장
				for(int d=0;d<4;d++) {
					int zr = r+dr[d];
					int zc = c+dc[d];
					if(zr<0||zc<0||zr>=N||zc>=N) continue;
					if(map[zr][zc]>0) {
						treeCnt+=1;
					}
					if(map[zr][zc]==0&&safeMap[zr][zc]<=y) {
						//빈칸이고, 안전한 칸
						childCnt+=1;
					}
				}
				
				nMap[r][c] = map[r][c]+treeCnt;
				if(childCnt==0) continue;
				//번식
				int child = (int)Math.floor((double)nMap[r][c]/childCnt);
				for(int d=0;d<4;d++) {
					int zr = r+dr[d];
					int zc = c+dc[d];
					if(zr<0||zc<0||zr>=N||zc>=N) continue;
					if(map[zr][zc]==0&&safeMap[zr][zc]<=y) {
						nMap[zr][zc] += child;
					}
				}
				
			}
		}
		return nMap;
	}
}