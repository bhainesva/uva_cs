/**
 *
 */
package analyzer;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.porterStemmer;
import structures.LanguageModel;
import structures.Post;
import structures.Token;

import java.io.*;
import java.util.*;

public class NewAnalyzer {
    //Tools
    Tokenizer tokenizer;
    SnowballStemmer stemmer;

    //a list of stopwords
    HashSet<String> m_stopwords;

    //Set of all (normalized/stemmed) tokens encountered
    ArrayList<Token> vocabulary;
    ArrayList<String> all_tokens;
    ArrayList<String> unigrams;

    //you can store the loaded reviews in this arraylist for further processing
    ArrayList<Post> m_reviews;

    //you might need something like this to store the counting statistics for validating Zipf's and computing IDF
    //HashMap<String, Token> m_stats;

    //we have also provided sample implementation of language model in src.structures.LanguageModel

    public NewAnalyzer() throws IOException {
        m_reviews = new ArrayList<Post>();
        m_stopwords = new HashSet<>();
        vocabulary = new ArrayList<>();
        unigrams = new ArrayList<>();
        all_tokens = new ArrayList<>();
        tokenizer = new TokenizerME(new TokenizerModel(new FileInputStream("./data/Model/en-token.bin")));
        stemmer = new englishStemmer();
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

    public void analyzeDocument(JSONObject json) {
        try {
            JSONArray jarray = json.getJSONArray("Reviews");
            for(int i=0; i<jarray.length(); i++) {
                Post review = new Post(jarray.getJSONObject(i));

                /**
                 * HINT: perform necessary text processing here, e.g., tokenization, stemming and normalization
                 */
                String content = review.getContent();
                String[] tokens = tokenizer.tokenize(content);
                ArrayList<String> tokenList = new ArrayList<>();
                for (String token : tokens) {
                    String parsedToken = SnowballStemming(Normalization(token));
                    if (!parsedToken.isEmpty()) {
                        tokenList.add(parsedToken);
                        //m_tokens.add(new Token(parsedToken));
                    }
                }
                review.setTokens(tokenList);

                for (String str : tokenList) {
                    if (review.tokenToTF.containsKey(str)) {
                        review.tokenToTF.put(str, review.tokenToTF.get(str) + 1);
                    }
                    else {
                        review.tokenToTF.put(str, 1.0);
                    }
                }

                m_reviews.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void LMAnalyze(JSONObject json, LanguageModel lm) {
        try {
            JSONArray jarray = json.getJSONArray("Reviews");
            for(int i=0; i<jarray.length(); i++) {
                Post review = new Post(jarray.getJSONObject(i));

                /**
                 * HINT: perform necessary text processing here, e.g., tokenization, stemming and normalization
                 */
                String content = review.getContent();
                String[] tokens = tokenizer.tokenize(content);
                ArrayList<String> tokenList = new ArrayList<>();
                for (String token : tokens) {
                    String parsedToken = SnowballStemming(Normalization(token));
                    if (!parsedToken.isEmpty()) {
                        tokenList.add(parsedToken);
                    }
                }

                //get term frequencies for unigrams
                for (int j=0; j < tokenList.size(); j++) {
                    if (lm.m_model.containsKey(tokenList.get(j))) {
                        lm.m_model.get(tokenList.get(j)).setValue(lm.m_model.get(tokenList.get(j)).getValue() + 1);
                        lm.total_unigram_count += 1;
                    }
                    else {
                        Token tk = new Token(tokenList.get(j));
                        tk.setValue(1.0);
                        lm.m_model.put(tokenList.get(j), tk);
                        lm.unique_unigram_count += 1;
                        lm.total_unigram_count += 1;
                        all_tokens.add(tokenList.get(j));
                        unigrams.add(tokenList.get(j));
                        lm.followers.put(tokenList.get(j), new ArrayList<>());
                    }
                }

                //get term frequencies for bigrams
                for (int j=0; j < tokenList.size() - 1; j++) {
                    if (lm.m_model.containsKey(tokenList.get(j) + " " + tokenList.get(j+1))) {
                        lm.m_model.get(tokenList.get(j) + " " + tokenList.get(j+1)).setValue(lm.m_model.get(tokenList.get(j) + " " + tokenList.get(j+1)).getValue() + 1);
                        lm.total_bigram_count += 1;
                    }
                    else {
                        Token tk = new Token(tokenList.get(j) + " " + tokenList.get(j + 1));
                        tk.setValue(1.0);
                        lm.m_model.put(tokenList.get(j) + " " + tokenList.get(j+1), tk);
                        lm.unique_bigram_count += 1;
                        lm.total_bigram_count += 1;
                        all_tokens.add(tokenList.get(j) + " " + tokenList.get(j+1));
                        lm.followers.get(tokenList.get(j)).add(tokenList.get(j+1));
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //sample code for loading a json file
    public JSONObject LoadJson(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            StringBuffer buffer = new StringBuffer(1024);
            String line;

            while((line=reader.readLine())!=null) {
                buffer.append(line);
            }
            reader.close();

            return new JSONObject(buffer.toString());
        } catch (IOException e) {
            System.err.format("[Error]Failed to open file %s!", filename);
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            System.err.format("[Error]Failed to parse json file %s!", filename);
            e.printStackTrace();
            return null;
        }
    }

    // sample code for demonstrating how to recursively load files in a directory
    public void LoadDirectory(String folder, String suffix) {
        File dir = new File(folder);
        int size = m_reviews.size();
        for (File f : dir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(suffix)){
                analyzeDocument(LoadJson(f.getAbsolutePath()));
            }
            else if (f.isDirectory())
                LoadDirectory(f.getAbsolutePath(), suffix);
        }
        size = m_reviews.size() - size;
        System.out.println("Loading " + size + " review documents from " + folder);
    }

    //sample code for demonstrating how to use Snowball stemmer
    public String SnowballStemming(String token) {
        stemmer.setCurrent(token);
        stemmer.stem();
        return stemmer.getCurrent();
    }

    //sample code for demonstrating how to use Porter stemmer
    public String PorterStemmingDemo(String token) {
        porterStemmer stemmer = new porterStemmer();
        stemmer.setCurrent(token);
        if (stemmer.stem())
            return stemmer.getCurrent();
        else
            return token;
    }

    //sample code for demonstrating how to perform text normalization
    public String Normalization(String token) {
        // remove all English punctuation
        token = token.replaceAll("\\p{Punct}+", "");
        // convert to lower case
        token = token.toLowerCase();

        //Note that this doesn't attempt to recognize doubles
        //This is because all periods were removed in the previous step.
        //At this point in the processing a double looks the same as an int.
        token = token.replaceAll("\\d+", "NUM");

        return token;
    }

    public Map<String, Integer> TotalTermFreq() {
        Map<String, Integer> freqs = new HashMap<>();
        for (Post doc : m_reviews) {
            for (String token : doc.getTokens()) {
                if (freqs.containsKey(token)) {
                    freqs.put(token, freqs.get(token) + 1);
                } else {
                    freqs.put(token, 1);
                }

            }
        }

        return freqs;
    }

    public HashMap<String, Integer> DocumentFrequency() {
        //Stores DF for one word tokens
        HashMap<String, Integer> freqs = new HashMap<>();
        for (Post doc : m_reviews) {
            //Set<String> tokens = new HashSet<>(doc.getTokens());
            HashMap<String, Integer> docFreqs = new HashMap<>();
            ArrayList<String> tokens = doc.getTokens();
            for (String token : tokens) {
                if (docFreqs.containsKey(token)) {
                    docFreqs.put(token, docFreqs.get(token) + 1);
                }
                else {
                    docFreqs.put(token, 1);
                }
            }

            //Update DF score for one word tokens
            for (String str : docFreqs.keySet()) {
                doc.tokenToTF.put(str, (double) docFreqs.get(str));
                if (freqs.containsKey(str)) {
                    freqs.put(str, freqs.get(str) + 1);
                }
                else {
                    freqs.put(str, 1);
                }

            }
        }

        return freqs;
    }

    public ArrayList<String> tokensByFreq(final Map<String, Integer> freqs) {
        ArrayList<String> sorted = new ArrayList<>();

        for (String s : freqs.keySet()) {
            sorted.add(s);
        }

        Collections.sort(sorted, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return -freqs.get(s).compareTo(freqs.get(t1));
            }
        });

        return sorted;

    }

    //Compares tokens by value
    //Used to sort vocabulary
    //Where value is DF
    //Sorts from highest to lowest
    static class TokenComp implements Comparator<Token>{
        @Override
        public int compare(Token e1, Token e2) {
            if (e1.getDF() < e2.getDF()) {
                return 1;
            }
            else if (e1.getDF() > e2.getDF()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }

    public void CreateVocabulary() {
        //Freq stores DF for each token
        HashMap<String, Integer> docFreqs = new HashMap<>();
        for (Post p : m_reviews) {
            HashMap<String, Integer> docGrams = new HashMap<>();
            //1-Gram
            for (int i = 0; i <= p.getTokens().size()-1; i++) {
                String token = p.getTokens().get(i);
                if (docGrams.containsKey(token)) {
                    docGrams.put(token, docGrams.get(token) + 1);
                } else {
                    docGrams.put(token, 0);
                }
            }

            //2-Grams
            for (int i = 0; i <= p.getTokens().size()-2; i++) {
                String token = p.getTokens().get(i) + " " + p.getTokens().get(i+1);
                if (docGrams.containsKey(token)) {
                    docGrams.put(token, docGrams.get(token) + 1);
                } else {
                    docGrams.put(token, 0);
                }
            }

            for (String str : docGrams.keySet()) {
                if (docFreqs.containsKey(str)) {
                    docFreqs.put(str, docFreqs.get(str) + 1);
                } else {
                    docFreqs.put(str, 1);
                }
            }
        }

        for (String str: docFreqs.keySet()) {
            Token tkn = new Token(str);
            tkn.setDF(docFreqs.get(str));
            vocabulary.add(tkn);
        }
    }

    public void InitializeLM(LanguageModel lm, String location, String suffix) {
        // Load m_model
        File dir = new File(location);
        int size = m_reviews.size();
        for (File f : dir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(suffix)){
                LMAnalyze(LoadJson(f.getAbsolutePath()), lm);
            }
            else if (f.isDirectory())
                LoadDirectory(f.getAbsolutePath(), suffix);
        }
        System.out.println("Loading " + size + " review documents from " + location);


    }

    public static class LinearComparator implements Comparator<String> {
        LanguageModel lm;

        LinearComparator(LanguageModel lm) {
            this.lm = lm;
        }

        @Override
        public int compare(String o1, String o2) {
            double o1score = lm.linearInterpolation(o1, "good");
            double o2score = lm.linearInterpolation(o1, "good");
            if (o1score < o2score) {
                return -1;
            }
            else if (o1score == o2score) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }

    public static void main(String[] args) throws IOException, JSONException {
        //Initialize analyzer, load data
        NewAnalyzer analyzer = new NewAnalyzer();
        LanguageModel model = new LanguageModel(2);
        analyzer.InitializeLM(model, "./data/samples/yelp/train", "json");

        File absFile = new File("absoluteDiscountScore.txt");
        File linFile = new File("linearInterpolationScore.txt");
        PrintWriter absWriter = new PrintWriter(absFile);
        PrintWriter linWriter = new PrintWriter(linFile);
        for (String str : analyzer.unigrams) {
            linWriter.println(str + " : "  + model.linearInterpolation(str, "good"));
            absWriter.println(str + " : "  + model.absoluteDiscounting(str, "good"));
        }
        linWriter.close();
        absWriter.close();

        // Generate Shit
        File unigenfile = new File("unigramGenerated.txt");
        File absgenfile = new File("absoluteGenerated.txt");
        File lingenfile = new File("linearGenerated.txt");
        PrintWriter unigenWriter = new PrintWriter(unigenfile);
        PrintWriter lingenWriter = new PrintWriter(lingenfile);
        PrintWriter absgenWriter = new PrintWriter(absgenfile);

        //Unigram Model
        ArrayList<ArrayList<String>> sentences = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            ArrayList<String> sen = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                double prob = Math.random(); // prepare to perform uniform sampling
                for(String token:analyzer.unigrams) {
                    prob -= model.additiveSmoothedUnigramProb(token);
                    if (prob<=0) {
                        sen.add(token);
                        break;
                    }
                }
            }
            sentences.add(sen);
        }

        for (ArrayList<String> sen : sentences) {
            for (String word : sen) {
                unigenWriter.print(word + " ");
            }
            unigenWriter.println("");
        }

        //Bigrams - linear
        sentences = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            // Generate the first word from the smoothed unigram model
            ArrayList<String> sen = new ArrayList<String>();
            double prob = Math.random();
            double likelihood = 1.0;
            for(String token:analyzer.unigrams) {
                prob -= model.additiveSmoothedUnigramProb(token);
                if (prob<=0) {
                    sen.add(token);
                    likelihood = model.additiveSmoothedUnigramProb(token);
                    break;
                }
            }
            for (int i = 0; i < 14; i++) {
                prob = Math.random(); // prepare to perform uniform sampling
                for(String token:analyzer.unigrams) {
                    prob -= model.linearInterpolation(token, sen.get(sen.size()-1));
                    if (prob<=0) {
                        likelihood *= model.linearInterpolation(token, sen.get(sen.size()-1));
                        sen.add(token);
                        break;
                    }
                }
            }
            sen.add(String.valueOf(likelihood));
            sentences.add(sen);
        }

        for (ArrayList<String> sen : sentences) {
            for (String word : sen) {
                lingenWriter.print(word + " ");
            }
            lingenWriter.println("");
        }

        //Bigrams - absolute
        sentences = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            // Generate the first word from the smoothed unigram model
            ArrayList<String> sen = new ArrayList<String>();
            double prob = Math.random();
            double likelihood = 1.0;
            boolean wordAdded = false;
            for(String token:analyzer.unigrams) {
                prob -= model.additiveSmoothedUnigramProb(token);
                if (prob<=0) {
                    sen.add(token);
                    likelihood = model.additiveSmoothedUnigramProb(token);
                    wordAdded = true;
                    break;
                }
            }
            if (!wordAdded) {
                sen.add(analyzer.unigrams.get(analyzer.unigrams.size()-1));
            }

            for (int i = 0; i < 14; i++) {
                prob = Math.random(); // prepare to perform uniform sampling
                wordAdded = false;
                for(String token:analyzer.unigrams) {
                    prob -= model.linearInterpolation(token, sen.get(sen.size()-1));
                    if (prob<=0) {
                        likelihood *= model.absoluteDiscounting(token, sen.get(sen.size()-1));
                        sen.add(token);
                        wordAdded = true;
                        break;
                    }
                }
                if (!wordAdded) {
                    sen.add(analyzer.unigrams.get(analyzer.unigrams.size()-1));
                }
            }
            sen.add(String.valueOf(likelihood));
            sentences.add(sen);
        }

        for (ArrayList<String> sen : sentences) {
            for (String word : sen) {
                absgenWriter.print(word + " ");
            }
            absgenWriter.println("");
        }
        absgenWriter.close();
        lingenWriter.close();
        unigenWriter.close();

        //PERPLEXITY UP IN THIS BITCH
        analyzer.LoadDirectory("./data/samples/yelp/test", "json");

        ArrayList<Double> uni_perp = new ArrayList<>();
        ArrayList<Double> lin_perp = new ArrayList<>();
        ArrayList<Double> abs_perp = new ArrayList<>();

        for(Post doc : analyzer.m_reviews) {
            ArrayList<String> sen = doc.getTokens();
            if (sen.size() == 0) {
                System.out.println("You fucked it up");
                continue;
            }
            Double unip = Math.log(model.additiveSmoothedUnigramProb(sen.get(0)));
            Double linp = Math.log(model.additiveSmoothedUnigramProb(sen.get(0)));
            Double absp = Math.log(model.additiveSmoothedUnigramProb(sen.get(0)));
            Double len = (double)sen.size();

            for(int i = 1; i < sen.size(); i++) {
                unip += Math.log(model.additiveSmoothedUnigramProb(sen.get(i)));
                linp += Math.log(model.linearInterpolation(sen.get(i), sen.get(i-1)));
                absp += Math.log(model.absoluteDiscounting(sen.get(i), sen.get(i-1)));
            }

            unip = Math.pow(Math.E, (-1.0/len)*unip);
            linp = Math.pow(Math.E, (-1.0/len)*linp);
            if (((Double)Math.pow(Math.E, (-1.0/len)*absp)).isInfinite()) {
                System.out.println("Jack we have a mistake");
            }
            absp = Math.pow(Math.E, (-1.0/len)*absp);

            uni_perp.add(unip);
            lin_perp.add(linp);
            abs_perp.add(absp);
        }

        double[] uniarr = ArrayUtils.toPrimitive(uni_perp.toArray(new Double[uni_perp.size()]));
        double[] linarr = ArrayUtils.toPrimitive(lin_perp.toArray(new Double[lin_perp.size()]));
        double[] absarr = ArrayUtils.toPrimitive(abs_perp.toArray(new Double[abs_perp.size()]));

        double tot = 0.0;
        for (double d : uniarr) {
            tot += d;
        }
        double finalunimean = (tot/(double)uniarr.length);

        tot = 0.0;
        for (Double d : uniarr)
            tot += Math.pow((d - finalunimean), 2);
        double finalunistd = Math.sqrt( tot / ( uniarr.length - 1 ) );

        System.out.println("Unigram avg. perplexity: " + finalunimean);
        System.out.println("Unigram stddev: " + finalunistd);

        tot = 0.0;
        for (double d : linarr) {
            tot += d;
        }
        double finallinmean = (tot/(double)linarr.length);

        tot = 0.0;
        for (Double d : linarr)
            tot += Math.pow((d - finallinmean), 2);
        double finallinstd = Math.sqrt( tot / ( linarr.length - 1 ) );
        System.out.println("Linear avg. perplexity: " + finallinmean);
        System.out.println("Linear stddev: " + finallinstd);

        tot = 0.0;
        double max = 0.0;
        for (double d : absarr) {
            tot += d;
            if (d > max) {
                max = d;
            }
        }
        System.out.println("max: " + max);
        double finalabsmean = (tot/(double)absarr.length);

        tot = 0.0;
        for (Double d : absarr)
            tot += Math.pow((d - finalabsmean), 2);
        double finalabsstd = Math.sqrt( tot / ( absarr.length - 1 ) );
        System.out.println("Abs avg. perplexity: " + finalabsmean);
        System.out.println("Abs stddev: " + finalabsstd);



        //Part one
        //analyzer.LoadStopwords("./data/Model/english.stop");
        //analyzer.LoadDirectory("./data/samples/yelp/train", "json");

        //Part 1:
        //Get total term frequencies for all tokens
//        Map<String, Integer> totalTermFreqs = analyzer.TotalTermFreq();
        //Sort tokens by decreasing TTF
//        ArrayList<String> ttfSorted = analyzer.tokensByFreq(totalTermFreqs);
//        System.out.println("TTF Calculated.");

//        File ttfFile = new File("ttfOut.csv");
//        PrintWriter ttfWriter = new PrintWriter(ttfFile);
//        System.out.println(ttfSorted.size());
//        for (String s : ttfSorted) {
//            ttfWriter.println(s + "," + totalTermFreqs.get(s));
//        }
//        ttfWriter.close();

       //  Map<String, Integer> docFreqs = analyzer.DocumentFrequency();
       //  ArrayList<String> dfSorted = analyzer.tokensByFreq(docFreqs);
       //  File dfFile = new File("dfOut.csv");
       //  PrintWriter dfWriter = new PrintWriter(dfFile);
       //  System.out.println(dfSorted.size());
       //  for (String s : dfSorted) {
       //      dfWriter.println(s + "," + docFreqs.get(s));
       //  }
//         dfWriter.close();

        //Part 2:
        //Generate and calculate document frequency for all Ngrams
//        analyzer.CreateVocabulary();
        //Find top 100 words by DF
//        Collections.sort(analyzer.vocabulary, new TokenComp());

        //Top 100 stop words in restaurant
        //This is top 100 Ngrams by DF
//        List<Token> top100 = new ArrayList<>();
//        for (int i = 0; i< 100; i++) {
//            top100.add(analyzer.vocabulary.get(i));
//        }

       // //Subtract original stopwords to find restaurant specific ones
//        File restSpecificFile = new File("restSpecificStopwords.txt");
//        PrintWriter restSpecificWriter = new PrintWriter(restSpecificFile);
//        for (Token i: top100) {
//            if (!analyzer.m_stopwords.contains(i.getToken())) {
//                restSpecificWriter.println(i.getToken() + "," + i.getDF());
//                analyzer.m_stopwords.add(i.getToken());
//            }
//        }
//        restSpecificWriter.close();
//        File stopF = new File("fullStopwords");
//        PrintWriter stopW = new PrintWriter(stopF);
//        for (String S : analyzer.m_stopwords) {
//            stopW.println(S);
//        }
//        stopW.close();


        //Filter words out of vocabulary that have DF less than 50
//         while(analyzer.vocabulary.get(analyzer.vocabulary.size()-1).getDF() < 50) {
//             analyzer.vocabulary.remove(analyzer.vocabulary.size()-1);
//         }


//        System.out.println("Writing Vocab With DF < 50 removed");
//        File vocabFile = new File("truncatedVocab.txt");
//        PrintWriter vocabWriter = new PrintWriter(vocabFile);

//        for (Token t : analyzer.vocabulary) {
//            vocabWriter.println(t.getToken() + "," + t.getDF());
//            boolean add = true;
//            for (String stopw : analyzer.m_stopwords) {
//                if (t.getToken().contains(stopw)) {
//                    add = false;
//                }
//            }
//            if (add) {
//                vocabWriter.println(t.getToken() + "," + t.getDF());
//            }
//        }
//        vocabWriter.close();
//        exit(0);

//        Scanner s = new Scanner(new File("fullStopwords"));
//        ArrayList<String> stopwords = new ArrayList<String>();
//        while (s.hasNextLine()){
//            stopwords.add(s.nextLine());
//        }
//        s.close();
//
//        System.out.println("Filtering stopwords...");
//        File vocabFile = new File("finalVocab.txt");
//        PrintWriter vocabWriter = new PrintWriter(vocabFile);
//        s = new Scanner(new File("truncatedVocab.txt"));
//        analyzer.vocabulary.clear();
//        while (s.hasNext()){
//            String line = s.nextLine();
//            String[] contentDF = line.split(",");
//            List<String> contentPts = Arrays.asList(contentDF[0].split(" "));
//            boolean add = true;
//            for (String stop: stopwords) {
//                if (contentDF[0].equals(stop) || contentPts.contains(stop)) {
//                    add = false;
//                    break;
//                }
//            }
//            if (add) {
//                vocabWriter.println(line);
//                Token tk = new Token(contentDF[0]);
//                tk.setDF(Double.parseDouble(contentDF[1]));
//                tk.setIDF(1 + Math.log(38688 / Double.parseDouble(contentDF[1])));
//                analyzer.vocabulary.add(tk);
//            }
//        }
//        s.close();
//        vocabWriter.close();
//
////        Size of controlled vocab
//        File vocabSizeFile = new File("vocabSize.txt");
//        PrintWriter vocabSizeWriter = new PrintWriter(vocabSizeFile);
//        vocabSizeWriter.println(analyzer.vocabulary.size());
//        vocabSizeWriter.close();
//
////        Top and bottom 50 words by DF and their corresponding IDFs
//         ArrayList<Token> top50IDF = new ArrayList<>();
//         ArrayList<Token> bottom50IDF = new ArrayList<>();
//         for(int i =0; i< 50 && i < analyzer.vocabulary.size(); i++) {
//             Token toptkn = new Token(analyzer.vocabulary.get(i).getToken());
//             toptkn.setIDF(analyzer.vocabulary.get(i).getIDF());
//             toptkn.setDF(analyzer.vocabulary.get(i).getDF());
//             Token bottkn = new Token(analyzer.vocabulary.get(analyzer.vocabulary.size()-(i+1)).getToken());
//             bottkn.setIDF(analyzer.vocabulary.get(analyzer.vocabulary.size() - (i + 1)).getIDF());
//             bottkn.setDF(analyzer.vocabulary.get(analyzer.vocabulary.size() - (i + 1)).getDF());
//             top50IDF.add(toptkn);
//             bottom50IDF.add(bottkn);
//         }
//
//         //Write Top 50 to file
//         File top50File = new File("top50Controlled.csv");
//         PrintWriter top50Writer = new PrintWriter(top50File);
//         top50Writer.println("Token" + "," + "IDF" + "," + "DF");
//         for (Token t : top50IDF) {
//             top50Writer.println(t.getToken() + "," + t.getIDF() + "," + t.getDF());
//         }
//         top50Writer.close();
//
//         //Write bottom 50 to file
//         File bot50File = new File("bot50Controlled.csv");
//         PrintWriter bot50Writer = new PrintWriter(bot50File);
//         bot50Writer.println("Token" + "," + "IDF" + "," + "DF");
//         for (Token t : bottom50IDF) {
//         bot50Writer.println(t.getToken() + "," + t.getIDF() + "," + t.getDF());
//         }
//         bot50Writer.close();
//        System.out.println("THERE ARE X REVIEWS: " + analyzer.m_reviews.size());
//        exit(0);

        //PART 3
        //Load stopwords, vocabulary, and reviews
//        analyzer.LoadStopwords("fullStopwords");
//        analyzer.LoadDirectory("./data/samples/yelp/test", "json");
//        Scanner s = new Scanner(new File("finalVocab.txt"));
//        analyzer.vocabulary.clear();
//        while (s.hasNext()){
//            String line = s.nextLine();
//            String[] contentDF = line.split(",");
//            Token tk = new Token(contentDF[0]);
//            tk.setDF(Double.parseDouble(contentDF[1]));
//            tk.setIDF(1 + Math.log(38688 / Double.parseDouble(contentDF[1])));
//            analyzer.vocabulary.add(tk);
//        }
//        s.close();
//        //Create index for looking up tokens by string
//        HashMap<String, Integer> index = new HashMap<>();
//        for (int i = 0; i < analyzer.vocabulary.size(); i++) {
//            index.put(analyzer.vocabulary.get(i).getToken(), i);
//            analyzer.vocabulary.get(i).setID(i);
//        }
//
//        //Compute vector representation of every document.
//        for (Post p : analyzer.m_reviews) {
//            //Use the keyset not getTokens because that's only the actual token/1grams
//            for (String str : p.tokenToTF.keySet()) {
//                Integer id = index.get(str);
//                if (id != null) {
//                    double tf = p.tokenToTF.get(str);
//                    double subLin_tf = 1 + Math.log(tf);
//
//                    double idf = analyzer.vocabulary.get(id).getIDF();
//                    double tf_idf = subLin_tf * idf;
//                    p.idToTF_IDF.put(id, tf_idf);
//                }
//            }
//        }
//
//        //Load special queries, convert to vector space representation and find most similar
//        //documents by cosine measure.
//        final ArrayList<Post> queries = new ArrayList<>();
//        JSONObject query = analyzer.LoadJson("./data/samples/query.json");
//        JSONArray jarray = query.getJSONArray("Reviews");
//        for(int i=0; i<jarray.length(); i++) {
//            Post review = new Post(jarray.getJSONObject(i));
//            String content = review.getContent();
//            String[] tokens = analyzer.tokenizer.tokenize(content);
//            ArrayList<String> tokenList = new ArrayList<>();
//            for (String token : tokens) {
//                String parsedToken = analyzer.SnowballStemming(analyzer.Normalization(token));
//                if (!parsedToken.isEmpty()) {
//                    tokenList.add(parsedToken);
//                }
//            }
//            review.setTokens(tokenList);
//            queries.add(review);
//        }
//
//        //Compute TF for each token in query
//        for (Post p : queries) {
//            ArrayList<String> tokenList = p.getTokens();
//            for (String str : tokenList) {
//                if (p.tokenToTF.containsKey(str)) {
//                    p.tokenToTF.put(str, p.tokenToTF.get(str) + 1);
//                }
//                else {
//                    p.tokenToTF.put(str, 1.0);
//                }
//            }
//        }
//
//        //Compute vector representation
//        for (Post p : queries) {
//            for (String str : p.tokenToTF.keySet()) {
//                Integer id = index.get(str);
//                if (id != null) {
//                    Double sublintf = 1 + Math.log(p.tokenToTF.get(str));
//                    Double tfidf = sublintf * analyzer.vocabulary.get(id).getIDF();
//                    p.idToTF_IDF.put(id, tfidf);
//                }
//            }
//
//        }
//
//        //FIND MOST SIMILAR
//        PriorityQueue<Post> similarTo0 = new PriorityQueue<>(new Comparator<Post>() {
//            @Override
//            public int compare(Post o, Post t1) {
//                double osim = queries.get(0).similiarity(o);
//                double t1sim = queries.get(0).similiarity(t1);
//                if (osim > t1sim) {
//                    return 1;
//                }
//                else if (osim == t1sim) {
//                    return 0;
//                }
//                return -1;
//            }
//        });
//         PriorityQueue<Post> similarTo1 = new PriorityQueue<>(new Comparator<Post>() {
//             @Override
//             public int compare(Post o, Post t1) {
//                 double osim = queries.get(1).similiarity(o);
//                 double t1sim = queries.get(1).similiarity(t1);
//                 if (osim > t1sim) {
//                     return 1;
//                 }
//                 else if (osim == t1sim) {
//                     return 0;
//                 }
//                 return -1;
//             }
//         });
//         PriorityQueue<Post> similarTo2 = new PriorityQueue<>(new Comparator<Post>() {
//             @Override
//             public int compare(Post o, Post t1) {
//                 double osim = queries.get(2).similiarity(o);
//                 double t1sim = queries.get(2).similiarity(t1);
//                 if (osim > t1sim) {
//                     return 1;
//                 }
//                 else if (osim == t1sim) {
//                     return 0;
//                 }
//                 return -1;
//             }
//         });
//         PriorityQueue<Post> similarTo3 = new PriorityQueue<>(new Comparator<Post>() {
//             @Override
//             public int compare(Post o, Post t1) {
//                 double osim = queries.get(3).similiarity(o);
//                 double t1sim = queries.get(3).similiarity(t1);
//                 if (osim > t1sim) {
//                     return 1;
//                 }
//                 else if (osim == t1sim) {
//                     return 0;
//                 }
//                 return -1;
//             }
//         });
//         PriorityQueue<Post> similarTo4 = new PriorityQueue<>(new Comparator<Post>() {
//             @Override
//             public int compare(Post o, Post t1) {
//                 double osim = queries.get(4).similiarity(o);
//                 double t1sim = queries.get(4).similiarity(t1);
//                 if (osim > t1sim) {
//                     return 1;
//                 }
//                 else if (osim == t1sim) {
//                     return 0;
//                 }
//                 return -1;
//             }
//         });
//
//        ArrayList<PriorityQueue<Post>> queues = new ArrayList<>();
//        queues.add(similarTo0);
//        queues.add(similarTo1);
//        queues.add(similarTo2);
//        queues.add(similarTo3);
//        queues.add(similarTo4);
//
//        for (Post p : analyzer.m_reviews) {
//            for (PriorityQueue q : queues) {
//                q.add(p);
//                if (q.size() > 3) {
//                    q.poll();
//                }
//            }
//        }
//
//        for (int i = 0; i < 5; i ++) {
//            PriorityQueue<Post> q = queues.get(i);
//            File qFile = new File("similarTo" + i + ".txt");
//            PrintWriter qWriter = new PrintWriter(qFile);
//            qWriter.println("COMPARING TO : " + queries.get(i).getContent());
//            qWriter.println();
//            for (Post t : q) {
//                qWriter.println(t.getContent());
//                qWriter.println();
//                qWriter.println("SCORE OF ABOVE: " + queries.get(i).similiarity(t));
//                qWriter.println();
//            }
//            qWriter.close();
//        }
//
    }
}


