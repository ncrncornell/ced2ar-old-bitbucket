package edu.ncrn.cornell.view

import edu.ncrn.cornell.view.common.Ced2arView
import org.springframework.stereotype.Component
import scalatags.Text.all._

@Component
class AboutView extends Ced2arView{
  
  def aboutPage() : String = {
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
              h2("About CED")(sup("2"))("AR"),
              p("The Comprehensive Extensible Data Documentation and Access Repository (CED")(sup("2"))("AR) is funded by the National Science Foundation (NSF), under grant ")
              (a(href:="http://www.nsf.gov/awardsearch/showAward?AWD_ID=1131848")("#1131848"))
              (" and developed by the Cornell Node of the NSF Census Research Network (NCRN). The Cornell NCRN branch includes researchers and developers from the Cornell Institute for Social and Economic Research ")
              (a(href:="http://ciser.cornell.edu/")("CISER"))
              (" and the Cornell ")
              (a(href:="http://www.ilr.cornell.edu/ldi/")(" Labor Dynamics Institute"))
              (". CED")(sup("2"))("AR is designed to improve the discoverability of both public and restricted data from the federal statistical system. The project is based upon leading metadata standards and ingests data from a variety of sources.")	
              ),
              //images will go here
            div(
                h3("Current Collaborators"),
                p("Brandon Barker - ")(i("Cornell Center for Advanced Computing, Cornell University"))(br())
                 ("William Block - ")(i("Cornell Institute for Social and Economic Research, Cornell University"))(br())
                 ("Warren Brown - ")(i("Cornell Institute for Social and Economic Research, Cornell University"))(br())
                 ("Kyle Brumsted - ")(i("Cornell Institute for Social and Economic Research, Cornell University"))(br())
                 ("Venky Kambhampaty - ")(i("Cornell Institute for Social and Economic Research, Cornell University"))(br())
                 ("Carl Lagoze - ")(i("School of Information, University of Michigan"))(br())
                 ("Benjamin Perry - ")(i("Cornell Institute for Social and Economic Research, Cornell University"))(br())
                 ("Flavio Stanchi - ")(i("Labor Dynamics Institute, Cornell University"))(br())
                 ("Lars Vilhuber - ")(i("Labor Dynamics Institute, Cornell University"))(br())
                 ("Jeremy Williams - ")(i("Cornell Institute for Social and Economic Research, Cornell University"))(br())
              ),
             div(
                 h3("Copyright Information"),
                 p("Copyright 2012-2016 Cornell University. All rights reserved. ")
                 ("CED")(sup("2"))("AR is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. (See")
                 (a(href:="http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt")("http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt)"))
                 ("Permission to copy, modify, and distribute all or any part of CED")(sup("2"))("  officially docketed at Cornell as D-6801,")
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
           )
         )
       )
       )
       
     typedHtml.toString()
  }
  
}