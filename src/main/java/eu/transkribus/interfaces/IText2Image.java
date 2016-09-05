/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import java.io.File;

import eu.transkribus.interfaces.types.Image;

/**
 * This tool can be used to match a (ASCII-)transcription to given
 * baselines.<br\>
 * INPUT:<br/>
 * Text-file: The UTF-8-coded text-file (diplomatic text) where U+000A ('LINE
 * FEED (LF)') denotes a new line.<br/>
 * PageXML-file: The PageXML-file have to contain baselines with its
 * corresponding surrounding polygon.<br/>
 * Image: The image corresponding to the PageXML-file.<br/>
 * Models: pathes to Optical and Language Model.<br/>
 * CharMap: In many cases the models cannot handle all characters from the
 * transcription text. The CharMap can be used to allocate specific character to
 * existing channels.<br/>
 * Properties: When the algorithm has some Properties/Thresholds, this structure
 * can be used to set them.
 *
 *
 * @author gundram
 */
public interface IText2Image extends IModule {

    /**
     * in URO-CITlab this is done by an MDRNN (sprnn). Output in this case would
     * be a Confidence-Matrix.
     *
     * @param pathToOpticalModel is either path to serialized RNN or path to
     * GMM/HMM.
     * @param pathToLanguageModel is either path to ARPA-file or other language
     * resource file.
     * @param pathToCharacterMap is path to character map file (TODO: add link
     * to file description)
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
