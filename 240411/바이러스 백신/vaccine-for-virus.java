import java.util.*;
import java.io.*;

public class Main {
	static class Pos{
		int r,c;
		Pos(int r, int c){
			this.r = r;
			this.c = c;
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[").append(r).append(",").append(c).append("]");
			return sb.toString();
		}
	}
	static int minTime = Integer.MAX_VALUE;
	static int allVirus;
	public static void main(String[] args) throws Exception{
		//n,n 병원, 벽, 바이러스
		//m개의 병원 선택
		//m개 선택하여 바이러스 전부 없애는데 걸리는 최소 시간
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		int[][] map = new int[N][N];
		
		ArrayList<Pos> hospitals = new ArrayList<>();
		
		for(int i=0;i<N;i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0;j<N;j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if(map[i][j]==2) {
					hospitals.add(new Pos(i,j));
				}
				if(map[i][j]==0) {
					allVirus++;
				}
			}
		}
		
		//지도, 병원정보, 선택병원, 병원idx, 선택된병원개수
		combi(map,hospitals,new Pos[M],0,0);
		
		if(minTime==Integer.MAX_VALUE) {
			System.out.println(-1);
		}else {
			System.out.println(minTime);
		}
	}
	
	static void combi(int[][] map, ArrayList<Pos> hospital, Pos[] save, int idx, int selIdx) {
		if(selIdx==save.length) {
			bfs(map,save);
			return;
		}
		
		for(int i=idx;i<hospital.size();i++) {
			save[selIdx] = hospital.get(i);
			combi(map,hospital,save,i+1,selIdx+1);
		}
	}
	
	static void bfs(int[][] map, Pos[] save) {
		int checkVirus = allVirus;
		
		boolean[][] visited = new boolean[map.length][map.length];
		Queue<Pos> que = new ArrayDeque<>();
		for(Pos cur : save) {
			que.add(new Pos(cur.r,cur.c));
			visited[cur.r][cur.c]= true; 
		}
		
		int[] dr = {-1,1,0,0};
		int[] dc = {0,0,-1,1};
		int time = 0;
//		System.out.println(Arrays.toString(save));
		while(!que.isEmpty()) {
//			System.out.println(time);
            if(checkVirus==0) {//더이상 바이러스가 없으면 종료(병원을 지나가려고 해서 멈춰야함)
				break;
			}
			for(int s=0,size=que.size();s<size;s++) {				
				Pos cur = que.poll();
//				System.out.println(cur);
				for(int d=0;d<4;d++) {
					int zr = cur.r+dr[d];
					int zc = cur.c+dc[d];
					if(zr<0||zc<0||zr>=map.length||zc>=map.length) continue;
					if(visited[zr][zc]) continue;
					if(map[zr][zc]==1) continue;
					
					
					if(map[zr][zc]==0) {
						//병원은 지나갈수있지만, 바이러스는 없다
						checkVirus--;
					}
					visited[zr][zc]=true;
					que.add(new Pos(zr,zc));
				}
			}
			
			if(que.isEmpty()) {//더이상 이동 불가면 종료
				break;
			}
			time++;
			
			
		}
		
//		System.out.println(time);
		if(checkVirus==0) {
			minTime = Math.min(minTime, time);
		}
		
	}
}