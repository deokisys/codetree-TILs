import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

//3:00
public class Main {
	static class Santa{
		int r,c;
		int point;
		int stunTurn=-1;//기절 여부
		boolean dead;//탈락여부
		int deadTurn;//죽었을때의 턴
		public Santa(int r, int c) {
			super();
			this.r = r;
			this.c = c;
		}
		
		public boolean isStun(int turn) {
			if(this.stunTurn==-1) return false;
			if(turn-stunTurn<2) {
				return true;
			}
			return false;
		}
		
		public int getDist(Rudolf rudolf) {
			return (rudolf.r-this.r)*(rudolf.r-this.r)+(rudolf.c-this.c)*(rudolf.c-this.c);
		}
		
	}
	
	static class Rudolf{
		int r,c;

		public Rudolf(int r, int c) {
			super();
			this.r = r;
			this.c = c;
		}
	}
	
	static int N;
	static int rudolfPower,santaPower;
	static int[]dr = {-1,0,1,0,-1,-1,1,1};
	static int[]dc = {0,1,0,-1,-1,1,-1,1};

	
	public static void main(String[] args) throws Exception{
		
		//1,1시작
		//루돌프이동-산타이동
			//기정,박으로 나간 탈락한 산타는 X
		
		//루돌프
			//가까운 산타로 이동
			//r이 큰산타로, c가 큰산타 
			//8방향이동
			//
		//산타
			//1~p번
			//기절,탈락X
			//산타 가까운 루돌프에게 돌진
			//다른산타X,판밖X
			//못움직이면 정지
			//상하좌우이동
				//여러개면, 상하좌우 우선순위에 맞춤
		//충돌
			//산타,루돌프 같은곳
			//루돌프가 충돌
				//산타가c점수 얻음
				//산타는c만큼 밀려난다
			//산타충돌
				//d점수 얻음
				//산타는반대로d로 밀림
			//밀리는동안 충돌X
			//게임밖으로 밀리면 탈락
		//상호작용
			//밀려난다음 찾지
			//다른 산타가 있으면 그 산타는 1칸 밀림
			//이것은 연쇄적이다.
		//기절
			//루돌프와 충돌 직후 기절
			//다음 턴까지만 기절한다.
			//기절 산타에게 돌진 가능
		//종료
			//m번 이후 종료
			//산타 전부 탈락 종료
			//매턴 이후 산타들에게 1점 부여 - 그냥 턴수를 점수로 주면됨
		
		// 각 산타의 최종 점수를 출력
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());//게임판 크기
		int M = Integer.parseInt(st.nextToken());//턴수
		int P = Integer.parseInt(st.nextToken());//산타수
		rudolfPower = Integer.parseInt(st.nextToken());//루돌프힘
		santaPower = Integer.parseInt(st.nextToken());//산타힘
		
		//초기화
		int[][] map = new int[N][N];//지도, 값은 산타의 번호
		//루돌프는 -1로 표시
		
		//루돌프 입력
		st = new StringTokenizer(br.readLine());
		
		int r = Integer.parseInt(st.nextToken());
		int c = Integer.parseInt(st.nextToken());
		
		Rudolf rudolf = new Rudolf(r-1,c-1);
		map[r-1][c-1] = -1;
		
		//산타 입력
		Santa santa[] = new Santa[P+1];
		
		for(int i=1;i<=P;i++) {
			st = new StringTokenizer(br.readLine());
			int id = Integer.parseInt(st.nextToken());
			r = Integer.parseInt(st.nextToken());
			c = Integer.parseInt(st.nextToken());
			santa[id] = new Santa(r-1,c-1);
			map[r-1][c-1] = id;
		}
		
//		print(map);
		
