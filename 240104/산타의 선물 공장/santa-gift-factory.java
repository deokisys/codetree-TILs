import java.util.*;
import java.io.*;


//11:48
public class Main {
	
	static class Present{
		int id,w;
		
		int beltId;//현재 속한 벨트
		Present next;
		Present prv;
		
		public Present(int id, int w) {
			super();
			this.id = id;
			this.w = w;
			this.next = null;
			this.prv = null;
		}
		
	}
	static class Belt{
		int id;
		Present head;
		Present tail;
		boolean isCrush;
		
		
		
		public Belt(int id) {
			super();
			this.id = id;
		}



		private void pushTail(Present p) {
			p.beltId = this.id;
			// 맨뒤에 추가한다.
			if(this.tail==null) {
				this.head = p;
				this.tail = p;
			}else {
				this.tail.next = p;
				p.prv = this.tail;
				this.tail = p;
			}
		}
		
		private Present pollHead() {
			//헤드를 뽑는다.
			if(this.head==null) {
				return null;
			}
			
			//this.head <-> np
			
			Present out = this.head;
			out.beltId = -1;//하차 처리
			
			this.head = this.head.next;
			if(this.head==null) {
				//뽑았는데 완전 비었어
				this.tail = null;
			}else {
				this.head.prv = null;
			}
			
			return out;
		}
	}
	
	static int[] beltIds;//벨트번호 - 실제벨트번호
	static Belt[] belts;//벨트
	static Map<Integer,Present> presents;//선물
	static int N,M;
	private static void build(int n, int m, int[] ids, int[] ws) {
		//m개의 벨트
		//n개의 물건
			//순서대로 1번벨트 부터 채운다.
		//채우는것은 벨트의 뒤로 채워지는 형태이다.
		//물건
		//고유번호ids, 무게ws
		//자신이 어느 벨트에 있는지 확인되야 한다.
		//이중링크드 - 삭제때문에
		//이전물건, 다음물건
		//물건은 map으로 관리한다.
		//고유번호 - 물건
		//벨트
		//고장유무
		//벨트번호-실제벨트번호 형태의 map으로 관리
		//고장나서 다른 벨트로 이동되면 실제 벨트번호만 수정하면 된다.
		//배열로 관리
		
		N = n;
		M = m;
		beltIds = new int[m+1];
		belts = new Belt[m+1];
		presents = new HashMap<Integer, Present>();
		
		//벨트초기화
		for(int i=1;i<=m;i++) {
			belts[i] = new Belt(i);
			beltIds[i]=i;
		}
		
		//물건초기화
		int beltCap = n/m;
		for(int i=0;i<n;i++) {
			int beltId = (i/beltCap)+1;
			presents.put(ids[i],new Present(ids[i], ws[i]));
			//물건 push
			belts[beltId].pushTail(presents.get(ids[i]));
		}
	}

	private static int down(int w_max) {
		
		//원하는 상자의 최대 무게
		//각 벨트 맨앞 선물중 w_max이하면 하차
			//아니면 맨뒤로 이동
		int sum = 0;
		
		
		for(int i=1;i<=M;i++) {
			if(belts[i].isCrush) continue;
			Present head = belts[i].pollHead();
			if(head==null) continue;
			
			if(head.w<=w_max) {
				sum+=head.w;
			}else {
				//맨뒤로 이동
				belts[i].pushTail(head);
			}
		}
		
		
		return sum;
	}

	private static int remove(int r_id) {
		// 물건제거
		// 해당 물건을 제거하고 상자들은 앞으로 이동된다.

		//상자가 있으면 r_id를 출력
		//상자가 없으면 -1

		
		//실제 존재유무
		if(!presents.containsKey(r_id)) return -1;
		Present outPresent = presents.get(r_id);
		//belt에서 나왔는지 유무
		if(outPresent.beltId == -1) return -1;

		//앞물건 - 지울문건 - 다음물건
		
		if(outPresent.prv!=null) {
			outPresent.prv.next = outPresent.next;
		}else {
			//head라는 의미
			//belt의 head를 수정
			belts[beltIds[outPresent.beltId]].head = outPresent.next;
		}
		if(outPresent.next!=null) {
			outPresent.next.prv = outPresent.prv;
		}else {
			//tail이라는 의미
			belts[beltIds[outPresent.beltId]].tail = outPresent.prv;
		}
		
		return r_id;
	}

