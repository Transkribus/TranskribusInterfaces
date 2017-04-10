/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import eu.transkribus.interfaces.types.Image;

/**
 * first implementation of KWS interface<br/>
 *
 * the KWS engine gets the following input:<br/>
 * A file with a list of storages, where it should search in.<br/>
 * A file with a list of images, which correspond to the previous file. (each
 * i-th line should fit together)<br/>
 * A file with queries in each line<br/>
 * The Engine can benefit from a language model: Assuming one searches for
 * "Anna" but "Anne" is also a valid name. Then one does not want to have "Anne"
 * in the results, just because they look similar. On the other hand, when one
 * searches for "James", there maybe is no similar looking other name. Then also
 * results which look not so similar can be right.<br/>
 * <br/>
 * the result is a json-file, which will be saved to the given path. The result
 * file have to look like that:<br/>
 * { "keywords": [ { "kw": "Anna", "pos": [ { "conf": 0.5, "bl": "[0.08;0.28]",
 * "id": "r1l60", "image": "IMG_6432766.jpg" }, { "conf": 0.2, "bl":
 * "[0.74;0.81]", "id": "r1l18", "image": "IMG_6432829.jpg" } }, { "kw":
 * "James", "pos": [ { "conf": 0.4, "bl": "[0.18;0.38]", "id": "r1l40", "image":
 * "IMG_6434766.jpg" }, { "conf": 0.3, "bl": "[0.4;0.5]", "id": "r1l18",
 * "image": "IMG_6432849.jpg" } } ] }
 *<br/><br/>
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
