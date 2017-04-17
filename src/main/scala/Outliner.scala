import scala.collection.mutable

object Outliner {
  case class Heading(weight: Int, text: String)
  case class Node(heading: Heading, children: List[Node])

  def main(args: Array[String]) {
    val headings: Iterator[Heading] = Iterator(Heading(1, "one"), Heading(2, "two"))
    val outline: Node = toOutline(headings)
    val html: String = toHtml(outline)
    println(html)
  }

  /** Converts a list of input headings into nested nodes
    * according to directions, this assumes only one H1 heading*/
  def toOutline(headings: Iterator[Heading]): Node = {

    //nodeStack will be list of H1 nodes
    var nodeStack = List[Node]()
    if (headings.hasNext) {
      var heading = headings.next()
      nodeStack = nodeStack :+ Node(Heading(heading.weight, heading.text), Nil)
    }
    var currentWeight = 0
    var currentText = ""

    while(headings.hasNext) {
      currentWeight = headings.next().weight
      currentText = headings.next().text
      var currentNode = Node(Heading(currentWeight, currentText), Nil)

//      if (currentWeight > nodeStack.last.heading.weight) { //keep going down
//        nodeStack.last.children :+ currentNode
//      }
      if (currentWeight == nodeStack.last.heading.weight) { //at same level, pop last node and add to new last node's children
        while (nodeStack.last.heading.weight == currentWeight) {
          nodeStack.dropRight(1)
        }
      }
      else { //go back up, explored current top of stack as far as we could
        while (nodeStack.last.heading.weight != currentWeight) {
          nodeStack.dropRight(1)
        }
      }
      nodeStack.last.children :+ currentNode
      nodeStack :+ currentNode
    }
    println(nodeStack.size)
    nodeStack(0)
  }

  /** Parses a line of input.
      This implementation is correct for all predefined test cases. */
  def parse(record: String): Option[Heading] = {
    val H = "H(\\d+)".r
    record.split(" ", 2) match {
      case Array(H(level), text) =>
        scala.util.Try(Heading(level.toInt, text.trim)).toOption
      case _ => None
    }
  }

  /** Converts a node to HTML.
      This implementation is correct for all predefined test cases. */
  def toHtml(node: Node): String = {
    val childHtml =
      if (node.children.isEmpty) ""
      else "<ol>" +
        node.children.map(
          child => "<li>" + toHtml(child) + "</li>"
        ).mkString("\n") +
        "</ol>"

    node.heading.text + "\n" + childHtml
  }
}
