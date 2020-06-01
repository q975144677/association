package com.association.admin.component;

import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    static long countDolphin(int n, int m, int[] birthYear, int x) {
        HashMap<String,Integer> map = new HashMap<>();
        int flag = 1 ;
        map.put(flag++ +","+n,0);
//        Arrays.stream(birthYear).boxed().collect(Collectors.toList());
        Set<Integer> set =  Arrays.stream(birthYear).boxed().collect(Collectors.toSet());
//        System.out.println(set);
        for(int i = 0 ; i < x ; i ++){
            Iterator<Map.Entry<String,Integer>> iterator=map.entrySet().iterator();
            while(iterator.hasNext()){
                System.out.println("CHECK DIE ");
                Map.Entry<String,Integer> entry =  iterator.next();
                if(entry.getValue() >= m){
                    System.out.println("si" + entry.getKey());
                    iterator.remove();
                }
            }
            System.out.println("过了一年"+i);
            map.entrySet().stream().forEach(x2->map.put(x2.getKey(),x2.getValue()+1));
            System.out.println("check +" + (i+1));
            if(set.contains(i + 1)){
//                map.put()
                int sum = map.keySet().stream().mapToInt(k -> Integer.parseInt(k.split(",")[1])).sum();
                map.put(flag++ +","+sum,0);
                System.out.println("生" + sum);

            }

        }
        System.out.println(JSONObject.toJSONString(map));
        AtomicInteger max = new AtomicInteger();
        map.entrySet().stream().forEach(z-> max.addAndGet(Integer.parseInt(z.getKey().split(",")[1])));
        return max.get();
    }
    /******************************结束写代码******************************/

//    public static void main(String[] args){
//        String[] map= {"surprise", "happy", "ctrip", "travel", "wellcome","student","system","program","editor"};
//        Scanner sc = new Scanner(System.in);
////        List<String> words=  new ArrayList<>();
//        while(sc.hasNextLine()){
//            String word = sc.nextLine();
////            words.add(word);
//        }
//
//    }
//    public static void check(int count){
//
//    }
//    static void add(int index , char c ){
//
//    }
//    static void del(int index){
//
//    }
//    static void change(int index , char c){
//
//    }
//    static int diff(String str1, String str2){
//            int n = str1.length();
//            int m = str2.length();
//            int[][] Arr = new int[m + 1][n + 1];
//            int cost = 0;
//            for(int i=0;i<=n;i++)
//                Arr[0][i] = i;
//            for(int j=0;j<=m;j++)
//                Arr[j][0] = j;
//            for(int i=1;i<=n;i++)
//                for(int j=1;j<=m;j++)
//                {
//                    if (str1.charAt(i-1) == str2.charAt(j-1))
//                        cost = 0;
//                    else
//                        cost = 1;
//                    Arr[j][i] = Arr[j-1][i]+1<Arr[j][i-1]+1?Arr[j-1][i]+1:Arr[j][i-1] +1 ;
//                    Arr[j][i] = Arr[j][i]<Arr[j-1][i-1]+cost?Arr[j][i]:Arr[j-1][i-1]+cost;
//                }
//            int nEdit = Arr[m][n];
//            return nEdit;
//
//    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        long res;

        int _n;
        _n = Integer.parseInt(in.nextLine().trim());

        int _m;
        _m = Integer.parseInt(in.nextLine().trim());

        int _birthYear_size = 0;
        _birthYear_size = Integer.parseInt(in.nextLine().trim());
        int[] _birthYear = new int[_birthYear_size];
        int _birthYear_item;
        for(int _birthYear_i = 0; _birthYear_i < _birthYear_size; _birthYear_i++) {
            _birthYear_item = Integer.parseInt(in.nextLine().trim());
            _birthYear[_birthYear_i] = _birthYear_item;
        }
        System.out.println(JSONObject.toJSONString(_birthYear));
        int _x;
        _x = Integer.parseInt(in.nextLine().trim());

        res = countDolphin(_n, _m, _birthYear, _x);
        System.out.println(String.valueOf(res));

    }
