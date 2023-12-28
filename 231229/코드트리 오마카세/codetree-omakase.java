import java.util.*;
import java.io.*;


//2:33 그냥 구현 4:10

//테케1통과 4:50
//테케전부통과5:31

// turn개선을 위해 belt수정
//treeset을 적용

public class Main {
	
	//L개의 의자, x는 0기저믕로 시계방향
	//회전초밥은 의자앞에 여러개
	//초밥벨트는 1초에 한칸 이동
		//시계방향으로 이동
		//index는 -1씩 줄어든다.
	
	//처음엔 초밥X, 사람X
	
	
	//벨트길이는 1000000000 10억
	//명령은 10만
		//최소 밸트 길이는 10만
	//시간은 10억
	
	//서로다른 이름은 15000개
	
	
	//사람
		//현재 앉은 위치
			//시간에 따라 -1로 간다.
		//먹어야 하는 갯수
	//초밥
		//hashmap(string,(map int위치, int갯수))
		//이름 - (놓인위치, 갯수)
	
	
	static int L;
	static HashMap<String, TreeMap<Integer,Integer>> belt;//벨트의 정보
	//이름 - 위치,갯수
	static HashMap<String,Guest> guests;//고객 정보 조회
	
	static class Guest{
		String name;
		int chair;
		int eat;//먹어야되는 갯수
		public Guest(String name,int chair, int eat) {
			super();
			this.name = name;
			this.chair = chair;
			this.eat = eat;
		}
		@Override
		public String toString() {
			return "Guest [name=" + name + ", chair=" + chair + ", eat=" + eat + "]";
		}
	}
	
	static int totalSusiCnt;//전체 초밥 갯수
	static int guestCnt;//손님수
	
	static int time = 0;//현재 시간
	
	static StringBuilder sb = new StringBuilder();
	
	private static void init(int l) {
		L=l;
		belt = new HashMap<>();
		guests = new HashMap<>();
		totalSusiCnt = 0;
		guestCnt = 0;
		time = 0;
		
	}
	
	private static int makeChairNum(int t, int x) {
		int result = x-(t-1);
		//result계산은 잘하자.
		result%=L;
		if(result<0) {
			//-result만큼 왼쪽으로 이동해야한다.
			//%로 줄인다.
			result+=L;
		}
		return result;
	}
	
	private static void makeSusi(int t, int x, String name) {
		//t에 x앞에 있는 벨트에 name을 부착한 초밥을 올린다.
		//회전이 일어난 직후 발생
		//같은 위치에 여러 초밥 올라갈 수 잇다.
		//같은 초밥이 같은 위치에 여러개 올락ㄹ 수 있다.
		
		turn(t);
		
		int cnt = 0;
		int chairNum = makeChairNum(t,x);
		
		
		if(!belt.containsKey(name)) {			
			belt.put(name, new TreeMap<>((a,b)->{
				return a-b;
			}));
		}else {
			if(!belt.get(name).containsKey(chairNum)) {
				belt.get(name).put(chairNum, 0);
			}			
			cnt = belt.get(name).get(chairNum);
		}
		
		//갯수+1해준다.
		belt.get(name).put(chairNum,cnt+1);
		totalSusiCnt+=1;
		
		
		//해당 초밥을 먹어버릴수 있음
		if(guests.containsKey(name)&&guests.get(name).chair==chairNum) {
			//먹어버림
			eat(name,chairNum);
		}

	}

	


	private static void enterGuest(int t, int x, String name,int n) {
		//name인 사람이 t시에 x로 앉는다.
		//회전이 일이난 직후
		//x앞으로 오는 초밥중에 이름이 있는 초밥 n개를 먹고 자리를 떤난다.
		//x에 앉는 즉시 먹기 시작
		//초밥 먹는데 시간x
		
		turn(t);
		
		int chairNum = makeChairNum(t,x);
		
		//회전
		guests.put(name,new Guest(name, chairNum,n));
		guestCnt+=1;
		
		
		//먹을게 있으면 먹자
		
		
		//해당 벨트의 초밥 갯수
		
		eat(name,chairNum);
	}

