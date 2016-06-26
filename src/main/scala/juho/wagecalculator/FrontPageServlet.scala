package juho.wagecalculator

import org.scalatra.ScalatraServlet
import scala.io.Source
import org.scalatra.servlet.{FileItem, FileUploadSupport}


class FrontPageServlet extends ScalatraServlet with FileUploadSupport {

  get("/") {
    <html>
      <head>
        <meta charset="UTF-8">
        </meta>
        <title>Wage Calculator</title>
      </head>
      <body>
        <h3>The real deal</h3>

        <p>Calculate wages from your own CSV file or download our sample first!</p>
        <a href="/csv/HourList201403.csv">Download sample</a>
        <form action="/wages" method="post" enctype="multipart/form-data" >
          <label>Upload file</label>
          <input type="file" accept=".csv" name="wageCsv"/>
          <input type="submit" value="Calculate wages"/>
        </form>
      </body>
    </html>
  }

  post("/wages") {
    val f: FileItem = fileParams("wageCsv")
    val lines: Iterator[String] = Source.fromInputStream(f.getInputStream).getLines()

    <p>Wow!</p>
  }


}
