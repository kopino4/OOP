package sflix

import scala.xml.XML

/**
 *
 * @param movieId id of the movie
 * @param quality streaming quality which can be one of
 *   - `1` - Standard Definition (SD)
 *   - `2` - High Definition (HD)
 *   - `3` - 4K (4K)
 */
class Streaming(val movieId: Int, val quality: Int)

object Customer {
  // TODO refactor it so it is possible to add other file formats (i.e. JSON) [1pt]
  def load(file: String): Seq[Customer] = {
    val doc = XML.loadFile(file)
    for {
      customer <- doc \ "customer"
    } yield {
      val id = (customer \@ "id").toInt
      val name = customer \@ "name"
      val streamings = for {
        s <- customer \\ "streaming"
        movieId = s \@ "movie"
        quality = s \@ "quality"
      } yield new Streaming(movieId.toInt, quality.toInt)

      new Customer(id, name, streamings)
    }
  }
}

class Customer(val id: Int, val name: String, val streamings: Seq[Streaming]) {

  // TODO remove global state [1pt]
  val movieService = ServiceLocator.movieService

  // TODO this method does too much, refactor the statement computation and formatting [4pt] ========= DONE
  // Hint: This method does two things - computes a statement and formats into a string.
  // This is problematic since we cannot reuse it for any other report format.
  // A way to think about it is that instead of returning a String, you can return some intermediate representation
  // of the statement.
  // A statement has a customer, some stats (number of streaming, movies, ...) and a list of streamed movies, where
  // each streamed movie has contains the movie itself, streaming quality and price.
  // The statement class can be then an input into into a formatter which turns it into a String.

  // Is the loyalty points bonus active?
  // If it is active, we double all the points
def statement(LoyaltyPointsBonus: Boolean): Statement = {

    var statement = new Statement
    statement.name = this.name
    statement.id = this.id
    statement.streamings = this.streamings

    for (streaming <- streamings) {
      val movie = movieService.movieById(streaming.movieId)

      statement.movie_title = movie.title

      if (movie != null) {
        // if movie is not in the movie database (it can happen, streaming industry is a dynamic business)
        // we simply ignore it, consider it a free gift from sflix.
        movie.category match {
          case 1 =>
            // regular movies
            statement.price = 2
            statement.qualitySurcharge = 1.0
            if (streaming.quality == 1) {
              // normal quality -- no surcharge
            } else if (streaming.quality == 2) {
              // HD quality
              statement.qualitySurcharge = 1.25
              statement.qualityText = "HD"
            } else if (streaming.quality == 3) {
              // 4K
              statement.qualitySurcharge = 1.5
              statement.qualityText = "4K"
            } else {
              println("Unknown streaming quality")
              sys.exit(1)
            }
          case 2 =>
            // new releases
            statement.price = 3
            if (streaming.quality == 1) {
              // normal quality -- no surcharge
            } else if (streaming.quality == 2) {
              // HD quality
              statement.qualitySurcharge = 1.5
              statement.qualityText = "HD"
            } else if (streaming.quality == 3) {
              // 4K
              statement.qualitySurcharge = 1.75
              statement.qualityText = "4K"
            }
          case 3 =>
            // for kids -- no quality surcharge
            statement.price = 2
            if (streaming.quality == 2) {
              // HD quality
              statement.qualityText = "HD"
            } else if (streaming.quality == 3) {
              // 4K
              statement.qualityText = "4K"
            }
        }

        // one point per streaming
        statement.loyaltyPoints += 1

        // extra point for better streaming quality
        if (streaming.quality >= 2) {
          statement.loyaltyPoints += 1
        }

        statement.total += statement.price * statement.qualitySurcharge
      }
    }

    // extra points for each streaming over 2
    if (streamings.size >= 2) {
      statement.loyaltyPoints += streamings.size - 2
    }

    // if the bonus is on, double the points
    if (LoyaltyPointsBonus) {
      statement.loyaltyPoints *= 2
    }

    var seenMovies = scala.collection.mutable.Set[Int]()
    for (streaming <- streamings) {
      if (!seenMovies.contains(streaming.movieId)) {
        statement.uniqueMovies += 1
        seenMovies.add(streaming.movieId)
      }
    }
    statement
  }
}
