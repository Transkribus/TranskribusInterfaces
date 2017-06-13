package eu.transkribus.interfaces.languageresources;

import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.transkribus.interfaces.IDictionary;

/**
 *
 * @author jnphilipp
 */
public interface IPagewiseTextExtractor extends ITextExtractor
{

    List<String> extractTextFromDocumentPagewise(String path);

    String extractTextFromPage(String path, int page);

    IDictionary extractAbbreviationsFromPage(String path, int page);
}
