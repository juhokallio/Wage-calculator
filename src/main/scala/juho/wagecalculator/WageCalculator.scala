package juho.wagecalculator

/** Calculation operations for the wages and work time
  */
object WageCalculator {

  // Normal time compensation per 15min, around $0.94
  val normalCompensation: Money = Money.fromCurrency(3, 75).divideBy4()

  // Compensation of the evening 15min, around $1.23
  val eveningCompensation: Money = normalCompensation + Money.fromCurrency(1, 15).divideBy4()

  // Begin and end of the normal working day
  val normalDayStart: TimeStamp = TimeStamp("6:00")
  val normalDayEnd: TimeStamp = TimeStamp("18:00")


  /** Calculates the total compensation from the time of work
    *
    * This is the function to be used for getting to know the monetary value of one work hour marking.
    *
    * @param priorWork  Amount of work on the same day, in 15min units
    * @param start      Moment when the work shift started
    * @param end        Moment when the work shift ended
    * @return           Money that the work shift was worth for the employee
    */
  def compensation(priorWork: Int, start: TimeStamp, end: TimeStamp): Money = {
    val normalMoney: Money = normalCompensation * normalQuarters(priorWork, start, end)
    val eveningMoney: Money = eveningCompensation * eveningWork(start, end)
    val plus25money: Money = (normalCompensation + normalCompensation.divideBy4()) * plus25work(priorWork, start, end)
    val plus50money: Money = (normalCompensation + normalCompensation.divideBy4() + normalCompensation.divideBy4()) * plus50work(priorWork, start, end)
    val plus100money: Money = (normalCompensation + normalCompensation) * plus100work(priorWork, start, end)

    normalMoney + eveningMoney + plus25money + plus50money + plus100money
  }

  /** Total count of 15min time units in a work shift
    *
    * @param start      Moment when the work shift started
    * @param end        Moment when the work shift ended
    * @return           Count of the quarters between the two timestamps
    */
  def totalQuarters(start: TimeStamp, end: TimeStamp): Int = {
    if (start.isBefore(end))
      start.gap(end)
    else
      start.gap(TimeStamp("24:00")) + TimeStamp("0:00").gap(end)
  }

  /** Amount of normal time within a workshift
    *
    * This means time between 6:00 and 18:00, and doesn't care about the amount of work, e.g. on a shift of 6:00-18:00
    * all the time is considered normal here, even though some will get extra compensation
    *
    * @param start      Moment when the work shift started
    * @param end        Moment when the work shift ended
    * @return           Amount of work within normal hours
    */
  def normalTime(start: TimeStamp, end: TimeStamp): Int = {
    val dayHoursStart: TimeStamp = if (normalDayStart.isBefore(start)) start else normalDayStart
    val dayHoursEnd: TimeStamp = if (normalDayEnd.isBefore(end)) normalDayEnd else end

    if (start.isBefore(end))
      dayHoursStart.gap(dayHoursEnd)
    else {
      // The normal hours in the beginning and end of the overnight shift
      dayHoursStart.gap(normalDayEnd) + normalDayStart.gap(dayHoursEnd)
    }
  }

  /** Amount of normal time within a workshift
    *
    * This method takes into account prior work and also limits the normal quarters to be max 32 per day, as this is the
    * number that should be used for normal compensation calculations.
    *
    * @param priorWork  Amount of work on the same day, in 15min units
    * @param start      Moment when the work shift started
    * @param end        Moment when the work shift ended
    * @return           Amount of work that can be paid only the normal wage
    */
  def normalQuarters(priorWork: Int, start: TimeStamp, end: TimeStamp): Int = {
    math.min(math.max(0, 32 - priorWork), normalTime(start, end))
  }

  /** Amount of time outside normal hours
    *
    * @param start      Moment when the work shift started
    * @param end        Moment when the work shift ended
    * @return           Work time that can be had with extra evening compensation
    */
  def eveningWork(start: TimeStamp, end: TimeStamp): Int = {
    totalQuarters(start, end) - normalTime(start, end)
  }

  /** Work time that should be billed extra 25%, e.g. work between the 8th and 10th hour of the day
    *
    * @param priorWork  Amount of work on the same day, in 15min units
    * @param start      Moment when the work shift started
    * @param end        Moment when the work shift ended
    * @return           Work time after first 8h and before 10h
    */
  def plus25work(priorWork: Int, start: TimeStamp, end: TimeStamp): Int = {
    val overtime = math.max(0, priorWork + totalQuarters(start, end) - 32)
    math.min(overtime, 8)
  }

  /** Work time that should be billed extra 50%, e.g. work between the 10th and 12th hour of the day
    *
    * @param priorWork  Amount of work on the same day, in 15min units
    * @param start      Moment when the work shift started
    * @param end        Moment when the work shift ended
    * @return           Work time after first 10h and before 12h
    */
  def plus50work(priorWork: Int, start: TimeStamp, end: TimeStamp): Int = {
    val overtime = math.max(0, priorWork + totalQuarters(start, end) - 40)
    math.min(overtime, 8)
  }

  /** Work time that should be billed extra 100%, time after working 12 hours on one day
    *
    * @param priorWork  Amount of work on the same day, in 15min units
    * @param start      Moment when the work shift started
    * @param end        Moment when the work shift ended
    * @return           Work time after working 12 hours
    */
  def plus100work(priorWork: Int, start: TimeStamp, end: TimeStamp): Int = {
    math.max(0, priorWork + totalQuarters(start, end) - 48)
  }
}
