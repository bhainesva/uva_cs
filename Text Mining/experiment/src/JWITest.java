import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ben on 4/13/16.
 */
public class JWITest {
    IDictionary dict;

    JWITest() throws IOException {
        String path = "/usr/share/wordnet";
        URL url = new URL ("file", null, path);
        dict = new Dictionary(url) ;
        dict.open() ;
    }
    public void testDictionary () throws IOException {
//        String path = "/usr/share/wordnet";
//        URL url = new URL ("file", null, path) ;
// construct the dictionary object and open it
//        IDictionary dict = new Dictionary(url) ;
//        dict.open () ;
// look up first sense of the word " dog "
        IIndexWord idxWord = dict.getIndexWord("dog", POS.NOUN ) ;
        IWordID wordID = idxWord.getWordIDs().get(5) ;
        IWord word = dict.getWord (wordID) ;
        System . out . println ( " Id = " + wordID ) ;
        System . out . println ( " Lemma = " + word . getLemma () ) ;
        System . out . println ( " Gloss = " + word . getSynset () . getGloss () ) ;
//        getSynonyms(dict);
    }

    public void getSynonyms (String query) {
// look up first sense of the word " dog "
        IIndexWord idxWord = dict.getIndexWord(query, POS.NOUN ) ;
        IWordID wordID = idxWord.getWordIDs().get(0) ; // 1 st meaning
        IWord word = dict.getWord(wordID) ;
        ISynset synset = word.getSynset() ;
// iterate over words associated with the synset
        for (IWord w : synset.getWords())
            System.out.println(w.getLemma());
    }

    public static void main(String[] args) throws IOException {
        JWITest test = new JWITest();
        test.getSynonyms("dog");
    }
}
