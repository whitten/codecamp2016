package twitterbotics;

import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;
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
    private KnowledgeBaseModule INTERCAT       = null;
    private KnowledgeBaseModule IDIOMATIC       = null;
    private Vector wordBank = new Vector();

    private String A = null;
    private String B = null;

    public Vector getWordBank () {
        return wordBank;
    }

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

    public void randomEventInvoker (int chance, int action) {
        if (roll(chance) != 0) return;
        if (action == 0) addLocalization();
        else if (action == 1) addCollaborator();
    }

    public void addLocalization () {
        String localization = null;
        Integer randNum = roll(3) + 1;
        int j = 0;
        while (localization == null && j < 5) {
            if (roll(2) == 0) {
                localization = NOC.getFirstValue("Address " + randNum.toString(), A);
                if (roll(3) == 0)
                    if (!getWehicle(A).equals("")) {
                        wordBank.add(A);
                        wordBank.add(getWehicle(A));
                    }
            }
            else {
                localization = NOC.getFirstValue("Address " + randNum.toString(), B);
                if (roll(3) == 0)
                    if (!getWehicle(B).equals("")) {
                        wordBank.add(B);
                        wordBank.add(getWehicle(B));
                    }
            }
            ++j;
        }
        if (localization == null) wordBank.add(NOC.getFirstValue("Address 1", B));
        wordBank.add(localization);
    }

    public String getWehicle (String person) {
        String wehicle = NOC.getFirstValue("Vehicle of Choice", person);
        return wehicle != null ? wehicle : "";
    }

    public void addCollaborator () {
        String collab;
        if (roll(2) == 0) {
            collab = NOC.getFirstValue("Opponent", A);
            if (collab != null) {
                wordBank.add(B);
                wordBank.add("collaborated");
                wordBank.add(collab);
            }
        }
        else {
            collab = NOC.getFirstValue("Opponent", B);
            if (collab != null) {
                wordBank.add(A);
                wordBank.add("collaborated");
                wordBank.add(collab);
            }
        }
    }

    public void generateAntagonists() {

        while(true) {
            try {
                attributeFields.add("Negative Talking Points");
                attributeFields.add("Positive Talking Points");


                String[] idiomaticStories = new String[3];
                A = (String) allPeople.get(roll(allPeople.size()));
                wordBank.clear();

                Vector attributeFields2 = NOC.getSimilarConcepts(A, attributeFields);
                B = (String)attributeFields2.get(roll(attributeFields2.size()));

                Vector firstPersonCat = NOC.getFieldValues("Category", A);
                Vector secondPersonCat = NOC.getFieldValues("Category", B);
                addLocalization();

                Vector tmp1;
                Vector tmp2;
                Vector intersect = new Vector();
                String randEvWord = "";

                for (int i = 0; i < firstPersonCat.size(); ++i) {
                    for (int j =  0; j < secondPersonCat.size(); ++j) {
                        tmp1 = INTERCAT.getAllKeysWithFieldValue("Subject", (String)firstPersonCat.get(i));
                        tmp2 = INTERCAT.getAllKeysWithFieldValue("Subject", (String)secondPersonCat.get(j));
                        intersect = INTERCAT.intersect(tmp1, tmp2);
                        if (intersect.size() < 1) continue;
                        i = firstPersonCat.size();
                        j = secondPersonCat.size();
                    }
                }

                if (intersect.size() < 1) continue;



                Vector verbs = INTERCAT.getFieldValues("Verbs", (String)intersect.get(roll(intersect.size())));
                String verb = (String)verbs.get(roll(verbs.size()));
                String normativeForm = IDIOMATIC.getFirstValue("Normative Form", verb)
                        .replace("A ", "").replace(" B", "").replace(" A", "").replace("B ", "");
                wordBank.add(A);
                wordBank.add(normativeForm);
                wordBank.add(B);
                Vector initStoryVec = INIT.getFieldValues("Establishing Action", verb);
                String initStory = (String)initStoryVec.get(roll(initStoryVec.size()));
                initStory = initStory.replace("B", "BZZ");
                initStory = initStory.replace("A", A);
                initStory = initStory.replace("BZZ", B);



                Vector idiomaticVerbs = new Vector();
                for (int i = 0; i < intersect.size(); ++i) {
                    idiomaticVerbs.addAll(INTERCAT.getFieldValues("Verbs", (String)intersect.get(i)));
                }


                for (int i = 0; (idiomaticVerbs.isEmpty() || i < 3); ++i) {
                    int whichVerb = roll(idiomaticVerbs.size());
                    verb = (String)idiomaticVerbs.get(whichVerb);
                    idiomaticVerbs.remove(whichVerb);


                    Vector idiomaticPart = IDIOMATIC.getFieldValues("Idiomatic Forms", verb);
                    int idiomaticCounter = roll(idiomaticPart.size());
                    String idiomaticStory = (String)idiomaticPart.get(idiomaticCounter);
                    normativeForm = IDIOMATIC.getFirstValue("Normative Form", verb)
                            .replace("A ", "").replace(" B", "").replace(" A", "").replace("B ", "");
                    wordBank.add(normativeForm);
                    idiomaticStory = idiomaticStory.replace("B", "BZZ");
                    idiomaticStory = idiomaticStory.replace("A", A);
                    idiomaticStory = idiomaticStory.replace("BZZ", B);
                    idiomaticStories [i]=idiomaticStory;
                    randomEventInvoker(2, 0);
                    randomEventInvoker(3, 1);
                }

                verbs = INTERCAT.getFieldValues("Verbs", (String)intersect.get(roll(intersect.size())));
                String endVerb = (String)verbs.get(roll(verbs.size()));
                Vector endingStoryVec = ENDING.getFieldValues("Closing Action", endVerb);

                String endingStory = (String)endingStoryVec.get(roll(endingStoryVec.size()));
                normativeForm = IDIOMATIC.getFirstValue("Normative Form", endVerb)
                        .replace("A ", "").replace(" B", "").replace(" A", "").replace("B ", "");
                wordBank.add(normativeForm);
                endingStory = endingStory.replace("B", "XYZZZZZ");
                endingStory = endingStory.replace("A", A);
                endingStory = endingStory.replace("XYZZZZZ", B);
                //System.out.println(wordBank);


                initStory = initStory.replace("\"","");

                endingStory = endingStory.replace("\"","");

                System.out.println(initStory+ ". ");
                for (int i=0; i<3;i++)
                {
                	idiomaticStories[i] = idiomaticStories[i].replace("\"","");

                	System.out.println(idiomaticStories[i] + ". ");

                }


                System.out.println(endingStory+ '.');


                break;
            }
            catch (NullPointerException e) {
                continue;
            }
        }



        
        
    }

}
