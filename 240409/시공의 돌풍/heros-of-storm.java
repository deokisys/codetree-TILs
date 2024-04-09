import java.util.*;
import java.io.*;

public class Main {

	public static void main(String[] args) throws Exception{
		
		//n,m격자 먼지
		//1번열 설치
		//두칸 차지
		
		//사이클
		//1
			//먼지 상하좌우 확산
				//돌풍 있거나, 범위를 벗어나면 확산 안함
				//원래 먼지/5 확산
				//원래칸은 확산한 먼지만큼 줄어듬
				//확산이 끝내고 나서 더해진다.
		//2 청소
			//윗칸은 반시계로 바람
			//아랫칸은 시계로 바람
			//바람 방향대로 한칸 씩 이동
			//에어컨으로 들어가면 사라짐
		
		//t초후 먼저의 양은?
		//System.setIn(new FileInputStream("src/시공의돌풍/input.txt"));
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int m = sc.nextInt();
		
		int T= sc.nextInt();
		int[][] map = new int[n][m];
		for(int i=0;i<n;i++) {
			for(int j=0;j<m;j++) {
				map[i][j] = sc.nextInt();
			}
		}
		
		for(int t=0;t<T;t++) {
			//먼지 확산
			//System.out.println(t);
			map = dust(map);
			
			//print(map);
			//돌풍
			storm(map);
			
			//print(map);
			
		}
		
		int answer = 0;
		for(int i=0;i<n;i++) {
			for(int j=0;j<m;j++) {
				if(map[i][j]!=-1) {
					answer+=map[i][j];
				}
			}
		}
		
		System.out.println(answer);
		
	}
	private static void print(int[][] map) {
		System.out.println("--");
		for(int i=0;i<map.length;i++) {
			System.out.println(Arrays.toString(map[i]));
		}
		
	}
	static void storm(int[][] map){
		
		upStorm(map,map.length/2-1);
		
		
		downStorm(map,map.length/2);


	}
	
	static void upStorm(int[][] map, int maxR) {
		
		int r = maxR-1;
		int c = 0;
		int[] dr = {-1,0,1,0};
		int[] dc = {0,1,0,-1};
		
		
		for(int d=0;d<4;d++) {
			while(true) {
				int zr = r+dr[d];
				int zc = c+dc[d];
				if(zr<0||zc<0||zr>maxR||zc>=map[0].length) {
					break;
				}
				if(map[zr][zc]==-1) {
					map[r][c] = 0;
				}else {
					map[r][c] = map[zr][zc];
				}
				r=zr;
				c=zc;
			}
		}
	}
	
	static void downStorm(int[][] map, int minR) {
		int r = minR+1;
		int c = 0;
		
		int[] dr = {1,0,-1,0};
		int[] dc = {0,1,0,-1};
		
		
		for(int d=0;d<4;d++) {
			while(true) {
				int zr = r+dr[d];
				int zc = c+dc[d];
				if(zr<minR||zc<0||zr>=map.length||zc>=map[0].length) {
					break;
				}
				if(map[zr][zc]==-1) {
					map[r][c] = 0;
				}else {
					map[r][c] = map[zr][zc];
				}
				r=zr;
				c=zc;
			}
		}
	}
	
	static int[][] dust(int[][] map){
		int[][] result = new int[map.length][map[0].length];
		
		int[] dr = {-1,1,0,0};
		int[] dc = {0,0,-1,1};
		
		for(int i=0;i<map.length;i++) {
			for(int j=0;j<map[0].length;j++) {
				
				//돌품 그대로 복사
				if(map[i][j]==-1) {
					result[i][j] = -1;
					continue;
				}
				
				int cnt = 0;//확산되는 갯수 세기
				for(int d=0;d<4;d++) {
					int zr = i+dr[d];
					int zc = j+dc[d];
					if(zr<0||zc<0||zr>=map.length||zc>=map[0].length) continue;
					if(map[zr][zc]==-1) continue;//돌풍은 피하기
					cnt++;
				}
				
				if(cnt>0) {
					int spread = map[i][j]/5;
					
					//현재 위치에서 제거
					result[i][j]+=(map[i][j]-spread*cnt);
					//확산된 값 더하기
					for(int d=0;d<4;d++) {
						int zr = i+dr[d];
						int zc = j+dc[d];
						if(zr<0||zc<0||zr>=map.length||zc>=map[0].length) continue;
						if(map[zr][zc]==-1) continue;//돌풍은 피하기
						result[zr][zc]+=spread;
					}
				}else {
					//그냥 복사
					result[i][j] += map[i][j];
				}
				
			}
		}
		
		
		
		
		return result;
	}
}