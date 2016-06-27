package juho.wagecalculator

import scala.util.control.TailCalls.TailRec

/** Reader for salary CSV
  */
object CsvReader {

  /** Parses the lines of a CSV into salary information
    *
    * @param lines  Csv file as list of lines
    * @return       Users with salaries in a Seq of UserSalary objects
    */
  def parseCsv(lines: List[String]): Seq[UserSalary] = {
    val userMarkingsMap: Map[Long, Seq[UserMarking]] = lines.drop(1)
        .map(_.split(","))
        .map(l => new UserMarking(l))
        .groupBy(_.id)

    val salaries: Seq[Money] = userMarkingsMap.values
        .map(ms => parseCompensation(ms, LastWork(0, "")))
        .toSeq
    userMarkingsMap.values
        .map(_.head)
        .zip(salaries)
        .map{ case (m, s) => UserSalary(m.id, m.name, s.toString)}
        .toSeq
  }

  /** Parses the salary of a user from the seq of user markings
    *
    * @param userMarkings Work markings of one user
    * @param lastWork     Prior work
    * @return             Total compensation employee should receive
    */
  @TailRec
  def parseCompensation(userMarkings: Seq[UserMarking], lastWork: LastWork): Money = {
    if (userMarkings.isEmpty)
      return Money.fromCurrency(0, 0)
    val first: UserMarking = userMarkings.head
    val sameDayWork: Int = if (first.date.equals(lastWork.date)) lastWork.time else 0
    val nextLastWork = LastWork(WageCalculator.totalQuarters(first.start, first.end) + sameDayWork, first.date)
    WageCalculator.compensation(sameDayWork, first.start, first.end) + parseCompensation(userMarkings.tail, nextLastWork)
  }

  /** Container for earlier work done
    *
    * @param time Amount of the work in quarters
    * @param date Date when the work started
    */
  case class LastWork(time: Int, date: String)

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
  case class UserMarking(name: String, id: Long, date: String, start: TimeStamp, end: TimeStamp) {
    def this(line: Array[String]) = this(line(0), line(1).toLong, line(2), TimeStamp(line(3)), TimeStamp(line(4)))
  }

  /** The final format for employee salary data
    *
    * @param id     Id of the user
    * @param name   Name of the user
    * @param salary Salary to be received
    */
  case class UserSalary(id: Long, name: String, salary: String)
}
