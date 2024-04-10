import java.util.*;


public class Main {
	static int w = 3;
	static int h = 3;
	public static void main(String[] args) throws Exception{
		
		//3,3
		//행의 개수>=열의개수
			//행에 대해 정렬, 출현빈도수가 적은 순서대로 
			//횟수가 같으면 숫자가 작은 순서대로
			//숫자,출현빈도수 출ㄹ력
		//열의개수>행의개수
			//위처럼 진행
		//행이나 열이 100을 넘으면 그냥 자름
		
		//r,c가 원하는 값이 되는데 걸리는 시간을 구하라.
		Scanner sc = new Scanner(System.in);
		
		int[][] map = new int[100][100];
		
		int r = sc.nextInt();
		int c = sc.nextInt();
		int k = sc.nextInt();
		
		r-=1;
		c-=1;
		
		
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				map[i][j] = sc.nextInt();
			}
		}
		
		int time=0;
		while(true) {
			if(time>100) {
				time=-1;
				break;
			}
			if(map[r][c]==k) break;
			
			if(h>=w) {
				map = row(map);
			}else {
				map = col(map);
			}
			time++;
		}
		System.out.println(time);
	}
	
	static int[][] row(int[][] map){
		int[][] result = new int[100][100];
		for(int i=0;i<h;i++) {
			Map<Integer,Integer> check = new HashMap<>();
			//값 확인
			for(int j=0;j<w;j++) {
				if(map[i][j]==0) break;
				if(!check.containsKey(map[i][j])) {
					check.put(map[i][j], 0);
				}
				check.put(map[i][j], check.get(map[i][j])+1);
			}
			
			//정렬
			ArrayList<int[]> list = new ArrayList<>();
			for(int k : check.keySet()) {
				list.add(new int[]{k,check.get(k)});
			}
			Collections.sort(list,(a,b)->{
				if(a[1]==b[1]) {
					return a[0]-b[0];
				}
				return a[1]-b[1];
			});
			
			int rowW = Math.min(100, list.size()*2);
			w = Math.max(w, rowW);//갱신

			//정렬된값 출력
			for(int j=0;j<rowW;j+=2) {
				result[i][j]=list.get(j/2)[0];
				if(j+1==100) break;
				result[i][j+1]=list.get(j/2)[1];
			}
		}
		
		return result;
	}
	
	static int[][] col(int[][] map){
		int[][] result = new int[100][100];
		
		for(int j=0;j<w;j++) {
			Map<Integer,Integer> check = new HashMap<>();
			//값 확인
			for(int i=0;i<h;i++) {
				if(map[i][j]==0) break;
				if(!check.containsKey(map[i][j])) {
					check.put(map[i][j], 0);
				}
				check.put(map[i][j], check.get(map[i][j])+1);
			}
			
			//정렬
			ArrayList<int[]> list = new ArrayList<>();
			for(int k : check.keySet()) {
				list.add(new int[]{k,check.get(k)});
			}
			Collections.sort(list,(a,b)->{
				if(a[1]==b[1]) {
					return a[0]-b[0];
				}
				return a[1]-b[1];
			});
			
			int colH = Math.min(100, list.size()*2);
			h = Math.max(h, colH);//갱신

			//정렬된값 출력
			for(int i=0;i<colH;i+=2) {
				result[i][j]=list.get(i/2)[0];
				if(i+1==100) break;
				result[i+1][j]=list.get(i/2)[1];
			}
		}
		
		return result;
	}
}