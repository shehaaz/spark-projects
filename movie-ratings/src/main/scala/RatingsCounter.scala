import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object RatingsCounter extends App {

  override def main(args: Array[String]): Unit = {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "RatingsCounter")

    //Read in each rating line
    val lines = sc.textFile(path = "ml-100k/u.data")

    /**
      * Split each line out by tabs and extract the third field
      * File format: userID, movieID, rating, timestamp
      * Map to (movieID, 1) tuples. Initialized count to 1.
      */
    val ratings = lines.map(x => x.split("\t")(2))

    //Count how many times a rating occurs
    val results = ratings.countByValue()

    //Sort the resultig Map of (rating, count) tuples.
    val sortedResults = results.toSeq.sortBy(_._1)

    sortedResults.foreach(println)

    /*
    There are 6110 one star revies and 21201 five star reviews
    (1,6110)
    (2,11370)
    (3,27145)
    (4,34174)
    (5,21201)
     */
  }
}
