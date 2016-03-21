/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import eu.transkribus.interfaces.types.Image;
import eu.transkribus.interfaces.types.Region;

import java.io.Serializable;

/**
 * For a given input, the representation (Confmat, WordGraph,...) is calculated.
 * Probably this interface is too simple and have to be changed.
 *
 * @author gundram
 */
public interface IHtr {

    /**
     * in URO-CITlab this is done by an MDRNN (sprnn). Output in this case would
     * be a Confidence-Matrix.
     *
     * @param lineImage
     * @param pathToModel is either path to serialized RNN or path to directory with HMM/LM/etc.
     * @param properties set of parameters for recognition (has to be documented)
     * @return
     */
    public static boolean process(
        String pathToModels,
        Image image, 
        String pageXmlIn, 
        String pageXmlOut, 
        String storageDir, 
        String[] props, 
        String[] lines
        );
        
//    public static boolean process(String pathToModels, Image image, String pageXmlIn, String pageXmlOut, String storageDir, String[] props);
//    public boolean process(Image image, String pageXmlIn, String pageXmlOut, String storageDir, String[] props);
//    public boolean processDecoding(String pathToModel, Image image, String pageXmlIn, String pageXmlOut, String storageDir, String[] props);

//    public boolean processBoth(String pathToModel, Image image, String pageXmlIn, String pageXmlOut, String storageDir, String[] props);
    //public boolean processOutput(Serializable representation, String[] properties);

//    public String processOutput(File path, String[] properties);

//    public Serializable processRepresentation(Image pageImage, Region[] lineRegions, String pathToModel, String[] properties);
    
}
