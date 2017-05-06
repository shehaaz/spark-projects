This is a simple  SBT project to learn the basics of Spark


Intro to Spark

Fast and general engine for large scala data processing. A framework for distributed processing of large datasets. 

It contains functions that let you import data from a data store, like HDFS, S3, etc. It provides a mechanism for simply and efficiently processing that data. 

You can run it on a cluster using the same code you used to run it on your desktop. You can start on your desktop and horizontally scale to a cluster. The same way Scala is a scalable language, where you can code in the REPL and then deploy it to server millions of requests. 

It is is highly scalable. 

why use spark?

Programs run up to 100x faster than Hadoop MapReduce in memory, or 10x faster on disk. 


DAG Engine (directed acyclic graph) optimizes workflows. It only executes when collect is called, so it can optmize as much as possible. 

Spark is based around one main concept a Resilient Distributed Dataset (RDD) it is just an abstraction over a giant set of data. (Transform and perform actions on RDDs)


Intro to RDDs

An encapsulation around a very large dataset, which you can apply transformations and actions on the data. RDD abstract away all the complexity of the distributed data. Fundamentally a dataset. A giant set of data. Lines of raw text or key value information. 

Where do they come from?
SparkContext: This is the one that creates RDDs for you. 

Once you get an RDD what can do with it?
map (transforms every row in the RDD), 
flatmap (if you don't have a one to one mapping from a row to output use flatmap, so you can map all rows to one row with flatmap), 
filter (this helps trim down the RDD based on the predicate you pass into the filter function), 
distinct (just returns distinct rows), 
sample(returns a sample of a larger dataset), 
Set operations: This can be used on any two RDDs and produce a resulting RDD from those operations.
union, intersection, subtract, cartesian. 

val rdd = sc.parallelize(List(1,2,3,4))
val sq = rdd.map(x => x*x)
> 1,4,9,16

You can also pass in a function to map. 

def square(x: Int): Int => {
	return x*x
}

val sq = rdd.map(square)

What are the ACTIONS you can perform on an RDD?

collect:
Takes the results of the RDD and brings it back to the Driver Script. You can then manupulate the results. Remember nothing happens in Spark until you call an ACTION on an RDD (e.g Collect). This can be tricky when debugging. For example, when "collect" is called the cluster manager will go back and figure out what is the optimal path to produce the results that I want. 
Spark will create a DAG on the cluster or desktop and then return the results

reduce:
The most common thing to do with RDD. Let's you combine together all the different values for a given key.

Other ACTIONS: count, countByValue,take, top

Further reading:
https://www.quora.com/As-it-is-mentioned-Apache-Spark-follows-a-DAG-Directed-Acyclic-Graph-execution-engine-for-execution-What-is-the-whole-concept-about-it-and-the-overall-architecture-of-the-Spark

https://www.sigmoid.com/apache-spark-internals/

https://www.quora.com/What-exactly-is-Apache-Spark-and-how-does-it-work/answer/Prithiviraj-Damodaran














