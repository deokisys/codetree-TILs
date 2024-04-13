import java.util.*;
import java.io.*;

//이진트리
    //2개이하
    //메인, 부서별 채팅방
//준비
    //0~N
    //메인은 항상 0
    //parents로 부모가 누구인지 표시
        //루트는 없다.
    //parnets는 1부터 N까지 
        //0 자리는 1번의 부모 번호가 있다.
//각 방은 권한을 가짐
    //상위에 알림이 간다.
    //권한만큼 올라간ㄷ.
    //0자리는 1번의 권한이 있다.
//알림 설정 on/off
    //off가 되면 자신포함, 아래에서 올라간는 알림을 안올린다.
//세기
    //세기를 변경
//부모 채팅 교환
    //둘은 같은 depth에 잇다.
    //둘의 부모를 서로 바꾼다.
//알림 받는 채팅방 수 조회
    //해당 채팅방에 도달하는 채팅 방의 수를 출력'

//이건 해당 채팅방에서 위로 보내지는 세기를 저장한다.
    //위에서 아래로 하는게 아닌, 아래에서 위로 계산하는게 팁
        //복잡도는 어떻게 계산하지?
        //위에서 아래는 bfs로 되서, 결국 전체탐색
        //아래에서 위는 log만 계산되서 좀더 빠른듯
    

