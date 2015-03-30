/* Machine learning Project
# PERZONALIZED SONG RECOMMENDER SYSTEM
# Michael Lau, Binjie Li, Beitong Zhang
# Data Preprocessing for the whole project
# -------------------------------------------------------------------------
# Input: .tsv listening history file for about 3G.
# Output: 3 outputs. 1st is the song/artist dictionary file; 2nd is user_song/user_artist train matrix; 3rd is user_song/user_artist test matrix
# need tune the parameters to statistic song OR artists!
# Our neighborhood model and the preprocessing program for LFM need output of this program.
*/


import java.io.*;
import java.util.*;

public class test{

    private static class ValueComparator implements Comparator<Map.Entry<String,Integer>>
    {
        public int compare(Map.Entry<String,Integer> m,Map.Entry<String,Integer> n)
        {
            return n.getValue()-m.getValue();
        }
    }

    private static void reverseFile(String filename) throws IOException{
        String filePath = "/Users/libinjie/IdeaProjects/dataProcess/" + filename;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
        LinkedList<String> list = new LinkedList<String>();
        String output = "/Users/libinjie/IdeaProjects/dataProcess/" + filename + "_r.txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(output));
        while (br.ready()) {
            String myreadline = br.readLine();
            list.add(myreadline);
        }
        for(int i = list.size() - 1; i >= 0; i--) {
            bw.append(list.get(i));
            bw.newLine();
        }
        if(br != null) br.close();
        if(bw != null) bw.close();
    }

