package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.site.view.common.Ced2arView
import org.springframework.stereotype.Component
import scalatags.Text.all._

@Component
class AboutView extends Ced2arView{
  
  def aboutPage: String = {
    val typedHtml = html(
        head(
        defaultMetaTags,
        defaultStyleSheetsAndScripts//,
      ),
        body(masterDiv(
        topBanner,
        navBar,
        masterDiv(
          div(
            h2(ced2ar),
            p("The Comprehensive Extensible Data Documentation and Access Repository (", ced2ar,
              ") is funded by the National Science Foundation (NSF), under grant ")
            (a(href:="http://www.nsf.gov/awardsearch/showAward?AWD_ID=1131848")("#1131848"))
            (" and developed by the ",
              a(
                "Cornell Node of the NSF Census Research Network (NCRN)",
                href:="https://www.ncrn.cornell.edu"
              ),
              ". The Cornell NCRN branch includes researchers and developers from the Cornell Institute for Social and Economic Research ")
            (a(href:="http://ciser.cornell.edu/")("CISER"))
            (" and the Cornell ")
            (a(href:="http://www.ilr.cornell.edu/ldi/")(" Labor Dynamics Institute.")), ced2ar,
            " is designed to improve the discoverability of both public and restricted data from ",
            "the federal statistical system. The project is based upon leading metadata standards ",
            "and ingests data from a variety of sources."
            ),
            //images will go here
          div(
            h3("Current Collaborators"),
            p(
              "Brandon Barker - ", i("Center for Advanced Computing, Cornell University"), br(),
             "William Block - ", i("Cornell Institute for Social and Economic Research, Cornell University"), br(),
             "Warren Brown - ", i("Cornell Institute for Social and Economic Research, Cornell University"), br(),
             "Kyle Brumsted - ", i("Cornell Institute for Social and Economic Research, Cornell University"), br(),
             "Carl Lagoze - ", i("School of Information, University of Michigan"), br(),
             "Lars Vilhuber - ", i("Labor Dynamics Institute, Cornell University"), br()
            )
          ),
          div(
            h3("Previous Collaborators"),
            p(
              "Venky Kambhampaty - ", i("Cornell Institute for Social and Economic Research, Cornell University"), br(),
              "Benjamin Perry - ", i("Cornell Institute for Social and Economic Research, Cornell University"), br(),
              "Haeyong Shin - ", i("Cornell University"), br(),
              "Flavio Stanchi - ", i("Labor Dynamics Institute, Cornell University"), br(),
              "Shoujun Su  - ", i("Cornell University"), br(),
              "Jeremy Williams - ", i("Cornell Institute for Social and Economic Research, Cornell University"), br()
            ),
            h4("Fall 2012 Class Project (Version 1.0 Beta)"),
            p(
              "Justin Burden - ", i("Cornell University"), br(),
              "Chantelle Farmer - ", i("Cornell University"), br(),
              "Jessica Kane - ", i("Cornell University"), br(),
              "Shudan Zheng  - ", i("Cornell University"), br()
            )
          ),
          div(
            h3("Copyright Information"),
             p("Copyright 2012-2016 Cornell University. All rights reserved. ")
             (ced2ar)(" is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. (See")
             (a(href:="http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt")("http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt)"))
             (". Permission to copy, modify, and distribute all or any part of ")(ced2ar)(", officially docketed at Cornell as D-6801,")
             ("titled \"The Comprehensive Extensible Data Documentation and Access Repository\" (\"WORK\")")
             ("and its associated copyrights for educational, research and non-profit purposes, without fee,")
             ("and without a written agreement is hereby granted, provided that the above copyright notice ")
             ("and the four paragraphs of this document appear in all copies."),

             p("Those desiring to incorporate WORK into commercial products or use WORK ")
             ("and its associated copyrights for commercial purposes ")
             ("should contact the Cornell Center for Technology Enterprise ")
             ("and Commercialization at 395 Pine Tree Road, Suite 310, Ithaca, NY 14850;")
             ("Email:cctecconnect@cornell.edu; Tel: 607-254-4698; Fax: 607-254-5454")
             ("for a commercial license."),

             p("In no event shall Cornell University be liable to any party for direct, ")
             ("indirect, special, incidental, or consequential damages, ")
             ("including lost profits, arising out of the use of work and its associated copyrights, ")
             ("even if Cornell University may have been advised of the possibility of such damage."),

             p("The work is provided on an \"AS IS\" basis, and Cornell University has no obligation ")
             ("to provide maintenance, support, updates, enhancements, or modifications. ")
             ("of merchantability or fitness for a particular purpose, or that the use of work ")
             ("and its associated copyrights will not infringe any patent, trademark or other rights.")
          )
        )))
    )
       
     typedHtml.toString()
  }
  
}