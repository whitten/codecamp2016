package twitterbotics;

public class Sample {
    public static void main (String[] args) {
    	String path = "D:/TSV Lists/";
    	//path = path.replaceAll("\\", "/");
    	KnowledgeBaseModule NOC          = new KnowledgeBaseModule(path + "Veale's The NOC List.txt", 0);
        KnowledgeBaseModule CATEGORIES   = new KnowledgeBaseModule(path + "Veale's Category Hierarchy.txt", 0);
        
    }
}