public class Main {
    static int N,Q;
    public static void main(String[] args) throws Exception{
        // System.setIn(new FileInputStream("코드트리메신저/input2.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        for(int q=0;q<Q;q++){
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());
            if(cmd==100){
                int[] p = new int[N];
                int[] a = new int[N];
                for(int n=0;n<N;n++){
                    p[n] = Integer.parseInt(st.nextToken());
                }
                for(int n=0;n<N;n++){
                    a[n] = Integer.parseInt(st.nextToken());
                }

                init(p,a);
            }else if(cmd==200){
                int c = Integer.parseInt(st.nextToken());
                onoff(c);
            }else if(cmd==300){
                int c = Integer.parseInt(st.nextToken());
                int power = Integer.parseInt(st.nextToken());
                powerChange(c,power);
            }else if(cmd==400){
                int c1 = Integer.parseInt(st.nextToken());
                int c2 = Integer.parseInt(st.nextToken());
                parentChange(c1,c2);
            }else if(cmd==500){
                int c = Integer.parseInt(st.nextToken());
                printCount(c);
            }
            // System.out.println(Arrays.toString(rooms));
        }
    }

    static class Room{
        int parent;//부모 번호
        int[] alerts;//부모로의 전파들
        int alert;//내가 받는 전파들
        int power;//내가 보내는 세기
        boolean isOn = true;
        Room(){}

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(this.alert+",").append(Arrays.toString(this.alerts)+",").append(this.power+",").append("]\n");
            return sb.toString();
        }
    }

    static Room[] rooms;
    private static void init(int[] p, int[] a) {
        //자식->부모 방향만 있어도 될듯?
        //N크기로 만든다.
        rooms = new Room[N+1];
        rooms[0] = new Room();
        for(int i=0;i<N;i++){
            rooms[i+1] = new Room();
            rooms[i+1].parent = p[i];//부모 번호
            rooms[i+1].power = a[i];//세기
            rooms[i+1].alerts = new int[N+2];
        }

        //초기화 진행
        for(int i=1;i<=N;i++){
            int curPower = rooms[i].power;
            int idx = i;
            while(curPower>0){
                //해당 방에서 부모 전달되는것들을 저장한다.
                for(int power=0;power<curPower;power++){
                    rooms[idx].alerts[power]+=1;
                }

                
                //부모의 알림수를 +1해준다.
                int parent = rooms[idx].parent;
                rooms[parent].alert++;

                //부모로 이동
                if(parent==0) break;//해당 부모가 0번이면 세기만 올림.
                idx=parent;
                curPower--;
            }
        }
    }
    private static void onoff(int c) {
        if(rooms[c].isOn){
            // System.out.println("자릅니당");
            // System.out.println(Arrays.toString(rooms));
            off(c);
            rooms[c].isOn = false;
            // System.out.println(Arrays.toString(rooms));
        }else{
            rooms[c].isOn = true;
            on(c);
        }
    }

    //c까지 올라온 정보들을 위로 올라가면서 전부 제거한다.
    private static void off(int c) {
        int[] alerts = rooms[c].alerts;//올라오는 정보들

        //현재 방의 세기
        int power = rooms[c].power;//c의 세기
        int idx = 0;//올라가는 세기 확인용 index

        //부모 번호 확인
        int parent = rooms[c].parent;

        //다른데서도 쓰니까 혹시 모르는 확인용
        if(!rooms[c].isOn) return;

        while(true){
            
            //해당 부모의 세기에서 아래에서 올라온거 제거
            rooms[parent].alert-=alerts[idx];
            
            if(parent==0) break;//현재가 0이면 멈춘다.


            //alerts들을 제거한다.
            int parentIdx = 0;
            for(int i=idx+1;i<power;i++){
                rooms[parent].alerts[parentIdx]-=alerts[i];
                parentIdx++;
            }

            if(!rooms[parent].isOn) break;//끊겨서 이제 더이상 위 계산 안해도됨
            parent = rooms[parent].parent;
            idx++;
        }

    }

    //현재까지 올라온 세기들을 위로 올리면서 더해준다.
    private static void on(int c) {
        int[] alerts = rooms[c].alerts;//올라오는 정보들

        //현재 방의 세기
        int power = rooms[c].power;//c의 세기
        int idx = 0;//올라가는 세기 확인용 index

        //부모 번호 확인
        int parent = rooms[c].parent;

        //다른데서도 쓰니까 혹시 모르는 확인용
        if(!rooms[c].isOn) return;
        while(true){
            //해당 부모의 세기에 아래에서 올라온거 더하기
            rooms[parent].alert+=alerts[idx];
            
            if(parent==0) break;//현재가 0이면 멈춘다.

            //alerts들을 추가한다.
            int parentIdx = 0;
            for(int i=idx+1;i<power;i++){
                rooms[parent].alerts[parentIdx]+=alerts[i];
                parentIdx++;
            }
            if(!rooms[parent].isOn) break;//끊겨서 이제 더이상 위 계산 안해도됨
            parent = rooms[parent].parent;
            idx++;
        }
    }
    private static void powerChange(int c, int power) {
        //c번의 power를 올리거나, 줄인다.
        //off를 하고
        //바뀐power로 진행하는거 어때?
        // System.out.println("~~~~힘을 바꿉니다.");
        off(c);
        // System.out.println(Arrays.toString(rooms));
        //현재 power세기만큼 변경해준다.
        //커진경우
        int beforePower = rooms[c].power;
        if(beforePower<power){
            //커지는 경우
            for(int i=beforePower;i<power;i++){
                rooms[c].alerts[i]++;
            }
            //해당 power만큼 추가로 alerts를 연장한다.
        }else{
            //작아지는 경우
            //gap만큼 -1해준다.
            for(int i=power;i<beforePower;i++){
                rooms[c].alerts[i]--;
            }
        }
        // System.out.println(Arrays.toString(rooms));

        //power수정
        rooms[c].power = power;
        //다시 켜서 갱신해준다.
        on(c);
        // System.out.println(Arrays.toString(rooms));
    }
    private static void parentChange(int c1, int c2) {
        off(c1);
        off(c2);


        int tmp = rooms[c1].parent;
        rooms[c1].parent = rooms[c2].parent;
        rooms[c2].parent = tmp;

        on(c1);
        on(c2);
    }
    private static void printCount(int c) {
        if(c<1){
            System.out.println(-1);
        }else{
            System.out.println(rooms[c].alert);
        }
    }
}