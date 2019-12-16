package sflix

object Formatter {

  var report:String = ""

  def getXml(statement: Statement): String = {
    if (Globals.UseEmphasis) {
      report = "Streaming report for *%s* (%d)\n".format(statement.name, statement.id)
    } else {
      report = "Streaming report for %s (%d)\n".format(statement.name, statement.id)
    }

    report += "- " + statement.movie_title
    if (statement.qualityText != null) {
      report += " (" + statement.qualityText + ")"
    }
    report += " "
    report +=("%f CZK").format(statement.price * statement.qualitySurcharge)
    report += "\n"


    report += "\n"
    report += "Streamings: %i".format(statement.streamings.size)
    report += "\n"
    report += ("Movies: %i").format(statement.uniqueMovies)
    report += "\n"

    if (Globals.UseEmphasis) {
      report += ("Total: *%f* CZK".format(statement.total))
    } else {
      report += ("Total: %f CZK".format(statement.total))
    }

    report += "\n"
    report += "Points: %d".format(statement.loyaltyPoints)
    report += "\n"
    report
  }
}
