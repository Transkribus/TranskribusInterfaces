/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import eu.transkribus.interfaces.types.Image;

/**
 * this interface can be used in many ways. For example ContrastEnhancement,
 * Binarization, SkewNormalization, SlantNormalization,...
 *
 * @author gundram
 */
public interface IImageManipulator {

    /**
     * process an image with maybe some properties
     *
     * @param image
     * @return
     */
    public Image process(Image image);

    public Image process(Image image, String[] props);
    
    public String usage();
    public String getToolName();
    public String getVersion();
    public String getProvider();
}
