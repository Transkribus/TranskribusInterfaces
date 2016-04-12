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
public interface IHtr extends IModule{
	
	public void createModel(String path, String[] pars);

    /**
     * in URO-CITlab this is done by an MDRNN (sprnn). Output in this case would
     * be a Confidence-Matrix.
     *
     * @param pathToModels is either path to serialized RNN or path to directory with HMM/LM/etc.
     * @param image
     * @param pageXmlIn
     * @param pageXmlOut
     * @param storageDir
     * @param props set of parameters for recognition (has to be documented)
     * @param lineIds 
     * @return
     */
    public void process(
        String pathToModels,
        Image image, 
        String xmlInOut, 
        String storageDir, 
        String[] lineIds,
        String[] props 
        );
    
//    public static boolean process(String pathToModels, Image image, String pageXmlIn, String pageXmlOut, String storageDir, String[] props);
//    public boolean process(Image image, String pageXmlIn, String pageXmlOut, String storageDir, String[] props);
//    public boolean processDecoding(String pathToModel, Image image, String pageXmlIn, String pageXmlOut, String storageDir, String[] props);

//    public boolean processBoth(String pathToModel, Image image, String pageXmlIn, String pageXmlOut, String storageDir, String[] props);
    //public boolean processOutput(Serializable representation, String[] properties);

//    public String processOutput(File path, String[] properties);

//    public Serializable processRepresentation(Image pageImage, Region[] lineRegions, String pathToModel, String[] properties);
    
}
