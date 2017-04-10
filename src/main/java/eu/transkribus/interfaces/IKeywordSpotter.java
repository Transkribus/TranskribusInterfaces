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
     * @param imagesIn path to image-list
     * @param imagesIn path to storage-list
     * @param queriesIn file which contains query strings
     * @param dictIn file which contains a unigram of possible words (can be
     * null)
     * @param resultOut path to json-output file
     * @param props properties (can be null)
     */
    public void process(String imagesIn, String storageIn, String queriesIn, String dictIn, String resultOut, String[] props);

}
