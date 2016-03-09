/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import eu.transkribus.interfaces.types.Image;
import eu.transkribus.interfaces.types.Region;

/**
 * Interface to calculate to given surrounding polygons the corresponding
 * baseline. The number (and order) of returned regions have to match the number
 * of surrounding polygons. Maybe this interface is not needed - it is the
 * "reverse" interface from {@link eu.transkribus.interfaces.IBaseLine2Coords}.
 *
 * @author gundram
 */
public interface ICoords2BaseLine {

    /**
     * needed??
     *
     * @param img
     * @param coords
     * @return
     */
    public Region process(Image img, Region coords);

    /**
     *
     * @param img
     * @param coords
     * @return
     */
    public Region[] process(Image img, Region[] coords);

}
