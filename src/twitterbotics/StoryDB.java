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
    private KnowledgeBaseModule IDIOMATIC       = null;
    private Vector wordBank = new Vector();


    public StoryDB(String kbDirectory) {
        knowledgeDir = kbDirectory;

        NOC = new KnowledgeBaseModule(knowledgeDir + "Veale's The NOC List.txt", 0);
        ANTONYMS = new KnowledgeBaseModule(knowledgeDir + "antonyms.txt", 0);
        NEG_QUALITIES = NOC.getInvertedField("Negative Talking Points");
        INIT = new KnowledgeBaseModule(knowledgeDir + "Veale's initial bookend actions.txt", 0);
        ENDING = new KnowledgeBaseModule(knowledgeDir + "Veale's closing bookend actions.txt", 0);
        IDIOMATIC = new KnowledgeBaseModule(knowledgeDir + "Veale's idiomatic actions.txt", 0);
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
                Vector firstPersonCat = NOC.getFieldValues("Category", A);
                Vector secondPersonCat = NOC.getFieldValues("Category", B);

                Vector tmp1;
                Vector tmp2;
                Vector intersect;
                String intersectValue = null;

                for (int i = 0; i < firstPersonCat.size(); ++i) {
                    for (int j =  0; j < secondPersonCat.size(); ++j) {
                        tmp1 = INTERCAT.getAllKeysWithFieldValue("Subject", (String)firstPersonCat.get(i));
                        tmp2 = INTERCAT.getAllKeysWithFieldValue("Subject", (String)secondPersonCat.get(j));
                        intersect = INTERCAT.intersect(tmp1, tmp2);
                        if (intersect.size() < 1) continue;
                        intersectValue = (String)intersect.get(0);
                        break;
                    }
                }

                if (intersectValue == null) continue;

                Vector verbs = INTERCAT.getFieldValues("Verbs", intersectValue);

                Vector initStoryVec = INIT.getFieldValues("Establishing Action", (String)verbs.get(roll(verbs.size())));
                String initStory = (String)initStoryVec.get(roll(initStoryVec.size()));
                initStory = initStory.replace("A", A);
                initStory = initStory.replace("B", B);
                
                
                Vector idiomaticPart = IDIOMATIC.getFieldValues("Idiomatic Forms", (String)verbs.get(roll(verbs.size())));
                String idiomaticStory = (String)idiomaticPart.get(roll(idiomaticPart.size()));
                idiomaticStory = idiomaticStory.replace("A", A);
                idiomaticStory = idiomaticStory.replace("B", B);
                

                Vector endingStoryVec = ENDING.getFieldValues("Closing Action", (String)verbs.get(roll(verbs.size())));
                String endingStory = (String)endingStoryVec.get(roll(endingStoryVec.size()));
                endingStory = endingStory.replace("A", A);
                endingStory = endingStory.replace("B", B);
                
                
                System.out.println(initStory);
                System.out.println(idiomaticStory);
                idiomaticPart = IDIOMATIC.getFieldValues("Idiomatic Forms", (String)verbs.get(roll(verbs.size())));
                idiomaticStory = (String)idiomaticPart.get(roll(idiomaticPart.size()));
                idiomaticStory = idiomaticStory.replace("A", A);
                idiomaticStory = idiomaticStory.replace("B", B);
                System.out.println(idiomaticStory);
                idiomaticPart = IDIOMATIC.getFieldValues("Idiomatic Forms", (String)verbs.get(roll(verbs.size())));
                idiomaticStory = (String)idiomaticPart.get(roll(idiomaticPart.size()));
                idiomaticStory = idiomaticStory.replace("A", A);
                idiomaticStory = idiomaticStory.replace("B", B);
                System.out.println(idiomaticStory);
                idiomaticPart = IDIOMATIC.getFieldValues("Idiomatic Forms", (String)verbs.get(roll(verbs.size())));
                idiomaticStory = (String)idiomaticPart.get(roll(idiomaticPart.size()));
                idiomaticStory = idiomaticStory.replace("A", A);
                idiomaticStory = idiomaticStory.replace("B", B);
                System.out.println(idiomaticStory);
                idiomaticPart = IDIOMATIC.getFieldValues("Idiomatic Forms", (String)verbs.get(roll(verbs.size())));
                idiomaticStory = (String)idiomaticPart.get(roll(idiomaticPart.size()));
                idiomaticStory = idiomaticStory.replace("A", A);
                idiomaticStory = idiomaticStory.replace("B", B);
                System.out.println(idiomaticStory);
                idiomaticPart = IDIOMATIC.getFieldValues("Idiomatic Forms", (String)verbs.get(roll(verbs.size())));
                idiomaticStory = (String)idiomaticPart.get(roll(idiomaticPart.size()));
                idiomaticStory = idiomaticStory.replace("A", A);
                idiomaticStory = idiomaticStory.replace("B", B);
                System.out.println(idiomaticStory);
                System.out.println(endingStory);

                wordBank.add(A);

                break;
            }
            catch (NullPointerException e) {
                continue;
            }
        }



        
        
    }

}
