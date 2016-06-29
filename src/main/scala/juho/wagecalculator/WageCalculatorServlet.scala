package juho.wagecalculator

import juho.wagecalculator.CsvReader.EmployeeSalary
import org.scalatra.ScalatraServlet

import scala.io.Source
import org.scalatra.servlet.{FileItem, FileUploadSupport}

import scala.xml.Unparsed


/** Servlet for the UI
  *
  * Not necessarily the best practice here for a bigger project, but for a simple thing like this, can you really get
  * any leaner? If I had more time, a single page app with AJAX file upload would be fancier, maybe.
  */
class WageCalculatorServlet extends ScalatraServlet with FileUploadSupport {

  /** The landing page
    *
    * Performance wise a static HTML page would be smarter, but lets just get everything here for once.
    */
  get("/") {
    <html>
      <head>
        <meta charset="UTF-8">
        </meta>
        <title>Wage Calculator</title>
      </head>
      <body>
        <h1>Wage Calculator</h1>

        <p>Calculate wages from your own CSV file or download our sample first!</p>
        <a href="/csv/HourList201403.csv">Download sample</a>
        <form action="/wages" method="POST" enctype="multipart/form-data" >
          <label>Process file</label>
          <input type="file" accept=".csv" name="wageCsv"/>
          <input type="submit" value="Calculate wages"/>
        </form>
        <br />
        <h3>The small print</h3>
        <p>
          All our poor employees get only 3.75$ per hour, but enjoy some minor benefits to game the system.
        </p>
        <ul>
          <li>The hours outside 6:00-18:00 net extra 1.15$.</li>
          <li>First two hours after 8 hour work day get +25%, but this is calculated only from the base 3.75$.</li>
          <li>First two hours after 10 hour work day get +50%, with similar logic.</li>
          <li>After that, the wage multiplier is +100%.</li>
          <li>If a work shift continues after midnight, the hours are calculated to be part of the first day.</li>
        </ul>
      </body>
    </html>
  }

  /** Result page
    *
    * This is where the input file will be read and result shown.
    */
  post("/wages") {
    val file: FileItem = fileParams("wageCsv")
    val lines: Seq[String] = Source.fromInputStream(file.getInputStream).getLines.toSeq
    val salaries: Seq[EmployeeSalary] = CsvReader.parseCsv(lines)

    <html>
      <head>
        <meta charset="UTF-8">
        </meta>
        <title>Wage Calculator - Results</title>
      </head>
      <body>
        <h3>Employee Wages</h3>
        <table>
        { Unparsed(salaries.map(s => <tr><td>{s.id}</td><td>{s.name}</td><td>{s.salary}</td></tr>).mkString("")) }
        </table>
      </body>
    </html>
  }

}
