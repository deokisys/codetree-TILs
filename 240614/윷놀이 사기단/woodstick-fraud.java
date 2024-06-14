import java.util.*;
import java.io.*;

public class Main {
	

	static int points[] = {0,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,13,16,19,25,22,24,28,27,26,30,35,0};
	static int maxPoint;
	public static void main(String[] args) throws Exception{
		//원할때 주사위수를 안다.
		//10번뿐
		//주어진 이동횟수에 나갈 말의 종류를 잘 조합하여 점수의 최댓값
		
		//말 4개
		//화살표따라서 이동만 가능
		//파란색 이동-빨간화살표
		//이동중 검은화살표
		//도착칸으로 이동하면 이동 마침
		
		//도착칸에 도착하지 않은 말을 원하는 이동횟수만큼 이동 가능
		//시작, 도착 제외하고 말들을 겹치지 못한다.
		//다른 말이 이미 있으면 불가능하다는 의미
			//시작, 도착칸이 별도로 존재
		//말이 이동할때마다 칸에 있는 수가 점수에 추가
		
		//완전탐색
			//4^10 = 100만뿐?
		
		//링크드 리스트로 만들기
		ArrayList<Integer>[] adjList = new ArrayList[33];
		for(int i=0;i<33;i++) {
			adjList[i] = new ArrayList<>();
		}
		
		adjList[0].add(1);
		adjList[1].add(2);
		adjList[2].add(3);
		adjList[3].add(4);
		adjList[4].add(5);
		adjList[5].add(6);
		adjList[5].add(21);//분기1
		adjList[21].add(22);
		adjList[22].add(23);
		adjList[23].add(24);
		
		adjList[6].add(7);
		adjList[7].add(8);
		adjList[8].add(9);
		adjList[9].add(10);
		adjList[10].add(11);//분기2
		adjList[10].add(25);
		adjList[25].add(26);
		adjList[26].add(24);
		
		
		adjList[11].add(12);
		adjList[12].add(13);
		adjList[13].add(14);
		adjList[14].add(15);
		adjList[15].add(16);//분기3
		adjList[15].add(27);
		adjList[27].add(28);
		adjList[28].add(29);
		adjList[29].add(24);
		
		adjList[24].add(30);//중간모이는곳
		adjList[30].add(31);
		adjList[31].add(20);
		
		adjList[16].add(17);
		adjList[17].add(18);
		adjList[18].add(19);
		adjList[19].add(20);
		adjList[20].add(32);//끝
		
		
		int[] horse = {0,0,0,0};
		int[] dices = new int[10];
				
		Scanner sc = new Scanner(System.in);
		for(int i=0;i<10;i++) {
			dices[i] = sc.nextInt();
		}
		
		maxPoint = 0;
		allSearch(adjList,horse,dices,0,0);
		
		System.out.println(maxPoint);
	}
	private static void allSearch(ArrayList<Integer>[] adjList, int[] horse, int[] dices, int idx, int point) {
		if(idx==10) {
			if(maxPoint<point) {
//				System.out.println(Arrays.toString(horse));
				maxPoint = point;
			}
			return;
		}
		
		for(int i=0;i<4;i++) {
			
			if(horse[i]==32) continue;//도착
			
			//이동
			int before = horse[i];
			int next = move(adjList,horse[i],dices[idx]);
			
			//겹쳐지는지 확인
			boolean isSame = false;
			for(int j=0;j<4;j++) {
				if(horse[j]!=32&&horse[j]==next) {
					//겹침
					isSame = true;
				}
			}
			if(isSame) {
				continue;//이동불가
			}
			horse[i] = next;
//			System.out.println(i+"번말을"+idx+"번째에 이동시킨다.");
//			System.out.println(Arrays.toString(horse));
			allSearch(adjList,horse,dices,idx+1,point+points[next]);
			horse[i] = before;
		}
		
	}
	private static int move(ArrayList<Integer>[] adjList, int cur, int move) {
		int next = cur;
		
		boolean first = true;
		for(int t=0;t<move;t++) {
			//도착했으면 종료하기
			if(next==32) break;
			//분기가 있는지 확인
			
			
			if(first&&adjList[next].size()>1) {
				next = adjList[next].get(1);
			}else {
				next = adjList[next].get(0);
			}
			first = false;
		}
		return next;
	}
}