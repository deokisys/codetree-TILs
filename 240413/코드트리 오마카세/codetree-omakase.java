import java.util.*;
import java.io.*;

// 테이블 크기 1,000,000,000  = 10억
// 10만번 수행

// t시간은 = 10억


// 초밥을 실제로 돌리는데 시간이 오래걸릴것
	//사람*t만큼이니까
// 그럼 다른 방식은 뭔데?
	// 명령어들만 돌리면 된다.

// 접근
	// 먹는 시간을 기준으로 pq를 정렬한다.
		// turn할때는 해당 시간보다 작으면 pq를 돌리면서 먹는다.
	// 사람이 들어오면 지금까지 해당 초밥을 언제 먹는지 시간을 계산해서 pq에 넣는다.
//문제점
	// 초밥 먹는시간을 모름
		// 그럼 계산하면 되지!
		// 사람이 들어오는 순간, 사람이 먹어야하는 시간을 계산해서 pq에 넣는다.


public class Main {
	
	//고객 정보
	static class Customer{
		int t;
		int x;
		int n;
		boolean isSeat = false;
		public Customer(int t, int x,int n) {
			super();
			this.t = t;
			this.x = x;
			this.n = n;
		}
		@Override
		public String toString() {
			return "Customer [t=" + t + ", x=" + x + ", n=" + n + ", isSeat=" + isSeat + "]";
		}
		
	}
	
	//초밥정보
	static class Susi{
		int t;
		int x;
		String name;
		public Susi(int t, int x, String name) {
			super();
			this.t = t;
			this.x = x;
			this.name = name;
		}
		@Override
		public String toString() {
			return "Susi [t=" + t + ", x=" + x + ", name=" + name + "]";
		}
		
	}
	
	//초밥 먹는거
	static PriorityQueue<Susi> eatQue = new PriorityQueue<>((a,b)->a.t-b.t);
	
	//아직 먹지는 못하는 초밥
	static HashMap<String,Queue<Susi>> notEatQue = new HashMap<>();
	
	//고객의 정보
	static HashMap<String,Customer> customer = new HashMap<>();
	static StringBuilder sb = new StringBuilder();
	
	static int L;
	static int susiCnt, customerCnt;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		L= Integer.parseInt(st.nextToken());
		int Q = Integer.parseInt(st.nextToken());

		//회전초밥은 의자앞에 여러개
		//1초에 한칸씩 시계방향으로 돈다. 
		
		
		for(int q=0;q<Q;q++) {
			st = new StringTokenizer(br.readLine());
			
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd==100) {
				int t = Integer.parseInt(st.nextToken());
				turn(t);
				int x = Integer.parseInt(st.nextToken());
				String name = st.nextToken();
				
				makeSusi(t,x,name);
			}else if(cmd==200) {
				int t = Integer.parseInt(st.nextToken());
				turn(t);
				int x = Integer.parseInt(st.nextToken());
				String name = st.nextToken();
				int n = Integer.parseInt(st.nextToken());
				enterGuest(t,x,name,n);
			}else if(cmd==300) {
				int t = Integer.parseInt(st.nextToken());
				turn(t);
				takePhoto(t);
			}
		}
		
		System.out.println(sb.toString());
		
	}





	private static void turn(int curTime) {
		while(true) {
			if(eatQue.isEmpty()) break;
			if(curTime<eatQue.peek().t) break;
			
			Susi susi = eatQue.poll();
			susiCnt--;
			
			customer.get(susi.name).n--;
			if(customer.get(susi.name).n==0) {
				customerCnt--;
			}
		}
	}

	private static void takePhoto(int t) {
		sb.append(customerCnt).append(" ").append(susiCnt).append("\n");
	}

	private static void enterGuest(int t, int x, String name, int n) {
		customer.put(name, new Customer(t, x, n));
		customerCnt++;
		
		//입장전에 들어온 초밥들을 먹을 수 있게 eatQue에 넣느다.
		if(notEatQue.containsKey(name)) {
			//입장전에 초밥들을 이제 먹을 시간으로 보낸다.
			while(!notEatQue.get(name).isEmpty()) {
				Susi susi = notEatQue.get(name).poll();
				
				//현재 시간을 t를 기준으로 x가 되는 시간에 먹인다.
				
				//susi가 입장하고 난 시간t, 현재시간 t
				//susi가 현재시간t에 있는 위치 = 초밥이 왔을때 위치 + (현시간-초밥온시간);
				
				
				int susiCurPosition = (susi.x+(t-susi.t))%L;
				
				//만약 같은 위치면 그냥 바로 먹인다.
				if(susiCurPosition==x) {
					susiCnt--;
					customer.get(name).n--;
					if(customer.get(name).n==0) {
						customerCnt--;
					}
				}else {
					//초밥위치<나 위치
						//현재 시간 t+(나의 위치-초밥위치)
					//초밥위치>나 위치
						//현재시간 t + (L-(초밥위치-나의위치))
					int susiEatTime = 0;
					if(susiCurPosition < x) {
						susiEatTime = t + (x-susiCurPosition);
					}else {
						susiEatTime = t + (L-(susiCurPosition-x));
					}
					
					eatQue.add(new Susi(susiEatTime, 0, name));
				}
				
			}
		}
	}

	private static void makeSusi(int t, int x, String name) {
		susiCnt++;
		if(customer.containsKey(name)) {
			//고객이 들어와 았는 경우
			
			//해당 고객위치에 맞게 위치를 계산하여 넣느다.
			int susiCurPosition = x;
			
			//만약 같은 위치면 그냥 바로 먹인다.
			if(susiCurPosition==customer.get(name).x) {
				susiCnt--;
				customer.get(name).n--;
				if(customer.get(name).n==0) {
					customerCnt--;
				}
			}else {
				//초밥위치<나 위치
					//현재 시간 t+(나의 위치-초밥위치)
				//초밥위치>나 위치
					//현재시간 t + (L-(초밥위치-나의위치))
				int susiEatTime = 0;
				if(susiCurPosition < customer.get(name).x) {
					susiEatTime = t + (customer.get(name).x-susiCurPosition);
				}else {
					susiEatTime = t + (L-(susiCurPosition-customer.get(name).x));
				}
				
				eatQue.add(new Susi(susiEatTime, 0, name));
			}
		}else {
			//고객이 아직 없음
			if(!notEatQue.containsKey(name)) {				
				notEatQue.put(name, new ArrayDeque<Susi>());
			}
			notEatQue.get(name).add(new Susi(t, x, name));
		}
		
	}
}