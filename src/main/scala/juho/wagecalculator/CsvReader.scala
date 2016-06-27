package juho.wagecalculator

import scala.annotation.tailrec

/** Reader for salary CSV
  */
object CsvReader {

  /** Parses the lines of a CSV into salary information
    *
    * @param lines  Csv file as list of lines
    * @return       Users with salaries in a Seq of UserSalary objects
    */
  def parseCsv(lines: List[String]): Seq[EmployeeSalary] = {
    val userMarkingsMap: Map[Long, Seq[EmployeeMarking]] = lines.drop(1)
        .map(_.split(","))
        .map(l => new EmployeeMarking(l))
        .groupBy(_.id)

    val salaries: Seq[Money] = userMarkingsMap.values
        .map(ms => parseCompensation(ms))
        .toSeq

    userMarkingsMap.values
        .map(_.head)
        .zip(salaries)
        .map{ case (m, s) => EmployeeSalary(m.id, m.name, s.toString) }
        .toSeq
  }

  /** Parses the salary of a user from the seq of user markings
    *
    * @param markings           Work markings of one employee
    * @param lastWork           Prior work
    * @param priorCompensation  Prior compensation
    * @return                   Total compensation employee should receive
    */
  @tailrec
  def parseCompensation(
                         markings: Seq[EmployeeMarking],
                         lastWork: LastWork = LastWork(),
                         priorCompensation: Money = Money(0, 0)
                       ): Money = {
    if (markings.isEmpty)
      return priorCompensation
    val first: EmployeeMarking = markings.head
    val sameDayWork: Int = if (first.date.equals(lastWork.date)) lastWork.time else 0
    val nextLastWork = LastWork(WageCalculator.totalQuarters(first.start, first.end) + sameDayWork, first.date)
    val compensation: Money = priorCompensation + WageCalculator.compensation(sameDayWork, first.start, first.end)
    parseCompensation(markings.tail, nextLastWork, compensation)
  }

  /** Container for earlier work done
    *
    * @param time Amount of the work in quarters
    * @param date Date when the work started
    */
  case class LastWork(time: Int = 0, date: String = "")

  /** Wrapper for CSV file line
    *
    * TODO: Construction should include handling for bad values
    *
    * @param name   Name of the user
    * @param id     Id of the user
    * @param date   Date of the start of the marking
    * @param start  Timestamp of the start of the marking
    * @param end    Timestamp of the end of the marking
    */
  case class EmployeeMarking(name: String, id: Long, date: String, start: TimeStamp, end: TimeStamp) {
    def this(line: Array[String]) = this(line(0), line(1).toLong, line(2), TimeStamp(line(3)), TimeStamp(line(4)))
  }

  /** The final format for employee salary data
    *
    * @param id     Id of the user
    * @param name   Name of the user
    * @param salary Salary to be received
    */
  case class EmployeeSalary(id: Long, name: String, salary: String)
}
