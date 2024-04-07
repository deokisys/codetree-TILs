import java.util.*;
import java.io.*;

public class Main {

    static class Table{
        int[] table;
        int top;
        Table(int[] table){
            this.table = table;
            this.top = 0;
        }

        public void turnRight(){
            top-=1;
            if(top<0){
                top+=8;
            }
        }

        public void turnLeft(){
            top+=1;
            top%=8;
        }

        public int getLeft(){
            int right = top-2;
            if(right<0){
                right+=8;
            }
            return this.table[right];
        }

        public int getRight(){

            return this.table[(top+2)%8];
            
        }

        public int getTop(){
            return this.table[top];
        }

        public String toString(){
            return Arrays.toString(this.table);
        }
    }
    public static void main(String[] args) throws Exception{
        //인접 두 의자의 출신이 다르면 같이 돈다.
            //시계로 돌리면, 반시계로 돔
        //12시 방향의 s의 출석여부
            //2진법 처럼 나옴
            //착석시1, 아니면 0
            //1번테이블 착석여부 * 2^0
            //2번테이블 착석여부 * 2^1

        // System.setIn(new FileInputStream("돌아가는팔각/input2.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        

        Table[] tables = new Table[4];

        for(int i=0;i<4;i++){
            String line = br.readLine();
            int[] lineN = new int[line.length()];
            for(int j=0;j<line.length();j++){
                lineN[j] = line.charAt(j)-'0';
            }
            tables[i] = new Table(lineN);
        }

        StringTokenizer st = new StringTokenizer(br.readLine());

        int k = Integer.parseInt(st.nextToken());


        // System.out.println(Arrays.toString(tables));
        // System.out.println("---");
        for(int i=0;i<k;i++){
            st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            //1은 시계, -1은 반시계
            
            turn(tables,n-1,d);
        }

        int answer = 0;
        //확인
        for(int i=0;i<4;i++){
            // System.out.println(tables[i].getTop());
            answer+=tables[i].getTop()*(int)Math.pow(2, i);
        }

        System.out.println(answer);
    }

    static public void turn(Table[] tables,int id,int d){
        //왼쪽확인
        if(id>0){
            if(tables[id-1].getRight()!=tables[id].getLeft()){
                turnLeft(tables,id-1,d*-1);
            }
        }
        //오른쪽확인
        if(id<3){
            if(tables[id+1].getLeft()!=tables[id].getRight()){
                turnRight(tables,id+1,d*-1);
            }
        }
        //회전
        if(d==1){
            tables[id].turnRight();
        }else{
            tables[id].turnLeft();
        }
    }

    static public void turnLeft(Table[] tables,int id,int d){
        //왼쪽확인
        if(id>0){
            if(tables[id-1].getRight()!=tables[id].getLeft()){
                turnLeft(tables,id-1,d*-1);
            }
        }
        //회전
        if(d==1){
            tables[id].turnRight();
        }else{
            tables[id].turnLeft();
        }
    }

    static public void turnRight(Table[] tables,int id,int d){

        //오른쪽확인
        if(id<3){
            if(tables[id+1].getLeft()!=tables[id].getRight()){
                turnRight(tables,id+1,d*-1);
            }
        }
        //회전
        if(d==1){
            tables[id].turnRight();
        }else{
            tables[id].turnLeft();
        }
    }
}