/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import eu.transkribus.interfaces.types.Image;

/**
 * Interface to calculate to given surrounding polygons the corresponding
 * baseline. The number (and order) of returned regions have to match the number
 * of surrounding polygons. Maybe this interface is not needed - it is the
 * "reverse" interface from {@link eu.transkribus.interfaces.IBaseLine2Coords}.
 *
 * @author gundram
 */
public interface ICoords2BaseLine extends IModuleDescription {

    /**
     * needed??
     *
     * @param img
     * @param pageXmlIn
     * @param pageXmlOut
     * @return
     */
    public boolean process(Image img, String pageXmlIn, String pageXmlOut);

    public boolean process(Image img, String pageXmlIn, String pageXmlOut, String[] idxs, String[] props);

}
