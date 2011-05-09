package org.aphreet.c3.snippet

/**
 * Copyright (c) 2011, Dmitry Ivanov
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *

 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the IFMO nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import net.liftweb._
import common.{Logger, Full, Empty, Box}
import http._
import util._
import Helpers._
import javax.activation.MimetypesFileTypeMap
import org.aphreet.c3.apiaccess.{C3ClientException, C3Client}
import org.apache.commons.httpclient.util.URIUtil

/**
 * Attach a function to the uploaded file.
 */


class UploadFileAjax {

  val logger = Logger(classOf[UploadFileAjax])

  // the request-local variable that hold the upload path (in c3) file parameter
  private object theUploadPath extends SessionVar[Box[String]](Empty)

  private def uploadFile(fph: FileParamHolder) = {

    println("Got a file "+fph.fileName)

    // this simple technique helps to predict uploaded file's type by it's name
    val mimeType: String = new MimetypesFileTypeMap().getContentType(fph.fileName)
    theUploadPath.is match {
      case Full(path) => {
        try {

            C3Client().uploadFile( URIUtil.decode(path,"UTF-8") + fph.fileName , fph.file, Map("content.type" -> mimeType))
        }
        catch {
            case e: C3ClientException => {
              S.error(e.toString)
            }
        }
      }
      case _ => {
        println("Unknown upload c3 path for "+fph.fileName)
      }
    }


  }

  def render = "type=file [name]" #> {

    theUploadPath(if(S.uri.contains("/group/")) Full(S.uri.split("/group/").last+"/") else Empty)
    println(theUploadPath.is.openOr(""))

    SHtml.fileUpload(fph => {
      uploadFile(fph)
    }).attribute("name").get
  }
}