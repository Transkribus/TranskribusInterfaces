/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces.languageresources;

import eu.transkribus.interfaces.IDictionary;
import eu.transkribus.interfaces.ITokenizer;

import java.util.List;
import java.util.Map;

/**
 *
 * @author max
 */
public interface ILanguageResource
{
    IDictionary getFrequencyDictionary(ITokenizer tokenizer, String text);
}
