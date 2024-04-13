import java.util.*;
import java.io.*;


//100
    //N u
        //N개의 채점기
        //url이 u라는 의미
    //url은 도메인/문제id
        //1~10억
    // 채점기는 1~N 번호
    // 0초에 채점 우선순위가 1이면서 url이 u인 초기 문제가 채점 요청이 들어온다.
    
    // 채점 대기큐에 들어간다.
//200
    //채점요청
        //200 t p u
            //t초에 채점 우선순위가 p, url이 u인 문제의 채점 요청
    //채점대기 큐로 들어간다.
        //채점 대기큐에 동일한 Url이 단 하나라도 존재하면 큐에 추가 안한다.
//300
    //채점시도
        //300 t
        //t초에 즉시 채점이 가능한 경우 중 우선순위가 가장 높은 채점 task를 골라 채점
    //현재 채점중인 '도메인'이면 불가능
    //도메인의 최근 채점 시간start, 종료시간 start+gap
        //현재시간이 < start+3*gap이면 채점 불가
    
    //우선순위
        //번호 P가 작을수록
            //시간이 빠를수록 
        
    //쉬는 채점기중 번호가 빠른 채점기가 시작
        //없으면 넘긴다.
//400
    //채점종료
        //jid번 채점기가 진행하던 채점 종료, 쉬게된다.
        //채점이 없으면 무시된다.
//500
    //채점 대기 큐의 수 출력
public class Main {

    static class Exam{
        int p,t;
        String url;
        String domain;
        int startT,endT;
        boolean isWorking;//채점 여부

        Exam(int t, int p, String url){
            this.t = t;
            this.p = p;
            this.url = url;
            this.domain = url.split("/")[0];
        }
    }

    //도메인별로 pq

    //도메인별 채점한 시간
    //도메인별 채점 종료 시간
    //도메인별 진행중인지 확인

    //최종 pq도 존재

    //쉬는 채점기 번호 확인 pq

    //채점기 번호 별 - 채점중 문제정보


    //채점 대기큐수
        //hashSet으로 url들을 넣엇허 갯수를 센다.
        //요청시 확인도 겸

    static HashSet<String> waitCheck;//대기중인 Url저장

    static HashMap<String,Boolean> isDomainJudging;//도메인별 채점정보 확인
    static HashMap<String,Integer> domainStart;//도메인별 채점시작시간
    static HashMap<String,Integer> domainEnd;//도메인별 채점종료시간

    static HashMap<String,PriorityQueue<Exam>> waitQue;//도메인별 대기
    static PriorityQueue<Integer> judge;//채점기 번호 확인용
    static Exam[] judgeCheck;//채점기별 채점중인 문제 정보가 저장.


    static int N;
    public static void main(String[] args) throws Exception {
        // System.setIn(new FileInputStream("코드트리채점기/input.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int Q= Integer.parseInt(st.nextToken());

        for(int q=0;q<Q;q++){
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());
            if(cmd==100){
                int N = Integer.parseInt(st.nextToken());
                String url = st.nextToken();
                intit(N,url);
            }else if(cmd==200){
                int t = Integer.parseInt(st.nextToken());
                int p = Integer.parseInt(st.nextToken());
                String u = st.nextToken();
                judgeRequest(t,p,u);
            }else if(cmd==300){
                int t = Integer.parseInt(st.nextToken());
                judgeStart(t);
            }else if(cmd==400){
                int t = Integer.parseInt(st.nextToken());
                int jid = Integer.parseInt(st.nextToken());
                judgeEnd(t,jid);
            }else if(cmd==500){
                int t = Integer.parseInt(st.nextToken());
                printWait(t);
            }
        }

        

    }


    private static void intit(int n, String url) {
        N=n;

        judge = new PriorityQueue<>((a,b)->a-b);
        judgeCheck = new Exam[N+1];
        //준비된 채점기 오름차순 정렬
        for(int t=1;t<=N;t++){
            judge.add(t);
        }
        waitCheck = new HashSet<>();//대기중인 Url저장
        waitQue = new HashMap<>();//도메인별 대기


        isDomainJudging = new HashMap<>();//도메인별 채점정보 확인
        domainStart = new HashMap<>();//도메인별 채점시작시간
        domainEnd = new HashMap<>();//도메인별 종료시간.

        judgeRequest(0,1,url);
    }
    
    private static void judgeRequest(int t, int p, String url) {
        if(!waitCheck.contains(url)){//대기중에 url이 같은게 없는경우 넣는다.
            Exam newExam = new Exam(t, p, url);
            waitCheck.add(url);
            if(!waitQue.containsKey(newExam.domain)){
                //도메인별로 대기큐 초기화
                    //우선순우, 시간 순
                waitQue.put(newExam.domain, new PriorityQueue<>((a,b)->{
                    if(a.p==b.p){
                        return a.t-b.t;
                    }
                    return a.p-b.p;
                }));
            }

            //대기큐에 넣는다.
            waitQue.get(newExam.domain).add(newExam);
        }
    }


    private static void judgeStart(int t) {

        //쉬는 채점기 확인
        if(judge.isEmpty()) return;//쉬는 채점기 없으니 넘김

        PriorityQueue<Exam> goJudge = new PriorityQueue<>((a,b)->{
            if(a.p==b.p){
                return a.t-b.t;
            }
            return a.p-b.p;
        });
        for(String domain : waitQue.keySet()){

            if(waitQue.get(domain).isEmpty()) continue;//대기중인게 없음

            //각 도메인별로 채점이 되는것들만 뺀다.
            if(isDomainJudging.containsKey(domain)){
                if(isDomainJudging.get(domain)){
                    //채점중
                    continue;
                }else{
                    //최근 시간 확인
                    int start = domainStart.get(domain);
                    int gap = domainEnd.get(domain)-start;

                    if(t<start+(3*gap)){
                        //부적절 하다.
                        continue;
                    }
                }
            }

            goJudge.add(waitQue.get(domain).peek());
        }

        if(goJudge.isEmpty()) return;//가능한 문제가 없음

        Exam exam = goJudge.poll();

        int judgeid = judge.poll();
        //채점기 채점 시작
        isDomainJudging.put(exam.domain, true);
        //채점기 시작 시간 기록
        domainStart.put(exam.domain, t);

        judgeCheck[judgeid] = exam;

        waitQue.get(exam.domain).poll();//해당 대기큐에서 제거

        //전체 대기큐에서 제거
        waitCheck.remove(exam.url);



    }


    private static void judgeEnd(int t, int jid) {
        if(judgeCheck[jid]==null) return;//채점하지 않고 있음
        
        //채점기 채점 완료
        isDomainJudging.put(judgeCheck[jid].domain, false);

        //채점기 종료 시간 작성
        domainEnd.put(judgeCheck[jid].domain, t);

        judgeCheck[jid] = null;

        //쉬는 처리
        judge.add(jid);
    }


    private static void printWait(int t) {

        System.out.println(waitCheck.size());
    }
}