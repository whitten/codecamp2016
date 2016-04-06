package twitterbotics;

public class Sample {
    public static void main (String[] args) {
	String kdir=args[0];
        KnowledgeBaseModule NOC          = new KnowledgeBaseModule(kdir + "Veale's The NOC List.txt", 0);
        KnowledgeBaseModule CATEGORIES   = new KnowledgeBaseModule(kdir + "Veale's Category Hierarchy.txt", 0);
    }
}