		//M번 반복
		for(int m=1;m<=M;m++) {
			
//			System.out.println(m+"턴------------------------------------");
			//루돌프 이동
			if(!moveRudolf(rudolf,santa,map,m)) {
				break;
			}
//			print(map);
			//산타 이동
//			System.out.println("산타 이동한다.");
			for(int id=1;id<=P;id++) {				
				moveSanta(rudolf,santa,id,map,m);
//				print(map);
			}
			
			//점수 더해준다.
			for(int id=1;id<=P;id++) {				
				if(santa[id].dead) continue;
				santa[id].point+=1;
			}
			
			
		}
		
		
		//출력
		for(int i=1;i<=P;i++) {
			//죽었으면
				//죽기 직전 turn점수-1을 더해준다.
			//안죽었으면
				//M을 더해준다.
			System.out.print(santa[i].point+" ");
		}
		System.out.println();
	}
	
	private static void print(int[][] map) {
		System.out.println(" ----- ");
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				System.out.print(map[i][j]+" ");
			}
			System.out.println();
		}
		
	}

	private static void moveSanta(Rudolf rudolf, Santa[] santa, int id, int[][] map, int turn) {
		if(santa[id].dead) return;
		if(santa[id].isStun(turn)) return;
		
		
		
		//루돌프를 향해 돌진
		int minDist = (rudolf.r-santa[id].r)*(rudolf.r-santa[id].r)+(rudolf.c-santa[id].c)*(rudolf.c-santa[id].c);
		int moveD = -1;
		int moveR = -1;
		int moveC = -1;
		for(int d=0;d<4;d++) {
			int zr = santa[id].r+dr[d];
			int zc = santa[id].c+dc[d];
			//게임판밖
			if(zr<0||zc<0||zr>=N||zc>=N) continue;
			//만약 다른 산타가 있으면 넘기기
			if(map[zr][zc]>0) continue;
			
			//santa->rudolf
			int dist = (zr-rudolf.r)*(zr-rudolf.r)+(zc-rudolf.c)*(zc-rudolf.c);
			if(dist<minDist) {
				minDist = dist;
				moveD = d;
				moveR = zr;
				moveC = zc;
			}
		}
		
		if(moveD==-1) return;//이 산타는 못움직인다.
		
		//현위치 일단 지운다.
		map[santa[id].r][santa[id].c] = 0;
		
		
		//루돌프 있는지 확인
		if(map[moveR][moveC]==-1) {
			//있으면 santaPower만큼 얻는다.
			
			//기절 처리
			santa[id].stunTurn = turn;
			
			//점수 획득
			santa[id].point+=santaPower;
						
			
			//이동하는 산타가 날라가는 위치
			
			//반대로 이동된다ㅣ
			moveD = (moveD+2)%4;
			
			//루돌프 위치를 기준으로 이동한다.
			int zr = moveR+dr[moveD]*santaPower;
			int zc = moveC+dc[moveD]*santaPower;
			
			slip(map,santa,zr,zc,id,moveD,turn);
			
			
		}else {
			//루돌프 없으면 그냥 이동
			map[moveR][moveC] = id;
			santa[id].r = moveR;
			santa[id].c = moveC;
		}
	}

	private static boolean moveRudolf(Rudolf rudolf, Santa[] santa, int[][] map, int turn) {
		
		//가까운 산타로 돌진
		//가까운 산타위치와 번호 받기
		int minDist = Integer.MAX_VALUE;
		int id = -1;
		for(int i=1;i<santa.length;i++) {
			if(santa[i].dead) continue;
			int dist = santa[i].getDist(rudolf);
			if(dist<minDist) {
				minDist = dist;
				id = i;
			}else if(dist==minDist) {
				//r좌표가 더 큰곳으로
				if(santa[id].r<santa[i].r) {
					id = i;
				}else if(santa[id].r==santa[i].r) {
					if(santa[id].c<santa[i].c) {
						id = i;
					}
				}
			}
		}
		
		if(id==-1) {
			//더이상 움직이는 산타 없음
			return false;
		}
		
		//8방향 계산해서 제일 가까운 이동
		//santa[id]로 이동
		minDist = Integer.MAX_VALUE;
		int moveR=-1;
		int moveC=-1;
		int moveD = -1;
		

		
		for(int d=0;d<8;d++) {
			int zr = rudolf.r+dr[d];
			int zc = rudolf.c+dc[d];
			if(zr<0||zc<0||zr>=N||zc>=N) continue;
			//거리 계산
			//zr,zc에서 santa[id]로 의 거리
			int dist = (zr-santa[id].r)*(zr-santa[id].r)+(zc-santa[id].c)*(zc-santa[id].c);
			if(dist<minDist) {
				minDist = dist;
				moveR = zr;
				moveC = zc;
				moveD =d;
			}
		}
		
		
		
		//이동
		
		
		if(map[moveR][moveC]>0) {
			//기절 처리
			santa[map[moveR][moveC]].stunTurn = turn;
			
			//점수 획득
			santa[map[moveR][moveC]].point+=rudolfPower;
			
			//이동하는 산타 id
			int moveSantaId = map[moveR][moveC];
			
			
			//이동하는 산타가 날라가는 위치
			
			int zr = santa[moveSantaId].r+dr[moveD]*rudolfPower;
			int zc = santa[moveSantaId].c+dc[moveD]*rudolfPower;
			
			//충돌
				//해당 위치에 산타가 있는경우
				//해당 산타는 C를 얻는다.
				//해당 산타를 루돌프가 이동한 방향으로 C이동시킨다.
			
			//밀림
				//그자리에 또 산타가 있으면 1칸씩 이동시킨다.
			slip(map,santa,zr,zc,moveSantaId,moveD,turn);
			
		}
		
		//루돌프 이동 처리
		map[rudolf.r][rudolf.c] = 0;
		map[moveR][moveC] = -1;
		rudolf.r=moveR;
		rudolf.c=moveC;
		
		
		
		
		
		return true;
	}
	
	public static void slip(int[][] map,Santa[] santa,int moveR, int moveC, int id,int dir,int turn) {
		//맵 밖인지 확인
		if(moveR<0||moveC<0||moveR>=N||moveC>=N) {
		//탈락 처리
			santa[id].dead=true;
			santa[id].deadTurn = turn;
			
			return;
		};
		
		//해당 위치에 산타 있는지 확인
		if(map[moveR][moveC]>0) {
			
			int moveId = map[moveR][moveC];
			int zr = santa[moveId].r+dr[dir];
			int zc = santa[moveId].c+dc[dir];
			
			slip(map,santa,zr,zc,moveId,dir,turn);
		}
		//산타 이동처리
		map[moveR][moveC] = id;
		santa[id].r=moveR;
		santa[id].c=moveC;
		
	}
}