    public static void main(String args[]) throws IOException {
        String filePath = "/Users/libinjie/IdeaProjects/dataProcess/userid-timestamp-artid-artname-traid-traname.tsv";
        //artistNumber is the size of dictionary
        int artistNumber = 10000;
        //3 stands for artist and change to 5 to statistic songs
        int property = 3;
        //size of listening history for train
        int train_size = 3000;
        //filter users who have history length above min_length
        int min_length = 10000;
        List<Integer> result = new ArrayList<Integer>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
        TreeMap<String, Integer> artistMap = new TreeMap<String, Integer>();
        int i = 0;
        TreeMap<String,Integer> map = new TreeMap<String,Integer>();
        while (br.ready()) {
            String myreadline = br.readLine();
            String[] strArray = myreadline.split("\t");
            if(!strArray[property].equals("")){
                if(map.containsKey(strArray[property])){
                    int val = map.get(strArray[property])+1;
                    map.put(strArray[property],val);
                }
                else {
                    map.put(strArray[property],1);
                }
            }
        }
        System.out.println("There are all " + map.size() + " artists.");
        //get top artistNumber artists
        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>();
        list.addAll(map.entrySet());
        test.ValueComparator vc = new ValueComparator();
        Collections.sort(list,vc);
        for(Iterator<Map.Entry<String,Integer>> it=list.iterator();it.hasNext();)
        {
            if(i < artistNumber){
                //System.out.println("Artist " + it.next().getKey() + " has played for " + it.next().getValue() + " times.");
                artistMap.put(it.next().getKey(),0);
            }else{
                break;
            }
            i++;
        }
        //output top artist list
        /*String artistFile = "/Users/libinjie/IdeaProjects/dataProcess/artist_list.txt";
        BufferedWriter bw_a = new BufferedWriter(new FileWriter(artistFile));
        int i1 = 1;
        Iterator iter_a = artistMap.entrySet().iterator();
        while (iter_a.hasNext()) {
            Map.Entry entry = (Map.Entry) iter_a.next();
            bw_a.append(i1 + ": ");
            bw_a.append(entry.getKey().toString());
            bw_a.newLine();
            i1++;
        }
        if(br != null) br.close();
        if(bw_a != null) bw_a.close();*/
        //filter users with small history
        String preUserID = "";
        br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
        HashMap<String,Integer> thrownMap = new HashMap<String, Integer>();
        HashMap<String,Integer> acceptMap = new HashMap<String, Integer>();
        String statis = "/Users/libinjie/IdeaProjects/dataProcess/user_song.txt";
        BufferedWriter bw_a = new BufferedWriter(new FileWriter(statis));
        int historyNumber = 1;
        int p = 1;
        while(br.ready()) {
            String myreadline = br.readLine();
            String[] strArray = myreadline.split("\t");
            if(!strArray[0].equals(preUserID)){
                if(!preUserID.equals("")){
                    if(historyNumber < min_length){
                        thrownMap.put(preUserID,historyNumber);
                    }else{
                        acceptMap.put(preUserID,historyNumber);
                    }
                    bw_a.append(Integer.toString(p));
                    bw_a.append("\t");
                    bw_a.append(Integer.toString(historyNumber));
                    bw_a.newLine();
                    p++;
                }
                preUserID = strArray[0];
                historyNumber = 1;
            }else{
                historyNumber++;
            }
        }
        System.out.println("There are " + thrownMap.size() + " invalid users.");
        System.out.println("There are " + acceptMap.size() + " valid users.");
        if(br != null) br.close();
        if(bw_a != null) bw_a.close();
        //output user artist train vector
        int userNumber = 0;
        HashMap<String,Integer> user_map = new HashMap<String,Integer>();
        preUserID = "";
        int number = 0;
        String outputFile = "/Users/libinjie/IdeaProjects/dataProcess/user_artist_vector_train.txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
        br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
        while (br.ready()) {
            String myreadline = br.readLine();
            String[] strArray = myreadline.split("\t");
            if(!strArray[0].equals(preUserID)){
                if(!preUserID.equals("")){
                    if(!thrownMap.containsKey(preUserID)){
                        //bw.append(preUserID);
                        int temp = 0;
                        Iterator iter = artistMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            //if(temp == 0) bw.append(";");
                            //else bw.append(",");
                            if(temp != 0) bw.append(" ");
                            Map.Entry entry = (Map.Entry) iter.next();
                            Object rate = entry.getValue();
                            bw.append(rate.toString());
                            if(Integer.parseInt(rate.toString()) > 0) userNumber++;
                            temp++;
                        }
                        //bw.append(";");
                        bw.newLine();
                        user_map.put(preUserID,userNumber);
                    }
                }
                preUserID = strArray[0];
                userNumber = 0;
                Iterator iter = artistMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    entry.setValue(0);
                }
                if(artistMap.containsKey(strArray[property])){
                    int val = artistMap.get(strArray[property])+1;
                    artistMap.put(strArray[property],val);
                }
                number = 1;
            }else{
                if(number < train_size){
                    if(artistMap.containsKey(strArray[property])){
                        int val = artistMap.get(strArray[property])+1;
                        artistMap.put(strArray[property],val);
                    }
                }
                number++;
            }
        }
        if(br != null) br.close();
        if(bw != null) bw.close();
        //output user artist test vector
        preUserID = "";
        outputFile = "/Users/libinjie/IdeaProjects/dataProcess/user_artist_vector_test.txt";
        bw = new BufferedWriter(new FileWriter(outputFile));
        br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
        while (br.ready()) {
            String myreadline = br.readLine();
            String[] strArray = myreadline.split("\t");
            if(!strArray[0].equals(preUserID)){
                if(!preUserID.equals("")){
                    if(!thrownMap.containsKey(preUserID)){
                        //bw.append(preUserID);
                        int temp = 0;
                        Iterator iter = artistMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            //if(temp == 0) bw.append(";");
                            //else bw.append(",");
                            if(temp != 0) bw.append(" ");
                            Map.Entry entry = (Map.Entry) iter.next();
                            Object rate = entry.getValue();
                            bw.append(rate.toString());
                            if(Integer.parseInt(rate.toString()) > 0) userNumber++;
                            temp++;
                        }
                        //bw.append(";");
                        bw.newLine();
                        int newNumber = userNumber-user_map.get(preUserID);
                        result.add(newNumber);
                        //System.out.println(preUserID + " has " + newNumber + " more artists.");
                    }
                }
                preUserID = strArray[0];
                userNumber = 0;
                Iterator iter = artistMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    entry.setValue(0);
                }
                if(artistMap.containsKey(strArray[property])){
                    int val = artistMap.get(strArray[property])+1;
                    artistMap.put(strArray[property],val);
                }
            }else{
                if(artistMap.containsKey(strArray[property])){
                    int val = artistMap.get(strArray[property])+1;
                    artistMap.put(strArray[property],val);
                }
            }
        }
        if(br != null) br.close();
        if(bw != null) bw.close();
        int min = result.get(0);
        for(int z = 1; z < result.size(); z++){
            if(result.get(z) < min) min = result.get(z);
        }
        System.out.println(min);
    }
}