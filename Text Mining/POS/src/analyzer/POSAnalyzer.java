/**
 *
 */
package analyzer;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

import edu.stanford.nlp.math.DoubleAD;
import edu.stanford.nlp.sequences.ListeningSequenceModel;
import edu.stanford.nlp.trees.international.arabic.ArabicTreeReaderFactory;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.porterStemmer;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import structures.*;
import sun.util.resources.cldr.zh.CalendarData_zh_Hans_HK;

import javax.print.Doc;
import javax.sql.rowset.CachedRowSet;

/**
 * @author hongning
 * Sample codes for demonstrating OpenNLP package usage
 * NOTE: the code here is only for demonstration purpose,
 * please revise it accordingly to maximize your implementation's efficiency!
 */
public class POSAnalyzer {
    //N-gram to be created
    int m_N;

    //a list of stopwords
    HashSet<String> m_stopwords;

    //you can store the loaded reviews in this arraylist for further processing
    ArrayList<Post> m_reviews;

    //you might need something like this to store the counting statistics for validating Zipf's and computing IDF
    HashMap<String, Token> m_stats;

    //we have also provided a sample implementation of language model in src.structures.LanguageModel
    Tokenizer m_tokenizer;

    //this structure is for language modeling
    LanguageModel m_langModel;

    HashMap<String, HashMap<String, Double>> tagToTag = new HashMap<>();
    HashMap<String, HashMap<String, Double>> wordToTag = new HashMap<>();
    HashMap<String, HashMap<String, Double>> tagToWord = new HashMap<>();

    public Double[] potentialValues = {.01, .1, .5, 2.0, 5.0, 10.0};
    public double delta = 0.5;
    public double sigma = 0.1;

    public List<String> allTags = new ArrayList<>();
    public List<String> possibleTags = new ArrayList<>();

    HashMap<Integer, HashSet<Document>> m_folds = new HashMap<>();

    HashMap<Double, HashMap<Double, ArrayList<Double>>> JJResults = new HashMap<>();
    HashMap<Double, HashMap<Double, ArrayList<Double>>> NNPResults = new HashMap<>();
    HashMap<Double, HashMap<Double, ArrayList<Double>>> NNResults = new HashMap<>();
    HashMap<Double, HashMap<Double, ArrayList<Double>>> VBResults = new HashMap<>();
    HashMap<Double, HashMap<Double, ArrayList<Double>>> OverallResults = new HashMap<>();

    ArrayList<HashMap<String, HashMap<String, Double>>> confusions = new ArrayList<>();


    public POSAnalyzer(String tokenModel, int N) throws InvalidFormatException, FileNotFoundException, IOException {
        m_N = N;
        m_reviews = new ArrayList<Post>();
        m_tokenizer = new TokenizerME(new TokenizerModel(new FileInputStream(tokenModel)));
        for (int i = 0; i < 5; i++) {
            m_folds.put(i, new HashSet<>());
        }
    }

