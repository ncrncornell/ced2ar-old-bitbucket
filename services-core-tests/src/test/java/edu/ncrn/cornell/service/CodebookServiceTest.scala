package edu.ncrn.cornell.service


import edu.ncrn.cornell.model.{FieldInst, ProfileField, RawDoc}
import edu.ncrn.cornell.model.dao._
import org.junit.gen5.api.Test
import org.junit.gen5.junit4.runner.JUnit5
import org.junit.runner.RunWith
import org.junit.gen5.api.Assertions._
import org.mockito.Mockito._

import collection.JavaConverters._


/**
  * @author Brandon Elam Barker
  *         2/16/2017
  */

@RunWith(classOf[JUnit5])
class CodebookServiceTest extends CodebookServiceTesters {

  protected val rawDocDao = mock(classOf[RawDocDao])
  protected val profileDao = mock(classOf[ProfileDao])
  protected val fieldDao = mock(classOf[FieldDao])
  protected val profileFieldDao = mock(classOf[ProfileFieldDao])
  protected val mappingDao = mock(classOf[MappingDao])
  protected val schemaDao = mock(classOf[SchemaDao])
  protected val fieldInstDao = mock(classOf[FieldInstDao])
  protected val fieldIndiceDao = mock(classOf[FieldIndiceDao])

  protected val codeBookService: CodebookService = new CodebookService(
    rawDocDao, profileDao, fieldDao, profileFieldDao,
    mappingDao, schemaDao, fieldInstDao, fieldIndiceDao
  )

  
  protected val rawDoc_ssbv602 = new RawDoc
  rawDoc_ssbv602.setId("ssbv602")
  rawDoc_ssbv602.setCodebookId("ssbv602_codebook")
  //rawDoc_ssbv602.setRawXml() // See XMLHandleTest if needed
  rawDoc_ssbv602.setSchemaId("ddi")
  rawDoc_ssbv602.setSchemaVersion("2.5")

  protected val fieldInst_ssbv602_codebookname = new FieldInst
  fieldInst_ssbv602_codebookname.setId(12837L)
  fieldInst_ssbv602_codebookname.setFieldId("odebookname")
  fieldInst_ssbv602_codebookname.setRawDocId("ssbv602")
  fieldInst_ssbv602_codebookname
    .setCanonicalXpath("/codeBook/docDscr/citation/titlStmt/titl")
  fieldInst_ssbv602_codebookname.setValue("SIPP Synthetic Beta v6.02")

  // Setup default config for ssbv602
  protected val rawDocList = List(rawDoc_ssbv602)
  when(rawDocDao.findAll).thenReturn(rawDocList.asJava)
  when(fieldInstDao.findByRawDocIdAndFieldId("ssbv602", "codebookname"))
    .thenReturn(List(fieldInst_ssbv602_codebookname).asJava)

  // Setup default profiles:
  protected val codebookdetailsProfileFields = List(
    "codebookname", "codebookalt", "abstract",
    "codebookdist", "codebookcit", "varfiles"
  ).zipWithIndex.map{case (pName, pId) => makeProfileField(pId, pName)}

  when(profileFieldDao.findByProfileId("codebookdetails")).thenReturn(
    codebookdetailsProfileFields.asJava
  )
//  when(codeBookService.getProfileFieldIds("codebookdetails")).thenReturn(List(
//    "codebookname", "codebookalt", "abstract",
//    "codebookdist", "codebookcit", "varfiles"
//  ))


  @Test
  def getAllHandlesJsonTests: Unit = {

    assertEquals(rawDocList.size, rawDocDao.findAll().size)

    getAllHandlesJsonTests(codeBookService)
  }


  // TODO: Not easily made into a unit test; run just as an integration test now
  //    @Test
  //    def getCodebookDetailsListJsonTests: Unit = {
  //      getCodebookDetailsListJsonTests(codeBookService)
  //    }


  private def makeProfileField(pId: Int, pName: String): ProfileField = {
    val pf = new ProfileField
    pf.setFieldId(pName)
    pf
  }
}

