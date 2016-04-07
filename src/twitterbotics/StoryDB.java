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


    public StoryDB(String kbDirectory) {
        knowledgeDir = kbDirectory;

        NOC = new KnowledgeBaseModule(knowledgeDir + "Veale's The NOC List.txt", 0);
        ANTONYMS = new KnowledgeBaseModule(knowledgeDir + "antonyms.txt", 0);
        NEG_QUALITIES = NOC.getInvertedField("Negative Talking Points");
        INIT = new KnowledgeBaseModule(knowledgeDir + "Veale's initial bookend actions.txt", 0);
        ENDING = new KnowledgeBaseModule(knowledgeDir + "Veale's closing bookend actions.txt", 0);
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
}
