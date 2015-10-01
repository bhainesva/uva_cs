import analyzer.DocAnalyzer;

/**
 * Created by ben on 9/24/15.
 */
public class Main {
    public static void main (String[] args) {
        DocAnalyzer myAnal = new DocAnalyzer();
        myAnal.LoadJson("data/samples/query.json");
        //myAnal.LoadDirectory("data/yelp", "json");
    }
}