	private static void takePhoto(int t) {
		//회전이 일어난뒤
		//초밥 먹은뒤
		//초밥짐의 사람수, 초밥수 출력
		
		//회전
		
		turn(t);
		
//		System.out.println(guestCnt+" "+totalSusiCnt);
		sb.append(guestCnt).append(" ").append(totalSusiCnt).append("\n");
	}
	



	//초밥은 한바퀴만 돌면 다 먹을 수 잇다!
		//그러면 어디에서 도착하는지가 중요하다.
	//원리
		//사람수만큼 반복
		//해당 사람을 t-time사이동안 이동시킨다.
		//한바퀴만 돌면 되므로 한바퀴 이상이면 한바퀴만 돌고, 최종자리로 이동
		//한바퀴 이내면, 해당 위치로 이동
			//이때가 L이 엄청 클때이다. 오래걸릴수밖에 없다.
	//줄이기
		//사람이 있으면 L을 반복문으로 하지 않고도 바로 먹을 만큼 먹을 수 있게 하자.
		//오래걸리는 이유 - L이 길때 
	//이름 - [테이블번호,갯수]로 된 arraylist면 되나?
		//번호를 기준으로 정렬을 해주면  된다.
		//필요없는 테이블 번호를 검사할 필요가 없다
		//근데 테이블에 1씩밖에 없으면 말짱 도루묵아님?
		//그리고 n번 테이블 부터 조회하기 위해서는 
	private static void turn(int t) {
		//기존time부터 해서 t까지 회전을 진행
		//모든 사람을 돈다.
		int gap = t-time;
		
		int jumpGap = gap%L;
		for(String name : guests.keySet()) {
			Guest g = guests.get(name);
			if(g.eat==0) continue;
			
			int moveChair = g.chair;//현재 앉은 자리
			
			
			//gap만큼 왼쪽으로 이동시킨다.
			
			//현재 위치에서 lower인 위치를 찾는다.
			//현위치-lower위치<gap이면 이동
			//현위치-lower위치>gap이면 이동X 종료
			//lower가 없는경우
				//현위치를 L로 해서 다시 계산
			int moveGap = gap;
			while(true) {
				//lower가 없다
				if(!belt.containsKey(g.name)) break;
				if(belt.get(g.name).size()==0) break;
				
				int next = 0;
				if(belt.get(g.name).lowerKey(moveChair)==null) {
					moveChair = moveChair+L; 
				}
				next = belt.get(g.name).lowerKey(moveChair);
				
				if(moveChair-next>moveGap) {
					//이동이 불가능
					break;
				}else {
					//먹자
					
					eat(name,next);
					moveGap-=(moveChair-next);
					moveChair = next;
				}
				
			}
			
			//위에서 돈만큼 이동한다.
			
			g.chair-=jumpGap;
			if(g.chair<0) {
				g.chair+=L;
			}
		}
		
		time = t;
		
//		System.out.println("무야호"+totalSusiCnt);
	}

	
	//먹기
	static public void eat(String name, int chair) {
		Guest g = guests.get(name);
		//해당 벨트의 초밥 갯수
		
		// 해당 벨트에 name으로 
		if(!belt.containsKey(name)) return;
		if(!belt.get(name).containsKey(chair)) return; 
		if(g.eat==0) return;
		
		int susiCount = belt.get(name).get(chair);
		
		
		if(g.eat>=susiCount) {					
			g.eat-=susiCount;
			totalSusiCnt-=susiCount;
			belt.get(name).remove(chair);
		}else {
			totalSusiCnt-=g.eat;
			susiCount-=g.eat;
			g.eat = 0;
			
			belt.get(name).put(chair,susiCount);
		}
		
		if(g.eat==0) {
			guestCnt-=1;
		}
	}
	

	public static void main(String[] args) throws IOException {
//		long beforeTime1 = System.currentTimeMillis();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int L= Integer.parseInt(st.nextToken());
		int Q = Integer.parseInt(st.nextToken());

		init(L);
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