package org.aphreet.c3.snippet.knowledge_base.snippet

import scala.language.postfixOps
import org.aphreet.c3.model._
import net.liftweb.util.Helpers._
import net.liftweb.util.CssSel
import net.liftweb.common.{ Empty, Full }
import scala.xml.NodeSeq
import net.liftweb.http.{ SHtml, S }

class KnowledgeBaseListPage {

  def list(): CssSel = {
    val kUnitList = KnowledgeUnit.findAll().toList

    (User.currentUser match {
      case Full(u) => "#add_knowledge" #> addKnowledge()
      case Empty =>
        "#add_knowledge" #> NodeSeq.Empty &
          "#add-knowledge-btn" #> NodeSeq.Empty
    })&
      ".container_k_unit" #> kUnitList.map {
        kUnit: KnowledgeUnit =>
          "a *" #> kUnit.name.is &
            "a [href]" #> "#" &
            ".knowledge_description *" #> kUnit.description &
            ".knowledge_owner  *" #> kUnit.owner.obj.map(_.shortName).openOr("N/A")
      }
  }
  def addKnowledge(): CssSel = {
    var newKnowledge = KnowledgeUnit.create

    def saveMe() = {
      newKnowledge.validate match {
        case Nil =>
          newKnowledge = newKnowledge.owner(User.currentUserUnsafe)
          if (newKnowledge.save) S.notice(S.?("knowledge.added") + newKnowledge.name)
          else S.warning(newKnowledge.name + " isn't added")

        case xs =>
          xs.foreach(f => S.error(f.msg))
      }
    }

    "name=name" #> SHtml.onSubmit(newKnowledge.name(_)) &
      "name=description" #> SHtml.onSubmit(newKnowledge.description(_)) &
      "name=resource" #> SHtml.onSubmit(newKnowledge.resource(_)) &
      "type=submit" #> SHtml.onSubmitUnit(saveMe)
  }
}
