/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces.languageresources;

import java.util.Map;
import java.util.Set;

import eu.transkribus.interfaces.IDictionary;

/**
 *
 * @author max
 */
public interface ITextExtractor
{
    String extractTextFromDocument(String path);

    String extractTextFromDocument(String path, String splitCharacter);

    IDictionary extractAbbreviations(String path);

    IDictionary extractPlaceNames(String path);

    IDictionary extractPersonNames(String path);
}
