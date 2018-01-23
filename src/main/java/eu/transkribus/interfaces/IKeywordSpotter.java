/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

/**
 * first implementation of KWS interface<br>
 *
 * the KWS engine gets the following input:<br>
 * An array with absolute paths to storages, where it should search in.<br>
 * An array with absolute paths to images, which correspond to the previous array. (each
 * i-th entry should fit together)<br>
 * An array with queries<br>
 * The Engine can benefit from a language model: Assuming one searches for
 * "Anna" but "Anne" is also a valid name. Then one does not want to have "Anne"
 * in the results, just because they look similar. On the other hand, when one
 * searches for "James", there maybe is no similar looking other name. Then also
 * results which look not so similar can be right.<br>
 * <br>
 * The result is a JSON String and has to look like that (with or without indentation):<br>
 * <pre>
 * {"keywords": [{
 *   "kw": "Anna",
 *   "pos": [
 *       {
 *           "conf": 0.5,
 *           "bl": "[0.08;0.28]",
 *           "id": "r1l60",
 *           "image": "IMG_6432766.jpg"
 *       },
 *       {
 *           "conf": 0.2,
 *           "bl": "[0.74;0.81]",
 *           "id": "r1l18",
 *           "image": "IMG_6432829.jpg"
 *       },
 *       {
 *           "kw": "James",
 *           "pos": [
 *               {
 *                   "conf": 0.4,
 *                   "bl": "[0.18;0.38]",
 *                   "id": "r1l40",
 *                   "image": "IMG_6434766.jpg"
 *               },
 *               {
 *                   "conf": 0.3,
 *                   "bl": "[0.4;0.5]",
 *                   "id": "r1l18",
 *                   "image": "IMG_6432849.jpg"
 *               }
 *           ]
 *       }
 *   ]
 *}]}</pre>
 * 
 * @author gundram
 * @author philip
 */
public interface IKeywordSpotter extends IModule {
	
    /**
     * @param imagesIn array with absolute paths to images (only for reference in result! can contain any identifier)
     * @param storageIn array with absolute paths to stored HTR results, e.g. ConfMat container. 
     * Aligned to imagesIn
     * @param queriesIn array that contains query strings
     * @param dictIn file which contains a unigram of possible words (can be null)
     * @param props properties (can be null)
     * @return JSON output String
     */
    public String process(String[] imagesIn, String[] storageIn, String[] queriesIn, String dictIn, String[] props);    
}
