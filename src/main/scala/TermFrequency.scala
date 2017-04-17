import scala.util.{Failure, Success, Try}

object TermFrequency {

  def processLine(line: Vector[String], words: Set[String]) : (Map[String, Int], Int) = {
    val defaultMap = words.foldLeft( (Map.empty[String, Int]) )( (z,i) => z + (i -> 0))
    def countWords(res: (Map[String, Int], Int), wordToProcess: String) = {
      words.contains(wordToProcess) match {
        case true =>
          val newCount = res._1.getOrElse(wordToProcess, 0) + 1
          (res._1 + (wordToProcess -> newCount), res._2 + 1)
        case false =>
          (res._1, res._2+1)
      }
    }
    line.foldLeft( (defaultMap, 0) )(countWords)
  }

  def combineLines(res: (Map[String, Int], Int), currentLine: (Map[String, Int], Int)) : (Map[String, Int], Int) = {
    val resMap = res._1
    val currentLineMap = currentLine._1
    val combinedMap = (resMap.keySet ++ currentLineMap.keySet).map {
      k => (k, resMap.getOrElse(k, 0) + currentLineMap.getOrElse(k, 0))
    }.toMap
    (combinedMap, res._2 + currentLine._2)
  }

  def processDocPar(fileName: String, words: Set[String]) : Map[String, Double] = {
    val (map, totalLength) = io.Source.fromFile(fileName).getLines.toIndexedSeq.par.map(line => {
      val wordsInLine = line.toLowerCase.split("\\W+").toVector
      processLine(wordsInLine, words)
    }).foldLeft( (Map.empty[String, Int], 0) )(combineLines)

    map.mapValues(v => v.toDouble/totalLength.toDouble)
  }

  def combineTfMaps(res: Map[String, (Double, String)], currentTfInfo: (String, Map[String, Double])) : Map[String, (Double, String)] = {
    val tfMap = currentTfInfo._2
    val tfDoc = currentTfInfo._1
    val combinedMap = (res.keySet ++ tfMap.keySet).map {
      k => (k, {
        val resVal = res.getOrElse(k, (0.0, "none"))
        val tfVal = tfMap.getOrElse(k, 0.0)
        if (resVal._1 >= tfVal)
          resVal
        else
          (tfVal, tfDoc)
      })
    }.toMap
    combinedMap
  }

  def main(args: Array[String]) {
    args.length match {
      case 2 =>
        val docs = args(0).split(",")
        val words = args(1).split(",").toSet
        val result = Try(docs.toVector.view.par.map(doc => (doc, processDocPar(doc, words)))) match {
          case Success(s) =>
            s.foldLeft((Map.empty[String, (Double, String)]))(combineTfMaps) mkString("\n")
          case Failure(ex) =>
            ex.getMessage
        }
        Console.println(result)
        System.exit(0)
      case _ =>
        Console.println("incorrect args")
        System.exit(0)
    }
  }
}
