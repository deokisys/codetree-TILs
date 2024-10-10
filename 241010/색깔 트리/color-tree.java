import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
	
	//id = 노드로 바로 조회가 가능
		//map으로 구현
	//노드
		//현재 노드id
		//자신의 색
		//자신이 가진 깊이
		//부모로부터 온 영향 최소 깊이
		
		//모든 부모리스트
		//자식 리스트
		//색이 변경된 시간 - 초기-1
	static class Color{
		int id;
		int color;
		int minDepth;
		int parent;
		List<Integer> childs;//바로 연결된 자식들
		int time;
		int changeTime;
		public Color(int id, int color, int minDepth, int parent,int time) {
			this.id = id;
			this.color = color;
			this.minDepth = minDepth;
			this.parent = parent;
			this.childs = new ArrayList<>();
			this.time = time;
			this.changeTime = -1;
		}
		
	}
	static Map<Integer,Color> map; 
	static List<Integer> rootIds;
	static int time;
	static int sum;
	public static void main(String[] args) throws Exception{
		
		//색깔 트리는 여러 색 노드가 존재
		//color, 최대깊이 max를 가진다.
		
		
		//추가
			//트리에 추가, id, 부모p, 색color, 최대깊이depth
			//color는 1~5
			//p가 -1이면 루트
			//depth는 현노드를 루트로 한 서브트리의 최대 깊이
				//1은 자기자신
			//depth에 맞지 않은 새로운 노드는 추가 X
		
		//색 변경
			//특정 노드id를 루트로 한 모든 서브 트리의 색을 color로 변경
		
		//색 조회
			//id의 색을 조회
		
		//점수 조회
			//모든 노드의 가치 계산, 가치 제곱의 합
			//가치 = 서브트리내의 서로 다른 색의 수
		
		
		//삭제가 없다 좀더 편함
		//추가
			//모든 부모를 확인해서 depth를 체크해야 한다.
			//2만번
		//변경
			//5만번
		//조회
			//2만번
		//점수
			//100번 - 전체탐색해도 어느정도 작동된다는 의미
		
		//명령은 10만번
			//트리는 2만개의 노드로 구성
		//추가
			//단순 부모를 확인한다면
				//최악은 1+2+...2만임  = 2만1*1만 = 2억 - 2초정도
			//만약 추가할때 자식들이 부모로부터 온 최소depth를 갱신한다면 o(1)로 해결됨
		//변경
			//모든 자식들을 돌면서 색을 수정해야함
				//최악은 약 2만*3만 = 6억 = 6초 보다더 나올듯
				//모든 자식을 돌면서 하면 안된다.
			//유니온파인드를 활용해야 할듯
				
		//조회
			//유니온파인드를 활용하면 추가 조건을 통해 바로 조회가 가능하다.
		
		
		//변경 아이디어
			//각 자식들이 가진 영향을 가진 부모를 가진다면?
			//색을 변경시 일단 해당 부모노드에 check=변경시간 ;으로 표시한다. - o(1)
			//조회시
				//본인포함, 해당 부모들을 확인해서 check가 0보다 큰것을 확인한다.
					//가장 나중에 변경된녀석의 색이 해당 자식의 색이된다.
				//아무도 안바뀌었으면 본인색
		
		//조회할때만 조금 시간이 걸리겠지만, 변경에서 시간을 줄일 수 있어서 문제 없을듯?
			//
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int Q = Integer.parseInt(st.nextToken());
		
		//초기화
		map = new HashMap<>();
		rootIds = new ArrayList<>();//여러 트리들의 루트들을 관리
		time=1;
		
		for(int q = 0;q<Q;q++) {
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd==100) {
				int mid = Integer.parseInt(st.nextToken());
				int pid = Integer.parseInt(st.nextToken());
				int color = Integer.parseInt(st.nextToken());
				int depth = Integer.parseInt(st.nextToken());
				
				add(mid,pid,color,depth);
			}else if(cmd==200) {
				int mid = Integer.parseInt(st.nextToken());
				int color = Integer.parseInt(st.nextToken());
				
				change(mid,color);
			}else if(cmd==300) {
				int mid = Integer.parseInt(st.nextToken());
				
				int color = colorCheck(mid);
				
				System.out.println(color);
			}else if(cmd==400) {
				int point = getPoint();
				System.out.println(point);
			}
		}
	}
	private static int getPoint() {
		//각 루트별로 계산을 진행
		
		//재귀로 계산 진행
		//해당 루트를 기준으로 리프노드까지 이동한다.
		//리프노드면 자신의 
		int result = 0;
		for(int root : rootIds) {
			
			sum = 0;
			calPoint(root,-1,-1);
			result+=sum;
		}
		
		return result;
	}
	private static boolean[] calPoint(int id,int changeColor, int changeTime) {
		//초기에 리프로 갈때는 색들을 전부 업데이트를 진행한다.
		//올라갈때 바로 해당 색을 사용할 수 있게 해준다.
		
		//부모가 전달하는 자신의 색을 자식에게 전달한다.
			//부모의 changeTime이 -1보다 크면 색이 전달된다.
		//자식의 입장
			//자식이 생성시간time과 수정시간changeTime중 큰것을 사용한다.
		boolean check[] = new boolean[6];//색의 유무
		
		Color curNode = map.get(id); 
		//현재 자신의 색을 업데이트, 자식에게 전파되는 색과 시간을 업데이트
		if(changeTime>-1) {
			//부모에게 어떤 변화가 있었고, 자식들에게 전달됐을수도 있음. 확인 진행
			if(changeTime>Math.max(curNode.changeTime,curNode.time)) {
				//부모의 색이 나의 색이된다.
				curNode.color = changeColor;
			}else {
				//내가 이제 아래에 영향을 줄 수 있음
				if(curNode.changeTime==-1) {
					//아래엔 영향이 없음
					changeColor = -1;
					changeTime = -1;
				}else {
					//자신의 색과 변경된 시간을 같이 넘긴다.
					changeColor = curNode.color;
					changeTime = curNode.changeTime;
				}
				
			}
		}else {
			//현재 자신에서 시작해서 넘겨질 수 있다.
			if(curNode.changeTime>-1) {
				changeColor = curNode.color;
				changeTime = curNode.changeTime;
			}else {
				//영향이 없음
				changeColor = -1;
				changeTime = -1;
			}
		}
		
		//자신의 색 확인
		check[curNode.color] = true;
		
		//모든 자식들 진행
		for(int child : curNode.childs) {
			boolean[] childCheck = calPoint(child,changeColor,changeTime);
			for(int i=1;i<=5;i++) {
				check[i] = check[i]||childCheck[i];
			}
		}
		
		int cnt = 0;
		for(int i=1;i<=5;i++) {
			if(check[i]) {
				cnt++;
			}
		}
		
		//가치를 제곱한다.
		sum+=(cnt*cnt);
		//System.out.println(id+"가치"+cnt+"-색-"+curNode.color);
		
		return check;
	}
	private static int colorCheck(int mid) {
		//해당 노드에서 부모로 쭉 올라간다.
		//제일 changeTime이 큰것의 색을 사용한다.
			//초기는 자신의 색, 자신의 changeTime
		
		Color node = map.get(mid);
		int curTime = Math.max(node.changeTime,node.time);//수정된 시간을 기준으로 진행
		int color = node.color;
		
		while(true) {
			//부모를 확인
			if(node.parent==-1) break;
			//부모로 이동
			node = map.get(node.parent);
			//시간 확인
			if(curTime<node.changeTime) {
				curTime = node.changeTime;
				color = node.color;
			}
		}
		
		
		return color;
	}
	private static void change(int mid, int color) {
		//색 변경
		Color colorNode = map.get(mid);
		
		//해당 노드 색 변경
		colorNode.color = color;
		
		//변경시간 기록
		colorNode.changeTime = time;
		
		//시간 추가
		time++;
	}
	private static void add(int mid, int pid, int color, int depth) {
		//추가
		//시간도 중요하다.
		
		if(pid==-1) {
			//루트로 새로운 트리가 생성된다.
			//int id, int color, int depth, int minDepth, int parent)\
			rootIds.add(mid);
			Color nColor = new Color(mid,color,depth,pid,time);
			map.put(mid, nColor);
			time++;
		}else {
			//해당 부모가 가진 minDepth를 확인한다.
			Color pColor = map.get(pid);
			//minDepth가 2이상이어야 사용된다.
			if(pColor.minDepth<2) {
				return;
			}
			
			//부모에게 온 최소depth-1, 자신의 depth 중에 최소를 가진다.
			Color nColor = new Color(mid,color,Math.min(pColor.minDepth-1,depth),pid,time);
			
			pColor.childs.add(mid);//해당 부모에게 자식들을 표시
			
			map.put(mid, nColor);
			time++;
		}
	}
}