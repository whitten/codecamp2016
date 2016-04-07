package twitterbotics;

import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import java.util.Iterator;

/**
 * Created by user on 2016-04-07.
 */
public class StoryDB {
    static Random DICE 						 = new Random();

    private String knowledgeDir				 = null;   // directory where knowledge-base(s) can be found

    // Various modules of the knowledge-base

    private KnowledgeBaseModule NOC          = null;
    private KnowledgeBaseModule INIT         = null;
    private KnowledgeBaseModule ENDING       = null;
    private KnowledgeBaseModule ANTONYMS	 = null;
    private Hashtable NEG_QUALITIES 		 = null;
    private Vector attributeFields           = new Vector();
    private Vector allPeople				 = null;
    private KnowledgeBaseModule INTERCAT       = null;


    public StoryDB(String kbDirectory) {
        knowledgeDir = kbDirectory;

        NOC = new KnowledgeBaseModule(knowledgeDir + "Veale's The NOC List.txt", 0);
        ANTONYMS = new KnowledgeBaseModule(knowledgeDir + "antonyms.txt", 0);
        NEG_QUALITIES = NOC.getInvertedField("Negative Talking Points");
        INIT = new KnowledgeBaseModule(knowledgeDir + "Veale's initial bookend actions.txt", 0);
        ENDING = new KnowledgeBaseModule(knowledgeDir + "Veale's closing bookend actions.txt", 0);
        INTERCAT = new KnowledgeBaseModule(knowledgeDir + "Veale's Inter-Category Relationships.txt", 0);
        allPeople       = NOC.getKeyConcepts();
    }

    public int roll(int size) {
        return DICE.nextInt(size);
    }

    public void generateAntagonists () {
        Vector exemplars = INIT.getKeyConcepts();
        String exemplar = (String)exemplars.elementAt(roll(exemplars.size()));
        Vector posQuals = NOC.getFieldValues("Positive Talking Points", exemplar);
        String pos = (String)posQuals.elementAt(roll(posQuals.size()));
        Vector opposites = ANTONYMS.getFieldValues("Antonym", pos);
        if (opposites == null) return;
        String opposite  = (String)opposites.elementAt(roll(opposites.size()));
        Vector instances = (Vector)NEG_QUALITIES.get(opposite);
        if (instances == null) return;
//      CODE GRAVEYARD
//        .............
//        .............
    }

    public void generateAntagonistsv2() {

        while(true) {
            try {
                attributeFields.add("Negative Talking Points");
                attributeFields.add("Positive Talking Points");
                String A = (String) allPeople.get(roll(allPeople.size()));
                Vector attributeFields2 = NOC.getSimilarConcepts(A, attributeFields);
                Random randomGenerator = new Random();
                int randomInt = randomGenerator.nextInt(attributeFields2.size());
                String B = (String)attributeFields2.get(randomInt);
                System.out.println(A + ", " + B + " ");
                Vector firstPersonCat = NOC.getFieldValues("Category", B);
                Vector secondPersonCat = NOC.getFieldValues("Category", A);
                int randomInt1 = randomGenerator.nextInt(firstPersonCat.size());
                int randomInt2 = randomGenerator.nextInt(secondPersonCat.size());

                String catOfFirstPerson = firstPersonCat.get(randomInt1).toString();
                String catOfSecondPerson = secondPersonCat.get(randomInt2).toString();

                //System.out.println(catOfFirstPerson);

                Vector categoryFirstPerson = INTERCAT.getAllKeysWithFieldValue("Subject", catOfFirstPerson);
                Vector categorySecondPerson = INTERCAT.getAllKeysWithFieldValue("Subject", catOfFirstPerson);
                String intersect = (String) INTERCAT.intersect(categoryFirstPerson, categorySecondPerson).get(0);

                Vector verbs = INTERCAT.getFieldValues("Verbs", intersect);
                System.out.println(verbs);

                Vector initStoryVec = INIT.getFieldValues("Establishing Action", (String)verbs.get(roll(verbs.size())));
                String initStory = (String)initStoryVec.get(roll(initStoryVec.size()));
                initStory = initStory.replace("A", A);
                initStory = initStory.replace("B", B);
                System.out.println(initStory);

                Vector endingStoryVec = ENDING.getFieldValues("Closing Action", (String)verbs.get(roll(verbs.size())));
                String endingStory = (String)endingStoryVec.get(roll(endingStoryVec.size()));
                endingStory = endingStory.replace("A", A);
                endingStory = endingStory.replace("B", B);

                System.out.println(endingStory);


                break;
            }
            catch (NullPointerException e) {
                continue;
            }
        }



        
        
    }

}
