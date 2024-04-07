import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
    
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st =new StringTokenizer(br.readLine());

        //지나갈수 잇는 열과 행은?
        //경사로 동안 같은 길이어야함
        //높이는 1차이어야함
        //높이가 1을 넘으면 안됨
        //경사로는 길어도 상관없음
        //경사로가 짧으면 안됨

        int n = Integer.parseInt(st.nextToken());
        int l = Integer.parseInt(st.nextToken());

        int[][] map = new int[n][n];

        for(int i=0;i<n;i++){
            st =new StringTokenizer(br.readLine());
            for(int j=0;j<n;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int answer = 0;
        //행 확인
        
        answer+=checkRow(map,n,l);
        answer+=checkCol(map,n,l);
        
        System.out.println(answer);
    }

    static public int checkRow(int[][] map,int n,int l){
        int result = 0;
        

        for(int i=0;i<n;i++){
            int idx = 0;
            boolean lineCheck = true;
            boolean[] visited = new boolean[map[0].length];//경사로 설치 여부
            while(true){
                if(idx==map[i].length-1) break;
                if(map[i][idx]==map[i][idx+1]){
                    idx++;
                }else{
                    if(Math.abs(map[i][idx]-map[i][idx+1])!=1){
                        //불가능
                        //System.out.println("불가능 단차1보다 컴");
                        lineCheck = false;
                        break;
                    }else{
                        if(map[i][idx]<map[i][idx+1]){
                            //현위치부터 뒤로 l만큼 같아야 한다.

                            if(l>idx+1){
                                //l만큼 거리가 부족
                                lineCheck = false;
                                //System.out.println("왼쪽 경사 거리부족");
                                break;
                            }

                            //뒤에서 l만큼 값이 같아야 한다.
                            int checkV = map[i][idx];
                            boolean isSame = true;
                            for(int k=idx+1-l;k<=idx;k++){
                                //경사로 설치한곳이면 넘긴다.
                                if(visited[k]){
                                    // System.out.println("겹침");
                                    isSame = false;
                                    break;
                                }
                                visited[k] = true;

                                if(checkV!=map[i][k]){
                                    isSame = false;
                                    break;
                                }
                            }
                            if(!isSame){
                                //불가능
                                // System.out.println("왼쪽 경사 평탄안함");
                                lineCheck = false;
                                break;
                            }else{
                                //한칸 더 이동
                                idx++;
                            }
                        }else{
                            //idx+1부터 l만큼 같아야 한다.

                            //거리 확인
                            if(map[i].length<=idx+l){
                                lineCheck = false;
                                // System.out.println("오른쪽 경사 부족");
                                break;
                            }

                            int checkV = map[i][idx+1];
                            boolean isSame = true;
                            for(int k=idx+1;k<=idx+l;k++){
                                //경사로 설치한곳이면 넘긴다.
                                if(visited[k]){
                                    isSame = false;
                                    // System.out.println("겹침");
                                    break;
                                }
                                visited[k] = true;

                                if(checkV!=map[i][k]){
                                    isSame = false;
                                    break;
                                }
                            }
                            if(!isSame){
                                //불가능
                                // System.out.println("오른쪽 경사 평탄아님");
                                lineCheck = false;
                                break;
                            }else{
                                //idx+l만큼 이동
                                idx+=l;
                            }
                        }

                    }
                }
            }

            if(lineCheck){
                // System.out.println("가능");
                result+=1;
            }
        }

        return result;
    }

    static public int checkCol(int[][] map,int n,int l){
        int result = 0;
        

        for(int j=0;j<n;j++){
            int idx = 0;
            boolean lineCheck = true;
            boolean[] visited = new boolean[map[0].length];//경사로 설치 여부
            while(true){
                if(idx==n-1) break;
                if(map[idx][j]==map[idx+1][j]){
                    idx++;
                }else{
                    if(Math.abs(map[idx][j]-map[idx+1][j])!=1){
                        //불가능
                        // System.out.println("불가능 단차1보다 컴");
                        lineCheck = false;
                        break;
                    }else{
                        if(map[idx][j]<map[idx+1][j]){
                            //현위치부터 뒤로 l만큼 같아야 한다.

                            if(l>idx+1){
                                //l만큼 거리가 부족
                                lineCheck = false;
                                // System.out.println("왼쪽 경사 거리부족");
                                break;
                            }

                            //뒤에서 l만큼 값이 같아야 한다.
                            int checkV = map[idx][j];
                            boolean isSame = true;
                            for(int k=idx+1-l;k<=idx;k++){
                                //경사로 설치한곳이면 넘긴다.
                                if(visited[k]){
                                    // System.out.println("겹침");
                                    isSame = false;
                                    break;
                                }
                                visited[k] = true;

                                if(checkV!=map[k][j]){
                                    isSame = false;
                                    break;
                                }
                            }
                            if(!isSame){
                                //불가능
                                // System.out.println("왼쪽 경사 평탄안함");
                                lineCheck = false;
                                break;
                            }else{
                                //한칸 더 이동
                                idx++;
                            }
                        }else{
                            //idx+1부터 l만큼 같아야 한다.

                            //거리 확인
                            if(n<=idx+l){
                                lineCheck = false;
                                // System.out.println("오른쪽 경사 부족");
                                break;
                            }

                            int checkV = map[idx+1][j];
                            boolean isSame = true;
                            for(int k=idx+1;k<=idx+l;k++){
                                //경사로 설치한곳이면 넘긴다.
                                if(visited[k]){
                                    isSame = false;
                                    // System.out.println("겹침");
                                    break;
                                }
                                visited[k] = true;

                                if(checkV!=map[k][j]){
                                    isSame = false;
                                    break;
                                }
                            }
                            if(!isSame){
                                //불가능
                                // System.out.println("오른쪽 경사 평탄아님");
                                lineCheck = false;
                                break;
                            }else{
                                //idx+l만큼 이동
                                idx+=l;
                            }
                        }

                    }
                }
            }

            if(lineCheck){
                // System.out.println("가능");
                result+=1;
            }
        }

        return result;
    }
}