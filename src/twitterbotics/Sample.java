package twitterbotics;
import java.util.Scanner;

public class Sample {
    public static void main (String[] args) {
    	String path = "D:/TSV Lists/";

		String kdir = "D:/TSV Lists/";
		String tdir = "D:/tdir/";		
		
		PersonOfInterest stereonomicon = new PersonOfInterest(kdir);
		
		stereonomicon.generateDreamConflicts(tdir);
		
		stereonomicon.makeOthersLookGood(tdir);
		
		stereonomicon.generateNietzscheanTweets(tdir);
		
		stereonomicon.generateShakespeareanTweets(tdir);
		
		stereonomicon.walkMileInShoes(tdir);
		
		stereonomicon.generateXYZs(tdir);
        
    }
}