    //sample code for loading a list of stopwords from file
    //you can manually modify the stopword file to include your newly selected words
    public void LoadStopwords(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            String line;

            while ((line = reader.readLine()) != null) {
                //it is very important that you perform the same processing operation to the loaded stopwords
                //otherwise it won't be matched in the text content
                line = SnowballStemming(Normalization(line));
                if (!line.isEmpty())
                    m_stopwords.add(line);
            }
            reader.close();
            System.out.format("Loading %d stopwords from %s\n", m_stopwords.size(), filename);
        } catch(IOException e){
            System.err.format("[Error]Failed to open file %s!!", filename);
        }
    }

    public void analyzeDocument(String doc) {
        Random random = new Random();
        ArrayList<String> tokens = new ArrayList<>();
        ArrayList<Tag> tags = new ArrayList<>();

        ArrayList<String> prevTags = new ArrayList<>();
        prevTags.add("START");
        doc = doc.replaceAll("[\\[\\]]","");
        doc = doc.trim();
        String[] split = doc.split("\\s+");
        for (String pair : split) {
            int p = pair.lastIndexOf("/");
            String currentWord = pair.substring(0, p);
//            currentWord = SnowballStemming(Normalization(currentWord));
            currentWord = Normalization(currentWord);
            String[] currentTagsArray = pair.substring(p+1).split("\\|");
            ArrayList<String> currentTags = new ArrayList<String>(Arrays.asList(currentTagsArray));
            if (currentWord.equals("") || currentTags.contains("$") || currentTags.contains(",") || currentTags.contains("UH") || currentTags.contains("FW") || currentTags.contains("LS") || currentTags.contains("WP$") || currentTags.contains("RBS") || currentTags.contains("PDT") || currentTags.contains("RP") || currentTags.contains("EX")) {
                continue;
            }

            tokens.add(currentWord);
            tags.add(new Tag(currentTags));

            for (String prevTag : prevTags) {
                if (!allTags.contains(prevTag)) {
                    allTags.add(prevTag);
                }

                // Count number of times tag occurs as a previous tag
                if (!tagToTag.containsKey(prevTag)) {
                    HashMap<String, Double> map = new HashMap<>();
                    map.put("count", 1.0);
                    tagToTag.put(prevTag, map);
                }
                else {
                    tagToTag.get(prevTag).put("count", tagToTag.get(prevTag).get("count") + 1);
                }

                for (String currentTag : currentTags) {
                    if (tagToTag.get(prevTag).containsKey(currentTag)) {
                        tagToTag.get(prevTag).put(currentTag, tagToTag.get(prevTag).get(currentTag) + 1);
                    }
                    else {
                        tagToTag.get(prevTag).put(currentTag, 1.0);
                    }
                }
            }
            prevTags = currentTags;

            for (String currentTag : currentTags) {
                if (!tagToWord.containsKey(currentTag)) {
                    HashMap<String, Double> map = new HashMap<>();
                    map.put(currentWord, 1.0);
                    map.put("count", 1.0);
                    tagToWord.put(currentTag, map);
                }

                else {
                    tagToWord.get(currentTag).put("count", tagToWord.get(currentTag).get("count") + 1);

                    if (tagToWord.get(currentTag).containsKey(currentWord)) {
                        tagToWord.get(currentTag).put(currentWord, tagToWord.get(currentTag).get(currentWord) + 1);
                    }
                    else {
                        tagToWord.get(currentTag).put(currentWord, 1.0);
                    }
                }
            }
        }
        Integer fold = random.nextInt(5);
        Document document = new Document(tokens, tags);
        m_folds.get(fold).add(document);
    }

     //sample code for loading a json file
    public String LoadPOS(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            StringBuffer buffer = new StringBuffer(1024);
            String line = reader.readLine();
            buffer.append(line);

            while ((line = reader.readLine()) != null) {
                if (line.equals("======================================")) {
                    continue;
                }
                else {
                    buffer.append(" " + line);
                }
            }
            reader.close();

            return buffer.toString();
        } catch (IOException e) {
            System.err.format("[Error]Failed to open file %s!", filename);
            e.printStackTrace();
            return null;
        }
    }

    // sample code for demonstrating how to recursively load files in a directory
    public void LoadDirectory(String folder, String suffix) {
        File dir = new File(folder);
        int size = m_reviews.size();
        for (File f : dir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(suffix))
                analyzeDocument(LoadPOS(f.getAbsolutePath()));
            else if (f.isDirectory())
                LoadDirectory(f.getAbsolutePath(), suffix);
        }
        size = m_reviews.size() - size;
        Collections.sort(allTags);
        possibleTags = new ArrayList(allTags);
        possibleTags.remove("START");
        System.out.println("Loading " + size + " review documents from " + folder);
    }

    //sample code for demonstrating how to use Snowball stemmer
    public String SnowballStemming(String token) {
        SnowballStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(token);
        if (stemmer.stem())
            return stemmer.getCurrent();
        else
            return token;
    }

    //sample code for demonstrating how to use Porter stemmer
    public String PorterStemming(String token) {
        porterStemmer stemmer = new porterStemmer();
        stemmer.setCurrent(token);
        if (stemmer.stem())
            return stemmer.getCurrent();
        else
            return token;
    }

    //sample code for demonstrating how to perform text normalization
    //you should implement your own normalization procedure here
    public String Normalization(String token) {
        // remove all non-word characters
        // please change this to removing all English punctuation
        token = token.replaceAll("\\W+", "");

        // convert to lower case
        token = token.toLowerCase();

        // add a line to recognize integers and doubles via regular expression
        // and convert the recognized integers and doubles to a special symbol "NUM"

        return token;
    }

    String[] Tokenize(String text) {
        return m_tokenizer.tokenize(text);
    }

    public void TokenizerDemon(String text) {
        System.out.format("Token\tNormalization\tSnonball Stemmer\tPorter Stemmer\n");
        for(String token:m_tokenizer.tokenize(text)){
            System.out.format("%s\t%s\t%s\t%s\n", token, Normalization(token), SnowballStemming(token), PorterStemming(token));
        }
    }

    public Double Transition(String given, String next) {
        double count = tagToTag.get(given).containsKey(next) ? tagToTag.get(given).get(next) : 0.0;
        double numerator = count + delta;
        double denominator = tagToTag.get(given).get("count") + delta * tagToTag.get(given).get("count");
        double result = numerator / denominator;
        return result;
    }

    public Double Emission(String word, String tag) {
        double count = tagToWord.get(tag).containsKey(word) ? tagToWord.get(tag).get(word) : 0.0;
        double numerator = count + sigma;
        double denominator = tagToWord.get(tag).get("count") + sigma * tagToWord.get(tag).get("count");
        double result = numerator / denominator;
        return result;
    }

    int maxCol(TrellisItem[][] trellis, int row) {
        int maxj = 0;
        double max = trellis[row][0].value;// assign 1st value of the column as max
        for (int i = 0; i < trellis[row].length; i++) { // i should be your column
            if (trellis[row][i].value > max){ // check the column elements instead of row elements
                max = trellis[row][i].value;// get the column values
                maxj = i;
            }
        }
        return maxj;
    }

    public List<String> Viterbi(List<String> wordSequence) {
        TrellisItem[][] trellis = new TrellisItem[wordSequence.size()][possibleTags.size()];
        ArrayList tagSequence = new ArrayList();

        //Initialize first column
        for (int j = 0; j < trellis[0].length; j++) {
            trellis[0][j] = new TrellisItem(Math.log(Emission(wordSequence.get(0), possibleTags.get(j)) * Transition("START", possibleTags.get(j))));
//            if (trellis[0][j].value > 0) {
//                trellis[0][j] = new TrellisItem(Math.log(Emission(wordSequence.get(0), possibleTags.get(j)) * Transition("START", possibleTags.get(j))));
//            }
        }

        //Fill out remaining columns
        for (int i = 1; i < trellis.length; i++) {
            for (int j = 0; j < trellis[0].length; j++) {
                double maxProb = trellis[i - 1][0].value + Math.log(Transition(possibleTags.get(0), possibleTags.get(j)) * Emission(wordSequence.get(i), possibleTags.get(j)));
                int maxTag = 0;
                for (int y0=0; y0 < possibleTags.size(); y0++) {
                    double prob = trellis[i - 1][y0].value + Math.log(Transition(possibleTags.get(y0), possibleTags.get(j)) * Emission(wordSequence.get(i), possibleTags.get(j)));
                    if (prob > 0) {
                        prob = trellis[i - 1][y0].value + Math.log(Transition(possibleTags.get(y0), possibleTags.get(j)) * Emission(wordSequence.get(i), possibleTags.get(j)));
                    }
                    if (prob > maxProb) {
                        maxProb = prob;
                        if (maxProb == Double.POSITIVE_INFINITY) {
                            System.out.println("That's a problem");
                        }
                        maxTag = y0;
                    }
                }
                trellis[i][j] = new TrellisItem(maxProb, maxTag);
            }
        }

        int maxj = maxCol(trellis, trellis.length-1);
        tagSequence.add(possibleTags.get(maxj));
        for (int i = trellis.length - 1; i > 0; i--) {
            maxj = trellis[i][maxj].pointer;
            tagSequence.add(0, possibleTags.get(maxj));
        }

        return tagSequence;
    }

    public List<String> Viterbi(Document doc) {
        return Viterbi(doc.getTokens());
    }


    public void TrainExcluding(int i) {
        tagToTag = new HashMap<>();
        tagToWord = new HashMap<>();
        for (String tag : allTags) {
            HashMap<String, Double> map = new HashMap<>();
            map.put("count", 0.0);
            tagToTag.put(tag, map);
            HashMap<String, Double> map2 = new HashMap<>();
            map2.put("count", 0.0);
            tagToWord.put(tag, map2);
        }

        for (int j = 0; j < 5; j++) {
            if (j == i) {
                continue;
            }

            for (Document doc : m_folds.get(j)) {
                Tag prevTags = new Tag("START");

                for (int index = 0; index < doc.size(); index++) {
                    String currentWord = doc.getToken(index);
                    Tag currentTags = doc.getTag(index);

                    for (String prevTag : prevTags.getTags()) {
                        tagToTag.get(prevTag).put("count", tagToTag.get(prevTag).get("count") + 1);

                        for (String currentTag : currentTags.getTags()) {
                            if (tagToTag.get(prevTag).containsKey(currentTag)) {
                                tagToTag.get(prevTag).put(currentTag, tagToTag.get(prevTag).get(currentTag) + 1);
                            }
                            else {
                                tagToTag.get(prevTag).put(currentTag, 1.0);
                            }
                        }
                    }
                    prevTags = currentTags;

                    for (String currentTag : currentTags.getTags()) {
                        tagToWord.get(currentTag).put("count", tagToWord.get(currentTag).get("count") + 1);

                        if (tagToWord.get(currentTag).containsKey(currentWord)) {
                            tagToWord.get(currentTag).put(currentWord, tagToWord.get(currentTag).get(currentWord) + 1);
                        }
                        else {
                            tagToWord.get(currentTag).put(currentWord, 1.0);
                        }
                    }
                }
            }
        }
    }

    public void CrossValidate() throws FileNotFoundException, UnsupportedEncodingException {
        for (int i = 0; i < 5; i++) {
            HashMap<String, HashMap<String, Double>> results = new HashMap<>();
            HashMap<String, HashMap<String, Double>> confusion = new HashMap<>();
            for (String tag : possibleTags) {
                HashMap<String, Double> map = new HashMap<>();
                for (String t : possibleTags) {
                    map.put(t, 0.0);
                }

                confusion.put(tag, map);
            }
            for (String tag : possibleTags) {
                HashMap<String, Double> map = new HashMap<>();
                map.put("TP", 0.0);
                map.put("FP", 0.0);
                map.put("TN", 0.0);
                map.put("FN", 0.0);
                results.put(tag, map);
            }

            HashSet<Document> evalSet = m_folds.get(i);
            TrainExcluding(i);

            for (Document doc : evalSet) {
                List<String> predTags = Viterbi(doc);
                for (int idx = 0; idx < predTags.size(); idx++) {
                    Tag actualTag = doc.getTag(idx);
                    String predTag = predTags.get(idx);
                    confusion.get(actualTag.getTags().iterator().next()).put(predTag, confusion.get(actualTag.getTags().iterator().next()).get(predTag) + 1);
                    if (actualTag.equals(predTag)) {
                       results.get(predTag).put("TP", results.get(predTag).get("TP") + 1);
                        for (String tag : possibleTags) {
                            if (!tag.equals(predTag)) {
                                results.get(tag).put("TN", results.get(tag).get("TN") + 1);
                            }
                        }
                    }
                    else {
                        results.get(predTag).put("FP", results.get(predTag).get("FP") + 1);
                        for (String tag : possibleTags) {
                            if (actualTag.equals(tag)) {
                                results.get(tag).put("FN", results.get(tag).get("FN") + 1);
                            }
                            else if (!tag.equals(predTag)){
                                results.get(tag).put("TN", results.get(tag).get("TN") + 1);
                            }
                        }
                    }
                }
            }
            confusions.add(confusion);

            PrintWriter resultWriter = new PrintWriter("fold" + i + "d=" + delta + "s=" + sigma + ".csv", "UTF-8");
            double totalTP = 0.0;
            double totalFP = 0.0;
            double totalTN = 0.0;
            double totalFN = 0.0;

            for (String tag : results.keySet()) {
                resultWriter.println(tag);
                Double tp = results.get(tag).get("TP");
                Double fp = results.get(tag).get("FP");
                Double tn = results.get(tag).get("TN");
                Double fn = results.get(tag).get("FN");
                Double recall = tp/(tp + fn);
                Double precision = tp/(tp + fp);
                Double f1 = 2/((1/precision) + (1/recall));
                totalTP += tp;
                totalFP += fp;
                totalTN += tn;
                totalFN += fn;


                resultWriter.println("TP," + tp);
                resultWriter.println("FP," + fp);
                resultWriter.println("TN," + tn);
                resultWriter.println("FN," + fn);
                resultWriter.println("PRECISION:," + precision);
                resultWriter.println("RECALL:," + recall);
                resultWriter.println("F1:," + f1);
                resultWriter.println();

                if (tag.equals("JJ"))
                    JJResults.get(delta).get(sigma).add(f1);
                if (tag.equals("NN"))
                    NNResults.get(delta).get(sigma).add(f1);
                if (tag.equals("NNP"))
                    NNPResults.get(delta).get(sigma).add(f1);
                if (tag.equals("VB"))
                    VBResults.get(delta).get(sigma).add(f1);
            }

            resultWriter.println("TOTALS");
            resultWriter.println("TP," + totalTP);
            resultWriter.println("FP," + totalFP);
            resultWriter.println("TN," + totalTN);
            resultWriter.println("FN," + totalFN);
            Double totalrecall = totalTP/(totalTP + totalFN);
            Double totalprecision = totalTP/(totalTP + totalFP);
            Double totalf1 = 2/((1/totalprecision) + (1/totalrecall));
            resultWriter.println("ACCURACY:," + (totalTN + totalTP)/(totalTP + totalTN + totalFP + totalFN));
            resultWriter.println("PRECISION:," + totalprecision);
            resultWriter.println("RECALL:," + totalrecall);
            resultWriter.println("F1:," + totalf1);
            OverallResults.get(delta).get(sigma).add(totalf1);
            resultWriter.close();
        }
    }

    public void printConfusions() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter resultWriter = new PrintWriter("folds.confusiond=" + delta + "s=" + sigma + ".csv", "UTF-8");
        for (int i = 0; i < confusions.size(); i++) {
            HashMap<String, HashMap<String, Double>> confusion = confusions.get(i);
            resultWriter.println("Fold: " + i);
            resultWriter.print("X");
            for (String tag : possibleTags) {
                resultWriter.print("," + tag);
            }
            resultWriter.print("\n");
            for (String tag : possibleTags) {
                resultWriter.print(tag);
                for (String t : possibleTags) {
                    resultWriter.print("," + confusion.get(tag).get(t));
                }
                resultWriter.print("\n");
            }

            resultWriter.println("\n");
        }
        resultWriter.close();
    }

    public void printCumulativeResults() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter resultWriter = new PrintWriter("cumulative.csv", "UTF-8");
        for (Double deltaval : JJResults.keySet()) {
            for (Double sigmaval : JJResults.get(deltaval).keySet()) {
                resultWriter.println("delta: " + deltaval + ", sigma: " + sigmaval);
                ArrayList<Double> list = JJResults.get(deltaval).get(sigmaval);
                resultWriter.print("JJ");
                for (Double f : list) {
                    resultWriter.print("," + f);
                }
                resultWriter.print("\n");
                list = NNResults.get(deltaval).get(sigmaval);
                resultWriter.print("NN");
                for (Double f : list) {
                    resultWriter.print("," + f);
                }
                resultWriter.print("\n");
                list = NNPResults.get(deltaval).get(sigmaval);
                resultWriter.print("NNP");
                for (Double f : list) {
                    resultWriter.print("," + f);
                }
                resultWriter.print("\n");
                list = VBResults.get(deltaval).get(sigmaval);
                resultWriter.print("VB");
                for (Double f : list) {
                    resultWriter.print("," + f);
                }
                resultWriter.print("\n");
                list = OverallResults.get(deltaval).get(sigmaval);
                resultWriter.print("Overall");
                for (Double f : list) {
                    resultWriter.print("," + f);
                }
                resultWriter.print("\n");
            }
        }
        resultWriter.close();
    }
    public static void main(String[] args) throws InvalidFormatException, FileNotFoundException, IOException {
        POSAnalyzer analyzer = new POSAnalyzer("./data/Model/en-token.bin", 2);

        //entry point to deal with a collection of documents
        analyzer.LoadDirectory("./data/tagged", ".pos");

        //Most Likely Tag to Follow VB
        for (String tag : analyzer.tagToTag.get("VB").keySet()) {
            System.out.println(tag + ": " + analyzer.tagToTag.get("VB").get(tag));
        }

//        Most Likely Word for NN
//        for (String word : analyzer.wordToTag.keySet()) {
//            if (analyzer.wordToTag.get(word).containsKey("NN")) {
//                System.out.println(word + ": " + analyzer.wordToTag.get(word).get("NN"));
//            }
//        }

//        Most Likely Word for NN
        for (String word : analyzer.tagToWord.get("NN").keySet()) {
                System.out.println(word + ": " + analyzer.tagToWord.get("NN").get(word));
        }

//        for (double deltaVal : analyzer.potentialValues) {
//            analyzer.delta = deltaVal;
//            HashMap<Double, ArrayList<Double>> map = new HashMap<>();
//            analyzer.JJResults.put(analyzer.delta, map);
//            analyzer.NNPResults.put(analyzer.delta, new HashMap<>(map));
//            analyzer.NNResults.put(analyzer.delta, new HashMap<>(map));
//            analyzer.VBResults.put(analyzer.delta, new HashMap<>(map));
//            analyzer.OverallResults.put(analyzer.delta, new HashMap<>(map));
//            for (double sigVal : analyzer.potentialValues) {
//                analyzer.sigma = sigVal;
//                analyzer.JJResults.get(analyzer.delta).put(analyzer.sigma, new ArrayList<>());
//                analyzer.NNPResults.get(analyzer.delta).put(analyzer.sigma, new ArrayList<>());
//                analyzer.NNResults.get(analyzer.delta).put(analyzer.sigma, new ArrayList<>());
//                analyzer.VBResults.get(analyzer.delta).put(analyzer.sigma, new ArrayList<>());
//                analyzer.OverallResults.get(analyzer.delta).put(analyzer.sigma, new ArrayList<>());
//            }
//        }
//
//        for (double deltaVal : analyzer.potentialValues) {
//            analyzer.delta = deltaVal;
//           for (double sigVal : analyzer.potentialValues) {
//               analyzer.sigma = sigVal;
//               analyzer.CrossValidate();
//               analyzer.printConfusions();
//               analyzer.confusions.clear();
//           }
//        }
//        analyzer.printCumulativeResults();

    }

}

