import org.apache.log4j._
import org.apache.spark._

object PopularMovies extends App {

  override def main(args: Array[String]): Unit = {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val sc = new SparkContext("local[*]", "PopularMovies")

    //Read in each rating line
    val lines = sc.textFile(path = "ml-100k/u.data")

    /**
      * Split each line out by tabs and extract the second field
      * File format: userID, movieID, rating, timestamp
      * Map to (movieID, 1) tuples. Initialized count to 1.
      */
    val movies = lines.map(x => (x.split("\t")(1).toInt, 1))

    // Count add up all the 1's for each movie
    /**
      * When called on a dataset of (K, V) pairs, returns a dataset of (K, V) pairs
      * where the values for each key are aggregated using the given reduce function func,
      * which must be of type (V,V) => V. (V=Value. It applies the function on pairs of Key/Value pairs)
      *
      *  the example of the reduceByKey operation.
      *  The reduceByKey operation generates a new RDD where all values for a single key are combined into
      *  a tuple - the key and the result of executing a reduce function against all values
      *  associated with that key.
      *
      *  This adds the values together for each movieID.
      *  It does it in pairs, v1 + v2 for a unique k
      */
    //val movieCounts = movies.reduceByKey((v1, v2) => v1 + v2)
    val movieCounts = movies.reduceByKey((v1, v2) => v1 + v2)

    // Flip (movieID, count) to (count, movieID)
    val flipped = movieCounts.map( x => (x._2, x._1) )

    // Sort
    val sortedMovies = flipped.sortByKey()

    // Collect and print results
    val results = sortedMovies.collect()

    results.foreach(println)
    /*
      Prints out (count, movieID)
      (18,811)
      (18,1105)
      (19,1248)
      (19,138)
      (19,894)
      (19,424)
     */
  }

}
