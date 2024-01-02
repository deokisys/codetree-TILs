import java.util.*;
import java.io.*;

//12:22
public class Main {
	
	/**
	 * p마리의 토키,N,M크기 격자위에서 경주 진행
	 * 토끼에게 고유 번호, 이동거리, 첫위치는 1,1
	 *  
	 * 우선순위높은 토끼 뽑아 k번 반복
	 * 우선순위
	 * 	총점프 작은, 행+열 작은, 행작은, 열작은, 고유번호 작은
	 * 
	 * 해당 토끼를 상하좌우 이동위치 구한다.
	 * 	벗어나면 반대로 이동
	 * 위치 우선순위
	 * 	행+열 큰, 행큰, 열큰
	 * 우선순위 높은위치로 이동
	 * 나머지 토끼는 r+c만큼 점수를 얻음
	 * 
	 * k번 진행후
	 * 우선순위
	 * 	k번동안 한번이라도 뽑힌 토끼중
	 * 	행+열 큰, 행큰, 열 큰, 고윸ㄴ
	 * 우선순위 높은토끼를 S더한다.
	 * 
	 * */
	
	
	static class Rabbit{
		int idx;
		int r;
		int c;
		int totalJump;
		public Rabbit(int idx,int r, int c) {
			super();
			this.idx = idx;
			this.r = 0;
			this.c = 0;
		}
	}
	
	static Map<Integer,Long> point;
	static Map<Integer, Integer> distance;
	
	static PriorityQueue<Rabbit> racers;
	static Rabbit kRacers;
	
	static int H,W;
	static long lowerPoint = 0l;
	private static void init(int h, int w, int p, int[] pids, int[] ds) {
		//1번
		//토끼는 2000마리
		//거리는 10억
		H = h;
		W = w;
		
		//고유번호 - 점수확인용 map
		point  = new HashMap<Integer, Long>();
		
		//고유번호 - 이동거리용 map
		distance = new HashMap<Integer, Integer>();
		
		for(int i=0;i<p;i++) {
			point.put(pids[i], 0l);
			distance.put(pids[i], ds[i]);
		}
		
		
		//토끼
			//고유번호, 현위치
			//총 점프
		
		//레이싱 pq
			//우선순위로 k번 레이싱에서 사용
		racers = new PriorityQueue<Rabbit>((a,b)->{
			//점프횟수적은 토끼
			if(a.totalJump==b.totalJump) {				
				//행+열 작은 토끼
				if(a.r+a.c == b.r+b.c) {					
					//행 작은 토끼
					if(a.r == b.r) {						
						//열 작은 토끼
						if(a.c==b.c) {
							//고유번호 작은
							return a.idx-b.idx;
						}
						return a.c-b.c;
					}
					return a.r-b.r;
				}
				return (a.r+a.c)-(b.r+b.c);
			}
			return a.totalJump-b.totalJump;
		});
		
		//racer에 입력
		
		for(int i=0;i<p;i++) {
			racers.add(new Rabbit(pids[i],0,0));
		}

	}
	private static void race(int k, int s) {
		//2000번
		
		//K번 반복 - 최대 100번		
		kRacers = new Rabbit(-1, -1, -1);
		for(int i=0;i<k;i++) {
			raceK();
		}
		
		//pq를 이용 1로 뽑음
		//k번 종료
			//k번중 뽑힌 토끼중 
			//우선순위 확인
			//높은 토끼S점 추가
		//Rabbit best = kRacers.poll();
		//S점 추가
		point.put(kRacers.idx,point.get(kRacers.idx)+s);
		
	}


