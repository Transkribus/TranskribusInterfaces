/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

/**
 *
 * @author gundram, philip
 */
public interface IModule {

    /**
     * returns a String (\n) for linebreak to describe the module and its
     * behavior
     *
     * @return
     */
    public String usage();

    /**
     * unique tool name, describing the main domain
     *
     * @return
     */
    public String getToolName();

    /**
     * version, matching [0-9]+\.[0-9]+\.[0-9]+
     *
     * @return
     */
    public String getVersion();

    /**
     * your organization
     *
     * @return
     */
    public String getProvider();

}
