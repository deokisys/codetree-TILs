import java.util.*;
import java.io.*;


//해설 보고 분석함
	//자존심 상한다!


//입장과 퇴장은 무조건 한번 - 이걸 까먹었음
	//이게 제일 중요한데!!!

//해설의 방식
//전체 명령를 한번 읽고 시작

//각 사람마다 명령을 확인
//초밥제거되는 명령과, 사람이 퇴출되는 시간에 따른 명령을 추가한다.
	//와 이거 신기하다 ㅋㅋㅋ


//제대로 숙지 못한 조건
	//사람은 한번 입장, 퇴장
	//초밥은 사람이 먹을만큼 제공
		//= 무한히 흐르면 초밥 전부 먹는다 - 


//초밥을 먹는다
	//사람이 들어왔을때 초밥의 위치 -> 이 사람의 초밥 위치가 되었을때의 시간
		//미리 입장 시간을 알아야 바로 계산을 할 수 있어서 미리 명령어를 읽어야 한다.
		//미리 입장 시간 몰라도 할 수 있게하자
	//퇴장시간을 이 초밥을 먹었을때의 시간으로 갱신한다.



public class Main {
	
	static StringBuilder sb = new StringBuilder();
		
	static class  Query{
		int t,x;//시간, 위치, 갯수
		public Query(int t, int x) {
			super();
			this.t = t;
			this.x = x;
		}
	}
	
	static class Guest{
		int time;//입장시간
		int n;//먹어야되는 갯수
		int x;//자리
		public Guest(int time,  int x,int n) {
			super();
			this.time = time;
			this.n = n;
			this.x = x;
		}
	}
	
	static class Eat{
		String name;
		int t;
		public Eat(String name, int t) {
			super();
			this.name = name;
			this.t = t;
		}
	
	}
	
	//사람별 명령 - 아직 먹지 못한 
	static HashMap<String, Queue<Query>> noteatQue = new HashMap<>();//먹어야하는 
	static PriorityQueue<Eat> eatQue = new PriorityQueue<>((a,b)->a.t-b.t);//고객들이 먹는 시간들
	
	//고객 정보
	static HashMap<String,Guest> guests = new HashMap<>();
	
	static int susiCnt =0;
	static int guestCnt = 0;
	
	static int L;

	private static void turn(int t) {
		//eatQue를 t보다 클때까지 돌린다.
		while(!eatQue.isEmpty()) {
			if(eatQue.peek().t>t) {
				break;
			}
			Eat cur = eatQue.poll();
			
			susiCnt-=1;
			
			guests.get(cur.name).n-=1;
			
			if(guests.get(cur.name).n==0) {
				guestCnt-=1;
			}
		}
	}
	
	private static void makeSusi(int t, int x, String name) {
		turn(t);
		
		
		if(guests.containsKey(name)) {
			//입장한 경우
			if(guests.get(name).x!=x) {
				//현재 시간에서 몇초후에 x에 도달할지 
				
				int goalX = guests.get(name).x;
				
				
				//startX에서 시간마다 +1씩 더한다.
				
				if(goalX>x) {
					//필요 시간은 goal-x이 된다.
					eatQue.add(new Eat(name,t+goalX-x));
					
				}else {
					//필요 시간은 goal+L-x가 된다.
					eatQue.add(new Eat(name,t+goalX+L-x));
				}
				susiCnt+=1;
				
			}else {
				//해당 자리에 스시가 올라갔으니 먹자
				guests.get(name).n-=1;
			}
			
			//다 먹음?
			if(guests.get(name).n==0) {
				guestCnt-=1;
			}
			
		}else {
			//입장전
			if(!noteatQue.containsKey(name)) {
				noteatQue.put(name, new ArrayDeque<>());
			}
			noteatQue.get(name).add(new Query(t, x));
			susiCnt+=1;
		}
	}

	private static void enterGuest(int t, int x, String name,int n) {
		turn(t);
		//그전에 온 스시들 전부 계산
		guests.put(name, new Guest(t,x,n));
		guestCnt+=1;
		if(noteatQue.containsKey(name)) {
			//입장전에 온 스시들 전부 que에 넣어준다.
			
			while(!noteatQue.get(name).isEmpty()) {
				Query q = noteatQue.get(name).poll();
				
				//해당 스시의 현시간 x를 구한다.
				int curX = (q.x+(t-q.t))%L;
				
				if(curX==x) {
					//해당 스시가 현재 위치한 스시이면 먹고 끝
					susiCnt-=1;
					guests.get(name).n-=1;
				}else {
					//만약 못먹는 스시면
					
					//curX에서 x까지 가는데 걸리는 시간을 구하여 eatQue에 추가한다.
					if(x>curX) {
						eatQue.add(new Eat(name,t+x-curX));
					}else {
						eatQue.add(new Eat(name,t+x+L-curX));
					}
				}
			}
		}
		
		//다 먹었니
		if(guests.get(name).n==0) {
			guestCnt-=1;
		}
	}

	private static void takePhoto(int t) {
		turn(t);
		sb.append(guestCnt).append(" ").append(susiCnt).append("\n");
	}



	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		L= Integer.parseInt(st.nextToken());
		int Q = Integer.parseInt(st.nextToken());
		
		for(int q=0;q<Q;q++) {
			st = new StringTokenizer(br.readLine());
			
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd==100) {
				int t = Integer.parseInt(st.nextToken());
				int x = Integer.parseInt(st.nextToken());
				String name = st.nextToken();
				
				makeSusi(t,x,name);
			}else if(cmd==200) {
				int t = Integer.parseInt(st.nextToken());
				int x = Integer.parseInt(st.nextToken());
				String name = st.nextToken();
				int n = Integer.parseInt(st.nextToken());
				enterGuest(t,x,name,n);
			}else if(cmd==300) {
				int t = Integer.parseInt(st.nextToken());
				takePhoto(t);
			}
		}
		
		System.out.println(sb.toString());

	}

}