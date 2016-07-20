package structures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ben on 4/19/16.
 */
public class Tag {
    private HashSet<String> m_tags;

    public Tag(HashSet<String> tags) {
        m_tags = tags;
    }

    public Tag(String tag) {
        m_tags = new HashSet<>();
        m_tags.add(tag);
    }

    public Tag(ArrayList<String> tags) {
        m_tags = new HashSet<String>(tags);
    }

    public Tag() {
        m_tags = new HashSet<>();
    }

    public HashSet<String> getTags() {
        return m_tags;
    }

    public boolean equals(String tag) {
        return m_tags.contains(tag);
    }

    public boolean equals(Tag tag) {
        Set<String> intersection = new HashSet<String>(m_tags); // use the copy constructor
        intersection.retainAll(tag.getTags());
        return intersection.isEmpty();
    }
}
