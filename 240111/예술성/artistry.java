import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args) throws Exception{
		//동일한 숫자가 인접하면 동일한 그룹
		
		//a,b조화로움
			//(a그룹칸수+b그룹칸수)*a숫자*b숫자*a,b맟닿은변의수
		
		//모든 조화값을 더하면 초기 예술 점수
		
		//십자 반시계로 회전
		
		//4개의 사각형들은 시계로 90회전
		
		//3회전까지의 예술점수의 합
//		System.setIn(new FileInputStream("src/예술성/input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int N = Integer.parseInt(st.nextToken());
		
		int[][] map = new int[N][N];
		
		for(int i=0;i<N;i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0;j<N;j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		int answer =0;
		for(int i=0;i<4;i++) {
			answer+=art(map,N);
//			print(map);
			map = crossTurn(map);
//			print(map);
			map = squareTurn(map);
//			print(map);
		}
		System.out.println(answer);
	}



	private static int[][] squareTurn(int[][] map) {
		int width = map.length;
		int miniWidth = width/2;
		
		int[][] nMap = new int[width][width];
		
		for(int i=0;i<width;i++) {
			for(int j=0;j<width;j++) {
				nMap[i][j] = map[i][j];
			}
		}
		
		for(int r=0;r<width;r+=(miniWidth+1)) {
			for(int c=0;c<width;c+=(miniWidth+1)) {
				int[][] smallMap = new int[miniWidth][miniWidth];
				
				
				for(int mr=r;mr<r+miniWidth;mr++) {
					for(int mc=c;mc<c+miniWidth;mc++) {
						smallMap[mr-r][mc-c] = map[mr][mc];
					}
				}
				
				smallMap = minTurn(smallMap);
				
				for(int mr=r;mr<r+miniWidth;mr++) {
					for(int mc=c;mc<c+miniWidth;mc++) {
						nMap[mr][mc] = smallMap[mr-r][mc-c];
					}
				}
				
			}			
		}
		
		
		
		
		
		
		return nMap;
	}



	private static int[][] minTurn(int[][] smallMap) {
		int width = smallMap.length;
		int nSmallMap[][] = new int[width][width];
		//시계로 회전
		
		//0,0 -> 0,1
			//0,1 = 0,0
			//r,c = 1-c,r
		//0,1->1,1
			//1,1 = 0,1
			//r,c = 1-c,r
		//1,1 -> 1,0
			//1,0=1,1
			//r,c = 1-c,r
		//1,0 -> 0,0
			//0,0 = 1,0
			//r,c = 1-c,r
		
		for(int r=0;r<width;r++) {
			for(int c=0;c<width;c++) {
				nSmallMap[r][c] = smallMap[width-1-c][r];
			}
		}
		
		//
		return nSmallMap;
	}



	private static int[][] crossTurn(int[][] map) {
	
		int width = map.length;
		int[][] nMap = new int[width][width];
		
		for(int i=0;i<width;i++) {
			for(int j=0;j<width;j++) {
				nMap[i][j] = map[i][j];
			}
		}
		//0,2 -> 2,0
		
			//2,0 = 0,2
			//r,c = c,4-r
		
		//2,0 -> 4,2
			//4,2 = 2,0
			//r,c = c,4-r
		
		//4,2 -> 2,4
			//2,4 = 4,2;
			//r,c = c,r??
		
		//2,4-> 0,2
			//0,2=2,4
			//r,c = c,4-r
		
		
		//1,2 -> 2,1
		
		int[] dr = {-1,1,0,0};
		int[] dc = {0,0,-1,1};
		
		int step  = 1;
		
		int r = width/2;
		int c = width/2;
		
		while(step*2+1<=width) {
			
			for(int d=0;d<4;d++) {
				int zr = r+dr[d]*step;
				int zc = c+dc[d]*step;
				nMap[zr][zc] = map[zc][width-1-zr];
			}
			
			step+=1;
		}
		
		
		
		return nMap;
	}



	private static int art(int[][] map, int n) {
		
		//필요한 정보
		
		
		//그룹간 맟닿은 변 갯수
		Map<Integer,Integer> touch = new HashMap<>();
		//낮은 그룹번호*1000+높은그룹번호 - 맛닿은것
		
		//그룹의 칸수
		Map<Integer,Integer> groupCount = new HashMap<>();
		//그룹의 번호정보
		Map<Integer,Integer> groupNum = new HashMap<>();
		
		//그룹 만들기
		
		int[][] visited= new int[n][n];//그룹번호
		int groupNumber = 1;
		
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				if(visited[i][j]==0) {					
					int cnt = bfs(i,j,map[i][j],groupNumber,map,visited,touch);
					groupCount.put(groupNumber, cnt);
					groupNum.put(groupNumber, map[i][j]);
					groupNumber++;
				}
			}
		}
		
		
		
		int point = 0;
		
		//그룹 점수 구하기
		for(int key:touch.keySet()) {
			int a = key/1000;
			int b = key%1000;
			
			//a와 b가 맞닿음
			//(a그룹칸수+b그룹칸수)*a숫자*b숫자*a,b맟닿은변의수
			point += (groupCount.get(a)+groupCount.get(b))*groupNum.get(a)*groupNum.get(b)*touch.get(key);
		}
		
		return point;
	}

	private static void print(int[][] visited) {
		for(int i=0;i<visited.length;i++) {
			for(int j=0;j<visited.length;j++) {
				System.out.print(visited[i][j]+" ");
			}
			System.out.println("");
		}
		System.out.println("");
		
	}

	static class Node{
		int r,c;

		public Node(int r, int c) {
			super();
			this.r = r;
			this.c = c;
		}
		
	}
	private static int bfs(int r, int c, int num, int groupNumber, int[][] map, int[][] visited, Map<Integer,Integer> touch) {
		Queue<Node> que = new ArrayDeque<>();
		visited[r][c] = groupNumber;
		que.add(new Node(r,c));
		
		int[] dr = {-1,1,0,0};
		int[] dc = {0,0,-1,1};
		
		int cnt = 0;
		
		while(!que.isEmpty()) {
			Node cur = que.poll();
			cnt+=1;
			for(int d=0;d<4;d++) {
				int zr = cur.r+dr[d];
				int zc = cur.c+dc[d];
				if(zr<0||zc<0||zr>=map.length||zc>=map.length) continue;
				
				if(visited[zr][zc]==groupNumber) continue;//내 그룹이 갔던곳
				if(visited[zr][zc]!=0) {//내그룹이 아닌데, 다른 그룹을 만남
					//맛닿은곳
					int min = Math.min(visited[zr][zc], groupNumber);
					int max = Math.max(visited[zr][zc], groupNumber);
					int key = min*1000+max;
					if(!touch.containsKey(key)) {
						touch.put(key, 0);
					}
					touch.put(key, touch.get(key)+1);
					
					continue;
				}
				
				if(map[zr][zc]!=num) continue;
				
				//다음 이동
				que.add(new Node(zr,zc));
				visited[zr][zc] = groupNumber;
			}
		}
		
		return cnt;
		
	}
}