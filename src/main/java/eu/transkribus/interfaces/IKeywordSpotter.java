/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import eu.transkribus.interfaces.types.Image;

/**
 *
 * @author gundram
 */
public interface IKeywordSpotter extends IModule {

    /**
     *
     * @param pathToOpticalModel path to optical model (always needed)
     * @param pathToLanguageModel path to language model or language resource
     * (optional)
     * @param pathToCharacterMap path to charmap (optional, only needed if
     * keywords/LM differs from OM)
     * @param image image where search should be done (can be null)
     * @param xmlIn xml-file to of image (always needed)
     * @param keywords unicode-coded file with keywords in each line
     * @param storageDir directory to already cached structure (like wordgraphs,
     * confmats)
     * @param result path where the json-file with results should be saved
     * @param props additional parameters
     */
    public void process(
            String pathToOpticalModel,
            String pathToLanguageModel,
            String pathToCharacterMap,
            Image image,
            Image xmlIn,
            String keywords,
            String storageDir,
            String result,
            String[] props
    );

}
