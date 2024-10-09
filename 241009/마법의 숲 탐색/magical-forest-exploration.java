import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
	
	static class Node{
		int r, c;
		int curId;
		Node(int r, int c,int curId){
			this.r = r;
			this.c= c;
			this.curId = curId;
		}
	}
	//북,동,남,서
	static int[] dr = {-1,0,1,0};
	static int[] dc = {0,1,0,-1};
	static int[][] map;
	static boolean[][] exitMap;
	static int R, C;
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		//r,c 마법의 숲 탐색
		//숲의 북쪽만 출입
		
		//k면의 정령, 골렘을 타고 숲을 탐ㅅ객
		//골렘은 십자모양의 구조
			//4방향중 하나만 출구
			//탑승은 맘대로, 내릴때는 출구에서만
		//골렘은 북에서 시작해, ci열에서 내려온다.
			//초기 출구는 di위치
		
		
		//우선순위
		//남으로 한칸
			//십자모양으로 비어있어야 가능
			//안되면 서쪽으로 회전해서 이동
				//서쪽은 반시계로 회전
			//안되면 동쪽으로 회전하면서 이동
				//동쪽은 시계로 회전
			//골렘이 더이상 움직이지 않을때까지
		//정령은 제일 남쪽으로 이동
			//출구와 골렘이 인접ㅎ면 골렘 이동이 가능
		//만약 골렘의 일부가 숲을 벗어나면 골렘을 초기화하고 나서 골렘 진행
			//해당 골렘은 답에 포함 X
		
		
		R = Integer.parseInt(st.nextToken());//r
		C = Integer.parseInt(st.nextToken());//c
		int K = Integer.parseInt(st.nextToken());//정령의 수
		
		int[] golemC = new int[K+1];//골렘 출발 열
		int[] golemD = new int[K+1];//골렘의 출구 방향
		//골렘 정보
		
		for(int k=1;k<=K;k++) {
			st = new StringTokenizer(br.readLine());
			int c = Integer.parseInt(st.nextToken());//r
			int d = Integer.parseInt(st.nextToken());//c
			
			golemC[k] = c-1;
			golemD[k] = d;
		}
		
		map = new int[R+2][C];//지도
		exitMap = new boolean[R+2][C];//지도
		//빈곳은 0, 골렘은 1, 출구는 2로 표시
		
		int answer =0;
		//진행
		for(int k=1;k<=K;k++) {
			
			int fairyR = 0;
			//아래로 한칸씩 이동
			for(int r=1;r<R;r++) {
				//바로 아래 이동가능
				if(moveDown(r,golemC[k])) {
					//변화없음
					fairyR = r+1;
					continue;
				}
				//왼쪽 아래 이동 가능
				if(moveLeft(r,golemC[k])) {
					//d가 반시계로 이동
					golemD[k]-=1;
					if(golemD[k]<0) {
						golemD[k]=3;
					}
					golemC[k]-=1;
					fairyR = r+1;
					continue;
				}
				//오른족 아래 이동 가능
				if(moveRight(r,golemC[k])) {
					//d가 시계로 이동
					golemD[k]=(golemD[k]+1)%4;
					golemC[k]+=1;
					fairyR = r+1;
					continue;
				}
				
				//이동이 불가능
				fairyR = r;
				break;
			}
			if(fairyR<=2) {
				//지도를 초기화 진행
				map = new int[R+2][C];
				exitMap = new boolean[R+2][C];
			}else {
				//지도에 골렘 표시
				//System.out.println("현재골램"+fairyR+","+golemC[k]);
				fixGolem(fairyR,golemC[k],golemD[k],k);
				answer+=moveFairy(fairyR,golemC[k]);
			}
		}

		//정령의 최종위치행의 합
		System.out.println(answer);
	}
	private static int moveFairy(int r, int c) {
		//r,c를 기준으로 제일 남쪽으로 이동하는 bfs를 찾는다.
		boolean[][] visited = new boolean[R+2][C];
		
		Queue<Node> que = new ArrayDeque<>();
		
		que.add(new Node(r,c,map[r][c]));
		visited[r][c] = true;
		
		int result = 0;//가장 밑으로 간 r을 갱신
		while(!que.isEmpty()) {
			Node cur = que.poll();
			
			result = Math.max(result, cur.r);
			for(int d=0;d<4;d++) {
				int zr = cur.r+dr[d];
				int zc = cur.c+dc[d];
				
				//밖으로 나가는지 확인
				if(zr<0||zc<0||zr>=map.length||zc>=C) continue;
				
				//로봇유무
				if(map[zr][zc]==0) continue;
				//중복 여부
				if(visited[zr][zc]) continue;
				//같은 로봇아이디는 그냥 이동
				if(map[zr][zc]==cur.curId) {
					visited[zr][zc] = true;
					que.add(new Node(zr,zc,map[zr][zc]));
				}else {
					//현위치가 로봇의 출구면 그냥 나가진다.
					if(exitMap[cur.r][cur.c]){
						visited[zr][zc] = true;
						que.add(new Node(zr,zc,map[zr][zc]));
					}
				}
				
			}
			
			
		}
		
		
		//System.out.println(result-1);
		return result-1;//2가 추가되고, 1을 빼야 행이 계산됨
	}
	private static void fixGolem(int fairyR, int fairyC, int golemD, int golemID) {
		//4방향에 1로 표시, 출구면 2로 표시
		map[fairyR][fairyC] = golemID;
		
		for(int d=0;d<4;d++) {
			int zr = fairyR+dr[d];
			int zc = fairyC+dc[d];
			
			map[zr][zc] = golemID;
			if(d==golemD) {
				exitMap[zr][zc] = true;
			}
		}
		
	}
	private static boolean moveRight(int r, int c) {
		//오른쪽 이동할 위치, 그다음 아래로 갈 위치만 확인한다.
		if(c+1>=C||c+2>=C) {
			return false;
		}
		if(map[r][c+2]==0&&map[r-1][c+1]==0&&map[r+1][c+1]==0
				&&map[r+1][c+2]==0&&map[r+2][c+1]==0) {
			return true;
		}
		
		return false;
	}
	private static boolean moveLeft(int r, int c) {
		//왼쪽 이동할 위치, 그다음 아래로 갈 위치만 확인한다.
		//왼쪽이 벽이면 이동 안됨
		if(c-1<0||c-2<0) {
			return false;
		}
		
		if(map[r][c-2]==0&&map[r-1][c-1]==0&&map[r+1][c-1]==0
				&&map[r+1][c-2]==0&&map[r+2][c-1]==0) {
			return true;
		}
		
		return false;
	}
	private static boolean moveDown(int r, int c) {
		//지도의 아래의 3좌표만 확인하면 된다.
		if(map[r+1][c-1]==0&&map[r+2][c]==0&&map[r+1][c+1]==0) {
			return true;
		}

		return false;
	}
}