/*
 * Copyright (c) 2015, streamdrill UG (haftungsbeschränkt)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package streamdrill.json

import net.minidev.json.parser.{JSONParser => SmartParser}

class JSONParserException(ex: Exception) extends RuntimeException(ex)

/**
 * Convenience Object to parse JSON
 * (currently the SmartJSONParser is the fastest)
 *
 * @author Matthias L. Jugel
 */
object JSONParser extends SmartJSONParser

/**
 * Trait that is used for JSON parsing
 */
trait JSONParser {
  def parse(s: String): JSONObject
}


/**
 * JSON Parser using the smart-json library
 */
class SmartJSONParser extends JSONParser {
  private val parser = new ThreadLocal[SmartParser] {
    override def initialValue = new SmartParser(SmartParser.MODE_JSON_SIMPLE | SmartParser.IGNORE_CONTROL_CHAR)
  }

  def parse(s: String) = try {
    new JSONObject(parser.get.parse(s))
  } catch {
    case ex: Exception => throw new JSONParserException(ex)
  }
}

///**
// * JSON Parser using the simple-json library
// */
//class SimpleJSONParser extends JSONParser {
//  private val parser = new ThreadLocal[SimpleParser] {
//    override def initialValue = new SimpleParser()
//  }
//
//  def parse(s: String) = new JSONObject(parser.get.parse(s))
//}
//
///**
// * JSON Parser using the jackson parser.
// */
//class JacksonJSONParser extends JSONParser {
//  private final val mapper = new ObjectMapper
//
//  def parse(s: String): JSONObject = new JSONObject(mapper.readValue(s, classOf[java.util.Map[String, Any]]))
//}
