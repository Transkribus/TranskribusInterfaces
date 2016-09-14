/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import java.io.File;

import eu.transkribus.interfaces.types.Image;

/**
 * This tool can be used to match a given transcript to a page (image+baselines).<br\>
 * INPUT:<br/>
 * Text-file(s): The UTF-8-coded text-file(s) (diplomatic text) where U+000A
 * ('LINE FEED (LF)') denotes a new line.<br/>
 * PageXML-file(s): The PageXML-file(s) have to contain baselines with its
 * corresponding surrounding polygon.<br/>
 * Image: The image corresponding to the PageXML-file or a List of pathes to
 * Images<br/>
 * Models: pathes to Optical and Language Model. These Models may be optional
 * depending on the implementation.<br/>
 * CharMap: In many cases the models cannot handle all characters from the
 * transcription text. The CharMap can be used to allocate specific character to
 * existing channels. The CharMap is optional.<br/>
 * Properties: When the algorithm has some Properties/Thresholds, this structure
 * can be used to set them. If props has the property "words:=true", an
 * alignment on word-level is done.<br/>
 * Region IDs: If the algorithm should only work on specific regions, the
 * regionIds of the PageXML file can be given.
 * <br/>
 * If a match is found, the algorithm tries to calculate a confidence of this
 * match. This confidence is saved in the TextEquiv-tag as "conf".
 *
 *
 * @author gundram
 */
public interface IText2Image extends IModule {

    /**
     * The methods tries to align a given text to a whole Collection. It is
     * assumed that the text (pathToText) has no page breaks. Line breaks may
     * be optional, depending on the implementation. The length of pathsToImages
     * and pathsToXmlInOut has to be the same.
     *
     * @param pathToOpticalModel
     * @param pathToLanguageModel
     * @param pathToCharacterMap
     * @param pathToText
     * @param pathsToImages
     * @param pathsToXmlInOut
     * @param props
     */
    public void matchCollection(
            String pathToOpticalModel,
            String pathToLanguageModel,
            String pathToCharacterMap,
            String pathToText,
            String[] pathsToImages,
            String[] pathsToXmlInOut,
            String[] props
    );

    /**
     * This method tries to align one text (with or without line breaks) to a
     * given image.
     *
     * @param pathToOpticalModel
     * @param pathToLanguageModel
     * @param pathToCharacterMap
     * @param pathToText
     * @param image
     * @param pathToXmlInOut
     * @param regionIds
     * @param props
     */
    public void matchImage(
            String pathToOpticalModel,
            String pathToLanguageModel,
            String pathToCharacterMap,
            String pathToText,
            Image image,
            String pathToXmlInOut,
            String[] regionIds,
            String[] props
    );

    /**
     * This method tries to match a set of transcriptions to a set of regions.
     * The length of pathsToText has to fit the length of regionIds.
     *
     * @param pathToOpticalModel
     * @param pathToLanguageModel
     * @param pathToCharacterMap
     * @param pathsToText
     * @param image
     * @param pathToXmlInOut
     * @param regionIds
     * @param props
     */
    public void matchRegions(
            String pathToOpticalModel,
            String pathToLanguageModel,
            String pathToCharacterMap,
            String[] pathsToText,
            String image,
            String pathToXmlInOut,
            String[] regionIds,
            String[] props
    );

}
