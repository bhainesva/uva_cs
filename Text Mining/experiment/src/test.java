import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.sentiment.BuildBinarizedDataset;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentTraining;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class test {

    public String m_filename = "";
    public Properties props;
    public StanfordCoreNLP pipeline;

    public test() {
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public void analyzeDocument(String doc) throws FileNotFoundException, UnsupportedEncodingException {
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(doc);
        System.out.println("Analyzing: " + m_filename);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        PrintWriter resultWriter = new PrintWriter("sentiments-" + m_filename + ".txt", "UTF-8");
        for(CoreMap sentence: sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            resultWriter.println(sentiment + "\t" + sentence);
        }
        resultWriter.close();
    }

    public void LoadDirectory(String folder, String suffix) throws FileNotFoundException, UnsupportedEncodingException {
        File dir = new File(folder);
        for (File f : dir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(suffix)) {
                m_filename = f.getName();
                analyzeDocument(LoadPOS(f.getAbsolutePath()));
            }
            else if (f.isDirectory())
                LoadDirectory(f.getAbsolutePath(), suffix);
        }
        System.out.println("Loading review documents from " + folder);
    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public String LoadPOS(String filename) {
        try {
            String text = readFile(filename, StandardCharsets.UTF_8); // Add your text here!
            return text;
        } catch (IOException e) {
            System.err.format("[Error]Failed to open file %s!", filename);
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution

        // read some text in the text variable
        test t = new test();
        t.LoadDirectory("../../../tm-project/fmoc-minutes/", "");
//        String text = readFile("../../../tm-project/fmoc-minutes/apr24-2012", StandardCharsets.UTF_8); // Add your text here!



        // create an empty Annotation just with the given text
//        Annotation document = new Annotation(t.text);

        // run all Annotators on this text
//        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
//        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

//        PrintWriter resultWriter = new PrintWriter("sentenceSentiment.txt", "UTF-8");
//        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
//            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
//                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
//                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                //String ne = token.getString(CoreAnnotations.NamedEntityTagAnnotation.class);
//                String ner = token.ner();
//                String sent = token.get(SentimentCoreAnnotations.SentimentClass.class);
//                System.out.println(sent + ": " + token);

//                System.out.println("Word: " + word + ", POS: " + pos + ", NER: " + ner);
//            }

            // this is the parse tree of the current sentence
//            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
//            resultWriter.println(sentiment + "\t" + sentence);

            // this is the Stanford dependency graph of the current sentence
//            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
//        }
//        resultWriter.close();

            // This is the coreference link graph
            // Each chain stores a set of mentions that link to each other,
            // along with a method for getting the most representative mention
            // Both sentence and token offsets start at 1!
//            Map<Integer, CorefChain> graph =
//                document.get(CorefCoreAnnotations.CorefChainAnnotation.class);

    }
}


