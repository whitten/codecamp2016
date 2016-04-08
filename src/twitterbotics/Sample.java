package twitterbotics;

public class Sample {
    public static void main (String[] args) {

		String kdir = "D:/TSV Lists/";
		String tdir = "D:/tdir/";
		StoryDB storyDB = new StoryDB(kdir);
		storyDB.generateAntagonists();
	}
}
