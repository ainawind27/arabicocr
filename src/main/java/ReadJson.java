import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ReadJson {
	public static void main(String[] args) {
	  JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("D:\\filetraining\\zhangsuen\\zhangsuen.Features.json"));
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray segments = (JSONArray) jsonObject.get("segments");
            Iterator<JSONObject> iterator = segments.iterator();
            while (iterator.hasNext()) {
            	JSONObject segment = iterator.next();
                
            	String label = (String) segment.get("label");
                System.out.println(label);
                
                Long dotCount = (Long) segment.get("dotCount");
                System.out.println(dotCount);
                
                Long dotPos= (Long) segment.get("dotPos");
                System.out.println(dotPos);
                
                JSONArray temp = (JSONArray) segment.get("normalizedBodyChain");
                Iterator<Long> bodyChain = temp.iterator();
                ArrayList<Long> normalizedBodyChain = new ArrayList<>();
                while (bodyChain.hasNext()) {
                	normalizedBodyChain.add((bodyChain.next()));
                }
                
                for(int i=0;i<normalizedBodyChain.size();++i) {
                	System.out.print(normalizedBodyChain.get(i) + " ");
                }
                System.out.println();
                System.out.println();
//                
//                System.out.println(dotCount);
//                System.out.println(dotPos);
//                System.out.println(normalizedBodyChain);
                
//                int [] features = new int [12];
//                
//                for (int i=0; i<features.length;i++) {
//                	features[i] = dotCount
//                }
                long [] features = new long [12];
                for (int i = 0 ; i<12 ;i++) {
                	features[0]=dotCount;
                	features[1]=dotPos; 
                	features[2]=normalizedBodyChain.get(0);
                	features[3]=normalizedBodyChain.get(1);
                	features[4]=normalizedBodyChain.get(2);
                	features[5]=normalizedBodyChain.get(3);
                	features[6]=normalizedBodyChain.get(4);
                	features[7]=normalizedBodyChain.get(5);
                	features[8]=normalizedBodyChain.get(6);
                	features[9]=normalizedBodyChain.get(7);
                	features[10]=normalizedBodyChain.get(8);
                	features[11]=normalizedBodyChain.get(9);

                }
                
                for (int i = 0 ; i<12 ;i++) {
                	 System.out.println(features[i]);
                }
                System.out.println();
               
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

	}
}
