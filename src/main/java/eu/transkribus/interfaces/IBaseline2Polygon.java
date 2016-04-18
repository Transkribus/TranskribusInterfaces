/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import eu.transkribus.interfaces.types.Image;

/**
 * Interface to calculate to given baseline the corresponding surrounding
 * polygons. The number (and order) of returned regions have to match the number
 * of baselines.
 *
 * @author gundram
 */
public interface IBaseline2Polygon extends IModule{

//    public boolean process(Image img, String pageXmlIn, String pageXmlOut);

    public void process(Image img, String xmlInOut, String[] idxs, String[] props);

}
