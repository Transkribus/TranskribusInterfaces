/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

/**
 * Interface to calculate to given baseline the corresponding surrounding
 * polygons. The number (and order) of returned regions have to match the number
 * of baselines.
 *
 * @author gundram
 */
public interface IBaseLine2Coords {

    public Region[] process(Image img, Region[] baselines);

}
