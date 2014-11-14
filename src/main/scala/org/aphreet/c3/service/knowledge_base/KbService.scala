package org.aphreet.c3.service.knowledge_base

import org.aphreet.c3.model.{ KnowledgeUnit }

trait KbService {

  def getRootUnits(): Iterable[KnowledgeUnit]

}
