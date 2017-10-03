package edu.ncrn.cornell.site.view.routing

import mhtml.Rx


object EndPoints {

  def codebook: Rx[String] = HostConfig.baseUri.map{baseUriStr =>
    s"$baseUriStr/codebook"
  }
  def codebook(id: String): Rx[String] = HostConfig.baseUri.map{baseUriStr =>
    s"$baseUriStr/codebook/$id"
  }
}
