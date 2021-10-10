package com.example.mysubway;



import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;




public class calculation extends AppCompatActivity {

    TextView departure;
    TextView destination;
    TextView textview_time;
    ListView route;
    String[] resultList;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calculation);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent2 = getIntent();

        //Extra다 가져와서
        Bundle bundle = intent2.getExtras();
        //그중에 Dep과
        String Dep = bundle.getString("Dep");

        //그중에 Des 받아온다.
        String Des = bundle.getString("Des");

        //연동
        departure=findViewById(R.id.textview_dep);
        destination=findViewById(R.id.textview_des);
        //바꿔치기
        departure.setText(Dep);
        destination.setText(Des);

        //route 연동
        route=(ListView)findViewById(R.id.listview_route);

        LinkedList<String> data = new LinkedList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        InputStream in2 = getResources().openRawResource(R.raw.data);

        BufferedReader br=null ;
        try {
            br = new BufferedReader(new InputStreamReader(in2,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String input = Dep+" "+Des;

        try {
            resultList=dijkstra(input,br);
        } catch (Exception e) {
            Log.d("haha",e.toString());
        }
        String[] in = resultList[0].split(" ");
        for(int i =0;i<in.length;i++)
            data.add(in[i]);

          route.setAdapter(adapter);

        textview_time = findViewById(R.id.textview_time);
        textview_time.setText(resultList[1]);
    }



    static String[] dijkstra(String input2, BufferedReader br) throws Exception {

        //모든 역정보를 담음
        HashMap<String, Station> stationInfo= new HashMap<>(); //key : 고유번호, value: 역정보를 담은 station class
        HashMap<String, Station> stationInfo2= new HashMap<>(); //key : 한글이름, value: 역정보를 담은 station class

        //DB
        HashMap<String, HashMap<String, Station>> DB= new HashMap<>(); //key : 한글이름, value: 인접역들은 담은 HashMap

        String Line;

        //역정보 구조만들기
        while(!(Line=br.readLine()).equals("")) {
            //stationinfo
            String[] input = Line.split(" "); //고유번호, 한글이름, 호선
            int dummy=-1;
            Station st = new Station(input[1],input[2], dummy); //st=한글이름+호선
            stationInfo.put(input[0], st);

            //stationinfo2
            Station st2 = new Station(input[0],input[2]); //st=역번호+호선
            if(stationInfo2.containsKey(input[1]))//기존에 있던 역이라면
                stationInfo2.get(input[1]).add(input[0],input[2]); //기존에 있던 station class 불러와서 거기에 역번호랑 호선 append
            else
                stationInfo2.put(input[1], st2);//중복이 아니라면 그냥 해시맵에 바로 put. [key:한글이름, value: station class]
        }


        //각station별로 HashMap을 갖고 그 HashMap에는 인접역들이 key로 들어감
        while(!((Line=br.readLine())==null)) {
            //특정역에 인접하는 역정보만 담음

            String[] input = Line.split(" "); //고유번호1, 고유번호2, 시간


            if(DB.containsKey(input[0])) //중복됐다면
            {
                Station st3= new Station(stationInfo.get(input[1]).getKoreanName(),
                        stationInfo.get(input[1]).getLine(),
                        Integer.parseInt(input[2])); //역번호, 호선, 시간

                DB.get(input[0]).put(input[1],st3);
            }
            else //키가 중복이 아니라면 adjacency HashMap 을 만들어서 key를 put한 후, DB에 adjacency HashMap 대입
            {
                HashMap<String, Station> adjacencyHashMap= new HashMap<>(); //key:특정역에 인접하는고유번호, value: 그역에 대한 정보를 담은 station class
                Station st3= new Station(stationInfo.get(input[1]).getKoreanName(),
                        stationInfo.get(input[1]).getLine(),
                        Integer.parseInt(input[2])); //한글, 호선, 시간

                adjacencyHashMap.put(input[1], st3);
                DB.put(input[0], adjacencyHashMap);
            }

            //System.out.println(stationInfo2.get("사당").getuUniqueNumber().toString());
            //환승역까지 추가해주기
            LinkedList<String> un= new LinkedList<>();
            if((un=(LinkedList<String>) stationInfo2.get(stationInfo.get(input[0]).getKoreanName()).getuUniqueNumber()).size()>1)
            {
                for(int i=0;i<un.size();i++)
                {
                    for(int j=i+1;j<un.size();j++)
                    {
                        if(DB.containsKey(un.get(i))) //키(한글이름)이 중복됐다면 => 기존에 해시맵을 불러와서 그 hashpmap에 키를 put.
                        {
                            Station st3= new Station(stationInfo.get(un.get(j)).getKoreanName(),
                                    stationInfo.get(un.get(j)).getLine(), 5); //한글, 호선, 5분
                            DB.get(un.get(i)).put(un.get(j),st3);
                        }
                        else //키가 중복이 아니라면 adjacency HashMap 을 만들어서 key를 put한 후, DB에 adjacency HashMap 대입
                        {
                            HashMap<String, Station> adjacencyHashMap= new HashMap<>(); //key:특정역에 인접하는고유번호, value: 그역에 대한 정보를 담은 station class

                            Station st3= new Station(stationInfo.get(un.get(j)).getKoreanName(),
                                    stationInfo.get(un.get(j)).getLine(), 5); //한글, 호선, 5분

                            adjacencyHashMap.put(un.get(j), st3);
                            DB.put(un.get(i), adjacencyHashMap);
                        }

                        if(DB.containsKey(un.get(un.size()-i-1))) //키(한글이름)이 중복됐다면 => 기존에 해시맵을 불러와서 그 hashpmap에 키를 put.
                        {
                            Station st3= new Station(stationInfo.get(un.get(un.size()-1-j)).getKoreanName(),
                                    stationInfo.get(un.get(un.size()-1-j)).getLine(), 5); //한글, 호선, 5분
                            DB.get(un.get(un.size()-1-i)).put(un.get(un.size()-1-j),st3);
                        }
                        else //키가 중복이 아니라면 adjacency HashMap 을 만들어서 key를 put한 후, DB에 adjacency HashMap 대입
                        {
                            HashMap<String, Station> adjacencyHashMap= new HashMap<>(); //key:특정역에 인접하는고유번호, value: 그역에 대한 정보를 담은 station class

                            Station st3= new Station(stationInfo.get(un.get(un.size()-1-j)).getKoreanName(),
                                    stationInfo.get(un.get(un.size()-1-j)).getLine(), 5); //한글, 호선, 5분

                            adjacencyHashMap.put(un.get(un.size()-1-j), st3);
                            DB.put(un.get(un.size()-1-i), adjacencyHashMap);
                        }
                    }
                }
            }

        }
        return command(input2,DB,stationInfo, stationInfo2);


    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static String[] command(String in, HashMap<String, HashMap<String, Station>> DB, HashMap<String, Station> stationInfo ,HashMap<String, Station> stationInfo2) throws IOException {

        String[] input=in.split(" ");
        //unique number
        String start = stationInfo2.get(input[0]).getuUniqueNumber().get(0); //대신DB속 모든 친구들끼리 환승시간 0으로 바꿔줘야함

        //만약 출발점과 도착점이 환승역이라면? 그 점은 환승시간을 0으로 바꿔줘야함
        if(stationInfo2.get(input[0]).getuUniqueNumber().size()>1) { //환승역이라면
            LinkedList<String> un = new LinkedList<>();
            un=(LinkedList<String>) stationInfo2.get(input[0]).getuUniqueNumber(); //un에는 사당 역이름(영어)가 담김 (환승역이므로)
            for(int i=0;i<un.size();i++) 									 //un영어끼리의 거리를 0으로 만들어줘야함
            {
                for(int j=i+1;j<un.size();j++)
                {
                    DB.get(un.get(i)).get(un.get(j)).setTime(0);
                    DB.get(un.get(un.size()-1-i)).get(un.get(un.size()-1-j)).setTime(0);
                }
            }
        }
        String end = stationInfo2.get(input[1]).getuUniqueNumber().get(0);

        if(stationInfo2.get(input[1]).getuUniqueNumber().size()>1) {
            LinkedList<String> un = new LinkedList<>();
            un=(LinkedList<String>) stationInfo2.get(input[1]).getuUniqueNumber(); //un에는 사당 역이름(영어)가 담김 (환승역이므로)
            for(int i=0;i<un.size();i++) 									 //un영어끼리의 거리를 0으로 만들어줘야함
            {
                for(int j=i+1;j<un.size();j++)
                {
                    DB.get(un.get(i)).get(un.get(j)).setTime(0);
                    DB.get(un.get(un.size()-1-i)).get(un.get(un.size()-1-j)).setTime(0);
                }
            }
        }


        HashMap<String, Integer> d = new HashMap<>();//역별 최소거리를 담는 hashmap



        for(Object v : DB.keySet().toArray())  d.put((String)v, Integer.MAX_VALUE); //모든 정점 거리 inf으로 초기화
        d.replace(start, 0);// 처음거리는 0

        PriorityQueue<Node> pq = new PriorityQueue<>(); //heap을 만들어서
        HashMap<String, Node> find = new HashMap<>(); //마지막 relaxation을 한 놈들을 담는 경로모음

        Node tmp2 = new Node(start,d.get(start));  tmp2.addpath(start);  tmp2.setsameline("dummy");

        pq.add(tmp2); //heap 처음에 첫역과 거리0을 넣는다
        find.put(start, tmp2);


        while(!pq.isEmpty())
        {
            LinkedList<LinkedList> adjList = new LinkedList<>(); //get(0)은 이름 get(1)은 시간인 LinkedList를 node로 갖는 LinkedList
            LinkedList<LinkedList> adjList2 = new LinkedList<>();
            //인접역의 이름과 시간을 모두 담는 LinkedList 생성
            for(Object ad : DB.get(pq.peek().vertex).keySet().toArray())
            {
                LinkedList set = new LinkedList<>();
                //이름, 시간
                set.add(ad); set.add((int)DB.get(pq.peek().vertex).get(ad).getTime());
                adjList.add(set);
            }

            Node node = pq.poll();


            for(LinkedList l : adjList) //모든 인접 vertex에 대해
            {
                if(d.get(l.get(0)) > (int) l.get(1)+node.distance)//기존 d배열보다 시간이 작으면 ==relaxation
                {
                    d.replace((String) l.get(0),(int) l.get(1)+node.distance); // d update

                    Node tmp3;
                    tmp3 = new Node((String) l.get(0), d.get((String) l.get(0)) );
                    tmp3.setpath((LinkedList<String>) node.getroute().clone());
                    tmp3.addpath((String) l.get(0));
                    pq.add(tmp3); //queue에 삽입
                    find.put((String) l.get(0), tmp3);
                }
            }
        }


        String [] path= new String[find.get(end).getroute().toArray().length];
        int p=0;
        for( Object o :find.get(end).getroute().toArray())
        {
            path[p++] =  stationInfo.get(o).getKoreanName();
        }
        LinkedList<String> answer= new LinkedList<>();
        String temp = "";
        for(int i=0; i<path.length;i++) {
            if(temp.equals(path[i])) {
                answer.remove(answer.size()-1); //뺏다가
                answer.add("["+ path[i]+ "]"); //지금꺼 다시 넣어
            }
            else { //만약 같지않다면 그냥 넣어
                answer.add(path[i]);
            }
            temp=answer.getLast();
        }

        if(answer.get(0).contains("["))
        {
            String str = new String(answer.get(0).substring(1, answer.get(0).length()-1));
            answer.remove(0);
            answer.add(0,str);
        }
        if(answer.get(answer.size()-1).contains("["))
        {
            String str = new String(answer.get(answer.size()-1).substring(1,answer.get(answer.size()-1).length()-1));
            answer.remove(answer.size()-1);
            answer.add(answer.size(),str);
        }

        StringBuilder result = new StringBuilder();
        for(int i=0;i<answer.size();i++) {
            result.append(answer.get(i));
            result.append(" ");
        }
        result.delete(result.length()-1, result.length());

        String time = Integer.toString(d.get(end));
        String[] strlist = new String[2];
        strlist[0]= result.toString();
        strlist[1]= time;

        //시간 0으로해줬던거 원상복귀

        if(stationInfo2.get(input[0]).getuUniqueNumber().size()>1) { //환승역이라면
            LinkedList<String> un = new LinkedList<>();
            un=(LinkedList<String>) stationInfo2.get(input[0]).getuUniqueNumber(); //un에는 사당 역이름(영어)가 담김 (환승역이므로)
            for(int i=0;i<un.size();i++) 									 //un영어끼리의 거리를 0으로 만들어줘야함
            {
                for(int j=i+1;j<un.size();j++)
                {
                    DB.get(un.get(i)).get(un.get(j)).setTime(5);
                    DB.get(un.get(un.size()-1-i)).get(un.get(un.size()-1-j)).setTime(5);
                }
            }
        }
        if(stationInfo2.get(input[1]).getuUniqueNumber().size()>1) {
            LinkedList<String> un = new LinkedList<>();
            un=(LinkedList<String>) stationInfo2.get(input[1]).getuUniqueNumber(); //un에는 사당 역이름(영어)가 담김 (환승역이므로)
            for(int i=0;i<un.size();i++) 									 //un영어끼리의 거리를 0으로 만들어줘야함
            {
                for(int j=i+1;j<un.size();j++)
                {
                    DB.get(un.get(i)).get(un.get(j)).setTime(5);
                    DB.get(un.get(un.size()-1-i)).get(un.get(un.size()-1-j)).setTime(5);
                }
            }
        }
        return strlist;
    }

}
class Station {
    private String koreanName;
    private List<String> uniqueNumber;
    private List<String> line;
    private int time;
    private boolean canTransfer = false;

    //Constructor
    Station(String korean, String line, int dummy) {
        this.line = new LinkedList<>();
        this.koreanName = korean;
        this.line.add(line);
    }


    //Constructor
    Station(String uN, String line) {
        this.line = new LinkedList<>();
        this.uniqueNumber = new LinkedList<>();
        this.uniqueNumber.add(uN);
        this.line.add(line);
    }

    //Constructor
    Station(String korean, List<String> line, int time) {
        this.koreanName = korean;
        this.line = new LinkedList<>(line);
        this.time = time;
    }

    //중복되는 역번호와 호선을 추가하는 함수
    void add(String uN, String line) {
        this.uniqueNumber.add(uN);
        this.line.add(line);
        if (uniqueNumber.size() > 1) this.canTransfer = true; //두개 이상일시 환승역으로 지정
    }


    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return this.time;
    }

    public void setKoreanName(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return this.koreanName;
    }

    public void setLine(List<String> line) {
        this.line = line;
    }

    public List<String> getLine() {
        return this.line;
    }

    public void setuUniqueNumber(List<String> uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public List<String> getuUniqueNumber() {
        return this.uniqueNumber;
    }

    public boolean getCantransfer() {
        return canTransfer;
    }
}