import java.util.*;
import java.io.*;


public class Main {
	static class Pos{
		int r,c;
		Pos(int r, int c){
			this.r = r;
			this.c =c ;
			
		}
		
		@Override
		public String toString() {
			return "["+r+","+c+"]";
		}
	}
	
	static int minGap = Integer.MAX_VALUE;
	static int[][] checkMap;
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		//n,n
		//5개 나누기
		//직사각형
			//오른쪽위, 왼쪽위, 왼쪽아래, 오른쪽아래 순으로 이동
		
		//나라크기 20,20 400
		
		int N = Integer.parseInt(st.nextToken());
		int[][] map = new int[N][N];
		int all = 0;
		for(int i=0;i<N;i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0;j<N;j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				all+=map[i][j];
			}
		}
		//System.out.println("총합"+all);
		int[] dr = {-1,-1,1,1};
		int[] dc = {1,-1,-1,1};
		
		int w = 1;//오른쪽위 가는 정도
		int h = 1;//왼족위 가는 정도
		//시작은 2,1부터 시작
		
		while(true) {
			if(w+h==N) {
				w+=1;
				h=1;
				if(w+h==N) break;//
				continue;
			}
			//System.out.println("-----"+w+","+h);
			
			for(int i=w+h;i<N;i++) {
				for(int j=h;j<N-w;j++) {
					//System.out.println("시작 "+i+","+j);
					Pos[] points = new Pos[4];
					int r = i;
					int c = j;
					for(int d=0;d<4;d++) {
						//0은 오른쪽,1은위, 2는 왼쪽, 3은 아래쪽 좌표
						if(d%2==0) {							
							r+=dr[d]*w;
							c+=dc[d]*w;
						}else {
							r+=dr[d]*h;
							c+=dc[d]*h;
						}
						points[d] = new Pos(r,c);
					}
					//System.out.println(Arrays.toString(points));
					//System.out.println("가운데값"+getCenter(map,points));
					//System.out.println("왼위값"+getLU(map,points));
					calc(map,points);
					//print(checkMap);
				}
			}
			h++;
		}
		
		System.out.println(minGap);

	}
	
	static void print(int[][] map) {
		System.out.println("---");
		for(int i=0;i<map.length;i++) {
			System.out.println(Arrays.toString(map[i]));
		}
	}
	
	static void calc(int[][] map,Pos[] points) {
		int max = 0;
		int min = Integer.MAX_VALUE;
		checkMap = new int[map.length][map.length];
		
		int center = getCenter(map,points);
		max = Math.max(max, center);
		min = Math.min(min, center);
		
		int leftUp = getLU(map,points);
		max = Math.max(max, leftUp);
		min = Math.min(min, leftUp);
		int rightUp = getRU(map,points);
		max = Math.max(max, rightUp);
		min = Math.min(min, rightUp);
		int leftDown = getLD(map,points);
		max = Math.max(max, leftDown);
		min = Math.min(min, leftDown);
		int rightDown = getRD(map,points);
		max = Math.max(max, rightDown);
		min = Math.min(min, rightDown);
		
		//System.out.println(center+leftUp+rightUp+leftDown+rightDown);
		minGap = Math.min(max-min, minGap);
		
	}

	private static int getCenter(int[][] map, Pos[] points) {
		//왼쪽좌표는 2번의 c, 으론쪽 좌표는 0번의 c
		//위좌표는 1의 r, 아래 좌표는 3의 r
		
		int downR = points[2].r;
		int upR = points[2].r;
		
		boolean upEnd = false;
		boolean downEnd = false;
		int result = 0;
		for(int j=points[2].c;j<=points[0].c;j++) {
			//System.out.println(upR+"-"+downR);
			for(int i=upR;i<=downR;i++) {
				checkMap[i][j] = 0;
				result+=map[i][j];
			}
			
			if(upR==points[1].r) {
				upEnd = true;
			}
			
			if(downR==points[3].r) {
				downEnd = true;
			}
			
			if(upEnd) {
				upR++;
			}else {				
				upR--;
			}
			if(downEnd) {				
				downR--;
			}else {
				downR++;				
			}
		}
		
		
		return result;
	}
	
	private static int getLU(int[][] map, Pos[] points) {
		//왼쪽좌표는 2번의 c, 으론쪽 좌표는 0번의 c
		//위좌표는 1의 r, 아래 좌표는 3의 r
		int result=0;
		
		//위 꼭지 위 포함, 왼 꼭지들은 미포함
		int w = points[1].c;
		for(int i=0;i<points[2].r;i++) {
			if(i>=points[1].r) {
				w--;
			}
			for(int j=0;j<=w;j++) {
				checkMap[i][j] = 1;
				result+=map[i][j];
			}
		}
		
		
		return result;
	}
	private static int getLD(int[][] map, Pos[] points) {
		//왼쪽좌표는 2번의 c, 으론쪽 좌표는 0번의 c
		//위좌표는 1의 r, 아래 좌표는 3의 r
		int result=0;
		
		//왼쪽칸은 포함, 아래는 미포함
		
		int w = points[2].c;
		for(int i=points[2].r;i<map.length;i++) {
			
			for(int j=0;j<w;j++) {
				checkMap[i][j] = 2;
				result+=map[i][j];
			}
			if(i<points[3].r) {
				w++;
			}
		}
		
		return result;
	}
	private static int getRU(int[][] map, Pos[] points) {
		//왼쪽좌표는 2번의 c, 으론쪽 좌표는 0번의 c
		//위좌표는 1의 r, 아래 좌표는 3의 r
		int result=0;
		//위는 미포함, 오른쪽꼭지 오른쪽은 포함
		
		int w = points[1].c+1;
		for(int i=0;i<=points[0].r;i++) {
			
			for(int j=w;j<map.length;j++) {
				checkMap[i][j] = 3;
				result+=map[i][j];
			}
			if(i>=points[1].r) {
				w++;
			}
		}
		
		return result;
	}
	private static int getRD(int[][] map, Pos[] points) {
		//왼쪽좌표는 2번의 c, 으론쪽 좌표는 0번의 c
		//위좌표는 1의 r, 아래 좌표는 3의 r
		int result=0;
		
		//아래는 포함, 오른쪽은 포함X
		
		int w = points[0].c;
		for(int i=points[0].r+1;i<map.length;i++) {
			
			for(int j=w;j<map.length;j++) {
				checkMap[i][j] = 4;
				result+=map[i][j];
			}
			if(i<=points[3].r) {
				w--;
			}
		}
		
		return result;
	}
}