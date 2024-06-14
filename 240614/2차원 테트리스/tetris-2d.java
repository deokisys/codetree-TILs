import java.io.*;
import java.util.*;

public class Main {
	
	
	static int[][][] blocks = {{},{{0,0}},{{0,0},{0,1}},{{0,0},{1,0}}};
	
	public static void main(String[] args) throws Exception{
		//1-1,1-2, 2-1 형태
		//파란색에 블럭 넣으면 빨간색 왼쪽으로 쌓임, 노란색은 아래로 쌓임
		
		//한줄이 쌓이면 지워짐
		//연한곳이 쌓이면, 연한거 만큼 알아서 지워짐
		
		
		//쌓이것과 연한곳 동시
			//쌓인것 먼저하고, 연한것처리한다.
		
		//얻은점수, 노란,빨간보드의 타일의 칸의 합은?
			//한줄당 1점
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		
		int[][] red = new int[4][6];
		int[][] yellow = new int[6][4];
		
		int K = Integer.parseInt(st.nextToken());
		
		int point = 0;
		int cnt = 0;
		for(int i=0;i<K;i++) {
			st = new StringTokenizer(br.readLine());
			int t = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());

			point+=goRed(red,t,x,y);
			point+=goYellow(yellow,t,x,y);
			
			
		}
		
		System.out.println(point);
		
		for(int i=0;i<4;i++) {
			for(int j=0;j<6;j++) {
				cnt+=red[i][j];
			}
		}
		for(int i=0;i<6;i++) {
			for(int j=0;j<4;j++) {
				cnt+=yellow[i][j];
			}
		}
		
		System.out.println(cnt);
		
	}

	private static void print(int[][] red) {
		System.out.println("---");
		for(int i=0;i<red.length;i++) {
			System.out.println(Arrays.toString(red[i]));
		}
		
	}

	private static int goRed(int[][] red, int t, int x, int y) {
		
		int point = 0;
		
		//쌓기
		int pos = 0;
		for(int j=0;j<6;j++) {			
			//해당 블럭이 겹쳐지는지 확인한다.
			boolean isPos = true;
			for(int b=0;b<blocks[t].length;b++) {
				//유효 확인
				
				//i를 넘기면 그냥 continue를 진행
				if(x+blocks[t][b][0]==4) {
					continue;
				}
				
				if(j+blocks[t][b][1]==6 || red[x+blocks[t][b][0]][j+blocks[t][b][1]]==1) {
					isPos = false;
					break;
				}
			}
			if(!isPos) break;
			pos = j;
		}
		for(int b=0;b<blocks[t].length;b++) {				
			red[x+blocks[t][b][0]][pos+blocks[t][b][1]]=1;
		}
		
		//한줄채워진게 있으면 지우기
		int j = 5;
		while(j>=0) {			
			//한줄 확인
			boolean isLine = true;
			for(int i=0;i<4;i++) {
				if(red[i][j]==0) {
					isLine = false;
					break;
				}
			}
			//지워지는 한줄이 있다면
			if(isLine) {
				//해당 기준으로 앞으로 한칸씩 다 땡긴다.
				pushRed(red,j);
				point++;
			}else {
				j-=1;
			}
		}
		
		
		
		//연한부분 나온 만큼 지우기
		while(true) {		
			
			//연한 영역에 있는지 확인
			boolean isOut = false;
			for(int i=0;i<4;i++) {
				if(red[i][1]==1) {
					isOut = true;
					break;
				}
			}
			
			if(isOut) {
				//맨끝에서 한칸씩 땡긴다.
				pushRed(red,5);
			}else {
				break;
			}
		}
		
		
		return point;
	}

	private static void pushRed(int[][] red, int piv) {
		for(int i=0;i<4;i++) {
			for(int j=piv;j>0;j--) {
				red[i][j] = red[i][j-1];
				red[i][j-1]=0;
			}
		}

	}
	
	
	private static int goYellow(int[][] yellow, int t, int x, int y) {
		
		int point = 0;
		
		//쌓기
		int pos = 0;
		for(int i=0;i<6;i++) {			
			//해당 블럭이 겹쳐지는지 확인한다.
			boolean isPos = true;
			for(int b=0;b<blocks[t].length;b++) {
				//j를 넘기면 그냥 continue를 진행
				if(y+blocks[t][b][1]==4) {
					continue;
				}
				
				if(i+blocks[t][b][0]==6 || yellow[i+blocks[t][b][0]][y+blocks[t][b][1]]==1) {
					isPos = false;
					break;
				}
			}
			if(!isPos) break;
			pos = i;
		}
		for(int b=0;b<blocks[t].length;b++) {				
			yellow[pos+blocks[t][b][0]][y+blocks[t][b][1]]=1;
		}
		
		//한줄채워진게 있으면 지우기
		int i = 5;
		while(i>=0) {			
			//한줄 확인
			boolean isLine = true;
			for(int j=0;j<4;j++) {
				if(yellow[i][j]==0) {
					isLine = false;
					break;
				}
			}
			//지워지는 한줄이 있다면
			if(isLine) {
				//해당 기준으로 앞으로 한칸씩 다 땡긴다.
				pushYellow(yellow,i);
				point++;
			}else {
				i-=1;
			}
		}
		
		
		
		//연한부분 나온 만큼 지우기
		while(true) {		
			
			//연한 영역에 있는지 확인
			boolean isOut = false;
			for(int j=0;j<4;j++) {
				if(yellow[1][j]==1) {
					isOut = true;
					break;
				}
			}
			
			if(isOut) {
				//맨끝에서 한칸씩 땡긴다.
				pushYellow(yellow,5);
			}else {
				break;
			}
		}
		
		
		return point;
	}

	private static void pushYellow(int[][] yellow, int piv) {
		for(int j=0;j<4;j++) {
			for(int i=piv;i>0;i--) {
				yellow[i][j] = yellow[i-1][j];
				yellow[i-1][j]=0;
			}
		}

	}

	
}