/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import java.io.File;

import eu.transkribus.interfaces.types.Image;

/**
 * For a given input, the representation (Confmat, WordGraph,...) is calculated.
 * Probably this interface is too simple and have to be changed.
 *
 * @author gundram
 */
public interface IText2Image extends IModule{
	
    /**
     * in URO-CITlab this is done by an MDRNN (sprnn). Output in this case would
     * be a Confidence-Matrix.
     *
     * @param pathToOpticalModel is either path to serialized RNN or path to GMM/HMM.
     * @param pathToLanguageModel is either path to ARPA-file or other language resource file.
     * @param pathToCharacterMap is path to character map file (TODO: add link to file description)
     * @param pathToText is path to an UTF-8 coded text file 
     * @param image
     * @param xmlInOut 
     * @param props set of parameters for recognition (has to be documented)
     */
    public void match(
        String pathToOpticalModel,
        String pathToLanguageModel,
        String pathToCharacterMap,
        String pathToText,
        Image image, 
        String xmlInOut, 
        String[] props 
        );
    
}
