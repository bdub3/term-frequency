import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

class TermFrequencyTest extends FlatSpec with Matchers {

  val termTest1 = Source.fromURL(getClass.getResource("/termTest1.txt"))
  val termTest2 = Source.fromURL(getClass.getResource("/termTest2.txt"))
//  val termTestNone = Source.fromURL(getClass.getResource("/termTestNone1.txt"))
  val termTest3 = Source.fromURL(getClass.getResource("/termText3.txt"))
  val termTest4 = Source.fromURL(getClass.getResource("/termText4.txt"))
  val x = getClass.getResource("/termTestNone1.txt").toString

  val word = "and"


  //test for no words
  "A file with no key words" should "have a value 0 in the map" in {
    val result = TermFrequency.processDocPar(x, Set(word))
    result
  }

  //test for words and

  //test for words 'and' and 'to'

  //test for no existing file
}
