import java.util.*;
import java.io.*;

public class Main {
	static class Virus{
		int r,c,age;
		Virus(int r, int c, int age){
			this.r = r;
			this.c =c ;
			this.age = age;
		}
		
		@Override
		public String toString() {
			return "["+this.r+","+this.c+":"+this.age+"]";
		}
	}
    public static void main(String[] args) throws Exception{

        Scanner sc = new Scanner(System.in);
        
        int n = sc.nextInt();
        int m = sc.nextInt();
        int K = sc.nextInt();
        
        int[][] map = new int[n][n];
        int[][] addMap = new int[n][n];
        PriorityQueue<Virus> virus= new PriorityQueue<>((a,b)->a.age-b.age);
        
        for(int i=0;i<n;i++) {
        	for(int j=0;j<n;j++) {
        		map[i][j] = 5;//5가 초기값
        		addMap[i][j] = sc.nextInt();//양분의 양
        	}
        }
        
        //초기엔 5만큼
        //m개의 바이러스로 시작
        
        //1
        	//나이만큼 양분 섭취
        	//여러 바이러스 있으면
        		//나이가 어린 바이러스부터 순서대로 섭취
        		//나이 증가
        		//나이만큼 못먹으면 죽음
    	//2
        	//죽은 바이러스 양분으로
        		//나이/2가 양분으로 추가
        //3
        	//번식
        	//5의 배수의 나이를 가진 바이러스만 진행
        	//8개의 인접나이가 1인 바이러스가 생산
        		//상하좌우, 대각선까지
    	//4
        	//양분의 양에 따라 칸에 양분이 추가
        
        //음 나이순서대로 어떻게 함?
        //그냥 전체 맵을 돌면서 바이러스있으면 진행해야 하는데?
        
        for(int i=0;i<m;i++) {
        	//바이러스 정보
        	int r = sc.nextInt();
        	int c = sc.nextInt();
        	int age = sc.nextInt();
        	virus.add(new Virus(r-1,c-1,age));
        }
        
        
        for(int k=0;k<K;k++) {
        	//print(map);
        	//살아남은 바이러스 저장
        	PriorityQueue<Virus> live= new PriorityQueue<>((a,b)->a.age-b.age);
        	//죽은 녀석들
        	Queue<Virus> death= new ArrayDeque<>();
        	//번식할것들
        	Queue<Virus> increase= new ArrayDeque<>();
        	
        	//양분 섭취
        	while(!virus.isEmpty()) {
        		Virus cur = virus.poll();
        		
        		if(map[cur.r][cur.c]>=cur.age) {
        			map[cur.r][cur.c]-=cur.age;
        			live.add(new Virus(cur.r,cur.c,cur.age+1));
        			if((cur.age+1)%5==0) {
        				//5의 배수가 되면 저장한다.
        				increase.add(new Virus(cur.r,cur.c,cur.age));
        			}
        		}else {
        			death.add(cur);
        		}	
        	}
        	//print(map);
        	//죽은 바이러스만큼 양분을 추가해준다.
        	while(!death.isEmpty()) {
        		Virus cur = death.poll();
        		map[cur.r][cur.c]+=cur.age/2;
        	}
        	//print(map);
        	
        	//번식
        		//5의 배수의 나이를 가진 바이러스만 진행
        	
        	
        	int[] dr = {-1,-1,-1,0,0,1,1,1};
        	int[] dc = {-1,0,1,-1,1,-1,0,1};
        	while(!increase.isEmpty()) {
        		Virus cur = increase.poll();
        		for(int d=0;d<8;d++) {
        			int zr = cur.r+dr[d];
        			int zc = cur.c+dc[d];
        			if(zr<0||zc<0||zr>=n||zc>=n) continue;
        			live.add(new Virus(zr,zc,1));
        		}
        	}
        	
        	//1씩 증가
        	//print(map);
        	for(int i=0;i<n;i++) {
        		for(int j=0;j<n;j++) {
        			map[i][j]+=addMap[i][j];
        		}
        	}
        	//print(map);
        	virus = live;
        }
        
        
        System.out.println(virus.size());

    }
    
    
    static void print(int[][] map) {
    	System.out.println("---");
    	for(int i=0;i<map.length;i++) {
    		System.out.println(Arrays.toString(map[i]));
    	}
    	
    }
}