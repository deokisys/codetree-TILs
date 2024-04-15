import java.util.*;
import java.io.*;

public class Main {
    static class Node{
        int idx;
        int n;
        Node(int idx,int n){
            this.idx =idx;
            this.n = n;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int N = sc.nextInt();

        //구역을 잘 잡아라.
        //서로다른 숫자가 최소 1번 나오는 최소 구간의 크기는?

        Set<Integer> numSet = new HashSet<>();

        ArrayList<Node> arr = new ArrayList<>();
        for(int i=0;i<N;i++){
            int idx = sc.nextInt();
            int n = sc.nextInt();
            numSet.add(n);
            arr.add(new Node(idx,n));
        }

        Collections.sort(arr,(a,b)->a.idx-b.idx);

        int allCnt = numSet.size();

        int start = 0;
        int[] checkNum = new int[N+1];

        Map<Integer,Integer> map = new HashMap<>();
        
        //초기
        int cnt = 0;
        int endIdx = 0;
        for(int i=0;i<arr.size();i++){
            if(!map.containsKey(arr.get(i).n)){
                map.put(arr.get(i).n,0);
                cnt++;
            }
            map.put(arr.get(i).n, map.get(arr.get(i).n)+1);

            if(cnt==allCnt){
                endIdx = i;
                break;
            }
        }
        int answer = arr.get(endIdx).idx-arr.get(0).idx;

        //나머지 전부 확인
        for(int startIdx=0;startIdx<arr.size();startIdx++){
            //startIdx제거
            map.put(arr.get(startIdx).n, map.get(arr.get(startIdx).n)-1);
            if(map.get(arr.get(startIdx).n)==0){
                cnt--;
            }

            if(cnt==allCnt){
                //값 확인 후 갱신
                int dist = arr.get(endIdx).idx-arr.get(startIdx+1).idx;
                answer = Math.min(dist,answer);
            }

            if(cnt<allCnt){
                //endIdx를 추가하면서 확인
                endIdx++;
                while(endIdx<arr.size()){
                    // System.out.println("반복");
                    if(map.get(arr.get(endIdx).n)==0){
                        cnt++;
                    }
                    map.put(arr.get(endIdx).n, map.get(arr.get(endIdx).n)+1);
                    // System.out.println(map);

                    if(cnt==allCnt){
                        break;
                    }
                    endIdx++;
                }
            }

            if(cnt==allCnt){
                //값 확인 후 갱신
                int dist = arr.get(endIdx).idx-arr.get(startIdx+1).idx;
                answer = Math.min(dist,answer);
            }

            

        }


        System.out.println(answer);
    }
}