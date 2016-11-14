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
public interface IFeatureGenerator extends IModule {

    public void process(Image image, String pathToFileOut);

    public void process(String pathToFileIn, String pathToFileOut);

    public void process(Image image, String pathToFileOut, String[] props);

    public void process(String pathToFileIn, String pathToFileOut, String[] props);

}