//    public static void solution3() {
//        Scanner sc = new Scanner(System.in);
//        int count = sc.nextInt();
//        sc.nextLine();
//        List<String> info = new HashSet<>();
//        for (int i = 0; i < count; i++) {
//            info.add(sc.nextLine());
//
//        }
//        count = sc.nextInt();
//        sc.nextLine();
////        String[] info2 = new String[count];
//        List<String> info2 = new HashSet<>();
//        for (int i = 0; i < count; i++) {
//            info2.add(sc.nextLine());
//        }
//        System.out.println(JSONObject.toJSONString(info) + "\n" + JSONObject.toJSONString(info2));
////        List<>
//    }
//
//    public static void solution2() {
//        Scanner sc = new Scanner(System.in);
//        int count = sc.nextInt();
//        Integer[][] info = new Integer[count][2];
//        int left = 0;
//        int right = 0;
//        for (int i = 0; i < count; i++) {
//            for (int j = 0; j < 2; j++) {
//                info[i][j] = sc.nextInt();
//                if (j == 0) {
//                    left = Math.max(info[i][j], left);
//                } else {
//                    right = Math.max(info[i][j], right);
//                }
//            }
//        }
//        List<Integer[]> list = Arrays.stream(info).collect(Collectors.toList());
//        AtomicInteger max = new AtomicInteger();
//        list.forEach(x->{
//            int w = x[0];
//            int h = x[1];
//            max.set(Math.max(max.get(), qiantao(list, w, h)));
//        });
//        System.out.println(max.get() + 1);
//    }
//    public static int qiantao(List<Integer[]> info,int w , int h){
//        int flag = 0 ;
//        int max = 0 ;
//        for(int i = 0 ; i < info.size() ; i ++){
//            Integer[] list =info.get(i);
//            int left = list[0];
//            int right = list[1];
////            if(left >= w || right >= h){
////                info.remove(i);
////                continue;
////            }
//            if(left > w && right > h) {
//                int res = qiantao(info, left, right) + 1;
//                max = Math.max(res, max);
//            }
//        }
//        return max ;
//    }
//    public static void get(List<List<Integer>> info) {
//
//    }
//
//    /*
//      4
//     15
//      5 3 4 6
//      20 10 12 30
//     */
//    public static void solution1() {
//        Scanner sc = new Scanner(System.in);
//        int count = sc.nextInt();
//        int v = sc.nextInt();
//        int[] wig = new int[count];
//        int[] value = new int[count];
//        for (int i = 0; i < count; i++) {
//            wig[i] = sc.nextInt();
//        }
//        for (int i = 0; i < count; i++) {
//            value[i] = sc.nextInt();
//        }
//        if (count == 1) {
//            if (v > wig[0]) {
//                System.out.println(value[0]);
//            } else {
//                System.out.println(0);
//            }
//            return;
//        }
//        int[][] dp = new int[count][v];
//        for (int i = 0; i < v; i++) {
//            if (i + 1 >= wig[0]) {
//                dp[0][i] = value[0];
//            }
//        }
//
//        for (int i = 1; i < count; i++) {
//            int weight = wig[i];
//            int val = value[i];
//            for (int j = 0; j < v; j++) {
//                if (j + 1 < weight) {
//                    dp[i][j] = dp[i - 1][j];
//                } else {
//                    /**
//                     * 3 - 3
//                     * 6-3   4
//                     * MMAx:20
//                     * putMax : 30
//                     */
//                    int minusMax;
//                    if (j - weight < 0) {
//                        minusMax = 0;
//                    } else {
//                        minusMax = dp[i - 1][j - weight];
//                    }
//                    int putMax = minusMax + val;
//                    dp[i][j] = putMax > dp[i - 1][j] ? putMax : dp[i - 1][j];
////                    System.out.println("ij+HJ" + j + "-" + weight + "j-+1" + (j + 1 - weight));
////                    System.out.println("MMAx:" + minusMax);
////                    System.out.println("putMax : " + putMax);
//                }
//            }
//        }
////        System.out.println(JSONObject.toJSONString(dp));
//        System.out.println(dp[dp.length - 1][dp[0].length - 1]);
//    }


}
