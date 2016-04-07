package twitterbotics;
<<<<<<< HEAD
import java.util.Scanner;
import java.util.Vector;
import java.util.Random;

public class Sample {
    public static void main (String[] args) {
    	
		String kdir = "D:/TSV Lists/";
		String tdir = "D:/tdir/";		
		
		//PersonOfInterest stereonomicon = new PersonOfInterest(kdir);
		
		//stereonomicon.generateDreamConflicts(tdir);
		
		//stereonomicon.makeOthersLookGood(tdir);
		
		//stereonomicon.generateNietzscheanTweets(tdir);
		
		//stereonomicon.generateShakespeareanTweets(tdir);
		
		//stereonomicon.walkMileInShoes(tdir);
		
		//stereonomicon.generateXYZs(tdir);
		
		KnowledgeBaseModule initialOfStory          = new KnowledgeBaseModule(kdir + "Veale's initial bookend actions.txt", 0);
		KnowledgeBaseModule NOC          = new KnowledgeBaseModule(kdir + "Veale's The NOC List.txt", 0);
		KnowledgeBaseModule CATEGORIES   = new KnowledgeBaseModule(kdir + "Veale's Category Hierarchy.txt", 0);
		KnowledgeBaseModule CLOTHES      = new KnowledgeBaseModule(kdir + "Veale's clothing line.txt", 1);  // 1 is the column number of the key value
		KnowledgeBaseModule CREATIONS    = new KnowledgeBaseModule(kdir + "Veale's creations.txt", 0);
		KnowledgeBaseModule DOMAINS      = new KnowledgeBaseModule(kdir + "Veale's domains.txt", 0);
		KnowledgeBaseModule WORLDS       = new KnowledgeBaseModule(kdir + "Veale's fictional worlds.txt", 0);
		KnowledgeBaseModule VEHICLES     = new KnowledgeBaseModule(kdir + "Veale's vehicle fleet.txt", 1);  // 1 is the column number of the key value
		KnowledgeBaseModule WEAPONS	     = new KnowledgeBaseModule(kdir + "Veale's weapon arsenal.txt", 1);  // 1 is the column number of the key value
		KnowledgeBaseModule PLACES       = new KnowledgeBaseModule(kdir + "Veale's place elements.txt", 0);
		KnowledgeBaseModule SUPERLATIVES = new KnowledgeBaseModule(kdir + "superlatives.txt", 0);
		
		
		Vector exemplars = initialOfStory.getKeyConcepts();
		
		for (int e = 0; e < exemplars.size(); e++)
		{
			String exemplar = (String)exemplars.elementAt(e);
			//System.out.println(initialOfStory.getFieldValues("Establishing Action", exemplar));
		}
		
		
		//System.out.println(initialOfStory.getFieldValues("Establishing Action", "obey"));
		KnowledgeBaseModule endOfStory          = new KnowledgeBaseModule(kdir + "Veale's closing bookend actions.txt", 0);
		//System.out.println(endOfStory.getFieldValues("Closing Action", "disobey"));

		Vector attributeFields = new Vector();
		
		attributeFields.add("Negative Talking Points");
		attributeFields.add("Positive Talking Points");
		
		Vector fictionalCharacters = NOC.getAllKeysWithFieldValue("Fictive Status", "fictional");

		//System.out.println(NOC.difference(NOC.getSimilarConcepts("Darth Vader", attributeFields), fictionalCharacters));
		//System.out.println(NOC.difference(NOC.getSimilarConcepts("The Joker", attributeFields), fictionalCharacters));
		Vector attributeFields2 = NOC.getSimilarConcepts("Darth Vader", attributeFields);
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(attributeFields2.size());
		Object objs = attributeFields2.get(randomInt);
		System.out.print(objs + " ");
		System.out.println();
		
=======
//import java.util.Scanner;

public class Sample {
    public static void main (String[] args) {
		String kdir = "D:/TSV Lists/";
		String tdir = "D:/tdir/";
		StoryDB storyDB = new StoryDB(kdir);

		
//		PersonOfInterest stereonomicon = new PersonOfInterest(kdir);
		
//		stereonomicon.generateDreamConflicts(tdir);
		
//		stereonomicon.makeOthersLookGood(tdir);
//
//		stereonomicon.generateNietzscheanTweets(tdir);
//
//		stereonomicon.generateShakespeareanTweets(tdir);
//
//		stereonomicon.walkMileInShoes(tdir);
//
//		stereonomicon.generateXYZs(tdir);
>>>>>>> origin/master
        
    }
}
