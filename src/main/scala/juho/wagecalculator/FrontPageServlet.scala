package juho.wagecalculator

import org.scalatra._

class FrontPageServlet extends WagecalculatorStack {

  get("/") {
    <html>
      <body>
        <h1>Wage Calculator</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

}
