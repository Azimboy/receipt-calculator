package ru.itmo.fpiikt

import java.io.InputStream

import scala.io.Source

object Calculator {

  private def loadStream(sourceName: String): InputStream = {
    getClass.getClassLoader.getResourceAsStream(s"$sourceName.txt")
  }

  def main(args: Array[String]): Unit = {
    val lines = Source.fromInputStream(loadStream("26.06.2020")).getLines()

    val receipts = lines.zipWithIndex.map { case (line, id) =>
      val lines = line.trim.split(",")
      val payer = lines(0)
      val paid = lines(1).toDouble
      val separatePaids = if (lines.size > 2 ) {
        lines(2).trim.split(";").map { spLine =>
          val spLines = spLine.trim.split(":")
          (spLines(0), spLines(1).toDouble)
        }.toSeq
      } else {
        Seq.empty[(String, Double)]
      }

      Receipt(id, payer, paid, separatePaids)
    }

    val consumersResult = receipts.foldLeft(Consumers) { (acc, receipt) =>
      println(receipt)
      val separatePaids = receipt.separatePaids.groupBy(_._1).mapValues { seq =>
        seq.reduce((x, y) => (x._1, x._2 + y._2))._2
      }
      acc.transform { (code, cost) =>
        val total = receipt.paid - separatePaids.values.sum
        val foreach = total / Consumers.size
        val paid = if (code == receipt.payer) receipt.paid else 0
        val separate = separatePaids.getOrElse(code, 0.0)
        cost.copy(cost.total + paid, cost.mutual + paid - foreach - separate, cost.separate + separate)
      }
    }

    println(consumersResult.mkString("\n"))
  }

}
