package org.aphreet.c3.service.knowledge_base.implementation

import org.aphreet.c3.service.knowledge_base.KbService
import org.aphreet.c3.util.C3Loggable
import org.aphreet.c3.model.KnowledgeUnit

class KnowledgeBaseService extends KbService with C3Loggable {

  override def getRootUnits(): Iterable[KnowledgeUnit] = {
    KnowledgeUnit.findAll()
  }
}