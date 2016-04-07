package twitterbotics;

import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

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



    public StoryDB(String kbDirectory) {
        knowledgeDir = kbDirectory;

        NOC = new KnowledgeBaseModule(knowledgeDir + "Veale's The NOC List.txt", 0);
        ANTONYMS = new KnowledgeBaseModule(knowledgeDir + "antonyms.txt", 0);
        NEG_QUALITIES = NOC.getInvertedField("Negative Talking Points");
        INIT = new KnowledgeBaseModule(knowledgeDir + "Veale's initial bookend actions.txt", 0);
        ENDING = new KnowledgeBaseModule(knowledgeDir + "Veale's closing bookend actions.txt", 0);
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

    }

    public void generateAntagonistsv2() {

        attributeFields.add("Negative Talking Points");
        attributeFields.add("Positive Talking Points");
        String A = (String)allPeople.get(roll(allPeople.size()));
        Vector attributeFields2 = NOC.getSimilarConcepts(A, attributeFields);
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(attributeFields2.size());
        Object objs = attributeFields2.get(randomInt);
        System.out.println(A + ", " + objs + " ");
        System.out.println(NOC.getFieldValues("Category", objs.toString()));
        System.out.println(NOC.getFieldValues("Category", A));
    }

}
