package org.aphreet.c3.api

import java.lang.{String, Integer}
import net.liftweb.json.JsonAST._
import net.liftweb.http.{XmlResponse, JsonResponse, GetRequest, Req}
import net.liftweb.http.rest.RestHelper
import net.liftweb.util.BasicTypesHelpers.AsInt
import org.aphreet.c3.model.{Group, User, UserGroup}


object Rest extends RestHelper {

/*
 just examples
 serve {
    case Req("api" :: "static" :: _, "xml", GetRequest) => <b>Static</b>
    case Req("api" :: "static" :: _, "json", GetRequest) => JString(compact(JsonAST.render(json2)))
}

  serve {

    case Req("api" :: "static" :: _, "json", GetRequest) => JString(UserGroup.findAll().toString())
  }
*/


  /*
  1. login
   */
  serve ( "api" / "login" prefix {
    // /api/login returns auth_key
    case Nil JsonPost _  => JString( " {" +
      "\"auth_key\": <auth_key>" +
      "}")

  })

  /*
  2. get groups
   */
  serve ( "api" / "groups" prefix {
    // /api/groups returns all current user's groups
    case Nil JsonGet _  => JString(UserGroup.findAll().toString())


  /*

  3. get group by id
   */
    // /api/groups/id returns group with specified id or 404
    case Group(group) :: Nil JsonGet _ => JString(new Group().toString())

  })


  /*

  4. get resources for folder
   */
  serve {
    case "api" :: "resources" :: folderId :: _ JsonGet _ => JString("json array");

  }
  /*

  5. get resource by id
   */
  serve {
    case "api" :: "resources" :: resourceId :: _ JsonGet _ => JString("resource object");
  }


  /*
  6. get user by id
   */

  serve {
    case "api" :: "resources" :: userId :: _ JsonGet _ => JString("User object");
  }


  /*
  7. get logs for group
   */

  serve {
    case "api" :: "logs" :: group_id :: _ JsonGet _ => JString("Logs array, example in api requirements");
  }


  /*
  8. get comments for log
   */

  serve {case  "api" ::  " comments" :: log_id :: _ JsonGet _ => JString (
      "[{" +
      "\"ownerId\" : 123, " +
      "\"text\” : \“nice!\” " +
      "\"date\" : \"Sat, 6 Dec 2014 13:02:44 GMT\"" +
      "}]"
      )

  }
  /*
  9. post comment
  */
  serve {
    case "api" :: "comments" :: AsInt(log_id) :: message :: _ JsonPut _ => JString(addMessage(log_id, message))
  }

/*  serve ( "api" / "comments" prefix {

    // PUT adds the item if the JSON is parsable
    case Nil JsonPut JString("comment object") -> _ => JString(addMessage("comment object"))

  })*/

  def addMessage (logId : Integer, message : String) : String = {
    /*
      here is adding message code
     */
    ""
  }


}