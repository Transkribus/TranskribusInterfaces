package eu.transkribus.interfaces;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * @author jnphilipp
 */
public interface IEntry {
    public String getKey();
    public int getFrequency();
    public int getFrequency(String name) throws NoSuchElementException;
    public Map<String, Integer> getValues();
    public boolean containsValue(String name);
}
