import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        //자연수 하나 적힘
        //n,m
        //다섯 테트리스 블럭 중 한개를 놓아, 숫자 합이 최대가 될떼 결과를 출력
        //회전, 뒤집기 가능


        //지도 크기는 200,200
        int[][][] tetris = {
            {{0,0},{0,1},{0,2},{0,3}},
            {{0,0},{1,0},{2,0},{3,0}},

            {{0,0},{0,1},{1,0},{1,1}},

            //ㄴ
            {{0,0},{1,0},{2,0},{2,1}},
            {{0,0},{0,1},{0,2},{-1,2}},
            {{0,0},{0,1},{1,1},{2,1}},
            {{0,0},{1,0},{0,1},{0,2}},

            //ㄱ
            {{0,0},{0,1},{0,2},{1,2}},
            {{0,0},{1,0},{2,0},{2,-1}},
            {{0,0},{1,0},{1,1},{1,2}},
            {{0,0},{1,0},{1,1},{1,2}},
            {{0,0},{0,1},{1,0},{2,0}},

            //
            {{0,0},{1,0},{1,1},{2,1}},
            {{0,0},{0,1},{-1,1},{-1,2}},

            {{0,0},{1,0},{1,-1},{2,-1}},
            {{0,0},{0,1},{1,1},{1,2}},

            {{0,0},{0,-1},{-1,0},{0,-1}},//ㅗ
            {{0,0},{0,-1},{-1,0},{0,1}},//ㅜ
            {{0,0},{0,-1},{-1,0},{1,0}},//ㅓ
            {{0,0},{0,1},{-1,0},{1,0}},//ㅏ
        };

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        int[][] map = new int[n][m];

        for(int i=0;i<n;i++){
            st = new StringTokenizer(br.readLine());
            for(int j=0;j<m;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int answer = 0;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                //모든 테트리스 확인

                for(int t = 0;t<tetris.length;t++){
                    int sum = 0;
                    for(int d=0;d<4;d++){
                        int zr = i+tetris[t][d][0];
                        int zc = j+tetris[t][d][1];
                        if(zr<0||zc<0||zr>=n||zc>=m) {
                            sum=-1;
                            break;
                        }
                        sum+=map[zr][zc];
                    }

                    answer = Math.max(answer,sum);
                }
            }
        }

        System.out.println(answer);

    }
}