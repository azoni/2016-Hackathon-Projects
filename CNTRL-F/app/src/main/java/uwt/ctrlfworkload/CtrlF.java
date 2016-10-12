package uwt.ctrlfworkload;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * Created by Charlton on 3/13/2016.
 */
public class CtrlF {
    private long totalTime;

    public void startSearch(String content) throws IOException, FileNotFoundException{

        String want = "the north";
        want = want.toLowerCase();

        final long startTime = System.currentTimeMillis();
        Scanner contentScan = new Scanner(content.toLowerCase());
        int numofInstances = getNum(contentScan, want);
        final long endTime = System.currentTimeMillis();
        contentScan.close();

        totalTime = (endTime - startTime);
        System.out.println("---------------------------------------------------");
        System.out.println("---------------------------------------------------");
        System.out.println("---------------------------------------------------");
        System.out.println(numofInstances);
        System.out.println("Execution time is: " + totalTime + " milliseconds");
        System.out.println("---------------------------------------------------");
        System.out.println("---------------------------------------------------");
        System.out.println("---------------------------------------------------");
    }
    public int getNum(Scanner file, String key){
        int count = 0;
        while(file.hasNext()){
            String x  = file.next();
            while(x.contains(key)){
                x = x.replaceFirst(Pattern.quote(key), "");
                count++;
            }
        }

        file.close();
        return count;
    }
}

