package edu.ncrn.cornell.site.view

import mhtml._

import scala.concurrent.Future
import scala.util.Try
import monix.execution.Scheduler.Implicits.global


object Utils {


  //TODO: use version now in in mhtml:
  def fromFuture[T](future: Future[T]): Rx[Option[Try[T]]] = {
    val result = Var(Option.empty[Try[T]])
    future.onComplete(x => result := Some(x))
    result
  }

}