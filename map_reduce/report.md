## Exercise 1

1) How does the number of reducers affect performance? How many reducers can be executed in parallel?
Increasing the number of reducers can improve the parallelism, so every reducer has to process a smaller amount of data and can do it in a shorter time (anyway we still need to take into account the time needed to send the data through the network and the balance between different partitions, as ideally every reducer should process the same amount of data). In this cluster we can execute at most 20 reducers at the time. The number of reducers depends also on the amount of different keys they have as input (is useless to execute more reducers than keys).

2) Use the JobTracker web interface to examine Job counters: can you explain the differences among the three variants of this exercise? For example, look at the amount of bytes shuffled by Hadoop.
In the three different experiments, the data read and written in the HDFS is always the same (3Gb input, 8.5Mb output). What changes is the amount of data read and written to the disks:

    FILE     read Map     written Map    read Reduce    written Reduce     shuffle 
Normal:       7.8Gb        15.6Gb          5.3Gb            5.3Gb           7.8Gb
IMC:            0            54Mb            0              497Kb           51Mb
Combiner:      71Mb         136Mb            0              498Kb           67Mb

In the normal approach, a lot of data has to be shuffled and sent through the network (7.8Gb). In the IMC case, all the data passing through a mapper can be combined togheter and a very small amount of data has to be shuffled (51Mb). In the last case, the combiners are executed only when a buffer on the mapper is full, and they cannot combine as much as in memory combiners, leading to a slightly bigger data shuffling (67Mb).

3) Can you explain how does the distribution of words affect your Job?
Since the partitioning of the key-value pairs to the reducers is done considering the hash value of the key (without load balancing), and the number of entries associated to different keys can be very different, it could happen that some reducers end up with a lot more data than other ones. The words distribution follows the Zipf's law, which means that some words are very common while others are very rare, and this can lead to unbalanced use of reducers.

## Exercise 2 - Pairs

1) How does the number of reducer influence the behavior of the Pairs approach?
In this case the data is more evenly distributed across different keys, so adding more reducers will in general improve the global performance, adding more parallelism. Anyway changing the number of reducers does not change the big amount of data that has to be sent across the network.

2) Why does TextPair need to be Comparable?
TextPair is used as key and as such it needs to be comparable in order to be sorted.

3) Can you use the implemented reducers as Combiner?
In this case is possible, since they do exactly the same operations, adding together all the values associated to the same key.

## Exercise 2 - Stripe

1) Can you use the implemented reducers as Combiner?
In this case is not possible because the output of the Combiner should be of type <Text, StringToIntMapWritable> (as the mapper), while the output of the reducer is of type <TextPair, LongWritable>. 

2) Do you think Stripes could be used with the in-memory combiner pattern?
It is possible to use in memory combiner, however we must always consider that the virtual memory could not be enough and the mapper could be killed (even if this representation is more compact that the pairs one).  

3) How does the number of reducer influence the behavior of the Stripes approach?
More reducers allows more parallelism, leading to a smaller amount of time required (if correctly balanced among different reducers).

4) Using the Jobtracker Web Interface, compare the shuffle phase of Pair and Stripes design patterns.
In the Pair approach the amount of shuffled data is about twice as big as the amount shuffled by the Stripe version. This can clearly show that the Stripe representation is more compact.

5) Why StringToIntMapWritable is not Comparable (differently from TextPair)?
StringToIntMapWritable does not need to be comparable since is not used as key and the sorting is done only on keys. We need to implement serialization and deserialization.

## Exercise 3

1) Do you think the Order Inversion approach is 'faster' than a naive approach with multiple jobs? For example, consider implementing a compound job in which you compute the numerator and the denominator separately, and then perform the computation of the relative frequency.
Our approach is better and faster than a naive approach, because in the naive approach you need to read two times the input file and to read intermediate results in order to compute the relative numbers.

2) What is the impact of the use of a 'special' compound key on the amounts of shuffled bytes?
It increases the number of pairs that need to be shuffled and sent through the network. Anyway the amount of extra data introduced is small compared to the total one.

3) How does the default partitioner works with TextPair? Can you imagine a different implementation that does not change the Partitioner?
The default one is based on the hash function of the object modulo the number of partitions. Insted of using a custom partitioner we could modify the hashCode() function in TextPair, in order to return only the hash of the first string.

4) For each key, the reducer receives its marginal before the co-occurence with the other words. Why?
Because they are automatically ordered and an empty string (as ASTERISK) will always precede all the non-empty ones, ending up to be the first.