	private static void raceK() {
	
		//우선순위 높은 토끼 뽑기
		Rabbit racer = racers.poll();
		//4방향 확인
			//우선순위 확인
		
		
		int maxR = -1;
		int maxC = -1;
		for(int d=0;d<4;d++) {
			int r = racer.r;
			int c = racer.c;
			if(d%2==0) {
				//0,2
				//상하
				r = getDist(d,r,distance.get(racer.idx),H);
			}else {	
				//1,3
				//좌우
				c = getDist(d,c,distance.get(racer.idx),W);
			}
			
			//이동 위치 r,c
			//행+열 큰, 행큰, 열큰
			
			if(r+c==maxR+maxC){
				if(r==maxR) {
					if(maxC<c) {
						maxR = r;
						maxC = c;
					}
				}else if(maxR<r) {
					maxR = r;
					maxC = c;
				}
			}else if(maxR+maxC<r+c) {
				maxR = r;
				maxC = c;
			}
		}
		
		//이동
			//나머지 토끼 r+c점수 얻음
		
		racer.r = maxR;
		racer.c = maxC;
		racer.totalJump+=1;
		racers.add(racer);
		
		
		
		//해당 토끼만 -점수 준다.
		point.put(racer.idx, point.get(racer.idx)-(maxR+maxC+2));
		
		if(lowerPoint>0&&lowerPoint+maxR+maxC+2<0) {
			System.out.println("--");
			System.out.println(maxR);
			System.out.println(maxC);
			System.out.println(lowerPoint);
		}
		lowerPoint+=(maxR+maxC+2);
		
		//kracer에 넣어준다.
		if(kRacers.idx==-1) {
			kRacers.idx = racer.idx;
			kRacers.r = racer.r;
			kRacers.c = racer.c;
		}else {
			//(현재 서있는 행 번호 + 열 번호가 큰 토끼, 행 번호가 큰 토끼, 열 번호가 큰 토끼, 고유번호가 큰 토끼
			if(kRacers.r+kRacers.c==racer.r+racer.c) {
				if(kRacers.r==racer.r) {
					if(kRacers.c==racer.c) {
						if(kRacers.idx<racer.idx) {
							kRacers.idx = racer.idx;
							kRacers.r = racer.r;
							kRacers.c = racer.c;
						}
					}else if(kRacers.c<racer.c) {
						kRacers.idx = racer.idx;
						kRacers.r = racer.r;
						kRacers.c = racer.c;
					}
				}else if(kRacers.r<racer.r) {
					kRacers.idx = racer.idx;
					kRacers.r = racer.r;
					kRacers.c = racer.c;
				}
				
			}else if((kRacers.r+kRacers.c)<(racer.r+racer.c)) {
				kRacers.idx = racer.idx;
				kRacers.r = racer.r;
				kRacers.c = racer.c;
			}
			
		}
		
	}
	
	
	
	
	private static int getDist(int d, int cur, int dist, int length) {
		
		
		if(d<2) {
			//2보다 작으면 -로이동
			
			//0으로 만들고 시작
			
			if(cur!=0) {
				if(dist>=cur) {
					dist-=cur;
					cur = 0;
				}else {
					cur-=dist;
					dist = 0;
				}
			}
			
			
			//한쪽 끝으로 가는데 필요한 거리 H-1씩 걸린다.
				//H-1 *2를 하면 한바퀴돈다.
			if(dist==0) {
				return cur;
			}
			
			
			//H-1 *2로 나눈다.
			dist%=((length-1)*2);
			
			
			if(dist > length-1) {
				dist -= (length-1);
				cur+=(length-1);
				
				cur-=dist;
				
			}else {
				cur+=dist;
			}
			
			return cur;
			
			
		}else {
			//2보다 크면 +로이동
			
			//H-1로 만들고 시작
			
			if(cur!=length-1) {
				
				//H-1로 만들기는 H-1 - cur만큼
				//H-1로 가는 거리가, 이동해야 하는 dist
				if((length-1)-cur<=dist) {
					dist -= (length-1)-cur;
					cur = length-1;
				}else {
					cur += dist;
					dist = 0;
				}
			}
			
			//한쪽 끝으로 가는데 필요한 거리 H-1씩 걸린다.
				//H-1 *2를 하면 한바퀴돈다.
			if(dist==0) {
				return cur;
			}
			
			
			//H-1 *2로 나눈다.
			dist%=((length-1)*2);
			
			
			if(dist > length-1) {
				dist -= (length-1);
				cur-=(length-1);
				
				cur+=dist;
				
			}else {
				cur-=dist;
			}
		}
		
		
		return cur;
	}
	private static void changeDist(int pid, int l) {
		//2000번
		distance.put(pid,distance.get(pid)*l);
	}


	private static void bestRabbit() {
		//1번
		long max = -1l;
		for(long value : point.values()) {
//			System.out.print(value+" ");
			long rabbitPoint = value + lowerPoint;
			max = Math.max(rabbitPoint, max);
		}
//		System.out.println("");
//		System.out.println(lowerPoint);
		System.out.println(max);
	}
	
	
	public static void main(String[] args) throws IOException {
//		long beforeTime1 = System.currentTimeMillis();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int Q= Integer.parseInt(st.nextToken());

		for(int q=0;q<Q;q++) {
			st = new StringTokenizer(br.readLine());
			
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd==100) {
				int h = Integer.parseInt(st.nextToken());//세로길이
				int w = Integer.parseInt(st.nextToken());//가로길이
				int p = Integer.parseInt(st.nextToken());//토끼갯수
				int[] pids = new int[p];
				int[] ds = new int[p];
				
				for(int i=0;i<p;i++) {
					pids[i] = Integer.parseInt(st.nextToken());
					ds[i] = Integer.parseInt(st.nextToken());
				}
				
				
				init(h,w,p,pids,ds);
			}else if(cmd==200) {
				int k = Integer.parseInt(st.nextToken());
				int s = Integer.parseInt(st.nextToken());
				race(k,s);
			}else if(cmd==300) {
				int pid = Integer.parseInt(st.nextToken());
				int l = Integer.parseInt(st.nextToken());
				
				changeDist(pid,l);
			}else if(cmd==400) {
				bestRabbit();
			}
		}
		
	}
	
}