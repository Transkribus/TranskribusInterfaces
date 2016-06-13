/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces.types;

/**
 * Token of language model (for example word, but also puncutation)
 *
 * @author gundram
 */
public class Token {

    double prob;
    String word;

    public Token(double prob, String word) {
        this.prob = prob;
        this.word = word;
    }

    /**
     * returns the corresponding text
     *
     * @return
     */
    public String getWord() {
        return word;
    }

    /**
     * returns the given probability (either the unigram probability or the
     * conditional probability for a given prefix)
     *
     * @return log likelyhood [-\infinity,0]
     */
    public double getProb() {
        return prob;
    }

}
