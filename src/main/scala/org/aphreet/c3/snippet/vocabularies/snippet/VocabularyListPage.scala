package org.aphreet.c3.snippet.vocabularies.snippet

import net.liftweb.util.Helpers._
import org.aphreet.c3.model.Vocabulary
import org.aphreet.c3.util.helpers.AdminPageHelper

class VocabularyListPage extends AdminPageHelper {

  override lazy val activeLocId = "vocabularies"

  def render = {
    val vocabularies = Vocabulary.findAll()
    ".vocabularies-list" #> vocabularies.map {
      v: Vocabulary =>
        ".vocabulary-name" #> v.name.is
    }
  }
}
