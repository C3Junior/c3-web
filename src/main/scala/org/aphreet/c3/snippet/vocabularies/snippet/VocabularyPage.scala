package org.aphreet.c3.snippet.vocabularies.snippet

import net.liftweb.common.Full
import net.liftweb.http.{ S, SHtml }
import net.liftweb.util.Helpers._
import org.aphreet.c3.model.{ User, Vocabulary }
import org.aphreet.c3.util.helpers.AdminPageHelper

import scala.xml.NodeSeq

class VocabularyPage extends AdminPageHelper {

  override lazy val activeLocId = "vocabularies"

  def list = {
    val vocabularies = Vocabulary.findAll()
    ".vocabularies-list" #> vocabularies.map {
      v: Vocabulary =>
        ".vocabulary-name *" #> v.name.is &
          ".edit-link [data-vocabulary-id]" #> v.id.is
    }
  }

  def create = {
    User.currentUser match {
      case Full(user) =>
        "#new-vocabulary" #> submitNew
      case _ =>
        "#new-vocabulary" #> NodeSeq.Empty
    }
  }

  def edit = {
    User.currentUser match {
      case Full(user) =>
        "#edit-vocabulary" #> submitEdited
      case _ =>
        "#edit-vocabulary" #> NodeSeq.Empty
    }
  }

  private def submitNew() = {
    val vocabulary = Vocabulary.create

    "name=name" #> SHtml.onSubmit(vocabulary.name(_)) &
      "type=submit" #> SHtml.onSubmitUnit(() => save(vocabulary))
  }

  private def submitEdited() = {
    var name = ""
    var id = ""

    "name=vocabulary-id" #> SHtml.onSubmit(id = _) &
      "name=name" #> SHtml.onSubmit(name = _) &
      "type=submit" #> SHtml.onSubmitUnit(() => save(Vocabulary.find(id).openOrThrowException("Wrong id").name(name)))
  }

  private def save(vocabulary: Vocabulary) =
    vocabulary.validate match {
      case Nil => {
        vocabulary.save()
        S.notice("Category is added")
      }
      case xs =>
        xs.foreach(f => S.error(f.msg))

    }
}
