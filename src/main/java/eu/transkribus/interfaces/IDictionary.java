package eu.transkribus.interfaces;

//import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * @author jnphilipp
 */
public interface IDictionary {
    /* meta */
    public String getName();
    public String getDescription();
    public String getLanguage();
    public int getNumberTokens();
    public int getNumberTypes();
    public Map<Character, Integer> getEntryCharacterTable();
    public Map<Character, Integer> getValueCharacterTable();
    public LocalDateTime getCreationDate();

    /* entries */
    public void merge(IDictionary dictionary);
    public boolean containsKey(String key);
    public boolean containsValue(String name);
    public Collection<IEntry> getEntries();
    public IEntry getEntry(String key) throws NoSuchElementException;
    public Collection<IEntry> getEntriesByValue(String name) throws NoSuchElementException;

    /**
     * should return strings that could be parsed
     * by TranskribusCore to instances of CostumTags.
     * @param entry
     * @return null if on tags available, a String for each tag otherwise
     */
    public String[] getTags(IEntry entry);
}
