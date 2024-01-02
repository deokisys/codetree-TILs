import java.io.*;
import java.util.*;

public class Main {

	
	static class Present{
		//다음 선물
		Present next;
		//이전선물
		Present prv;
		
		int id;//번호

		public Present(int id) {
			super();
			this.id = id;
		}
	}
	
	static class Belt{
		//맨앞선물
		Present head;
		//맨뒤선물
		Present tail;
		//선물개수
		int cnt;
		
		
		public Belt() {
			super();
			this.head = null;
			this.tail = null;
			this.cnt = 0;
		}


		//초기에 추가할때만 사용
		public void initAdd(Present p) {
			if(this.cnt==0) {
				this.head = p;
				this.tail = p;
			}else {
				//tail로 가서 붙여준다.
				
				//tail의 다음을 p로 지정
				this.tail.next = p;
				//p의 이전을 tail로 지정
				p.prv = this.tail;
				
				//tail을 p로 지정
				this.tail = p;
			}
			
			this.cnt+=1;
		}
		
		
		//앞에것을 뽑는다.
		private Present poll() {
			if(this.cnt==0) return null;
			this.cnt-=1;
			
			if(this.cnt==1) {
				Present out = this.head;
				this.head = null;
				this.tail = null;

				return out;
			}else {
				Present out = this.head;
				this.head = out.next;
				
				out.next = null;
				out.prv = null;
				
				return out;
			}
		}
		
		//앞에 넣는다.
		private void headPush(Present p) {
			if(this.cnt==0) {
				
				this.head = p;
				this.tail = p;

			}else {
				p.next = this.head;
				this.head.prv = p;
				
				this.head = p;
			}
			this.cnt+=1;
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			
			StringBuilder sb = new StringBuilder();
			
			Present head = this.head;
			while(head!=null) {
				sb.append(head.id).append("-");
				head = head.next;
			}
			return sb.append("\n").toString();
		}
	}
	
	static Belt[] belts;
	static Present[] presents;
	
	
	
	private static void build(int n, int m, int[] bnum) {
		//n개의 벨트, m개의 물건
		belts = new Belt[n+1];
		for(int i=1;i<=n;i++) {
			belts[i] = new Belt();
		}
		
		presents = new Present[m+1];

		//각 m개의 선물들을 벨트 번호에 추가
		//벨트는 1~n으로 이루어짐
		//물건 번호도 1부터 
		for(int i=0;i<m;i++) {
			presents[i+1] = new Present(i+1);
			
			belts[bnum[i]].initAdd(presents[i+1]);
		}
		
		
	}

	private static void move(int src, int dst) {
		//src벨트를 모두 dst로 옮김
		//dst의 벨트에는 src-dst 순으로 생성된다.
		
		//dst의 벨트의 선물 개수 출력
		
		
		Belt srcBelt = belts[src];
		Belt dstBelt = belts[dst];
		
		
		
		
		//둘다 그거 있을때 가능
		if(srcBelt.cnt>0&&dstBelt.cnt>0) {			
			//src tail의 다음을 dst head로한다.
			srcBelt.tail.next = dstBelt.head;
			//dst head의 이전을 src tail로 한다.
			dstBelt.head.prv = srcBelt.tail;
			
			//dst head를 src head로 한다.
			dstBelt.head = srcBelt.head;
			
			//src head와 tail을 비운다.
			srcBelt.head = null;
			srcBelt.tail = null;
			
			dstBelt.cnt += srcBelt.cnt;
			srcBelt.cnt = 0;
			
			System.out.println(dstBelt.cnt);
		}else if(srcBelt.cnt>0) {
			//src만 존재
			
			dstBelt.head = srcBelt.head;
			dstBelt.tail = srcBelt.tail;
			
			dstBelt.cnt = srcBelt.cnt;
			srcBelt.cnt = 0;
			
			System.out.println(dstBelt.cnt);
		}else {
			//dst만 존재하거나, src,dst둘다 없음
			
			System.out.println(dstBelt.cnt);
		}
		
		
		
		
	}

	private static void headMove(int src, int dst) {
		//앞물건만 교체
		
		//src의 앞을 dst의 앞과 교체
			//둘중에 한곳만 없으면 그냥 이동만 한다.
		
		//dst의 선물 개수 출력
		Belt srcBelt = belts[src];
		Belt dstBelt = belts[dst];
		
		Present srcHead = srcBelt.poll();
		Present dstHead = dstBelt.poll();
		
		
		if(srcHead!=null) {
			dstBelt.headPush(srcHead);
		}
		if(dstHead!=null) {
			srcBelt.headPush(dstHead);
		}
		
		System.out.println(dstBelt.cnt);
		
		
		
	}

