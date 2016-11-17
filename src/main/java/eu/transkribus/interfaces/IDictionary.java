package eu.transkribus.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
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
}
