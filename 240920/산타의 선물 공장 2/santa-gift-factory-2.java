import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	//선물을 바로 접근할 수 있는 배열을 선언-
	//선물은 앞과 뒤의 어떤 선물이 존재하는지 확인 가능-
	
	//각 벨트는 양방향 링크드리스트로 구현-
	
	//벨트는 앞과 뒤를 접근 가능-
	//벨트는 자신의 선물 개수를 확인 가능-
	//벨트는 자기 앞을 뺄수있다.-
	//벨트는 앞에서 추가가능하다.-
	//벨트는 뒤에 추가가능하다.-
	
	
	static class Gift{
		int id;//자기 번호
		Gift left;
		Gift right;
		
		Gift(int id){
			this.id = id;
			this.left = null;
			this.right = null;
		}
	}
	
	static class Belt{
		Gift head;
		Gift tail;
		int cnt;
		
		Belt(){
			this.cnt = 0;
			this.head = this.tail = null;
		}
		
		//꼬리 추가
		void addTail(Gift nNode) {
			if(this.head == null) {
				this.head = this.tail = nNode;
			}else {
				//꼬리에 다음에 추가한다.
				this.tail.right = nNode;
				nNode.left = this.tail;
				this.tail = nNode;
			}
			
			this.cnt++;
		}
		
		//헤드추가
		void addHead(Gift nNode) {
			if(this.head == null) {
				this.head = this.tail = nNode;
			}else {
				//머리 이전에 추가한다.
				this.head.left = nNode;
				nNode.right = this.head;
				this.head = nNode;
			}
			this.cnt++;
		}
		
		//헤드꺼내기
		Gift popHead() {
			if(this.head == null) {
				return null;
			}else {
				this.cnt--;
				//머리는 다음으로 이동한다.
				Gift out = this.head;
				if(this.head.id==this.tail.id) {
					//같으면 그냥 전부 제거된다.
					this.head = this.tail = null;
					out.left = null;
					out.right = null;
				}else {
					this.head = this.head.right;
					out.left = null;
					out.right = null;
					this.head.left = null;
				}
				return out;
			}
			
		}
		
		void reset() {
			this.head = this.tail = null;
			this.cnt=0;
		}

		public void print() {
			
			Gift head = this.head;
			System.out.print("[");
			while(head!=null) {
				System.out.print(head.id+"-");
				head = head.right;
			}
			System.out.println("]");
			
		}
	}
	
	
	static Gift[] gifts;
	static Belt[] belts;
	public static void init(int n, int m, int[] B) {
		//n개의 벨트
		//m개의 선물
		//m개의 서물의 위치
		//선물들은 벨트에 올린다.
		
		belts = new Belt[n+1];
		for(int i=1;i<=n;i++) {
			belts[i] = new Belt();
		}
		gifts = new Gift[m+1];
		for(int i=1;i<=m;i++) {
			gifts[i] = new Gift(i);
		}
		
		//벨트에 추가하기
		for(int i=1;i<=m;i++) {
			belts[B[i]].addTail(gifts[i]);
		}
		
		
	}
	
	public static void move(int mSrc, int mDst) {
		//모두 옮기기
		
		//src의 모두를 dst로 옮긴다.
			//dst의 앞에 추가된다.
		
		
		//msrc의 선물갯수+dst의 선물갯수를 미리 저장한다.
		int result = belts[mSrc].cnt+belts[mDst].cnt;
		
		
		//옮기기 시작
		if(belts[mSrc].cnt!=0) {
			//src가 1개라도 있으면 옮기기 진행
			
			//src의 앞a과 뒤b의 선물정보를 가져온다.
			
			//src는 head와 tail정보 제거, 리셋
			
			//dst는 head의 왼쪽을b로연결, b의 오른쪽은 head로 연결
			//dst의 head를 a로 변경한다.
			//dst에 합친 결과로 cnt를 갱신
			
			Gift srcHead = belts[mSrc].head;
			Gift srcTail = belts[mSrc].tail;
			
			belts[mSrc].reset();
			
			//만약 dst가 0이었으면 그냥 head랑 tail에 꽂는다.
			if(belts[mDst].head==null) {
				belts[mDst].head = srcHead;
				belts[mDst].tail = srcTail;
				belts[mDst].cnt = result;
			}else {				
				belts[mDst].head.left = srcTail;
				srcTail.right = belts[mDst].head;
				belts[mDst].head = srcHead;
				belts[mDst].cnt = result;
			}
		}
		
		
		System.out.println(result);
	}
	
	public static void change(int mSrc, int mDst) {
		//앞만 교체
		
		//둘중 하나라도 선물이 없으면 옮겨지기만 한다.
		
		Gift srcHead = belts[mSrc].popHead();
		Gift dstHead = belts[mDst].popHead();
		
		if(srcHead!=null) {			
			belts[mDst].addTail(srcHead);
		}
		
		if(dstHead!=null) {
			belts[mSrc].addTail(dstHead);
		}
		
		
		//mDst의 선물 총 수 출렫
		System.out.println(belts[mDst].cnt);
	}
	
	public static void split(int mSrc, int mDst) {
		//물건 나누기
		
		//src의 선물 개수의 절ㅁ반을 dst로 옮긴다.
			//floor로 버림처리
		//1개면 안옮긴다.
		
		if(belts[mSrc].cnt<=1) {
			System.out.println(belts[mDst].cnt);
			return;
		}
		
		int moveCnt = (int)Math.floor((double)belts[mSrc].cnt/2);
		
		
		
		
		int result = moveCnt+belts[mDst].cnt;
		
		
		//옮기기 시작
		Gift srcHead = belts[mSrc].head;
		Gift srcTail = belts[mSrc].head;
		//moveCnt만큼 이동한다.
		for(int i=0;i<moveCnt-1;i++) {
			srcTail = srcTail.right;
		}
		
		//src의 헤드를 리셋한다.
		belts[mSrc].head = srcTail.right;
		belts[mSrc].head.left = null;
		srcTail.right = null;
		belts[mSrc].cnt -= moveCnt;
		
		//이동한다.
		if(belts[mDst].head==null) {
			belts[mDst].head = srcHead;
			belts[mDst].tail = srcTail;
			belts[mDst].cnt = result;
		}else {	
			belts[mDst].head.left = srcTail;
			srcTail.right = belts[mDst].head;
			belts[mDst].head = srcHead;
			belts[mDst].cnt = result;
		}
		
		
		System.out.println(result);
		//100번만 수행
	}
	
	public static void getGift(int pNum) {
		//선물 정보 얻기
		//선물번호 pNum
		//해당 선물 앞과 뒤를 확인할 수 있어야 함
		
		//앞선물a,뒤선물b일때 a+2*b를 출력
			//없으면 -1을 대입
		
		Gift check = gifts[pNum];
		int a = -1;
		int b = -1;
		
		if(check.left!=null) {
			a = check.left.id;
		}
		
		if(check.right!=null) {
			b = check.right.id;
		}
		
		System.out.println(a+2*b);
		
	}
		
	public static void getBelt(int bNum) {
		//벨트정보 얻기
		
		//벨트 맨앞을 a, 맨뒤를 b, 벨트의 선물 개수 c
		//a+2b+3c를 출력
			//없으면 -1을 대입
			//c는 0으로 한다.
		
		Belt belt = belts[bNum];
		int a = -1;
		int b = -1;
		int c = 0;
		
		if(belt.head!=null) {
			a = belt.head.id;
		}
		if(belt.tail!=null) {
			b = belt.tail.id;
		}
		if(belt.cnt!=0) {
			c = belt.cnt;
		}
		
		System.out.println(a+2*b+3*c);
		
	}
	
	public static void printCheck() {
		System.out.println("------------");
		for(int i=1;i<belts.length;i++) {
			System.out.println("벨트"+i+",갯수 : "+belts[i].cnt);
			belts[i].print();
		}
	}
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int Q = Integer.parseInt(st.nextToken());
		for(int q=0;q<Q;q++) {
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());
			if(cmd==100) {
				int n = Integer.parseInt(st.nextToken());
				int m = Integer.parseInt(st.nextToken());
				int B[] = new int[m+1];
				for(int i=1;i<=m;i++) {
					B[i] = Integer.parseInt(st.nextToken());
				}
				init(n,m,B);
			}else if(cmd==200) {
				int src = Integer.parseInt(st.nextToken());
				int dst = Integer.parseInt(st.nextToken());
				move(src,dst);
			}else if(cmd==300) {
				int src = Integer.parseInt(st.nextToken());
				int dst = Integer.parseInt(st.nextToken());
				change(src,dst);
			}else if(cmd==400) {
				int src = Integer.parseInt(st.nextToken());
				int dst = Integer.parseInt(st.nextToken());
				split(src,dst);
			}else if(cmd==500) {
				int p = Integer.parseInt(st.nextToken());
				getGift(p);
			}else if(cmd==600) {
				int b = Integer.parseInt(st.nextToken());
				getBelt(b);
			}
			
			//확인용 출력하기
//			printCheck();
		}
	}
}