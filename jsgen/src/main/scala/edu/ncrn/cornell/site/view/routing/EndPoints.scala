package edu.ncrn.cornell.site.view.routing

import edu.ncrn.cornell.service.api.CodebookDetails
import mhtml.Rx


object EndPoints {

  def codebook: Rx[String] = HostConfig.baseUri.map{baseUriStr =>
    s"$baseUriStr/codebook"
  }
  def codebook(id: String): Rx[String] = HostConfig.baseUri.map{baseUriStr =>
    s"$baseUriStr/codebook/$id"
  }
  def variable: Rx[String] = HostConfig.baseUri.map{baseUriStr =>
    s"$baseUriStr/var"
  }
  def variable(cid: String): Rx[String] = codebook(cid).map{codebookUri =>
    s"$codebookUri/var"
  }
  def variable(cid: String, vid: String): Rx[String] = codebook(cid).map{codebookUri =>
    s"$codebookUri/var/$vid"
  }
}
