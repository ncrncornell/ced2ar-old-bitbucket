package edu.ncrn.cornell.service

package object api {

  // https://stackoverflow.com/questions/46672329/how-to-create-a-map-constructor-using-mapk-v-instead-of-mapk-v

  trait CanBuildIterable[A, B] {
    type Out <: Iterable[B]
  }

  implicit def defaultCanBuildIterable[A[X] <: Iterable[X], B] = new CanBuildIterable[A[_], B] {
    type Out = A[B]
  }

  implicit def mapCanBuildIterable[K, V] = new CanBuildIterable[Map[_, _], (K, V)] {
    type Out = Map[K, V]
  }

  // See https://stackoverflow.com/questions/46669194/how-to-get-the-parameter-of-a-type-constructor-in-scala/46671710#46671710
  // For possible improvements in using the API

  type VarDetails = List[(String, List[String])]

  type CodebookItem = (String, List[String])
  type CodebookCollection[+A] = List[A]
  type CodebookDetails = CodebookCollection[CodebookItem]

  type CodebookId = String
  type CodebookName = String
  type CodebookNameItem = (CodebookId, CodebookName)
  type CodebookNameCollection[+A] = List[A]
  type CodebookNames = CodebookNameCollection[CodebookNameItem]
  type CodebookNameMap = Map[CodebookId, CodebookName]




}
