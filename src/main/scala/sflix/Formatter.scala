package sflix

class Formatter {

  var report:String = ""

  def getXml(statement: Statement): String = {
    if (Globals.UseEmphasis) {
      report = "Streaming report for *" + name + "* (" + id + ")\n"
    } else {
      report = "Streaming report for " + name + " (" + id + ")\n"
    }

    report += "- " + movie.title
    if (qualityText != null) {
      report += " (" + qualityText + ")"
    }
    report += " "
    report += price * qualitySurcharge + " CZK"
    report += "\n"


    report += "\n"
    report += ("Streamings: " + streamings.size)
    report += "\n"
    report += ("Movies: " + uniqueMovies)
    report += "\n"

    if (Globals.UseEmphasis) {
      report += ("Total: *" + total + "* CZK")
    } else {
      report += ("Total: " + total + " CZK")
    }

    report += "\n"
    report += ("Points: " + loyaltyPoints)
    report += "\n"
    report
  }
}