	private static int confirm(int f_id) {
		// 무건확인
		// 물건이 있는 벨트의 번호를 출력
		//없으면 -1
		
		if(!presents.containsKey(f_id)) return -1;
		Present outPresent = presents.get(f_id);
		//belt에서 나왔는지 유무
		if(outPresent.beltId == -1) return -1;
		
		// 물건이 있으면 해당 상자부터 뒤의 상자들을 앞으로 이동한다.

		//head - 물건 - 찾는물건 - 뒤물건 - tail
		
		int beltId = beltIds[outPresent.beltId];
		
		if(outPresent.prv!=null) {
			//head가 아니라는 의미
			//head - 찾는 물건 앞 <- 이것들을 뒤로 옮기면 된다.

			//벨트정보
			//벨트의 head와 tail을 연결
			Present head = belts[beltId].head;
			Present tail = belts[beltId].tail;
			
			head.prv = tail;
			tail.next = head;
			
			//새로운 헤드 선언
			belts[beltId].head = outPresent;
			
			//새로운 꼬리 선언
			belts[beltId].tail = outPresent.prv;
			
			//헤드와 꼬리 연결끊기
			belts[beltId].head.prv = null;
			belts[beltId].tail.next = null;
		}
		
		
		return beltId;
	}

	private static int crush(int b_num) {
		// 벨트 고장
		//이미 고장난 벨트면 -1
		if(belts[b_num].isCrush) return -1;
		
		// 벨트번호 주어짐
		// 해당 벨트는 고장처리, 오른쪽 벨트들을 확인
			//고장나면 넘기기
			//끝으로 가면 1번부터 다시
		//고장난 벨트 그대로 해당 벨트 뒤에 붙인다.
		
		int nextId = b_num;
		for(int i=1;i<M;i++) {
			nextId+=1;
			if(nextId>M) {
				nextId = 1;
			}
			if(!belts[nextId].isCrush) break;			
		}
		

		//이동처리
		if(belts[nextId].head==null) {
			//이동하려는 벨트가 비어있음
			belts[nextId].head = belts[b_num].head;
			belts[nextId].tail = belts[b_num].tail;
		}else {
			//이동하려는 벨트가 물건 존재
			
			
			if(belts[b_num].head!=null) {
				//고장난 벨트에 물건이 존재
				
				//next의 꼬리와, b의 헤드를연결
				belts[nextId].tail.next = belts[b_num].head;
				belts[b_num].head.prv = belts[nextId].tail;
				
				//next에게 꼬리를 갱신시킴
				belts[nextId].tail = belts[b_num].tail;
			}
			
			//고장난 밸트에 물건이 없으면 아무런 변화가 없다.
		}
		
		//벨트 이동 처리
		belts[b_num].isCrush = true;
		beltIds[b_num] = nextId;
		return b_num;
	}
	

	public static void main(String[] args) throws Exception{
//		BufferedReader solutionBR = new BufferedReader(new FileReader("src/산타의선물공장/output.txt"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int Q = Integer.parseInt(st.nextToken());
		int result = 0;
		for(int q=0;q<Q;q++) {
			st = new StringTokenizer(br.readLine());
			
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd==100) {
				int n = Integer.parseInt(st.nextToken());
				int m = Integer.parseInt(st.nextToken());
				int[] ids = new int[n];
				int[] ws = new int[n];
				
				for(int i=0;i<n;i++) {
					ids[i] = Integer.parseInt(st.nextToken());
				}
				for(int i=0;i<n;i++) {
					ws[i] = Integer.parseInt(st.nextToken());
				}
				
				build(n,m,ids,ws);
			}else if(cmd==200) {
				int w_max = Integer.parseInt(st.nextToken());
				
				result = down(w_max);
				System.out.println(result);
			}else if(cmd==300) {
				int r_id = Integer.parseInt(st.nextToken());
				result = remove(r_id);
				System.out.println(result);
			}else if(cmd==400) {
				int f_id = Integer.parseInt(st.nextToken());
				result = confirm(f_id);
				System.out.println(result);
			}else if(cmd==500) {
				int b_num = Integer.parseInt(st.nextToken());
				result = crush(b_num);
				System.out.println(result);
			}
			
//			if(cmd!=100) {
//				if(!check(result,Integer.parseInt(solutionBR.readLine()))) break;				
//			}
			
		}
	}
	


	private static boolean check(int result, int solution) {
		if(result!=solution) {
			System.out.println("예측 : "+result);
			System.out.println("정답 : "+solution);
			System.out.println("틀림");
			return false;
		}
		return true;
	}
}