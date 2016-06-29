package juho.wagecalculator

import juho.wagecalculator.CsvReader.{EmployeeMarking, EmployeeSalary}
import org.scalatest.FlatSpec

/**
  * Created by juho on 6/27/16.
  */
class CsvReaderSpec extends FlatSpec {

  "EmployeeMarking" should "be constructed correctly from normal input" in {
    val marking = new EmployeeMarking(Array("Scott Scala", "2", "2.3.2014", "6:00", "14:00"))
    assert(marking.name == "Scott Scala")
    assert(marking.id == 2)
    assert(marking.date == "2.3.2014")
    assert(marking.start.timeStr == "6:00")
    assert(marking.end.timeStr == "14:00")
  }

  "parseCompensation" should "be correct with simple case" in {
    val markings = Seq(
      new EmployeeMarking(Array("Scott Scala", "2", "2.3.2014", "6:00", "14:00"))
    )
    assert(CsvReader.parseCompensation(markings).toString == "$30.00")
  }

  "parseCompensation" should "be correct with simple multiday case" in {
    val markings = Seq(
      new EmployeeMarking(Array("Scott Scala", "2", "2.3.2014", "6:00", "14:00")),
      new EmployeeMarking(Array("Scott Scala", "2", "3.3.2014", "7:00", "15:00")),
      new EmployeeMarking(Array("Scott Scala", "2", "4.3.2014", "8:00", "16:00"))
    )
    assert(CsvReader.parseCompensation(markings).toString == "$90.00")
  }

  "parseCompensation" should "be correct with simple same day case" in {
    val markings = Seq(
      new EmployeeMarking(Array("Scott Scala", "2", "2.3.2014", "6:00", "7:00")),
      new EmployeeMarking(Array("Scott Scala", "2", "2.3.2014", "8:00", "9:00"))
    )
    assert(CsvReader.parseCompensation(markings).toString == "$7.50")
  }

  "parseCompensation" should "be correct with 25% extra hours compensation" in {
    val markings = Seq(
      new EmployeeMarking(Array("Scott Scala", "2", "2.3.2014", "6:00", "14:00")),
      new EmployeeMarking(Array("Scott Scala", "2", "2.3.2014", "15:00", "17:00"))
    )
    assert(CsvReader.parseCompensation(markings).toString == "$39.38")
  }


  "parseCsv" should "be correct with simple case" in {
    val lines = Seq(
      "Person Name,Person ID,Date,Start,End",
      "Larry Lolcode,3,7.3.2014,6:00,11:00"
    )
    val results: Seq[EmployeeSalary] = CsvReader.parseCsv(lines)
    assert(results.size == 1, "Wrong amount of employee salaries parsed")
    assert(results.head.id == 3, "Wrong employee id")
    assert(results.head.name == "Larry Lolcode", "Wrong employee name")
    assert(results.head.salary == "$18.75", "Wrong employee salary")
  }
}
