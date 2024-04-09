import java.util.*;
import java.io.*;

public class Main {
	static class Virus{
		int s,d,size;//속력,방향,크기
		Virus(int s, int d, int size){
			this.s = s;
			this.d =d ;
			this.size = size;
		}
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[").append(s).append(",").append(d).append(",").append(size).append("]");
			return sb.toString();
		}
	}
	public static void main(String[] args) throws FileNotFoundException {
		//n,m 곰팡이 채취
		//곰팡이 크기, 속력
	
		
		//탐색 첫 열부터 시작
		//해당 열의 위에서 아래로 가며 제일 빨리 발견한 곰팡이 채취
			//빈칸이 된다.
				//곰팡이 없을 수 있음
		//곰팡이는 이동
			//벽에 도달하면 방향을 바꿔서 이동한다.
		//이동후 두마리 이상이면 큰 곰팡이가 다른 곰팡이 먹는다.
		
		//1초
			//오른쪽 열로 이동
		
		
		//n,m은 10000번
			//100*100000 100만번
		
		//이동
			//곰팡이의 정보를 저장
				//r,c,방향,속력
			//배열 형태로 저장한다.
		
		//지도
			//r,c에 곰팡이 번호를 저장
		
		//열에서 위에서 아래로 내려가면서 확인
			//곰팡이 번호 확인
				//해당 곰팡이 번호를 배열에서 제거
		
		//전체다 돌리는 수밖에 없는거 같은데
		
		
		
		//그냥 지도 전체 탐색하자.
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int m = sc.nextInt();
		int k = sc.nextInt();
		
		Virus[][] map = new Virus[n][m];
		
		
		
		
		for(int i=0;i<k;i++) {
			int x = sc.nextInt();
			int y = sc.nextInt();
			int s = sc.nextInt();
			int d = sc.nextInt();
			int b = sc.nextInt();
			map[x-1][y-1] = new Virus(s,d-1,b);
		}
		
		int answer = 0;
		for(int j=0;j<m;j++) {
			//바이러스 하나 삭제
			for(int i=0;i<n;i++) {
				if(map[i][j]!=null) {
					answer+=map[i][j].size;
					map[i][j] = null;
					break;
				}
			}
			
			//이동
			map = move(map);
			
		}
		System.out.println(answer);
		
		
		
	}
	private static void print(Virus[][] map) {
		System.out.println("----");
		for(int i=0;i<map.length;i++) {
			System.out.println(Arrays.toString(map[i]));
		}
		
	}
	private static Virus[][] move(Virus[][] map) {
		Virus[][] result = new Virus[map.length][map[0].length];
		
		int[] dr = {-1,1,0,0};
		int[] dc = {0,0,1,-1};
		
		for(int i=0;i<map.length;i++) {
			for(int j=0;j<map[0].length;j++) {
				if(map[i][j]!=null) {
					int r = i;
					int c = j;
					int d = map[i][j].d;
					for(int speed = 0;speed<map[i][j].s;speed++) {
						int zr = r+dr[d];
						int zc = c+dc[d];
						if(zr<0||zc<0||zr>=map.length||zc>=map[0].length) {
							//방향전환
							if(d%2==0) {
								d++;
							}else {
								d--;
							}
							r+=dr[d];
							c+=dc[d];
						}else {
							r = zr;
							c = zc;
						}
					}
					
					if(result[r][c]!=null) {
						//비교를 한다.
						if(result[r][c].size<map[i][j].size) {
							result[r][c] = map[i][j];
							result[r][c].d = d;
						}
					}else {
						result[r][c] = map[i][j];
						result[r][c].d = d;
					}
				}
			}
		}
		
		return result;
	}
}