	private static void split(int src, int dst) {
		//src의 앞 절반까지를 dst의 앞으로 이동
		//dst에는 src절반 - dst 순으로 만들어진다.
		
		
		//dst의 선물 개수 출력
		Belt srcBelt = belts[src];
		Belt dstBelt = belts[dst];
		
		//1개인 경우 안옮기고 출력
		if(srcBelt.cnt==1) {
			System.out.println(dstBelt.cnt);
			return;
		}
		
		
		Present srcSplitHead = srcBelt.head;
		Present srcSplitTail = srcBelt.head;
		
		for(int i=0;i<srcBelt.cnt/2-1;i++) {
			srcSplitTail = srcSplitTail.next;
		}
		
		int moveCnt = srcBelt.cnt/2;
		
		//srcSHead에서  srcSTail까지를 dst로 옮기는 과정이다.
		
		if(dstBelt.cnt==0) {
			dstBelt.head = srcSplitHead;
			
			srcSplitTail.next.prv = null;
			
			srcBelt.head = srcSplitTail.next;
			
			srcSplitTail.next = null;
			
			dstBelt.tail = srcSplitTail;
			
		}else {			
			Present tmp = srcSplitTail.next;
			tmp.prv = null;
			
			srcSplitTail.next = dstBelt.head;
			dstBelt.head.prv = srcSplitTail;
			
			dstBelt.head = srcSplitHead;
			srcBelt.head = tmp;
			
		}
		
		srcBelt.cnt-=moveCnt;
		dstBelt.cnt+=moveCnt;
		
		System.out.println(dstBelt.cnt);
		
	}

	private static void getPresent(int p) {
		//선물의 앞,뒤 번호 확인
		
		//a+2*b를 출력
		//선물이 없으면 -1로 한다.
		
		int a = -1;
		int b = -1;
		
		if(presents[p].prv!=null) {			
			a = presents[p].prv.id;
		}
	
		if(presents[p].next!=null) {			
			b = presents[p].next.id;
		}
		
		System.out.println(a+2*b);
		
	}

	private static void getBelt(int bid) {
		//해당 벨트 맨앞 선물 번호 a
		//맨뒤 선물을 b로한다.
		//선물개수를 c로 한다.
		//a+2*b+3*c를 출력
		//선물이 없으면 a,b는 -1이된다.
		
		int a = -1;
		int b = -1;
		
		if(belts[bid].head!=null) {			
			a = belts[bid].head.id;
		}
	
		if(belts[bid].tail!=null) {			
			b = belts[bid].tail.id;
		}
		
		int c = belts[bid].cnt;
		
		System.out.println(a+2*b+3*c);
		
	}
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int Q = Integer.parseInt(st.nextToken());

		for(int q=0;q<Q;q++) {
			st = new StringTokenizer(br.readLine());
			
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd==100) {
				int n = Integer.parseInt(st.nextToken());
				int m = Integer.parseInt(st.nextToken());
				int[] bnum = new int[m];
				
				for(int i=0;i<m;i++) {
					bnum[i] = Integer.parseInt(st.nextToken());
				}
				
				build(n,m,bnum);
			}else if(cmd==200) {
				int src = Integer.parseInt(st.nextToken());
				int dst = Integer.parseInt(st.nextToken());
				
				move(src,dst);
			}else if(cmd==300) {
				int src = Integer.parseInt(st.nextToken());
				int dst = Integer.parseInt(st.nextToken());
				headMove(src,dst);
			}else if(cmd==400) {
				int src = Integer.parseInt(st.nextToken());
				int dst = Integer.parseInt(st.nextToken());
				split(src,dst);
			}else if(cmd==500) {
				int p = Integer.parseInt(st.nextToken());
				getPresent(p);
			}else if(cmd==600) {
				int b = Integer.parseInt(st.nextToken());
				getBelt(b);
			}
			
//			print();
			
		}
	}

	private static void print() {
		System.out.println("---");
		for(int i=1;i<belts.length;i++) {
			System.out.println(belts[i]);
		}
		System.out.println("----");
		
	}

}